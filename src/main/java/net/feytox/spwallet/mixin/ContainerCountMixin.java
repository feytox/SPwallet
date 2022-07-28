package net.feytox.spwallet.mixin;

import net.feytox.spwallet.client.OnlineWallet;
import net.feytox.spwallet.client.SPwalletConfig;
import net.feytox.spwallet.client.SPwalletClient;
import net.feytox.spwallet.client.SlotsSelector;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.GenericContainerScreen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.inventory.Inventory;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static net.feytox.spwallet.client.CounterHUD.drawHud;

@Mixin(GenericContainerScreen.class)
public class ContainerCountMixin {

    @Inject(method = "drawBackground", at = @At("RETURN"))

    public void onDrawBackground (MatrixStack matrices, float delta, int mouseX, int mouseY,  CallbackInfo ci) {
        MinecraftClient client = MinecraftClient.getInstance();
        SPwalletClient.lastScreen = client.currentScreen;

        if (SPwalletConfig.showCounter && client.player != null) {
            ScreenHandler screenHandler = client.player.currentScreenHandler;
            if (screenHandler instanceof GenericContainerScreenHandler) {
                if (client.currentScreen != null) {
                    Text screenTitle = client.currentScreen.getTitle();
                    Inventory inventory = ((GenericContainerScreenHandler) screenHandler).getInventory();
                    int inventoryCount = SPwalletClient.getItemCount();
                    int containerCount = SPwalletClient.getItemCount(inventory);

                    if (SPwalletConfig.showInInventoryCount && SPwalletConfig.showOnlineCounter) {
                        if (SPwalletClient.ticks == -1) {
                            SPwalletClient.ticks = 0;
                            OnlineWallet.reloadBalance();
                        }

                        Integer currentBalance = OnlineWallet.getCurrentBalance();

                        if (currentBalance != null) {
                            inventoryCount += currentBalance;
                        }
                    }

                    int allCount = inventoryCount + containerCount;

                    if (screenTitle instanceof TranslatableText && allCount > 0) {
                        String screenName = ((TranslatableText) screenTitle).getKey();

                        if (SlotsSelector.isSlotsSelected()) {
                            drawHud(matrices, new ArrayList<>(List.of(SlotsSelector.getSlotsCount())), "inv_" + screenName);
                        }
                        else if (inventoryCount > 0 && containerCount > 0) {
                            drawHud(matrices, new ArrayList<>(Arrays.asList(inventoryCount, containerCount, allCount)), screenName);
                        } else if (containerCount == 0) {
                            drawHud(matrices, new ArrayList<>(List.of(inventoryCount)), "inv_" + screenName);
                        } else if (SPwalletConfig.simpleMode){
                            drawHud(matrices, new ArrayList<>(List.of(containerCount)), "chest_" + screenName);
                        } else {
                            drawHud(matrices, new ArrayList<>(List.of(containerCount)), screenName);
                        }
                    }
                }
            }
        }
        SlotsSelector.highlightSlots(matrices);
    }
}