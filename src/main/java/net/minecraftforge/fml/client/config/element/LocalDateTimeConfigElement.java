package net.minecraftforge.fml.client.config.element;

import net.minecraftforge.fml.client.config.ConfigEntryListWidget;
import net.minecraftforge.fml.client.config.ConfigScreen;
import net.minecraftforge.fml.client.config.entry.ConfigListEntry;
import net.minecraftforge.fml.client.config.entry.ElementConfigListEntry;
import net.minecraftforge.fml.client.config.entry.widget.LocalDateTimeTextField;
import net.minecraftforge.fml.client.config.entry.widget.WidgetValueReference;

import java.time.LocalDateTime;

/**
 * @author Cadiboo
 */
public class LocalDateTimeConfigElement extends ConfigElement<LocalDateTime> {

	public LocalDateTimeConfigElement(final ConfigElementContainer<LocalDateTime> entryConfigValue) {
		super(entryConfigValue);
	}

	@Override
	public ConfigListEntry<LocalDateTime> makeConfigListEntry(final ConfigScreen configScreen, final ConfigEntryListWidget configEntryListWidget) {
		final WidgetValueReference<LocalDateTime> widgetValueReference = new WidgetValueReference<>(this::get, this::set, this::getDefault, this::isDefault, this::resetToDefault, this::isChanged, this::undoChanges, this::isValid, this::save);
		final LocalDateTimeTextField widget = new LocalDateTimeTextField(widgetValueReference);
		return new ElementConfigListEntry<>(configScreen, widget, this);
	}

}
