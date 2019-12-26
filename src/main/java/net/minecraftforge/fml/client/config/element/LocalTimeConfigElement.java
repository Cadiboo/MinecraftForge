package net.minecraftforge.fml.client.config.element;

import net.minecraftforge.fml.client.config.ConfigEntryListWidget;
import net.minecraftforge.fml.client.config.ConfigScreen;
import net.minecraftforge.fml.client.config.entry.ConfigListEntry;
import net.minecraftforge.fml.client.config.entry.ElementConfigListEntry;
import net.minecraftforge.fml.client.config.entry.widget.LocalTimeTextField;
import net.minecraftforge.fml.client.config.entry.widget.WidgetValueReference;

import java.time.LocalTime;

/**
 * @author Cadiboo
 */
public class LocalTimeConfigElement extends ConfigElement<LocalTime> {

	public LocalTimeConfigElement(final ConfigElementContainer<LocalTime> entryConfigValue) {
		super(entryConfigValue);
	}

	@Override
	public ConfigListEntry<LocalTime> makeConfigListEntry(final ConfigScreen configScreen, final ConfigEntryListWidget configEntryListWidget) {
		final WidgetValueReference<LocalTime> widgetValueReference = new WidgetValueReference<>(this::get, this::set, this::getDefault, this::isDefault, this::resetToDefault, this::isChanged, this::undoChanges, this::isValid, this::save);
		final LocalTimeTextField widget = new LocalTimeTextField(widgetValueReference);
		return new ElementConfigListEntry<>(configScreen, widget, this);
	}

}
