package ru.feytox.spwallet.mixin;

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
import ru.feytox.spwallet.config.ModConfig;
import ru.feytox.spwallet.counter.SlotsSelector;
import ru.feytox.spwallet.gui.CounterEditor;

@Mixin(Mouse.class)
public class MouseMixin {

    @Inject(method = "onMouseButton", at = @At("RETURN"))
    public void onOnMouseButton (long window, int button, int action, int mods, CallbackInfo ci) {
        Screen screen = MinecraftClient.getInstance().currentScreen;
        ModConfig config = ModConfig.get();

        if (mods == 1) mods = 4;
        else if (mods == 4) mods = 1;

        if (screen instanceof InventoryScreen || screen instanceof GenericContainerScreen || screen instanceof ShulkerBoxScreen) {
            if (action == 1) {
                if (button == 0) {
                    Mouse mouse = MinecraftClient.getInstance().mouse;

                    int click_x = (int) (mouse.getX() / MinecraftClient.getInstance().getWindow().getScaleFactor());
                    int click_y = (int) (mouse.getY() / MinecraftClient.getInstance().getWindow().getScaleFactor());

                    CounterEditor.setActiveEditor(click_x, click_y);
                }

                if (config.selectKeybind_key == button && config.selectKeybind_mod == mods) {
                    SlotsSelector.selectSlot();
                } else if (config.showCountInStacks_key == button && config.showCountInStacks_mod == mods) {
                    config.isCountInStacks = !config.isCountInStacks;
                    ModConfig.save();
                }
            }
        }
    }

    @Inject(method = "onCursorPos", at = @At("HEAD"))
    public void onOnCursorPos(long window, double x, double y, CallbackInfo ci) {
        if (((Mouse) (Object) this).wasLeftButtonClicked()) {
            int click_x = (int) (x / MinecraftClient.getInstance().getWindow().getScaleFactor());
            int click_y = (int) (y / MinecraftClient.getInstance().getWindow().getScaleFactor());

            CounterEditor.setActiveEditor(click_x, click_y);
        }
    }
}
