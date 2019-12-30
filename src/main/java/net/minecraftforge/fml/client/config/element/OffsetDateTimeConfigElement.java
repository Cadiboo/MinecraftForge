package net.minecraftforge.fml.client.config.element;

import net.minecraftforge.fml.client.config.ConfigScreen;
import net.minecraftforge.fml.client.config.entry.ConfigListEntry;
import net.minecraftforge.fml.client.config.entry.ElementConfigListEntry;
import net.minecraftforge.fml.client.config.entry.widget.IConfigListEntryWidget;
import net.minecraftforge.fml.client.config.entry.widget.OffsetDateTimeTextField;

import java.time.OffsetDateTime;

/**
 * @author Cadiboo
 */
public class OffsetDateTimeConfigElement extends ConfigElement<OffsetDateTime> {

	public OffsetDateTimeConfigElement(final ConfigElementContainer<OffsetDateTime> entryConfigValue) {
		super(entryConfigValue);
	}

	@Override
	public ConfigListEntry<OffsetDateTime> makeConfigListEntry(final ConfigScreen configScreen) {
		final IConfigListEntryWidget.Callback<OffsetDateTime> callback = new IConfigListEntryWidget.Callback<>(this::get, this::set, this::getDefault, this::isDefault, this::resetToDefault, this::isChanged, this::undoChanges, this::isValid, this::save);
		final OffsetDateTimeTextField widget = new OffsetDateTimeTextField(callback);
		return new ElementConfigListEntry<>(configScreen, widget, this);
	}

}
