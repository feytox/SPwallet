package name.uwu.feytox.spwallet.gui;

import name.uwu.feytox.spwallet.config.ModConfig;
import name.uwu.feytox.spwallet.counter.ContainerType;
import name.uwu.feytox.spwallet.counter.SingleCounter;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

public class GuiCounter {
    ContainerType containerType;
    GuiTexture containerTexture;
    GuiTexture countTexture;
    String count;
    int count_ax;

    public GuiCounter(SingleCounter counter) {
        this.containerType = counter.containerType;
        this.containerTexture = counter.containerType.getContainerTexture();

        this.count = String.valueOf(counter.count);
        ModConfig config = ModConfig.get();

        if (counter.count == -621) {
          this.count = "?";
        } else if (config.isCountInStacks) {
            this.count = getCountInStacks(counter.count);
        }

        int u1;
        if (counter.count < 64) {
            u1 = 0;
        } else if (counter.count < 576) {
            u1 = 7;
        } else if (counter.count < 1728) {
            u1 = 14;
        } else {
            u1 = 21;
        }
        this.countTexture = new GuiTexture(u1, 15, u1 + 5, 20);

        this.count_ax = getCountPxLength(count);
    }

    public void drawCount(MatrixStack matrices, int x, int y) {
        DrawableHelper.drawTextWithShadow(matrices, MinecraftClient.getInstance().textRenderer, Text.literal(this.count),
                x, y, -1);
    }

    private static String getCountInStacks(int count) {
        if (count >= 64) {
            if (count % 64 != 0) {
                return count / 64 + I18n.translate("spwallet.st") + " " + (int) Math.round((((double) count) / 64 - count / 64) * 64);
            } else {
                return count / 64 + I18n.translate("spwallet.st");
            }
        }
        return String.valueOf(count);
    }

    protected static int getCountPxLength(String count) {
        int length = 0;
        for (String char1 : count.split("")) {
            switch (char1) {
                case "." -> length += 2;
                case " " -> length += 4;
                default -> length += 6;
            }
        }
        return length + 2;
    }
}
