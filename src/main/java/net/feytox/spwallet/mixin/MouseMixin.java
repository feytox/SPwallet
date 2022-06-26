package net.feytox.spwallet.mixin;

import net.feytox.spwallet.client.SPwalletClient;
import net.feytox.spwallet.client.SPwalletConfig;
import net.feytox.spwallet.client.SlotsSelector;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.Mouse;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.GenericContainerScreen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.gui.screen.ingame.ShulkerBoxScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;

@Mixin(Mouse.class)
public class MouseMixin {

    @Inject(method = "onMouseButton", at = @At("RETURN"), cancellable = true)
    public void onOnMouseButton (long window, int button, int action, int mods, CallbackInfo ci) {
        Screen screen = MinecraftClient.getInstance().currentScreen;
        if ((screen instanceof InventoryScreen || screen instanceof GenericContainerScreen || screen instanceof ShulkerBoxScreen)
                && SPwalletClient.lastKeyPressed != button) {
            if ((Objects.equals(SPwalletConfig.selectKeybind_key, SPwalletConfig.KeybindsEnum.VANILLA)
                    && SPwalletClient.selectSlot_keybind.matchesMouse(button)) |
                    SPwalletConfig.selectKeybind_key.getKeycode() == button) {
                SlotsSelector.selectSlot();
                SPwalletClient.ticks = 0;
            } else if ((Objects.equals(SPwalletConfig.showCountInStacks_key, SPwalletConfig.KeybindsEnum.VANILLA)
                    && SPwalletClient.showCountInStacks_keybind.matchesMouse(button)) |
                    SPwalletConfig.showCountInStacks_key.getKeycode() == button) {
                SPwalletConfig.isCountInStacks = !SPwalletConfig.isCountInStacks;
                SPwalletConfig.write("spwallet");
                SPwalletClient.ticks = 0;
            }
        }
        SPwalletClient.lastKeyPressed = button;
    }
}
