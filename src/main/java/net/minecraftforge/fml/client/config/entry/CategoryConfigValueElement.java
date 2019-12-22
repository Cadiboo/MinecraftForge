package net.minecraftforge.fml.client.config.entry;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.client.config.ConfigEntryListWidget;
import net.minecraftforge.fml.client.config.ConfigScreen;

import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

public class CategoryConfigValueElement implements IConfigValueElement<String> {

	final String title;
	private final List<IConfigValueElement<?>> list;

	public CategoryConfigValueElement(final String title, final List<IConfigValueElement<?>> list) {
		this.title = title;
		this.list = list;
	}

	@Override
	public boolean isProperty() {
		return true;
	}

	@Override
	public ConfigListEntry makeConfigEntry(final ConfigScreen configScreen, final ConfigEntryListWidget configEntryListScreen) {
		return new CategoryConfigListEntry(configScreen, configEntryListScreen, this);
	}

	@Override
	public String getName() {
		return title;
	}

	@Override
	public String getTranslationKey() {
		return title;
	}

	@Override
	public String getComment() {
		return "Uhhhhhhh help Idk what code to write here";
	}

	@Override
	public List<IConfigValueElement<?>> getChildElements() {
		return list;
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
		return true;
	}

	@Override
	public String getDefault() {
		return title;
	}

	@Override
	public String[] getDefaults() {
		return new String[]{getDefault()};
	}

	@Override
	public void resetToDefault() {
	}

	@Override
	public boolean requiresWorldRestart() {
		return false;
	}

	@Override
	public boolean showInGui() {
		return true;
	}

	@Override
	public boolean requiresMcRestart() {
		return false;
	}

	@Override
	public String get() {
		return title;
	}

	@Override
	public String[] getList() {
		return new String[]{get()};
	}

	@Override
	public void set(final String value) {

	}

	@Override
	public void set(final String[] aVal) {

	}

	@Override
	public String[] getValidValues() {
		return new String[0];
	}

	@Override
	public String getMinValue() {
		return title;
	}

	@Override
	public String getMaxValue() {
		return title;
	}

	@Override
	public Pattern getValidationPattern() {
		return Pattern.compile(".+");
	}

}
