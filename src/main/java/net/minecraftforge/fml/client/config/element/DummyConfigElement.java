package net.minecraftforge.fml.client.config.element;

import net.minecraftforge.fml.client.config.ConfigEntryListWidget;
import net.minecraftforge.fml.client.config.ConfigScreen;
import net.minecraftforge.fml.client.config.entry.ConfigElementContainer;
import net.minecraftforge.fml.client.config.entry2.ConfigListEntry;
import net.minecraftforge.fml.client.config.entry2.DummyConfigListEntry;

import javax.annotation.Nullable;

/**
 * @author Cadiboo
 */
public class DummyConfigElement extends ConfigElement<String> {

	private final String label;

	public DummyConfigElement(@Nullable final ConfigElementContainer<String> configElementContainer, final String label) {
		super(configElementContainer);
		this.label = label;
	}

	public DummyConfigElement(final String label) {
		this(null, label);
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
	public String getDefault() {
		return label;
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
	public String get() {
		return label;
	}

	@Override
	public void set(final String value) {
	}

	@Override
	public void save() {
	}

	@Override
	public ConfigListEntry<String> makeWidgetThing(final ConfigScreen configScreen, final ConfigEntryListWidget configEntryListWidget) {
		return new DummyConfigListEntry(configScreen, this);
	}

}
