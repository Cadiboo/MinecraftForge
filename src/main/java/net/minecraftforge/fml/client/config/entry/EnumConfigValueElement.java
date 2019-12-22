package net.minecraftforge.fml.client.config.entry;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.client.config.ConfigEntryListWidget;
import net.minecraftforge.fml.client.config.ConfigScreen;
import net.minecraftforge.fml.config.ModConfig;

import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

public class EnumConfigValueElement implements IConfigValueElement<Enum<?>> {

	final EntryConfigValue<Enum<?>> entryConfigValue;

	public EnumConfigValueElement(final List<String> path, final ModConfig modConfig, final ForgeConfigSpec.ConfigValue<Enum<?>> configValue) {
		entryConfigValue = new EntryConfigValue<>(path, modConfig, modConfig.getSpec().get(path), configValue);
	}

	@Override
	public boolean isProperty() {
		return true;
	}

	@Override
	public ConfigListEntry makeConfigEntry(final ConfigScreen configScreen, final ConfigEntryListWidget configEntryListScreen) {
		return new EnumConfigListEntry(this, configScreen, configEntryListScreen);
	}

	@Override
	public String getName() {
		return entryConfigValue.getLabel();
	}

	@Override
	public String getTranslationKey() {
		return entryConfigValue.getLabel();
	}

	@Override
	public String getComment() {
		return entryConfigValue.getComment();
	}

	@Override
	public List<IConfigValueElement<?>> getChildElements() {
		return Collections.emptyList();
	}

	@Override
	public boolean isList() {
		return false;
	}

	@Override
	public boolean isListLengthFixed() {
		return false;
	}

	@Override
	public int getMaxListLength() {
		return 0;
	}

	@Override
	public boolean isDefault() {
		return entryConfigValue.isDefault();
	}

	@Override
	public Enum<?> getDefault() {
		return entryConfigValue.getDefaultValue();
	}

	@Override
	public Enum<?>[] getDefaults() {
		return new Enum[0];
	}

	@Override
	public void resetToDefault() {
		entryConfigValue.resetToDefault();
	}

	@Override
	public boolean requiresWorldRestart() {
		return entryConfigValue.requiresWorldRestart();
	}

	@Override
	public boolean showInGui() {
		return true;
	}

	@Override
	public boolean requiresMcRestart() {
		return entryConfigValue.requiresMcRestart();
	}

	@Override
	public Enum<?> get() {
		return entryConfigValue.getCurrentValue();
	}

	@Override
	public Enum<?>[] getList() {
		return new Enum[0];
	}

	@Override
	public void set(final Enum value) {
		entryConfigValue.setCurrentValue(value);
	}

	@Override
	public void set(final Enum[] aVal) {
	}

	@Override
	public String[] getValidValues() {
		return new String[0];
	}

	@Override
	public Enum<?> getMinValue() {
		return null;
	}

	@Override
	public Enum<?> getMaxValue() {
		return null;
	}

	@Override
	public Pattern getValidationPattern() {
		return null;
	}

}
