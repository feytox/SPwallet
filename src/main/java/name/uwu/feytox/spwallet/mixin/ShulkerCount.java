package name.uwu.feytox.spwallet.mixin;

import name.uwu.feytox.spwallet.SPwalletClient;
import name.uwu.feytox.spwallet.config.ModConfig;
import name.uwu.feytox.spwallet.counter.ScreenType;
import name.uwu.feytox.spwallet.counter.SingleCounter;
import name.uwu.feytox.spwallet.counter.SlotsSelector;
import name.uwu.feytox.spwallet.gui.CounterHUD;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.ShulkerBoxScreen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ShulkerBoxScreenHandler;
import net.minecraft.util.collection.DefaultedList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ShulkerBoxScreen.class)
public class ShulkerCount {

    @Inject(method = "drawBackground", at = @At("RETURN"))
    public void onDrawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY, CallbackInfo ci) {
        MinecraftClient client = MinecraftClient.getInstance();
        SPwalletClient.lastScreen = client.currentScreen;
        ModConfig config = ModConfig.get();

        if (config.enableMod && client.currentScreen != null && client.player != null) {
            ScreenHandler screenHandler = client.player.currentScreenHandler;
            ShulkerBoxScreenHandler shulkerBoxScreenHandler = ((ShulkerBoxScreenHandler) screenHandler);
            DefaultedList<ItemStack> itemStacks = shulkerBoxScreenHandler.getStacks();

            SingleCounter inventoryCounter = SingleCounter.inventory();
            SingleCounter shulkerCounter = SingleCounter.shulker(itemStacks);

            CounterHUD hud = new CounterHUD(ScreenType.CHEST)
                    .add(inventoryCounter)
                    .add(shulkerCounter)
                    .add(SingleCounter.allCount(inventoryCounter, shulkerCounter));
            hud.draw(matrices);
        }
        SlotsSelector.highlightSlots(matrices);
    }
}
