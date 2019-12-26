package net.minecraftforge.fml.client.config.element;

import net.minecraftforge.fml.client.config.ConfigEntryListWidget;
import net.minecraftforge.fml.client.config.ConfigScreen;
import net.minecraftforge.fml.client.config.entry.ConfigListEntry;
import net.minecraftforge.fml.client.config.entry.ElementConfigListEntry;
import net.minecraftforge.fml.client.config.entry.widget.OffsetDateTimeTextField;
import net.minecraftforge.fml.client.config.entry.widget.WidgetValueReference;

import java.time.OffsetDateTime;

/**
 * @author Cadiboo
 */
public class OffsetDateTimeConfigElement extends ConfigElement<OffsetDateTime> {

	public OffsetDateTimeConfigElement(final ConfigElementContainer<OffsetDateTime> entryConfigValue) {
		super(entryConfigValue);
	}

	@Override
	public ConfigListEntry<OffsetDateTime> makeConfigListEntry(final ConfigScreen configScreen, final ConfigEntryListWidget configEntryListWidget) {
		final WidgetValueReference<OffsetDateTime> widgetValueReference = new WidgetValueReference<>(this::get, this::set, this::getDefault, this::isDefault, this::resetToDefault, this::isChanged, this::undoChanges, this::isValid, this::save);
		final OffsetDateTimeTextField widget = new OffsetDateTimeTextField(widgetValueReference);
		return new ElementConfigListEntry<>(configScreen, widget, this);
	}

}
