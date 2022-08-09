package name.uwu.feytox.spwallet.mixin;

import name.uwu.feytox.spwallet.SPwalletClient;
import name.uwu.feytox.spwallet.config.ModConfig;
import name.uwu.feytox.spwallet.counter.ScreenType;
import name.uwu.feytox.spwallet.counter.SingleCounter;
import name.uwu.feytox.spwallet.counter.SlotsSelector;
import name.uwu.feytox.spwallet.gui.CounterHUD;
import name.uwu.feytox.spwallet.spapi.OnlineWallet;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InventoryScreen.class)
public class InventoryCount {

    @Inject(method = "drawBackground", at = @At("RETURN"))
    public void onDrawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY, CallbackInfo ci) {
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
            hud.draw(matrices);

            if (config.showOnlineCounter) {
                CounterHUD cardHud = new CounterHUD(screenType).add(SingleCounter.card());
                cardHud.draw(matrices);
            }
        }
        SlotsSelector.highlightSlots(matrices);
    }
}

