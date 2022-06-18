package net.feytox.spwallet.client;

import eu.midnightdust.lib.config.MidnightConfig;

public class SPwalletConfig extends MidnightConfig {
    @Entry
    public static boolean showCounter = true;
    @Entry
    public static boolean simpleMode = false;

    @Entry(min=-999999, max=999999)
    public static int inventoryOffsetX = -139;
    @Entry(min=-999999, max=999999)
    public static int inventoryOffsetY = 83;
    @Entry(min=-999999, max=999999)
    public static int inventoryOffsetX_withRecipeBook = -60;
    @Entry(min=-999999, max=999999)
    public static int inventoryOffsetY_withRecipeBook = 103;

    @Entry(min=-999999, max=999999)
    public static int chestOffsetX = -139;
    @Entry(min=-999999, max=999999)
    public static int chestOffsetY = 84;

    @Entry(min=-999999, max=999999)
    public static int doubleChestOffsetX = -139;
    @Entry(min=-999999, max=999999)
    public static int doubleChestOffsetY = 111;

    @Entry(min=-999999, max=999999)
    public static int simpleInvX_withRecipeBook = 116;
    @Entry(min=-999999, max=999999)
    public static int simpleInvY_withRecipeBook = 5;
    @Entry(min=-999999, max=999999)
    public static int simpleInvX = 39;
    @Entry(min=-999999, max=999999)
    public static int simpleInvY = 16;

    @Entry(min=-999999, max=999999)
    public static int simpleChestX_inv = 5;
    @Entry(min=-999999, max=999999)
    public static int simpleChestY_inv = 8;

    @Entry(min=-999999, max=999999)
    public static int simpleChestX_chest = 5;
    @Entry(min=-999999, max=999999)
    public static int simpleChestY_chest = -4;

    @Entry(min=-999999, max=999999)
    public static int simpleChestX_all = 5;
    @Entry(min=-999999, max=999999)
    public static int simpleChestY_all = -13;

    @Entry(min=-999999, max=999999)
    public static int simpleDoubleChestX_inv = 5;
    @Entry(min=-999999, max=999999)
    public static int simpleDoubleChestY_inv = 5;

    @Entry(min=-999999, max=999999)
    public static int simpleDoubleChestX_chest = 5;
    @Entry(min=-999999, max=999999)
    public static int simpleDoubleChestY_chest = -4;

    @Entry(min=-999999, max=999999)
    public static int simpleDoubleChestX_all = 5;
    @Entry(min=-999999, max=999999)
    public static int simpleDoubleChestY_all = -13;
}
