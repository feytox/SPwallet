package ru.feytox.spwallet.config;

import blue.endless.jankson.Jankson;
import blue.endless.jankson.JsonElement;
import blue.endless.jankson.JsonObject;
import blue.endless.jankson.JsonPrimitive;
import blue.endless.jankson.api.SyntaxError;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import net.fabricmc.loader.api.FabricLoader;
import org.lwjgl.glfw.GLFW;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

import static ru.feytox.spwallet.SPwalletClient.LOGGER;
import static ru.feytox.spwallet.SPwalletClient.MOD_ID;

@Config(name = MOD_ID)
public class ModConfig implements ConfigData {

    public static ModConfig get() {
        return AutoConfig.getConfigHolder(ModConfig.class).getConfig();
    }

    public static void save() {
        AutoConfig.getConfigHolder(ModConfig.class).save();
    }

    public static void load() {
        AutoConfig.getConfigHolder(ModConfig.class).load();
    }

    public String config_version = "v1";

    // p.s. -621 = empty state
    public int cachedBalance = -621;

    // main settings
    public boolean enableMod = true;
    public boolean darkMode = false;
    public boolean simpleMode = false;
    public boolean isCountInStacks = false;
    public boolean enableShulkerCount = false;
    public boolean editorMode = false;

    // selector settings
    public int select_color = 9237731;
    public int select_alpha = 127;

    // keybinds settings
    public int selectKeybind_key = GLFW.GLFW_MOUSE_BUTTON_MIDDLE;
    public int selectKeybind_mod = 0;
    public int showCountInStacks_key = GLFW.GLFW_KEY_G;
    public int showCountInStacks_mod = 0;

    // for Cards
    public boolean showOnlineCounter = true;
    public boolean showInInventoryCount = false;
    public int reloadCooldown = 15;
    public String cardId = "";
    public String cardToken = "";


    // non-simple mode
    public int inventoryOffsetX = -52;
    public int inventoryOffsetY = -84;
    public int inventoryOffsetX_withRecipeBook = -52;
    public int inventoryOffsetY_withRecipeBook = -84;

    // for SP Wallet
    public int onlineOffsetX = 1;
    public int onlineOffsetY = -84;
    public int onlineOffsetX_withRecipeBook = 1;
    public int onlineOffsetY_withRecipeBook = -84;

    
    public int chestOffsetX = -24;
    public int chestOffsetY = -84;
    
    public int doubleChestOffsetX = 89;
    public int doubleChestOffsetY = -12;

    // simple mode
    public int simpleInvX = 40;
    public int simpleInvY = 10;
    public int simpleInvX_withRecipeBook = 116;
    public int simpleInvY_withRecipeBook = 10;

    // for SP Wallet
    public int simpleOnlineOffsetX = 40;
    public int simpleOnlineOffsetY = 20;
    public int simpleOnlineOffsetX_withRecipeBook = 116;
    public int simpleOnlineOffsetY_withRecipeBook = 20;
    
    public int simpleChestX_inv = 40;
    public int simpleChestY_inv = 10;
    
    public int simpleChestX_chest = 40;
    public int simpleChestY_chest = 76;
    
    public int simpleChestX_all = 0;
    public int simpleChestY_all = 10;
    
    public int simpleDoubleChestX_inv = 40;
    public int simpleDoubleChestY_inv = -18;
    
    public int simpleDoubleChestX_chest = 40;
    public int simpleDoubleChestY_chest = 104;
    
    public int simpleDoubleChestX_all = 0;
    public int simpleDoubleChestY_all = -18;

    public static void resetCoordsConfig(boolean isReset) {
        if (isReset) {
            ModConfig config = get();

            // non-simple mode
            config.inventoryOffsetX = -52;
            config.inventoryOffsetY = -84;
            config.inventoryOffsetX_withRecipeBook = -52;
            config.inventoryOffsetY_withRecipeBook = -84;

            // for SP Wallet
            config.onlineOffsetX = 1;
            config.onlineOffsetY = -84;
            config.onlineOffsetX_withRecipeBook = 1;
            config.onlineOffsetY_withRecipeBook = -84;

            config.chestOffsetX = -52;
            config.chestOffsetY = -84;
            config.doubleChestOffsetX = 89;
            config.doubleChestOffsetY = -12;

            // simple mode
            config.simpleInvX = 40;
            config.simpleInvY = 10;
            config.simpleInvX_withRecipeBook = 116;
            config.simpleInvY_withRecipeBook = 10;

            // for SP Wallet
            config.simpleOnlineOffsetX = 40;
            config.simpleOnlineOffsetY = 20;
            config.simpleOnlineOffsetX_withRecipeBook = 116;
            config.simpleOnlineOffsetY_withRecipeBook = 20;


            config.simpleChestX_inv = 40;
            config.simpleChestY_inv = 10;

            config.simpleChestX_chest = 40;
            config.simpleChestY_chest = 76;

            config.simpleChestX_all = 0;
            config.simpleChestY_all = 10;

            config.simpleDoubleChestX_inv = 40;
            config.simpleDoubleChestY_inv = -18;

            config.simpleDoubleChestX_chest = 40;
            config.simpleDoubleChestY_chest = 104;

            config.simpleDoubleChestX_all = 0;
            config.simpleDoubleChestY_all = -18;
        }
    }

    public static void checkConfigUpdates() {
        // old SP Wallet -> new SP Wallet
        Path newCfgPath = FabricLoader.getInstance().getConfigDir().resolve("spwallet.json5");
        if (!newCfgPath.toFile().exists()) {
            Path oldCfgPath = FabricLoader.getInstance().getConfigDir().resolve("spwallet.json");
            try {
                Files.move(oldCfgPath, newCfgPath, StandardCopyOption.REPLACE_EXISTING);

                try {
                    JsonObject configObject = Jankson
                            .builder()
                            .build()
                            .load(newCfgPath.toFile());

                    configObject.remove("selectKeybind_key");
                    configObject.remove("showCountInStacks_key");

                    if (configObject.containsKey("select_color")) {
                        //  Color.getColor(String.valueOf(configObject.get("select_color"))).getRGB()
                        JsonElement colorLine = configObject.get("select_color");
                        if (colorLine instanceof JsonPrimitive) {
                            String hex = ((JsonPrimitive) colorLine).asString();
                            int color = Integer.parseInt(hex.replaceFirst("#", ""), 16);
                            configObject.put("select_color", new JsonPrimitive(color));
                        }
                    }

                    BufferedWriter writer = Files.newBufferedWriter(newCfgPath);
                    writer.write(configObject.toJson());
                    writer.close();
                }
                catch (IOException | SyntaxError e) {
                    e.printStackTrace();
                }

                LOGGER.info("Successfully updated from 1.2.4");
            } catch (IOException e) {
                LOGGER.error("An error occurred while updating from 1.2.4");
                e.printStackTrace();
            }
        }
    }
}
