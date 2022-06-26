package net.feytox.spwallet.client;

import eu.midnightdust.lib.config.MidnightConfig;
import org.lwjgl.glfw.GLFW;

public class SPwalletConfig extends MidnightConfig {

    @Comment
    public static Comment mainSettings;
    @Entry
    public static boolean showCounter = true;
    @Entry
    public static boolean simpleMode = false;
    @Entry
    public static boolean isCountInStacks = false;

    @Comment
    public static Comment selectorSettings;

    @Entry(width = 7, min = 7, isColor = true)
    public static String select_color = "#8cf4e3";
    @Entry
    public static int select_alpha = 127;

    @Comment
    public static Comment hotkeysSettings;
    @Entry
    public static KeybindsEnum selectKeybind_key = KeybindsEnum.MMB;
    @Entry
    public static KeybindsEnum showCountInStacks_key = KeybindsEnum.G;

    public enum KeybindsEnum {
        VANILLA(-1),
        W(GLFW.GLFW_KEY_W),
        D(GLFW.GLFW_KEY_D),
        G(GLFW.GLFW_KEY_G),
        CTRL(GLFW.GLFW_KEY_LEFT_CONTROL),
        ALT(GLFW.GLFW_KEY_LEFT_ALT),
        MMB(GLFW.GLFW_MOUSE_BUTTON_MIDDLE);

        private final int keycode;
        KeybindsEnum(int keycode) {
            this.keycode = keycode;
        }
        public int getKeycode() { return keycode; }

    }

    @Comment
    public static Comment hudSettings;

    @Entry(min=-999999, max=999999)
    public static int inventoryOffsetX = -24;
    @Entry(min=-999999, max=999999)
    public static int inventoryOffsetY = -84;
    @Entry(min=-999999, max=999999)
    public static int inventoryOffsetX_withRecipeBook = -24;
    @Entry(min=-999999, max=999999)
    public static int inventoryOffsetY_withRecipeBook = -84;

    @Entry(min=-999999, max=999999)
    public static int chestOffsetX = -24;
    @Entry(min=-999999, max=999999)
    public static int chestOffsetY = -84;

    @Entry(min=-999999, max=999999)
    public static int doubleChestOffsetX = 89;
    @Entry(min=-999999, max=999999)
    public static int doubleChestOffsetY = -12;

    @Comment
    public static Comment simpleModeSettings;

    @Entry(min=-999999, max=999999)
    public static int simpleInvX_withRecipeBook = 116;
    @Entry(min=-999999, max=999999)
    public static int simpleInvY_withRecipeBook = 10;
    @Entry(min=-999999, max=999999)
    public static int simpleInvX = 40;
    @Entry(min=-999999, max=999999)
    public static int simpleInvY = 10;

    @Entry(min=-999999, max=999999)
    public static int simpleChestX_inv = 40;
    @Entry(min=-999999, max=999999)
    public static int simpleChestY_inv = 10;

    @Entry(min=-999999, max=999999)
    public static int simpleChestX_chest = 40;
    @Entry(min=-999999, max=999999)
    public static int simpleChestY_chest = 76;

    @Entry(min=-999999, max=999999)
    public static int simpleChestX_all = 0;
    @Entry(min=-999999, max=999999)
    public static int simpleChestY_all = 10;

    @Entry(min=-999999, max=999999)
    public static int simpleDoubleChestX_inv = 40;
    @Entry(min=-999999, max=999999)
    public static int simpleDoubleChestY_inv = -18;

    @Entry(min=-999999, max=999999)
    public static int simpleDoubleChestX_chest = 40;
    @Entry(min=-999999, max=999999)
    public static int simpleDoubleChestY_chest = 104;

    @Entry(min=-999999, max=999999)
    public static int simpleDoubleChestX_all = 0;
    @Entry(min=-999999, max=999999)
    public static int simpleDoubleChestY_all = -18;
}
