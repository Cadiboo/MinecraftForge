package net.minecraftforge.fml.client.config.element;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.client.config.ConfigEntryListWidget;
import net.minecraftforge.fml.client.config.ConfigScreen;
import net.minecraftforge.fml.client.config.entry.ConfigListEntry;
import net.minecraftforge.fml.client.config.entry.DummyConfigListEntry;

import javax.annotation.Nullable;

/**
 * @author Cadiboo
 */
public class DummyConfigElement<T> implements IConfigElement<T> {

	private final String label;

	public DummyConfigElement(final String label) {
		this.label = label;
	}

	@Override
	public String getLabel() {
		return label;
	}

	@Override
	public String getTranslationKey() {
		return label;
	}

	@Override
	public String getComment() {
		return label;
	}

	@Override
	public boolean isDefault() {
		return true;
	}

	@Override
	public T getDefault() {
		return null;
	}

	@Override
	public void resetToDefault() {
	}

	@Override
	public boolean isChanged() {
		return false;
	}

	@Override
	public void undoChanges() {
	}

	@Override
	public boolean requiresWorldRestart() {
		return false;
	}

	@Override
	public boolean requiresGameRestart() {
		return false;
	}

	@Override
	public T get() {
		return null;
	}

	@Override
	public void set(final T value) {
	}

	@Override
	public void save() {
	}

	@Override
	public boolean isValid(final T o) {
		return true;
	}

	@Override
	public ConfigListEntry<T> makeConfigListEntry(final ConfigScreen configScreen, final ConfigEntryListWidget configEntryListWidget) {
		return new DummyConfigListEntry<>(configScreen, this.getLabel());
	}

	@Nullable
	@Override
	public ForgeConfigSpec.Range<?> getRange() {
		return null;
	}

}