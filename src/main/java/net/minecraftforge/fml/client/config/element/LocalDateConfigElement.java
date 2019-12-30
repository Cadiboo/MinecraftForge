package net.minecraftforge.fml.client.config.element;

import net.minecraftforge.fml.client.config.ConfigScreen;
import net.minecraftforge.fml.client.config.entry.ConfigListEntry;
import net.minecraftforge.fml.client.config.entry.ElementConfigListEntry;
import net.minecraftforge.fml.client.config.entry.widget.IConfigListEntryWidget;
import net.minecraftforge.fml.client.config.entry.widget.LocalDateTextField;

import java.time.LocalDate;

/**
 * @author Cadiboo
 */
public class LocalDateConfigElement extends ConfigElement<LocalDate> {

	public LocalDateConfigElement(final ConfigElementContainer<LocalDate> entryConfigValue) {
		super(entryConfigValue);
	}

	@Override
	public ConfigListEntry<LocalDate> makeConfigListEntry(final ConfigScreen configScreen) {
		final IConfigListEntryWidget.Callback<LocalDate> callback = new IConfigListEntryWidget.Callback<>(this::get, this::set, this::getDefault, this::isDefault, this::resetToDefault, this::isChanged, this::undoChanges, this::isValid, this::save);
		final LocalDateTextField widget = new LocalDateTextField(callback);
		return new ElementConfigListEntry<>(configScreen, widget, this);
	}

}
