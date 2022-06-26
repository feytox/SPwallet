package net.feytox.spwallet.mixin;

import net.feytox.spwallet.client.SPwalletClient;
import net.feytox.spwallet.client.SPwalletConfig;
import net.feytox.spwallet.client.SlotsSelector;
import net.minecraft.client.Keyboard;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.GenericContainerScreen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.gui.screen.ingame.ShulkerBoxScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;

@Mixin(Keyboard.class)
public class KeyboardMixin {

    @Inject(method="onKey", at = @At("RETURN"), cancellable = true)
    public void onOnKey (long window, int key, int scancode, int action, int modifiers, CallbackInfo ci) {
        Screen screen = MinecraftClient.getInstance().currentScreen;
        if ((screen instanceof InventoryScreen || screen instanceof GenericContainerScreen || screen instanceof ShulkerBoxScreen)
                && SPwalletClient.lastKeyPressed != key) {
              if ((Objects.equals(SPwalletConfig.selectKeybind_key, SPwalletConfig.KeybindsEnum.VANILLA)
                      && SPwalletClient.selectSlot_keybind.matchesKey(key, scancode)) |
                      SPwalletConfig.selectKeybind_key.getKeycode() == key) {
                SlotsSelector.selectSlot();
                SPwalletClient.ticks = 0;
            } else if ((Objects.equals(SPwalletConfig.showCountInStacks_key, SPwalletConfig.KeybindsEnum.VANILLA)
                      && SPwalletClient.showCountInStacks_keybind.matchesKey(key, scancode)) |
                      SPwalletConfig.showCountInStacks_key.getKeycode() == key) {
                SPwalletConfig.isCountInStacks = !SPwalletConfig.isCountInStacks;
                SPwalletConfig.write("spwallet");
                SPwalletClient.ticks = 0;
            }
        }
        SPwalletClient.lastKeyPressed = key;
    }
}
