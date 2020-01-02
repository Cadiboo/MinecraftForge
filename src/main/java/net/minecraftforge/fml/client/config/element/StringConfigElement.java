package net.minecraftforge.fml.client.config.element;

import net.minecraftforge.fml.client.config.ConfigScreen;
import net.minecraftforge.fml.client.config.entry.ConfigListEntry;
import net.minecraftforge.fml.client.config.entry.ElementConfigListEntry;
import net.minecraftforge.fml.client.config.entry.widget.IConfigListEntryWidget;
import net.minecraftforge.fml.client.config.entry.widget.StringTextField;

/**
 * @author Cadiboo
 */
public class StringConfigElement extends ConfigElement<String> {

	public StringConfigElement(final ConfigElementContainer<String> entryConfigValue) {
		super(entryConfigValue);
	}

	@Override
	public ElementConfigListEntry<String> makeConfigListEntry(final ConfigScreen configScreen) {
		final IConfigListEntryWidget.Callback<String> callback = new IConfigListEntryWidget.Callback<>(this::get, this::set, this::getDefault, this::isDefault, this::resetToDefault, this::isChanged, this::undoChanges, this::isValid, this::save);
		final StringTextField widget = new StringTextField(callback);
		return new ElementConfigListEntry<>(configScreen, widget, this);
	}

}
