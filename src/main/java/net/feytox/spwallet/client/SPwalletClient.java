package net.feytox.spwallet.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.Window;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.collection.DefaultedList;

import java.util.Objects;

@Environment(EnvType.CLIENT)
public class SPwalletClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        SPwalletConfig.init("spwallet", SPwalletConfig.class);
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
}
