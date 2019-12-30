package net.minecraftforge.fml.client.config.element;

import net.minecraftforge.fml.client.config.ConfigEntryListWidget;
import net.minecraftforge.fml.client.config.ConfigScreen;
import net.minecraftforge.fml.client.config.entry.ConfigListEntry;
import net.minecraftforge.fml.client.config.entry.ElementConfigListEntry;
import net.minecraftforge.fml.client.config.entry.widget.BooleanButton;
import net.minecraftforge.fml.client.config.entry.widget.ConfigListEntryWidget;

/**
 * @author Cadiboo
 */
public class BooleanConfigElement extends ConfigElement<Boolean> {

	public BooleanConfigElement(final ConfigElementContainer<Boolean> entryConfigValue) {
		super(entryConfigValue);
	}

	@Override
	public ConfigListEntry<Boolean> makeConfigListEntry(final ConfigScreen configScreen, final ConfigEntryListWidget configEntryListWidget) {
		final ConfigListEntryWidget.Callback<Boolean> widgetValueReference = new ConfigListEntryWidget.Callback<>(this::get, this::set, this::getDefault, this::isDefault, this::resetToDefault, this::isChanged, this::undoChanges, this::isValid, this::save);
		final BooleanButton widget = new BooleanButton(widgetValueReference);
		return new ElementConfigListEntry<>(configScreen, widget, this);
	}

}
