package net.feytox.spwallet.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.client.util.Window;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.collection.DefaultedList;
import org.lwjgl.glfw.GLFW;

import java.util.Objects;

@Environment(EnvType.CLIENT)
public class SPwalletClient implements ClientModInitializer {

    public static int ticks = 0;
    public static Screen lastScreen;

    public static KeyBinding selectSlot_keybind = KeyBindingHelper.registerKeyBinding(new KeyBinding("spwallet.midnightconfig.selectKeybind_key",
            InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_UNKNOWN, "key.category.spwallet"));
    public static KeyBinding selectSlot_shadowKey = new KeyBinding("", InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_UNKNOWN, "");

    public static KeyBinding showCountInStacks_keybind = KeyBindingHelper.registerKeyBinding(new KeyBinding("spwallet.midnightconfig.showCountInStacks_key",
            InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_UNKNOWN, "key.category.spwallet"));
    public static KeyBinding showCountInStacks_shadowKey = new KeyBinding("", InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_UNKNOWN, "");

    @Override
    public void onInitializeClient() {
        SPwalletConfig.init("spwallet", SPwalletConfig.class);
        OnlineWallet.initCommand();

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (ticks > -1) {
                ticks += 1;
            }
            if (ticks >= SPwalletConfig.reloadCooldown * 20) {
                ticks = -1;
            }

            if (lastScreen != client.currentScreen) {
                SlotsSelector.cleanSelectedSlots();
                lastScreen = client.currentScreen;
            }
        });
    }

    public static int getItemCount(Inventory inventory, Item item) {
        return inventory.count(item);
    }

    public static int getItemCount(Item item) {
        return getItemCount(MinecraftClient.getInstance().player.getInventory(), item);
    }

    public static int getCountFromItemStacks(DefaultedList<ItemStack> itemStacks) {
        int count = 0;
        for (ItemStack itemStack: itemStacks) {
            if (Objects.equals(itemStack.getItem(), Items.DEEPSLATE_DIAMOND_ORE) |
                    Objects.equals(itemStack.getItem(), Items.DIAMOND_ORE)) {
                count += itemStack.getCount();
            }
        }
        return count - getItemCount();
    }

    public static int getItemCount(Inventory inventory) {
        return getItemCount(inventory, Items.DIAMOND_ORE) + getItemCount(inventory, Items.DEEPSLATE_DIAMOND_ORE);
    }

    public static int getItemCount() {
        return getItemCount(Items.DIAMOND_ORE) + getItemCount(Items.DEEPSLATE_DIAMOND_ORE);
    }

    public static int getCoordFromCenter(String type, int coord) {
        Window window = MinecraftClient.getInstance().getWindow();
        if ("x".equals(type)) {
            coord += window.getScaledWidth() / 2;
        } else {
            coord = (window.getScaledHeight() / 2) - coord;
        }
        return coord;
    }

    public static int convertToInt(Object num) {
        if (num instanceof Double) {
            return (int) Math.round((Double) num);
        } else {
            return (int) num;
        }
    }
}
