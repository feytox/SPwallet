package name.uwu.feytox.spwallet.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

import static name.uwu.feytox.spwallet.SPwalletClient.MOD_ID;

public class GuiTexture {
    Identifier image;
    int u1;
    int v1;
    int u2;
    int v2;
    int width;
    int height;
    int texture_width;
    int texture_height;

    public GuiTexture(int u1, int v1, int u2, int v2) {
        this(new Identifier(MOD_ID, "textures/hud/item_wallet_gui.png"), u1, v1, u2, v2, 27, 27);
    }

    public GuiTexture(Identifier image, int u1, int v1, int u2, int v2, int texture_width, int texture_height) {
        this(image, u1, v1, u2, v2, u2 - u1 + 1, v2 - v1 + 1, texture_width, texture_height);
    }

    public GuiTexture(Identifier image, int u1, int v1, int u2, int v2, int width, int height, int texture_width, int texture_height) {
        this.image = image;
        this.u1 = u1;
        this.v1 = v1;
        this.u2 = u2;
        this.v2 = v2;
        this.width = width;
        this.height = height;
        this.texture_width = texture_width;
        this.texture_height = texture_height;
    }

    public void drawTexture(MatrixStack matrices, int x, int y) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, this.image);
        DrawableHelper.drawTexture(matrices, x, y, this.u1, this.v1, this.width, this.height, this.texture_width, this.texture_height);
    }
}
