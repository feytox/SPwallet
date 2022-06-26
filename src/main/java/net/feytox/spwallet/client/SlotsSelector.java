package net.feytox.spwallet.client;

import net.feytox.spwallet.mixin.HandledScreenAccessor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.screen.ingame.GenericContainerScreen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.gui.screen.ingame.ShulkerBoxScreen;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.Window;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.math.ColorHelper;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class SlotsSelector {

    private static Map<Integer, Slot> selectedSlots = new HashMap<>();

    public static void selectSlot () {
        MinecraftClient client = MinecraftClient.getInstance();
        SPwalletClient.lastScreen = client.currentScreen;
        if (client.currentScreen instanceof GenericContainerScreen || client.currentScreen instanceof ShulkerBoxScreen
                || client.currentScreen instanceof InventoryScreen) {
            HandledScreen<?> handledScreen = (HandledScreen<?>) client.currentScreen;
            Slot selectedSlot = ((HandledScreenAccessor) handledScreen).getFocusedSlot();
            if (selectedSlot != null) {
                if (!selectedSlots.containsKey(selectedSlot.id)) {
                    Item item = selectedSlot.getStack().getItem();
                    if (Objects.equals(item, Items.DEEPSLATE_DIAMOND_ORE) |
                            Objects.equals(item, Items.DIAMOND_ORE ) | Objects.equals(item, Items.AIR)) {
                        selectedSlots.put(selectedSlot.id, selectedSlot);
                    }
                } else {
                    selectedSlots.remove(selectedSlot.id);
                }
            }
        }
    }

    public static void cleanSelectedSlots () {
        selectedSlots.clear();
    }
    public static void highlightSlots (MatrixStack matrices) {
        Window window = MinecraftClient.getInstance().getWindow();
        HandledScreen<?> handledScreen = (HandledScreen<?>) MinecraftClient.getInstance().currentScreen;

        int x = (window.getScaledWidth() - ((HandledScreenAccessor) handledScreen).getBackgroundWidth()) / 2;
        if (handledScreen instanceof InventoryScreen && ((InventoryScreen) handledScreen).getRecipeBookWidget().isOpen()) {
            x += 77;
        }
        int y = (window.getScaledHeight() - ((HandledScreenAccessor) handledScreen).getBackgroundHeight()) / 2;
        for (Integer slot_id: selectedSlots.keySet()) {
            Slot slot = selectedSlots.get(slot_id);
            int x1 = x + slot.x;
            int y1 = y + slot.y;
            Color selectColor = Color.decode(SPwalletConfig.select_color);
            DrawableHelper.fill(matrices, x1, y1, x1+16, y1+16, ColorHelper.Argb.getArgb(SPwalletConfig.select_alpha,
                    selectColor.getRed(), selectColor.getGreen(), selectColor.getBlue()));
        }
    }

    public static int getSlotsCount () {
        int count = 0;
        for (Integer slot_id: selectedSlots.keySet()) {
            Slot slot = selectedSlots.get(slot_id);
            ItemStack itemStack = slot.getStack();
            if (Objects.equals(itemStack.getItem(), Items.DEEPSLATE_DIAMOND_ORE) |
                    Objects.equals(itemStack.getItem(), Items.DIAMOND_ORE)) {
                count += itemStack.getCount();
            }
        }
        return count;
    }

    public static boolean isSlotsSelected () {
        return !selectedSlots.isEmpty();
    }
}
