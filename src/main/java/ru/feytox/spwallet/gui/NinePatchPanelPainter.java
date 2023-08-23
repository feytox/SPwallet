package ru.feytox.spwallet.gui;

import juuxel.libninepatch.NinePatch;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public class NinePatchPanelPainter {
    final NinePatch<Identifier> ninePatch;
    final CounterPanel panel;

    public NinePatchPanelPainter(NinePatch<Identifier> ninePatch, CounterPanel panel) {
        this.ninePatch = ninePatch;
        this.panel = panel;
    }

    public void paint(DrawContext context, int x, int y) {
        MatrixStack matrices = context.getMatrices();
        matrices.push();
        matrices.translate(x, y, 0);
        ninePatch.draw(NinePatchTextureRenderer.INSTANCE, matrices, this.panel.width, this.panel.height);
        matrices.pop();
    }
}
