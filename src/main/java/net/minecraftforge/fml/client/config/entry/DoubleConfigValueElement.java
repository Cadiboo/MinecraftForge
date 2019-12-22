package net.minecraftforge.fml.client.config.entry;

import net.minecraftforge.common.ForgeConfigSpec.ConfigValue;
import net.minecraftforge.fml.client.config.ConfigEntryListWidget;
import net.minecraftforge.fml.client.config.ConfigScreen;
import net.minecraftforge.fml.config.ModConfig;

import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

public class DoubleConfigValueElement implements IConfigValueElement<Double> {

	final EntryConfigValue<Double> entryConfigValue;

	public DoubleConfigValueElement(final List<String> path, final ModConfig modConfig, final ConfigValue<Double> configValue) {
		entryConfigValue = new EntryConfigValue<>(path, modConfig, modConfig.getSpec().get(path), configValue);
	}

	@Override
	public boolean isProperty() {
		return true;
	}

	@Override
	public ConfigListEntry makeConfigEntry(final ConfigScreen configScreen, final ConfigEntryListWidget configEntryListScreen) {
		return new DoubleConfigListEntry(this, configScreen, configEntryListScreen);
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
	public Double getDefault() {
		return entryConfigValue.getDefaultValue();
	}

	@Override
	public Double[] getDefaults() {
		return new Double[]{getDefault()};
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
	public Double get() {
		return entryConfigValue.getCurrentValue();
	}

	@Override
	public Double[] getList() {
		return new Double[]{get()};
	}

	@Override
	public void set(final Double value) {
		entryConfigValue.setCurrentValue(value);
	}

	@Override
	public void set(final Double[] aVal) {
	}

	@Override
	public String[] getValidValues() {
		return new String[0];
	}

	@Override
	public Double getMinValue() {
		return entryConfigValue.getCurrentValue();
	}

	@Override
	public Double getMaxValue() {
		return entryConfigValue.getCurrentValue();
	}

	@Override
	public Pattern getValidationPattern() {
		return Pattern.compile(".+");
	}

}
