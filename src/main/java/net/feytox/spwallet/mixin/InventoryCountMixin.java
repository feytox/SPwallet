package net.feytox.spwallet.mixin;

import net.feytox.spwallet.client.OnlineWallet;
import net.feytox.spwallet.client.SPwalletClient;
import net.feytox.spwallet.client.SPwalletConfig;
import net.feytox.spwallet.client.SlotsSelector;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.TextContent;
import net.minecraft.text.TranslatableTextContent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;

import static net.feytox.spwallet.client.CounterHUD.drawHud;

@Mixin(InventoryScreen.class)
public class InventoryCountMixin {

    @Inject(method = "drawBackground", at = @At("RETURN"))

    public void onDrawBackground (MatrixStack matrices, float delta, int mouseX, int mouseY,  CallbackInfo ci) {
        MinecraftClient client = MinecraftClient.getInstance();
        SPwalletClient.lastScreen = client.currentScreen;
        if (SPwalletConfig.showCounter && client.currentScreen != null && client.player != null) {
            TextContent screenTitle = client.currentScreen.getTitle().getContent();
            if (screenTitle instanceof TranslatableTextContent) {
                String screenName = ((TranslatableTextContent) screenTitle).getKey();
                int allCount = SPwalletClient.getItemCount();

                if (SPwalletConfig.showOnlineCounter && SPwalletClient.ticks == -1) {
                    SPwalletClient.ticks = 0;
                    OnlineWallet.reloadBalance();
                }

                if (SPwalletConfig.showInInventoryCount) {
                    if (SPwalletClient.ticks == -1) {
                        SPwalletClient.ticks = 0;
                        OnlineWallet.reloadBalance();
                    }

                    Integer currentBalance = OnlineWallet.getCurrentBalance();

                    if (currentBalance != null) {
                        allCount += currentBalance;
                    }
                }

                if ("container.crafting".equals(screenName) && (allCount > 0 || (SPwalletConfig.showOnlineCounter
                        && !SPwalletConfig.showInInventoryCount))) {
                    InventoryScreen inventoryScreen = (InventoryScreen) client.currentScreen;
                    boolean isRecipeBookOpen = inventoryScreen.getRecipeBookWidget().isOpen();

                    if (SlotsSelector.isSlotsSelected()) {
                        allCount = SlotsSelector.getSlotsCount();
                    }

                    if (isRecipeBookOpen) {
                        drawHud(matrices, new ArrayList<>(List.of(allCount)), "inv_recipe");
                    } else {
                        drawHud(matrices, new ArrayList<>(List.of(allCount)));
                        }
                    }
                }
            }
        SlotsSelector.highlightSlots(matrices);
        }
    }

