package net.minecraftforge.fml.client.config.element;

import net.minecraftforge.fml.client.config.ConfigScreen;
import net.minecraftforge.fml.client.config.entry.ConfigListEntry;
import net.minecraftforge.fml.client.config.entry.ElementConfigListEntry;
import net.minecraftforge.fml.client.config.entry.widget.BooleanButton;
import net.minecraftforge.fml.client.config.entry.widget.IConfigListEntryWidget;

/**
 * @author Cadiboo
 */
public class BooleanConfigElement extends ConfigElement<Boolean> {

	public BooleanConfigElement(final ConfigElementContainer<Boolean> entryConfigValue) {
		super(entryConfigValue);
	}

	@Override
	public ElementConfigListEntry<Boolean> makeConfigListEntry(final ConfigScreen configScreen) {
		final IConfigListEntryWidget.Callback<Boolean> callback = new IConfigListEntryWidget.Callback<>(this::get, this::set, this::getDefault, this::isDefault, this::resetToDefault, this::isChanged, this::undoChanges, this::isValid, this::save);
		final BooleanButton widget = new BooleanButton(callback);
		return new ElementConfigListEntry<>(configScreen, widget, this);
	}

}
