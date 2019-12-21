package net.minecraftforge.fml.client.config.entry;

import net.minecraftforge.common.ForgeConfigSpec.ConfigValue;
import net.minecraftforge.fml.client.config.ConfigEntryListWidget;
import net.minecraftforge.fml.client.config.ConfigScreen;
import net.minecraftforge.fml.config.ModConfig;

import java.time.temporal.Temporal;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

public class TemporalConfigValueElement implements IConfigValueElement<Temporal> {

	final EntryConfigValue<Temporal> entryConfigValue;

	public TemporalConfigValueElement(final List<String> path, final ModConfig modConfig, final ConfigValue<Temporal> configValue) {
		entryConfigValue = new EntryConfigValue<>(path, modConfig, modConfig.getSpec().get(path), configValue);
	}

	@Override
	public boolean isProperty() {
		return true;
	}

	@Override
	public ConfigListEntry makeConfigEntry(final ConfigScreen configScreen, final ConfigEntryListWidget configEntryListScreen) {
		return new TemporalConfigListEntry(this, configScreen, configEntryListScreen);
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
	public List<IConfigValueElement> getChildElements() {
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
	public Temporal getDefault() {
		return entryConfigValue.getDefaultValue();
	}

	@Override
	public Temporal[] getDefaults() {
		return new Temporal[]{getDefault()};
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
	public Temporal get() {
		return entryConfigValue.getCurrentValue();
	}

	@Override
	public Temporal[] getList() {
		return new Temporal[]{get()};
	}

	@Override
	public void set(final Temporal value) {
		entryConfigValue.setCurrentValue(value);
	}

	@Override
	public void set(final Temporal[] aVal) {
	}

	@Override
	public String[] getValidValues() {
		return new String[0];
	}

	// TODO: This could actually be implemented?
	@Override
	public Temporal getMinValue() {
		return entryConfigValue.getCurrentValue();
	}

	// TODO: This could actually be implemented?
	@Override
	public Temporal getMaxValue() {
		return entryConfigValue.getCurrentValue();
	}

	@Override
	public Pattern getValidationPattern() {
		return Pattern.compile("[0-9]{1,4}-[0-9]{1,2}-[0-9]{1,2}");
	}

}
