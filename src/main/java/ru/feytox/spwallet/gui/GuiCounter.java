package ru.feytox.spwallet.gui;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import ru.feytox.spwallet.config.ModConfig;
import ru.feytox.spwallet.counter.ContainerType;
import ru.feytox.spwallet.counter.SingleCounter;
import ru.feytox.spwallet.spapi.OnlineWallet;

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

    public void drawCount(DrawContext context, int x, int y) {
        Text countText = Text.literal(count);
        TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
        if (containerType.equals(ContainerType.CARD) && OnlineWallet.isBadResponse) {
            countText = Text.literal(count).formatted(Formatting.RED);
            context.drawTextWithShadow(textRenderer, countText, x, y, -1);
        } else {
            context.drawTextWithShadow(textRenderer, countText, x, y, -1);
        }
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
