package net.minecraftforge.fml.client.config.element;

import net.minecraftforge.fml.client.config.ConfigScreen;
import net.minecraftforge.fml.client.config.entry.ConfigListEntry;
import net.minecraftforge.fml.client.config.entry.ScreenElementConfigListEntry;
import net.minecraftforge.fml.client.config.entry.widget.IConfigListEntryWidget;
import net.minecraftforge.fml.client.config.entry.widget.ListButton;
import net.minecraftforge.fml.client.config.entry.widget.ScreenButton;

import java.util.List;

/**
 * @author Cadiboo
 */
public class ListConfigElement<T extends List<?>> extends ConfigElement<T> {

	public ListConfigElement(final ConfigElementContainer<T> elementContainer) {
		super(elementContainer);
	}

	@Override
	public ScreenElementConfigListEntry<T> makeConfigListEntry(final ConfigScreen configScreen) {
		final IConfigListEntryWidget.Callback<T> callback = new IConfigListEntryWidget.Callback<>(this::get, this::set, this::getDefault, this::isDefault, this::resetToDefault, this::isChanged, this::undoChanges, this::isValid, this::save);
		final ScreenButton<T> widget = new ListButton<>(getLabel(), configScreen, callback);
		return new ScreenElementConfigListEntry<>(configScreen, widget, this);
	}


}
