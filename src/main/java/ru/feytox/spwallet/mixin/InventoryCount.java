package ru.feytox.spwallet.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ru.feytox.spwallet.SPwalletClient;
import ru.feytox.spwallet.config.ModConfig;
import ru.feytox.spwallet.counter.ScreenType;
import ru.feytox.spwallet.counter.SingleCounter;
import ru.feytox.spwallet.counter.SlotsSelector;
import ru.feytox.spwallet.gui.CounterHUD;
import ru.feytox.spwallet.spapi.OnlineWallet;

@Mixin(InventoryScreen.class)
public class InventoryCount {

    @Inject(method = "drawBackground", at = @At("RETURN"))
    public void onDrawBackground(DrawContext context, float delta, int mouseX, int mouseY, CallbackInfo ci) {
        MinecraftClient client = MinecraftClient.getInstance();
        SPwalletClient.lastScreen = client.currentScreen;
        ModConfig config = ModConfig.get();

        if (config.enableMod && client.currentScreen != null && client.player != null) {
            boolean isRecipeBookOpen = ((InventoryScreen) client.currentScreen).getRecipeBookWidget().isOpen();
            ScreenType screenType = ScreenType.INVENTORY;
            if (isRecipeBookOpen) {
                screenType = ScreenType.INVENTORY_WITH_RECIPE;
            }

            if ((config.showInInventoryCount || config.showOnlineCounter) && SPwalletClient.online_ticks == -1) {
                SPwalletClient.online_ticks = 0;
                OnlineWallet.reloadBalance();
            }

            CounterHUD hud = new CounterHUD(screenType).add(SingleCounter.inventory());
            hud.draw(context);

            if (config.showOnlineCounter) {
                CounterHUD cardHud = new CounterHUD(screenType).add(SingleCounter.card());
                cardHud.draw(context);
            }
        }
        SlotsSelector.highlightSlots(context);
    }
}

