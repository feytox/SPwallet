package name.uwu.feytox.spwallet.gui;

import name.uwu.feytox.spwallet.SPwalletClient;
import name.uwu.feytox.spwallet.config.ModConfig;
import name.uwu.feytox.spwallet.counter.ContainerType;
import name.uwu.feytox.spwallet.counter.ScreenType;
import net.minecraft.sound.SoundEvents;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import static name.uwu.feytox.spwallet.gui.CounterHUD.getXFromCenter;
import static name.uwu.feytox.spwallet.gui.CounterHUD.getYFromCenter;

public class CounterEditor {
    static CounterEditor activeEditor = null;
    static List<CounterEditor> availableEditors = new ArrayList<>();
    Consumer<Coord> saveConsumer;
    int x1;
    int y1;
    int x2;
    int y2;

    private CounterEditor(ScreenType screenType, EditorLineInfo editorLineInfo) {
        this(getXFromCenter(editorLineInfo.line_x), getYFromCenter(editorLineInfo.line_y),
                getXFromCenter(editorLineInfo.line_x + 5), getYFromCenter(editorLineInfo.line_y - 5));
        this.genSaveConsumer(screenType, editorLineInfo);
    }

    public CounterEditor(int x1, int y1, int x2, int y2) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
    }

    public static void setActiveEditor(int click_x, int click_y) {
        if (!ModConfig.get().editorMode) {
            return;
        }

        for (CounterEditor editor : availableEditors) {
            if (editor.x1 <= click_x && click_x <= editor.x2) {
                if (editor.y1 <= click_y && click_y <= editor.y2) {
                    activeEditor = editor;
                    SPwalletClient.playSound(SoundEvents.UI_BUTTON_CLICK);
                    return;
                }
            }
        }
    }

    public static boolean isActive() {
        return activeEditor != null;
    }

    public static void addAvailableEditor(ScreenType screenType, EditorLineInfo line_info) {
        availableEditors.add(new CounterEditor(screenType, line_info));
    }

    public static void change(int delta_x, int delta_y) {
        activeEditor.saveConsumer.accept(new Coord(delta_x, delta_y));
    }

    public static void save() {
        ModConfig.save();
        activeEditor = null;
        SPwalletClient.playSound(SoundEvents.ENTITY_VILLAGER_YES);
    }

    public static void discard() {
        ModConfig.load();
        activeEditor = null;
        SPwalletClient.playSound(SoundEvents.ENTITY_VILLAGER_NO);
    }

    public void genSaveConsumer(ScreenType screenType, EditorLineInfo line_info) {
        ModConfig config = ModConfig.get();

        if (!config.simpleMode) {
            switch (screenType) {
                case INVENTORY -> this.saveConsumer = coord_change -> {
                    if (!line_info.containerType.equals(ContainerType.CARD)) {
                        config.inventoryOffsetX += coord_change.x;
                        config.inventoryOffsetY += coord_change.y;
                    } else {
                        config.onlineOffsetX += coord_change.x;
                        config.onlineOffsetY += coord_change.y;
                    }
                };
                case INVENTORY_WITH_RECIPE -> this.saveConsumer = coord_change -> {
                    if (!line_info.containerType.equals(ContainerType.CARD)) {
                        config.inventoryOffsetX_withRecipeBook += coord_change.x;
                        config.inventoryOffsetY_withRecipeBook += coord_change.y;
                    } else {
                        config.onlineOffsetX_withRecipeBook += coord_change.x;
                        config.onlineOffsetY_withRecipeBook += coord_change.y;
                    }
                };
                case CHEST -> this.saveConsumer = coord_change -> {
                    config.chestOffsetX += coord_change.x;
                    config.chestOffsetY += coord_change.y;
                };
                case DOUBLE_CHEST -> this.saveConsumer = coord_change -> {
                    config.doubleChestOffsetX += coord_change.x;
                    config.doubleChestOffsetY += coord_change.y;
                };
            }
        } else {
            switch (screenType) {
                case INVENTORY -> this.saveConsumer = coord_change -> {
                    if (!line_info.containerType.equals(ContainerType.CARD)) {
                        config.simpleInvX += coord_change.x;
                        config.simpleInvY += coord_change.y;
                    } else {
                        config.simpleOnlineOffsetX += coord_change.x;
                        config.simpleOnlineOffsetY += coord_change.y;
                    }
                };
                case INVENTORY_WITH_RECIPE -> this.saveConsumer = coord_change -> {
                    if (!line_info.containerType.equals(ContainerType.CARD)) {
                        config.simpleInvX_withRecipeBook += coord_change.x;
                        config.simpleInvY_withRecipeBook += coord_change.y;
                    } else {
                        config.simpleOnlineOffsetX_withRecipeBook += coord_change.x;
                        config.simpleOnlineOffsetY_withRecipeBook += coord_change.y;
                    }
                };
                case CHEST -> {
                    if (line_info.containerType.equals(ContainerType.INVENTORY) || line_info.containerType.equals(ContainerType.SELECTOR)) {
                        this.saveConsumer = coord_change -> {
                            config.simpleChestX_inv += coord_change.x;
                            config.simpleChestY_inv += coord_change.y;
                        };
                    } else if (!line_info.containerType.equals(ContainerType.ALL_COUNT)) {
                        this.saveConsumer = coord_change -> {
                            config.simpleChestX_chest += coord_change.x;
                            config.simpleChestY_chest += coord_change.y;
                        };
                    } else {
                        this.saveConsumer = coord_change -> {
                            config.simpleChestX_all += coord_change.x;
                            config.simpleChestY_all += coord_change.y;
                        };
                    }
                }
                case DOUBLE_CHEST -> {
                    if (line_info.containerType.equals(ContainerType.INVENTORY) || line_info.containerType.equals(ContainerType.SELECTOR)) {
                        this.saveConsumer = coord_change -> {
                            config.simpleDoubleChestX_inv += coord_change.x;
                            config.simpleDoubleChestY_inv += coord_change.y;
                        };
                    } else if (!line_info.containerType.equals(ContainerType.ALL_COUNT)) {
                        this.saveConsumer = coord_change -> {
                            config.simpleDoubleChestX_chest += coord_change.x;
                            config.simpleDoubleChestY_chest += coord_change.y;
                        };
                    } else {
                        this.saveConsumer = coord_change -> {
                            config.simpleDoubleChestX_all += coord_change.x;
                            config.simpleDoubleChestY_all += coord_change.y;
                        };
                    }
                }
            }
        }

    }

    private static class Coord {
        int x;
        int y;

        protected Coord(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    protected static class EditorLineInfo {
        ContainerType containerType;
        int line_x;
        int line_y;

        protected EditorLineInfo(GuiCounter guiCounter, int line_x, int line_y) {
            this.containerType = guiCounter.containerType;
            this.line_x = line_x;
            this.line_y = line_y;
        }
    }
}
