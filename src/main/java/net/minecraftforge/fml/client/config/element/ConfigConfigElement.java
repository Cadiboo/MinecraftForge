package net.minecraftforge.fml.client.config.element;

import com.electronwill.nightconfig.core.Config;
import net.minecraftforge.fml.client.config.ConfigEntryListWidget;
import net.minecraftforge.fml.client.config.ConfigScreen;
import net.minecraftforge.fml.client.config.entry.ConfigListEntry;
import net.minecraftforge.fml.client.config.entry.ScreenElementConfigListEntry;
import net.minecraftforge.fml.client.config.entry.widget.ConfigButton;
import net.minecraftforge.fml.client.config.entry.widget.ConfigListEntryWidget;
import net.minecraftforge.fml.client.config.entry.widget.ScreenButton;

/**
 * Config Element for a ConfigValue<Config> (as opposed to a category)
 *
 * @author Cadiboo
 */
public class ConfigConfigElement extends ConfigElement<Config> {

	public ConfigConfigElement(final ConfigElementContainer<Config> elementContainer) {
		super(elementContainer);
	}

	@Override
	public ConfigListEntry<Config> makeConfigListEntry(final ConfigScreen configScreen, final ConfigEntryListWidget configEntryListWidget) {
		final ConfigListEntryWidget.Callback<Config> widgetValueReference = new ConfigListEntryWidget.Callback<>(this::get, this::set, this::getDefault, this::isDefault, this::resetToDefault, this::isChanged, this::undoChanges, this::isValid, this::save);
		final ScreenButton<Config> widget = new ConfigButton(getLabel(), configScreen, widgetValueReference);
		return new ScreenElementConfigListEntry<>(configScreen, widget, this);
	}

}
