package name.uwu.feytox.spwallet.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import juuxel.libninepatch.ContextualTextureRenderer;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Matrix4f;

public enum NinePatchTextureRenderer implements ContextualTextureRenderer<Identifier, MatrixStack> {
    INSTANCE;

    @Override
    public void draw(Identifier texture, MatrixStack matrices, int x, int y, int width, int height, float u1, float v1, float u2, float v2) {
        if (width <= 0) width = 1;
        if (height <= 0) height = 1;

        int color = 0xFF_FFFFFF;

        float r = (color >> 16 & 255) / 255.0F;
        float g = (color >> 8 & 255) / 255.0F;
        float b = (color & 255) / 255.0F;
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();
        Matrix4f model = matrices.peek().getPositionMatrix();
        RenderSystem.enableBlend();
        RenderSystem.setShaderTexture(0, texture);
        RenderSystem.setShaderColor(r, g, b, 1.0F);
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        buffer.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE);
        buffer.vertex(model, x,         y + height, 0).texture(u1, v2).next();
        buffer.vertex(model, x + width, y + height, 0).texture(u2, v2).next();
        buffer.vertex(model, x + width, y,          0).texture(u2, v1).next();
        buffer.vertex(model, x,         y,          0).texture(u1, v1).next();
        buffer.end();
        BufferRenderer.draw(buffer);
        RenderSystem.disableBlend();
    }
}