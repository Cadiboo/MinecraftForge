package net.minecraftforge.fml.client.config.element;

import net.minecraftforge.fml.client.config.ConfigEntryListWidget;
import net.minecraftforge.fml.client.config.ConfigScreen;
import net.minecraftforge.fml.client.config.entry.ConfigListEntry;
import net.minecraftforge.fml.client.config.entry.ElementConfigListEntry;
import net.minecraftforge.fml.client.config.entry.widget.StringTextField;
import net.minecraftforge.fml.client.config.entry.widget.WidgetValueReference;

/**
 * @author Cadiboo
 */
public class StringConfigElement extends ConfigElement<String> {

	public StringConfigElement(final ConfigElementContainer<String> entryConfigValue) {
		super(entryConfigValue);
	}

	@Override
	public ConfigListEntry<String> makeConfigListEntry(final ConfigScreen configScreen, final ConfigEntryListWidget configEntryListWidget) {
		final WidgetValueReference<String> widgetValueReference = new WidgetValueReference<>(this::get, this::set, this::getDefault, this::isDefault, this::resetToDefault, this::isChanged, this::undoChanges, this::isValid, this::save);
		final StringTextField widget = new StringTextField(widgetValueReference);
		return new ElementConfigListEntry<>(configScreen, widget, this);
	}

}
