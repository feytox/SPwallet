package name.uwu.feytox.spwallet.counter;

import name.uwu.feytox.spwallet.config.ModConfig;
import name.uwu.feytox.spwallet.spapi.OnlineWallet;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableTextContent;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.registry.Registry;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class SingleCounter {
    public ContainerType containerType;
    public int count;

    public static SingleCounter inventory() {
        if (SlotsSelector.isSlotsSelected()) {
            return SingleCounter.selectorCount();
        }

        int count = getCount();
        if (ModConfig.get().showInInventoryCount) {
            count += OnlineWallet.getCurrentBalance2() != -621 ? OnlineWallet.getCurrentBalance2() : 0;
        }

        return new SingleCounter(ContainerType.INVENTORY, count);
    }

    public static SingleCounter chest() {
        return new SingleCounter(ContainerType.CHEST);
    }

    public static SingleCounter doubleChest() {
        return new SingleCounter(ContainerType.DOUBLE_CHEST);
    }

    public static SingleCounter enderChest() {
        return new SingleCounter(ContainerType.ENDER_CHEST);
    }

    public static SingleCounter barrel() {
        return new SingleCounter(ContainerType.BARREL);
    }

    public static SingleCounter shulker(DefaultedList<ItemStack> itemStacks) {
        return new SingleCounter(ContainerType.SHULKER, getCount(itemStacks));
    }

    public static SingleCounter allCount(SingleCounter first, SingleCounter second) {
        if (SlotsSelector.isSlotsSelected()) {
            return SingleCounter.selectorCount();
        }
        return new SingleCounter(ContainerType.ALL_COUNT, first.count + second.count);
    }

    public static SingleCounter selectorCount() {
        return new SingleCounter(ContainerType.SELECTOR, SlotsSelector.getCount());
    }

    public static SingleCounter card() {
        return new SingleCounter(ContainerType.CARD, OnlineWallet.getCurrentBalance2());
    }

    public static SingleCounter getChestCounter(Screen screen) {
        Text screenTitle = screen.getTitle();

        if (screenTitle instanceof TranslatableTextContent) {
            String screenName = ((TranslatableTextContent) screenTitle).getKey();

            switch (screenName) {
                case "container.chest" -> {
                    return SingleCounter.chest();
                }
                case "container.chestDouble" -> {
                    return SingleCounter.doubleChest();
                }
                case "container.enderchest" -> {
                    return SingleCounter.enderChest();
                }
                case "container.barrel" -> {
                    return SingleCounter.barrel();
                }
            }
        }

        return SingleCounter.chest();
    }

    private SingleCounter(ContainerType containerType, int count) {
        this.containerType = containerType;
        this.count = count;
    }

    private SingleCounter(ContainerType containerType) {
        this.containerType = containerType;
        MinecraftClient client = MinecraftClient.getInstance();

        if (client.player != null) {
            ScreenHandler screenHandler = client.player.currentScreenHandler;
            if (screenHandler != null) {
                Inventory inventory = ((GenericContainerScreenHandler) screenHandler).getInventory();
                this.count = getCount(inventory);
                return;
            }
        }

        this.count = 0;
    }

    public static Item getWalletItem() {
        return Items.DIAMOND_ORE;
    }

    private static int getCount() {
        return getCount(getWalletItem());
    }

    private static int getCount(Inventory inventory) {
        return getCount(inventory, getWalletItem());
    }

    private static int getCount(Item item) {
        return getCount(MinecraftClient.getInstance().player.getInventory(), item);
    }

    private static int getCount(Inventory inventory, Item item) {
        int result = inventory.count(item);
        result += inventory.count(Items.DEEPSLATE_DIAMOND_ORE);

        if (ModConfig.get().enableShulkerCount) {
            result += getShulkerCount(() -> {
               List<ItemStack> itemStacks = new ArrayList<>();

                for(int j = 0; j < inventory.size(); ++j) {
                    ItemStack itemStack = inventory.getStack(j);
                    if (Registry.ITEM.getId(itemStack.getItem()).toString().contains("shulker_box")) {
                        itemStacks.add(itemStack);
                    }
                }

                return itemStacks;
            });
        }

        return result;
    }

    public static int getCount(DefaultedList<ItemStack> itemStacks) {
        int count = getCountInItemStacks(itemStacks, getWalletItem());
        count += getCountInItemStacks(itemStacks, Items.DEEPSLATE_DIAMOND_ORE);

        if (ModConfig.get().enableShulkerCount) {
            count += getShulkerCount(() -> {
                List<ItemStack> result = new ArrayList<>();
                itemStacks.forEach(itemStack -> {
                    if (Registry.ITEM.getId(itemStack.getItem()).toString().contains("shulker_box")) {
                        result.add(itemStack);
                    }
                });
                return result;
            });
        }

        return count - getCount();
    }

    private static int getCountInItemStacks(DefaultedList<ItemStack> itemStacks, Item item) {
        int count = 0;
        for (ItemStack itemStack: itemStacks) {
            if (itemStack.getItem().equals(item)) {
                count += itemStack.getCount();
            }
        }

        return count;
    }

    public static int getShulkerCount(Supplier<List<ItemStack>> shulkersGetter) {
        int count = 0;

        String walletItem_id = Registry.ITEM.getId(getWalletItem()).toString();
        String extraItem_id = Registry.ITEM.getId(Items.DEEPSLATE_DIAMOND_ORE).toString();
        List<ItemStack> shulkers = shulkersGetter.get();

        List<SimpleStack> allShulkerItems = new ArrayList<>();
        try {
            shulkers.forEach(itemStack -> {
                NbtList nbtItems = (NbtList) itemStack.getNbt().getCompound("BlockEntityTag").get("Items");
                nbtItems.forEach(nbtElement -> {
                    NbtCompound nbtItem = (NbtCompound) nbtElement;
                    allShulkerItems.add(new SimpleStack(nbtItem.getString("id"), nbtItem.getByte("Count")));
                });
            });
        } catch (NullPointerException ignored) {}

        for (SimpleStack simpleStack : allShulkerItems) {
            if (walletItem_id.equals(simpleStack.item_id) || extraItem_id.equals(simpleStack.item_id)) {
                count += simpleStack.count;
            }
        }

        return count;
    }

    public static class SimpleStack {
        String item_id;
        int count;

        public SimpleStack(String item_id, int count) {
            this.item_id = item_id;
            this.count = count;
        }
    }
}
