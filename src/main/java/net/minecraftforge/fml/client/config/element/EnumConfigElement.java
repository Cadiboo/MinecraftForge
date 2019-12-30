package net.minecraftforge.fml.client.config.element;

import net.minecraftforge.fml.client.config.ConfigEntryListWidget;
import net.minecraftforge.fml.client.config.ConfigScreen;
import net.minecraftforge.fml.client.config.entry.ConfigListEntry;
import net.minecraftforge.fml.client.config.entry.ElementConfigListEntry;
import net.minecraftforge.fml.client.config.entry.widget.ConfigListEntryWidget;
import net.minecraftforge.fml.client.config.entry.widget.EnumButton;

/**
 * @author Cadiboo
 */
public class EnumConfigElement<T extends Enum<?>> extends ConfigElement<T> {

	public EnumConfigElement(final ConfigElementContainer<T> entryConfigValue) {
		super(entryConfigValue);
	}

	@Override
	public ConfigListEntry<T> makeConfigListEntry(final ConfigScreen configScreen, final ConfigEntryListWidget configEntryListWidget) {
		final ConfigListEntryWidget.Callback<T> widgetValueReference = new ConfigListEntryWidget.Callback<>(this::get, this::set, this::getDefault, this::isDefault, this::resetToDefault, this::isChanged, this::undoChanges, this::isValid, this::save);
		final EnumButton<T> widget = new EnumButton<>(getLabel(), widgetValueReference);
		return new ElementConfigListEntry<>(configScreen, widget, this);
	}

}
