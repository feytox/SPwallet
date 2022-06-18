package net.feytox.spwallet.client;

import eu.midnightdust.lib.config.MidnightConfig;

public class SPwalletConfig extends MidnightConfig {
    @Entry
    public static boolean showCounter = true;
    @Entry
    public static boolean simpleMode = false;

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

    @Entry(min=-999999, max=999999)
    public static int simpleInvX_withRecipeBook = 116;
    @Entry(min=-999999, max=999999)
    public static int simpleInvY_withRecipeBook = 5;
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
