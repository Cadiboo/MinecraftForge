package net.minecraftforge.fml.client.config.entry;

import net.minecraftforge.fml.client.config.ConfigEntryListWidget;
import net.minecraftforge.fml.client.config.ConfigScreen;

import java.util.List;
import java.util.regex.Pattern;

/**
 * @author Cadiboo
 */
public class CategoryConfigValueElement implements IConfigValueElement {

	@Override
	public boolean isProperty() {
		return false;
	}

	@Override
	public ConfigListEntry makeConfigEntry(final ConfigScreen configScreen, final ConfigEntryListWidget configEntryListScreen) {
		return new CategoryEntry(configScreen, configEntryListScreen, this);
	}

	@Override
	public String getName() {
//		return this.name;
		return null;
	}

	@Override
	public String getTranslationKey() {
		return null;
	}

	@Override
	public String getComment() {
		return null;
	}

	@Override
	public List<IConfigValueElement> getChildElements() {
		return null;
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
		return false;
	}

	@Override
	public Object getDefault() {
		return null;
	}

	@Override
	public Object[] getDefaults() {
		return new Object[0];
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
		return false;
	}

	@Override
	public boolean requiresMcRestart() {
		return false;
	}

	@Override
	public Object get() {
		return null;
	}

	@Override
	public Object[] getList() {
		return new Object[0];
	}

	@Override
	public void set(final Object value) {

	}

	@Override
	public void set(final Object[] aVal) {

	}

	@Override
	public String[] getValidValues() {
		return new String[0];
	}

	@Override
	public Object getMinValue() {
		return null;
	}

	@Override
	public Object getMaxValue() {
		return null;
	}

	@Override
	public Pattern getValidationPattern() {
		return null;
	}

}
