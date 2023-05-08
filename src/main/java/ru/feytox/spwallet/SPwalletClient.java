package ru.feytox.spwallet;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.feytox.spwallet.config.ModConfig;
import ru.feytox.spwallet.counter.SlotsSelector;
import ru.feytox.spwallet.gui.CounterEditor;
import ru.feytox.spwallet.spapi.OnlineWallet;

@Environment(EnvType.CLIENT)
public class SPwalletClient implements ClientModInitializer {

    public static final String MOD_ID = "spwallet";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static Screen lastScreen;

    public static int online_ticks = 0;

    @Override
    public void onInitializeClient() {
        ModConfig.checkConfigUpdates();

        AutoConfig.register(ModConfig.class, JanksonConfigSerializer::new);

        ModConfig config = ModConfig.get();
        if ((config.inventoryOffsetX - config.onlineOffsetX) == -25
                && config.inventoryOffsetX == -24 && config.inventoryOffsetX_withRecipeBook == -24) {

            config.inventoryOffsetX = -52;
            config.inventoryOffsetX_withRecipeBook = -52;

            ModConfig.save();
            LOGGER.info("Successfully updated from 1.1.3");
        }

        OnlineWallet.initCommand();

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (online_ticks > -1) {
                online_ticks += 1;
            }

            int cooldown = OnlineWallet.isBadAPI ? 60 : ModConfig.get().reloadCooldown;
            if (online_ticks >= cooldown * 20) {
                online_ticks = -1;
            }

            if (lastScreen != client.currentScreen) {
                SlotsSelector.cleanSelectedSlots();
                if (CounterEditor.isActive()) {
                    CounterEditor.discard();
                }
                lastScreen = client.currentScreen;
            }
        });
    }

    public static void playSound(SoundEvent sound) {
        MinecraftClient client = MinecraftClient.getInstance();

        if (client.world != null && client.player != null) {
            client.player.playSound(sound, SoundCategory.BLOCKS, 0.5f, 1f);
        }
    }
}
