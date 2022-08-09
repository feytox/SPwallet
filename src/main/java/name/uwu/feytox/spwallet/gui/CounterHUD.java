package name.uwu.feytox.spwallet.gui;

import name.uwu.feytox.spwallet.config.ModConfig;
import name.uwu.feytox.spwallet.counter.ContainerType;
import name.uwu.feytox.spwallet.counter.ScreenType;
import name.uwu.feytox.spwallet.counter.SingleCounter;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;

import java.util.ArrayList;
import java.util.List;

import static name.uwu.feytox.spwallet.gui.GuiCounter.getCountPxLength;

public class CounterHUD {
    List<GuiCounter> counterList;
    ScreenType screenType;

    public CounterHUD(ScreenType screenType) {
        this.counterList = new ArrayList<>();
        this.screenType = screenType;
    }

    public CounterHUD add(SingleCounter counter) {
        if (counter.containerType.equals(ContainerType.SELECTOR)) {
            this.counterList = new ArrayList<>(List.of(new GuiCounter(counter)));
            return this;
        }

        if (counter.containerType.equals(ContainerType.ALL_COUNT) && counterList.size() != 2) {
            return this;
        }

        if (counter.count > 0 || counter.count == -621) {
            this.counterList.add(new GuiCounter(counter));
        }

        return this;
    }

    public void draw(MatrixStack matrices) {
        if (!this.counterList.isEmpty()) {
            CounterCoord counterCoords = new CounterCoord(this);
            if (counterCoords.anchor_x != null) {
                // non-simple panel render
                int panel_width = 28;
                int panel_height = 18;

                if (counterCoords.line3_x != null) {
                    panel_height = 36;
                }

                int lineLength = getLineLength();
                if (lineLength > 11) {
                    panel_width += lineLength - 11;
                }

                CounterPanel panel = new CounterPanel(panel_width, panel_height);
                CounterPanel.createNinePatch(panel)
                        .paint(matrices,
                                getXFromCenter(counterCoords.anchor_x),
                                getYFromCenter(counterCoords.anchor_y));
            }
            if (counterCoords.line1_info != null) {
                // simple, one line (not inventory)
                CounterEditor.availableEditors.clear();
                drawCounterLine(matrices, counterCoords.line1_info, counterCoords.line1_x, counterCoords.line1_y,
                        this.screenType);
            } else {
                // all other
                if (!counterList.get(0).containerType.equals(ContainerType.CARD)) {
                    CounterEditor.availableEditors.clear();
                }
                for (int i = 0; i < counterList.size(); i++) {
                    GuiCounter line_info = counterList.get(i);

                    int line_x = counterCoords.line1_x;
                    int line_y = counterCoords.line1_y;
                    switch (i) {
                        case 1 -> {
                            line_x = counterCoords.line2_x;
                            line_y = counterCoords.line2_y;
                        }
                        case 2 -> {
                            line_x = counterCoords.line3_x;
                            line_y = counterCoords.line3_y;
                        }
                    }

                    drawCounterLine(matrices, line_info, line_x, line_y, this.screenType);
                }
            }
        }
    }

    private int getLineLength() {
        int lastLength = 0;

        for (GuiCounter line_info : counterList) {
            lastLength = Math.max(getCountPxLength(line_info.count) + 6, lastLength);
        }

        return lastLength;
    }

    private static void drawCounterLine(MatrixStack matrices, GuiCounter line_info, int line_x, int line_y,
                                        ScreenType screenType) {
        line_info.containerTexture.drawTexture(matrices,
                getXFromCenter(line_x),
                getYFromCenter(line_y));
        line_info.countTexture.drawTexture(matrices,
                getXFromCenter(line_x + 8 + line_info.count_ax),
                getYFromCenter(line_y));
        line_info.drawCount(matrices,
                getXFromCenter(line_x + 8),
                getYFromCenter(line_y + 1));

        CounterEditor.addAvailableEditor(screenType, new CounterEditor.EditorLineInfo(line_info, line_x, line_y));
    }

    protected static int getXFromCenter(int x) {
        x += MinecraftClient.getInstance().getWindow().getScaledWidth() / 2;
        return x;
    }

    protected static int getYFromCenter(int y) {
        y = (MinecraftClient.getInstance().getWindow().getScaledHeight() / 2) - y;
        return y;
    }

    protected static class CounterCoord {
        Integer anchor_x = null;
        Integer anchor_y = null;

        // инвентарь
        int line1_x;
        int line1_y;
        GuiCounter line1_info = null;
        // контейнер
        Integer line2_x = null;
        Integer line2_y = null;
        // всё
        Integer line3_x = null;
        Integer line3_y = null;

        protected CounterCoord(CounterHUD counterHUD) {
            ModConfig config = ModConfig.get();
            if (!config.simpleMode) {
                switch (counterHUD.screenType) {
                    case INVENTORY -> {
                        if (!counterHUD.counterList.get(0).containerType.equals(ContainerType.CARD)) {
                            this.anchor_x = config.inventoryOffsetX;
                            this.anchor_y = config.inventoryOffsetY;
                        } else {
                            this.anchor_x = config.onlineOffsetX;
                            this.anchor_y = config.onlineOffsetY;
                        }
                        this.line1_x = this.anchor_x + 5;
                        this.line1_y = this.anchor_y - 6;
                    }
                    case INVENTORY_WITH_RECIPE -> {
                        if (!counterHUD.counterList.get(0).containerType.equals(ContainerType.CARD)) {
                            this.anchor_x = config.inventoryOffsetX_withRecipeBook;
                            this.anchor_y = config.inventoryOffsetY_withRecipeBook;
                        } else {
                            this.anchor_x = config.onlineOffsetX_withRecipeBook;
                            this.anchor_y = config.onlineOffsetY_withRecipeBook;
                        }
                        this.line1_x = this.anchor_x + 5;
                        this.line1_y = this.anchor_y - 6;
                    }
                    case CHEST -> {
                        this.anchor_x = config.chestOffsetX;
                        this.anchor_y = config.chestOffsetY;
                        this.line1_x = this.anchor_x + 5;
                        this.line1_y = this.anchor_y - 6;
                        if (counterHUD.counterList.size() == 3) {
                            this.line2_x = this.anchor_x + 5;
                            this.line2_y = this.anchor_y - 15;
                            this.line3_x = this.anchor_x + 5;
                            this.line3_y = this.anchor_y - 24;
                        }
                    }
                    case DOUBLE_CHEST -> {
                        this.anchor_x = config.doubleChestOffsetX;
                        this.anchor_y = config.doubleChestOffsetY;
                        this.line1_x = this.anchor_x + 5;
                        this.line1_y = this.anchor_y - 6;
                        if (counterHUD.counterList.size() == 3) {
                            this.line2_x = this.anchor_x + 5;
                            this.line2_y = this.anchor_y - 15;
                            this.line3_x = this.anchor_x + 5;
                            this.line3_y = this.anchor_y - 24;
                        }
                    }
                }
            } else {
                switch (counterHUD.screenType) {
                    case INVENTORY -> {
                        if (!counterHUD.counterList.get(0).containerType.equals(ContainerType.CARD)) {
                            this.line1_x = config.simpleInvX;
                            this.line1_y = config.simpleInvY;
                        } else {
                            this.line1_x = config.simpleOnlineOffsetX;
                            this.line1_y = config.simpleOnlineOffsetY;
                        }
                    }
                    case INVENTORY_WITH_RECIPE -> {
                        if (!counterHUD.counterList.get(0).containerType.equals(ContainerType.CARD)) {
                            this.line1_x = config.simpleInvX_withRecipeBook;
                            this.line1_y = config.simpleInvY_withRecipeBook;
                        } else {
                            this.line1_x = config.simpleOnlineOffsetX_withRecipeBook;
                            this.line1_y = config.simpleOnlineOffsetY_withRecipeBook;
                        }
                    }
                    case CHEST -> {
                        if (counterHUD.counterList.size() == 3) {
                            this.line1_x = config.simpleChestX_inv;
                            this.line1_y = config.simpleChestY_inv;
                            this.line2_x = config.simpleChestX_chest;
                            this.line2_y = config.simpleChestY_chest;
                            this.line3_x = config.simpleChestX_all;
                            this.line3_y = config.simpleChestY_all;
                        } else {
                            this.line1_info = counterHUD.counterList.get(0);
                            switch (counterHUD.counterList.get(0).containerType) {
                                case INVENTORY, SELECTOR -> {
                                    this.line1_x = config.simpleChestX_inv;
                                    this.line1_y = config.simpleChestY_inv;
                                }
                                case CHEST, BARREL, ENDER_CHEST, SHULKER -> {
                                    this.line1_x = config.simpleChestX_chest;
                                    this.line1_y = config.simpleChestY_chest;
                                }
                            }
                        }
                    }
                    case DOUBLE_CHEST -> {
                        if (counterHUD.counterList.size() == 3) {
                            this.line1_x = config.simpleDoubleChestX_inv;
                            this.line1_y = config.simpleDoubleChestY_inv;
                            this.line2_x = config.simpleDoubleChestX_chest;
                            this.line2_y = config.simpleDoubleChestY_chest;
                            this.line3_x = config.simpleDoubleChestX_all;
                            this.line3_y = config.simpleDoubleChestY_all;
                        } else {
                            this.line1_info = counterHUD.counterList.get(0);
                            switch (counterHUD.counterList.get(0).containerType) {
                                case INVENTORY, SELECTOR -> {
                                    this.line1_x = config.simpleDoubleChestX_inv;
                                    this.line1_y = config.simpleDoubleChestY_inv;
                                }
                                case DOUBLE_CHEST -> {
                                    this.line1_x = config.simpleDoubleChestX_chest;
                                    this.line1_y = config.simpleDoubleChestY_chest;
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
