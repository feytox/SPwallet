package net.feytox.spwallet.client;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Identifier;

import java.util.List;
import java.util.Objects;

import static net.feytox.spwallet.client.SPwalletClient.convertToInt;
import static net.feytox.spwallet.client.SPwalletClient.getCoordFromCenter;

public class CounterHUD {
    public static void drawHud(MatrixStack matrices, List<Integer> count, String containerType) {
        int aX = getCoordByType("x", containerType);
        int aY = getCoordByType("y", containerType);

        if (Objects.equals(containerType, "inv_recipe")) {
            aX = SPwalletConfig.inventoryOffsetX_withRecipeBook;
            aY = SPwalletConfig.inventoryOffsetY_withRecipeBook;
        }

        MinecraftClient client = MinecraftClient.getInstance();

        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, new Identifier("spwallet", "textures/hud/sp_wallet_gui.png"));
        switch (count.size()) {
            case 1 -> {
                // это база
                int invLine_x = getCoordFromCenter("x", aX + 5);
                int invLine_y = getCoordFromCenter("y", aY - 6);

                if (!SPwalletConfig.simpleMode) {
                    DrawableHelper.drawTexture(matrices, getCoordFromCenter("x", aX),
                            getCoordFromCenter("y", aY), 48, 18,
                            0, 36, 48, 18, 64, 64);
                } else {
                    invLine_x = getCoordFromCenter("x", SPwalletConfig.simpleInvX);
                    invLine_y = getCoordFromCenter("y", SPwalletConfig.simpleInvY);
                    if (Objects.equals(containerType, "inv_recipe")) {
                        invLine_x = getCoordFromCenter("x", SPwalletConfig.simpleInvX_withRecipeBook);
                        invLine_y = getCoordFromCenter("y", SPwalletConfig.simpleInvY_withRecipeBook);
                    }
                    if (containerType.length() > 3) {
                        switch (containerType) {
                            case "inv_container.chestDouble" -> {
                                invLine_x = getCoordFromCenter("x", SPwalletConfig.simpleDoubleChestX_inv);
                                invLine_y = getCoordFromCenter("y", SPwalletConfig.simpleDoubleChestY_inv);
                            }
                            case "chest_container.chestDouble" -> {
                                invLine_x = getCoordFromCenter("x", SPwalletConfig.simpleDoubleChestX_chest);
                                invLine_y = getCoordFromCenter("y", SPwalletConfig.simpleDoubleChestY_chest);
                            }
                            default -> {
                                if (Objects.equals(containerType.substring(0, 3), "inv") &
                                        !Objects.equals(containerType, "inv_recipe")) {
                                    invLine_x = getCoordFromCenter("x", SPwalletConfig.simpleChestX_inv);
                                    invLine_y = getCoordFromCenter("y", SPwalletConfig.simpleChestY_inv);
                                } else if (!Objects.equals(containerType, "inv_recipe")){
                                    invLine_x = getCoordFromCenter("x", SPwalletConfig.simpleChestX_chest);
                                    invLine_y = getCoordFromCenter("y", SPwalletConfig.simpleChestY_chest);
                                }
                            }
                        }
                    }
                }

                // иконка
                DrawableHelper.drawTexture(matrices, invLine_x, invLine_y,6, 6,
                        getIconXByType(containerType), getIconYBySlotsSelected(), 6, 6, 64, 64);

                String countLine1 = String.valueOf(count.get(0));
                if (!SPwalletConfig.isCountInStacks | count.get(0) < 64) {
                    // Иконки АР
                    DrawableHelper.drawTexture(matrices, invLine_x + 8 + getXByCount(count.get(0)), invLine_y, 6, 6,
                            56, getTextureYByARcount(count.get(0)), 6, 6, 64, 64);
                } else {
                    countLine1 = getCountInStacks(count.get(0));
                }

                // Число АР
                DrawableHelper.drawTextWithShadow(matrices, client.textRenderer, new LiteralText(countLine1),
                        invLine_x + 8, invLine_y - 1, -1);
            }
            case 3 -> {
                // это база
                int invLine_x = getCoordFromCenter("x", aX + 5);
                int invLine_y = getCoordFromCenter("y", aY - 6);
                int chestLine_x = getCoordFromCenter("x", aX + 5);
                int chestLine_y = getCoordFromCenter("y", aY - 15);
                int allLine_x = getCoordFromCenter("x", aX + 5);
                int allLine_y = getCoordFromCenter("y", aY - 24);
                if (!SPwalletConfig.simpleMode) {
                    DrawableHelper.drawTexture(matrices, getCoordFromCenter("x", aX),
                            getCoordFromCenter("y", aY), 48, 36,
                            0, 0, 48, 36, 64, 64);
                } else {
                    if ("container.chestDouble".equals(containerType) | "inv_container.chestDouble".equals(containerType)) {
                        invLine_x = getCoordFromCenter("x", SPwalletConfig.simpleDoubleChestX_inv);
                        invLine_y = getCoordFromCenter("y", SPwalletConfig.simpleDoubleChestY_inv);
                        chestLine_x = getCoordFromCenter("x", SPwalletConfig.simpleDoubleChestX_chest);
                        chestLine_y = getCoordFromCenter("y", SPwalletConfig.simpleDoubleChestY_chest);
                        allLine_x = getCoordFromCenter("x", SPwalletConfig.simpleDoubleChestX_all);
                        allLine_y = getCoordFromCenter("y", SPwalletConfig.simpleDoubleChestY_all);
                    } else {
                        invLine_x = getCoordFromCenter("x", SPwalletConfig.simpleChestX_inv);
                        invLine_y = getCoordFromCenter("y", SPwalletConfig.simpleChestY_inv);
                        chestLine_x = getCoordFromCenter("x", SPwalletConfig.simpleChestX_chest);
                        chestLine_y = getCoordFromCenter("y", SPwalletConfig.simpleChestY_chest);
                        allLine_x = getCoordFromCenter("x", SPwalletConfig.simpleChestX_all);
                        allLine_y = getCoordFromCenter("y", SPwalletConfig.simpleChestY_all);
                    }
                }

                // иконки
                DrawableHelper.drawTexture(matrices, invLine_x, invLine_y,6, 6,
                            2, 56, 6, 6, 64, 64);
                DrawableHelper.drawTexture(matrices, chestLine_x, chestLine_y,6, 6,
                        getTextureXByStorage(containerType), 56, 6, 6, 64, 64);
                DrawableHelper.drawTexture(matrices, allLine_x, allLine_y,6, 7,
                        20, 56, 6, 7, 64, 64);

                String countLine1 = String.valueOf(count.get(0));
                String countLine2 = String.valueOf(count.get(1));
                String countLine3 = String.valueOf(count.get(2));
                if (!SPwalletConfig.isCountInStacks | count.get(0) < 64) {
                    // Иконки АР
                    DrawableHelper.drawTexture(matrices, invLine_x + 8 + getXByCount(count.get(0)), invLine_y, 6, 6,
                            56, getTextureYByARcount(count.get(0)), 6, 6, 64, 64);
                } else {
                    countLine1 = getCountInStacks(count.get(0));
                }
                if (!SPwalletConfig.isCountInStacks | count.get(1) < 64) {
                    DrawableHelper.drawTexture(matrices, chestLine_x + 8 + getXByCount(count.get(1)), chestLine_y, 6, 6,
                            56, getTextureYByARcount(count.get(1)), 6, 6, 64, 64);
                } else {
                    countLine2 = getCountInStacks(count.get(1));
                }
                if (!SPwalletConfig.isCountInStacks | count.get(2) < 64) {
                    DrawableHelper.drawTexture(matrices, allLine_x + 8 + getXByCount(count.get(2)), allLine_y, 6, 6,
                            56, getTextureYByARcount(count.get(2)), 6, 6, 64, 64);
                } else {
                    countLine3 = getCountInStacks(count.get(2));
                }

                // Число АР
                DrawableHelper.drawTextWithShadow(matrices, client.textRenderer, new LiteralText(countLine1),
                        invLine_x + 8, invLine_y - 1, -1);
                DrawableHelper.drawTextWithShadow(matrices, client.textRenderer, new LiteralText(countLine2),
                        chestLine_x + 8, chestLine_y - 1, -1);
                DrawableHelper.drawTextWithShadow(matrices, client.textRenderer, new LiteralText(countLine3),
                        allLine_x + 8, allLine_y - 1, -1);
            }
        }

    }

    public static void drawHud(MatrixStack matrices, List<Integer> count) {
        drawHud(matrices, count, "inv");
    }

    private static int getTextureYByARcount(int count) {
        int y;
        if (count < 64) {
            y = 20;
        } else if (count < 576) {
            y = 29;
        } else if (count < 1728) {
            y = 38;
        } else {
            y = 47;
        }
        return y;
    }

    private static int getTextureXByStorage(String type) {
        int x;
        switch (type) {
            case "container.shulkerBox", "inv_container.shulkerBox" -> x = 56;
            case "container.barrel", "inv_container.barrel" -> x = 38;
            case "container.enderchest", "inv_container.enderchest" -> x = 29;
            case "container.chest", "container.chestDouble", "inv_container.chest", "inv_container.chestDouble" -> x = 47;
            default -> x = 11;
        }
        return x;
    }

    private static int getXByCount(Integer count) {
        String string_count = Integer.toString(count);
        return (string_count.length() * 6) + 1;
    }

    // определение координат оффсета
    private static int getCoordByType(String coordType, String type) {
        int coord;
        if ("x".equals(coordType)) {
            switch (type) {
                case "container.shulkerBox", "container.chest", "container.enderchest", "container.barrel",
                        "inv_container.shulkerBox", "inv_container.chest", "inv_container.enderchest", "inv_container.barrel" ->
                        coord = SPwalletConfig.chestOffsetX;
                case "container.chestDouble", "inv_container.chestDouble", "chest_container.chestDouble" -> coord = SPwalletConfig.doubleChestOffsetX;
                default -> coord = SPwalletConfig.inventoryOffsetX;
            }
        } else {
            switch (type) {
                case "container.shulkerBox", "container.chest", "container.enderchest", "container.barrel",
                        "inv_container.shulkerBox", "inv_container.chest", "inv_container.enderchest", "inv_container.barrel" ->
                    coord = SPwalletConfig.chestOffsetY;
                case "container.chestDouble", "inv_container.chestDouble", "chest_container.chestDouble" -> coord = SPwalletConfig.doubleChestOffsetY;
                default -> coord = SPwalletConfig.inventoryOffsetY;
            }
        }
        return coord;
    }

    private static int getIconXByType(String type) {
        int x;
        if (SlotsSelector.isSlotsSelected()) {
            x = 48;
        }
        else if (type.length() == 3 || Objects.equals(type.substring(0, 3), "inv")) {
            x = 2;
        } else if (Objects.equals(type.substring(0, 5), "chest")) {
            x = getTextureXByStorage(type.substring(6));
        }
         else {
            x = getTextureXByStorage(type);
        }
        return x;
    }

    private static int getIconYBySlotsSelected() {
        if (SlotsSelector.isSlotsSelected()) {
            return 0;
        }
        return 56;
    }

    private static String getCountInStacks(int count) {
        if (count >= 64) {
            if (count % 64 != 0) {
                return count / 64 + "." + convertToInt((((double) count) / 64 - count / 64) * 64);
            } else {
                return count / 64 + "ст.";
            }
        }
        return String.valueOf(count);
    }
}