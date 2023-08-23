package ru.feytox.spwallet.gui;

import juuxel.libninepatch.NinePatch;
import juuxel.libninepatch.TextureRegion;
import net.minecraft.util.Identifier;
import ru.feytox.spwallet.config.ModConfig;

import java.util.function.Consumer;

import static ru.feytox.spwallet.SPwalletClient.MOD_ID;

public class CounterPanel {
    public int width;
    public int height;

    public CounterPanel(int width, int height) {
        this.width = width;
        this.height = height;
    }

    private static Identifier getPanelId() {
        ModConfig config = ModConfig.get();
        String postfix = config.darkMode ? "dark" : "light";
        return new Identifier(MOD_ID, "textures/hud/panel_" + postfix + ".png");
    }

    public static NinePatchPanelPainter createNinePatch(CounterPanel panel) {
        Consumer<NinePatch.Builder<Identifier>> configurator = builder -> builder.cornerSize(4).cornerUv(0.25F);
        TextureRegion<Identifier> region = new TextureRegion<>(getPanelId(), 0, 0, 1, 1);
        var builder = NinePatch.builder(region);
        configurator.accept(builder);
        return new NinePatchPanelPainter(builder.build(), panel);
    }
}
