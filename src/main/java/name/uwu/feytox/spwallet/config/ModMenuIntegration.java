package name.uwu.feytox.spwallet.config;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import me.shedaniel.clothconfig2.api.Modifier;
import me.shedaniel.clothconfig2.api.ModifierKeyCode;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ModMenuIntegration implements ModMenuApi {

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return ModMenuIntegration::createConfigScreen;
    }

    private static Screen createConfigScreen(Screen parent) {
        ConfigBuilder builder = ConfigBuilder.create()
                .setParentScreen(parent)
                .setTitle(Text.literal("#SP Wallet"));
        ConfigEntryBuilder entryBuilder = builder.entryBuilder();
        ModConfig config = ModConfig.get();

        builder.setDefaultBackgroundTexture(new Identifier("minecraft:textures/block/cobbled_deepslate.png"));

        builder.getOrCreateCategory(Text.translatable("spwallet.config.generalCategory"))
                .addEntry(entryBuilder.startBooleanToggle(
                            Text.translatable("spwallet.config.enableMod"),
                            config.enableMod)
                        .setDefaultValue(true)
                        .setSaveConsumer(value -> config.enableMod = value)
                        .build())
                .addEntry(entryBuilder.startBooleanToggle(
                                Text.translatable("spwallet.config.simpleMode"),
                                config.simpleMode)
                        .setTooltip(Text.translatable("spwallet.tooltip.simpleMode"))
                        .setDefaultValue(false)
                        .setSaveConsumer(value -> config.simpleMode = value)
                        .build())
                .addEntry(entryBuilder.startBooleanToggle(
                                Text.translatable("spwallet.config.isCountInStacks"),
                                config.isCountInStacks)
                        .setDefaultValue(false)
                        .setSaveConsumer(value -> config.isCountInStacks = value)
                        .build())
                .addEntry(entryBuilder.startBooleanToggle(
                                Text.translatable("spwallet.config.enableShulkerCount"),
                                config.enableShulkerCount)
                        .setDefaultValue(false)
                        .setSaveConsumer(value -> config.enableShulkerCount = value)
                        .build())
                .addEntry(entryBuilder.startBooleanToggle(
                                Text.translatable("spwallet.config.editorMode"),
                                config.editorMode)
                        .setTooltip(Text.translatable("spwallet.tooltip.editorMode"))
                        .setDefaultValue(false)
                        .setSaveConsumer(value -> config.editorMode = value)
                        .build())
                .addEntry(entryBuilder.startBooleanToggle(
                                Text.translatable("spwallet.config.coordsReset"),
                                false)
                        .setDefaultValue(false)
                        .setSaveConsumer(ModConfig::resetCoordsConfig)
                        .build());

        builder.getOrCreateCategory(Text.translatable("spwallet.config.cardCategory"))
                .addEntry(entryBuilder.startBooleanToggle(
                                Text.translatable("spwallet.config.showOnlineCounter"),
                                config.showOnlineCounter)
                        .setDefaultValue(true)
                        .setSaveConsumer(value -> config.showOnlineCounter = value)
                        .build())
                .addEntry(entryBuilder.startBooleanToggle(
                                Text.translatable("spwallet.config.showInInventoryCount"),
                                config.showInInventoryCount)
                        .setDefaultValue(false)
                        .setSaveConsumer(value -> config.showInInventoryCount = value)
                        .build())
                .addEntry(entryBuilder.startIntField(
                                Text.translatable("spwallet.config.reloadCooldown"),
                                config.reloadCooldown)
                        .setDefaultValue(15)
                        .setMin(2)
                        .setSaveConsumer(alpha -> config.reloadCooldown = alpha)
                        .build())
                .addEntry(entryBuilder.startStrField(
                                Text.translatable("spwallet.config.cardId"),
                                config.cardId)
                        .setDefaultValue("")
                        .setTooltip(Text.translatable("spwallet.tooltip.cardId"))
                        .setSaveConsumer(card_id -> config.cardId = card_id)
                        .build())
                .addEntry(entryBuilder.startStrField(
                                Text.translatable("spwallet.config.cardToken"),
                                config.cardToken)
                        .setDefaultValue("")
                        .setTooltip(Text.translatable("spwallet.tooltip.cardId"))
                        .setSaveConsumer(card_token -> config.cardToken = card_token)
                        .build());

        builder.getOrCreateCategory(Text.translatable("spwallet.config.selectCategory"))
                .addEntry(entryBuilder.startColorField(
                                Text.translatable("spwallet.config.select_color"),
                                config.select_color)
                        .setDefaultValue(9237731)
                        .setSaveConsumer(color -> config.select_color = color)
                        .build())
                .addEntry(entryBuilder.startIntField(
                                Text.translatable("spwallet.config.select_alpha"),
                                config.select_alpha)
                        .setDefaultValue(127)
                        .setMin(0)
                        .setMax(255)
                        .setSaveConsumer(alpha -> config.select_alpha = alpha)
                        .build());

        builder.getOrCreateCategory(Text.translatable("spwallet.config.keysCategory"))
                .addEntry(entryBuilder.startModifierKeyCodeField(
                                Text.translatable("spwallet.config.selectKeybind_key"),
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
                                Text.translatable("spwallet.config.showCountInStacks_key"),
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