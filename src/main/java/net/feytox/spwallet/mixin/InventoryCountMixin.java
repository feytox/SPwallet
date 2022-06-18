package net.feytox.spwallet.mixin;

import net.feytox.spwallet.client.SPwalletConfig;
import net.feytox.spwallet.client.SPwalletClient;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;

import static net.feytox.spwallet.client.CounterHUD.drawHud;

@Mixin(InventoryScreen.class)
public class InventoryCountMixin {

    @Inject(method = "drawBackground", at = @At("RETURN"), cancellable = true)

    public void onDrawBackground (MatrixStack matrices, float delta, int mouseX, int mouseY,  CallbackInfo ci) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (SPwalletConfig.showCounter && client.currentScreen != null && client.player != null) {
            Text screenTitle = client.currentScreen.getTitle();
            if (screenTitle instanceof TranslatableText) {
                String screenName = ((TranslatableText) screenTitle).getKey();
                int allCount = SPwalletClient.getItemCount();
                if ("container.crafting".equals(screenName) && allCount > 0) {
                    InventoryScreen inventoryScreen = (InventoryScreen) client.currentScreen;
                    boolean isRecipeBookOpen = inventoryScreen.getRecipeBookWidget().isOpen();
                    if (isRecipeBookOpen) {
                        drawHud(matrices, new ArrayList<>(List.of(allCount)), "inv_recipe");
                    } else {
                        drawHud(matrices, new ArrayList<>(List.of(allCount)));
                    }
                }
            }
        }
    }
}
