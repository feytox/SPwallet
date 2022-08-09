package name.uwu.feytox.spwallet.config;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import me.shedaniel.clothconfig2.api.Modifier;
import me.shedaniel.clothconfig2.api.ModifierKeyCode;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.LiteralText;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;

public class ModMenuIntegration implements ModMenuApi {

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return ModMenuIntegration::createConfigScreen;
    }

    private static Screen createConfigScreen(Screen parent) {
        ConfigBuilder builder = ConfigBuilder.create()
                .setParentScreen(parent)
                .setTitle(new LiteralText("#SP Wallet"));
        ConfigEntryBuilder entryBuilder = builder.entryBuilder();
        ModConfig config = ModConfig.get();

        builder.setDefaultBackgroundTexture(new Identifier("minecraft:textures/block/cobbled_deepslate.png"));

        builder.getOrCreateCategory(new TranslatableText("spwallet.config.generalCategory"))
                .addEntry(entryBuilder.startBooleanToggle(
                            new TranslatableText("spwallet.config.enableMod"),
                            config.enableMod)
                        .setDefaultValue(true)
                        .setSaveConsumer(value -> config.enableMod = value)
                        .build())
                .addEntry(entryBuilder.startBooleanToggle(
                                new TranslatableText("spwallet.config.simpleMode"),
                                config.simpleMode)
                        .setTooltip(new TranslatableText("spwallet.tooltip.simpleMode"))
                        .setDefaultValue(false)
                        .setSaveConsumer(value -> config.simpleMode = value)
                        .build())
                .addEntry(entryBuilder.startBooleanToggle(
                                new TranslatableText("spwallet.config.isCountInStacks"),
                                config.isCountInStacks)
                        .setDefaultValue(false)
                        .setSaveConsumer(value -> config.isCountInStacks = value)
                        .build())
                .addEntry(entryBuilder.startBooleanToggle(
                                new TranslatableText("spwallet.config.enableShulkerCount"),
                                config.enableShulkerCount)
                        .setDefaultValue(false)
                        .setSaveConsumer(value -> config.enableShulkerCount = value)
                        .build())
                .addEntry(entryBuilder.startBooleanToggle(
                                new TranslatableText("spwallet.config.editorMode"),
                                config.editorMode)
                        .setTooltip(new TranslatableText("spwallet.tooltip.editorMode"))
                        .setDefaultValue(false)
                        .setSaveConsumer(value -> config.editorMode = value)
                        .build())
                .addEntry(entryBuilder.startBooleanToggle(
                                new TranslatableText("spwallet.config.coordsReset"),
                                false)
                        .setDefaultValue(false)
                        .setSaveConsumer(ModConfig::resetCoordsConfig)
                        .build());

        builder.getOrCreateCategory(new TranslatableText("spwallet.config.cardCategory"))
                .addEntry(entryBuilder.startBooleanToggle(
                                new TranslatableText("spwallet.config.showOnlineCounter"),
                                config.showOnlineCounter)
                        .setDefaultValue(true)
                        .setSaveConsumer(value -> config.showOnlineCounter = value)
                        .build())
                .addEntry(entryBuilder.startBooleanToggle(
                                new TranslatableText("spwallet.config.showInInventoryCount"),
                                config.showInInventoryCount)
                        .setDefaultValue(false)
                        .setSaveConsumer(value -> config.showInInventoryCount = value)
                        .build())
                .addEntry(entryBuilder.startIntField(
                                new TranslatableText("spwallet.config.reloadCooldown"),
                                config.reloadCooldown)
                        .setDefaultValue(15)
                        .setMin(2)
                        .setSaveConsumer(alpha -> config.reloadCooldown = alpha)
                        .build())
                .addEntry(entryBuilder.startStrField(
                                new TranslatableText("spwallet.config.cardId"),
                                config.cardId)
                        .setDefaultValue("")
                        .setTooltip(new TranslatableText("spwallet.tooltip.cardId"))
                        .setSaveConsumer(card_id -> config.cardId = card_id)
                        .build())
                .addEntry(entryBuilder.startStrField(
                                new TranslatableText("spwallet.config.cardToken"),
                                config.cardToken)
                        .setDefaultValue("")
                        .setTooltip(new TranslatableText("spwallet.tooltip.cardId"))
                        .setSaveConsumer(card_token -> config.cardToken = card_token)
                        .build());

        builder.getOrCreateCategory(new TranslatableText("spwallet.config.selectCategory"))
                .addEntry(entryBuilder.startColorField(
                                new TranslatableText("spwallet.config.select_color"),
                                config.select_color)
                        .setDefaultValue(9237731)
                        .setSaveConsumer(color -> config.select_color = color)
                        .build())
                .addEntry(entryBuilder.startIntField(
                                new TranslatableText("spwallet.config.select_alpha"),
                                config.select_alpha)
                        .setDefaultValue(127)
                        .setMin(0)
                        .setMax(255)
                        .setSaveConsumer(alpha -> config.select_alpha = alpha)
                        .build());

        builder.getOrCreateCategory(new TranslatableText("spwallet.config.keysCategory"))
                .addEntry(entryBuilder.startModifierKeyCodeField(
                                new TranslatableText("spwallet.config.selectKeybind_key"),
                                ModifierKeyCode.of(getKeyCode(config.selectKeybind_key),
                                        Modifier.of((short) config.selectKeybind_mod)))
                        .setDefaultValue(ModifierKeyCode.of(getKeyCode(2),
                                Modifier.none()))
                        .setModifierSaveConsumer(modifierKeyCode -> {
                            config.selectKeybind_key = modifierKeyCode.getKeyCode().getCode();
                            config.selectKeybind_mod = modifierKeyCode.getModifier().getValue();
                        })
                        .build())
                .addEntry(entryBuilder.startModifierKeyCodeField(
                                new TranslatableText("spwallet.config.showCountInStacks_key"),
                                ModifierKeyCode.of(getKeyCode(config.showCountInStacks_key),
                                        Modifier.of((short) config.showCountInStacks_mod)))
                        .setDefaultValue(ModifierKeyCode.of(InputUtil.Type.KEYSYM.createFromCode(71),
                                Modifier.none()))
                        .setModifierSaveConsumer(modifierKeyCode -> {
                            config.showCountInStacks_key = modifierKeyCode.getKeyCode().getCode();
                            config.showCountInStacks_mod = modifierKeyCode.getModifier().getValue();
                        })
                        .build());

        builder.transparentBackground();

        return builder
                .setSavingRunnable(ModConfig::save)
                .build();
    }

    private static InputUtil.Key getKeyCode(int code) {
        return code > 3 ? InputUtil.Type.KEYSYM.createFromCode(code) : InputUtil.Type.MOUSE.createFromCode(code);
    }
}