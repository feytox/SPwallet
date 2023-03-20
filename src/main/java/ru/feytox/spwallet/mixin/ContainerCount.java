package ru.feytox.spwallet.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.GenericContainerScreen;
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

@Mixin(GenericContainerScreen.class)
public class ContainerCount {

    @Inject(method = "drawBackground", at = @At("RETURN"))
    public void onDrawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY, CallbackInfo ci) {
        MinecraftClient client = MinecraftClient.getInstance();
        SPwalletClient.lastScreen = client.currentScreen;
        ModConfig config = ModConfig.get();

        if (config.enableMod && client.currentScreen != null && client.player != null) {
            SingleCounter inventoryCounter = SingleCounter.inventory();
            SingleCounter chestCounter = SingleCounter.getChestCounter(client.currentScreen);

            CounterHUD hud = new CounterHUD(ScreenType.getScreenType(client.currentScreen))
                    .add(inventoryCounter)
                    .add(chestCounter)
                    .add(SingleCounter.allCount(inventoryCounter, chestCounter));
            hud.draw(matrices);
        }
        SlotsSelector.highlightSlots(matrices);
    }
}
