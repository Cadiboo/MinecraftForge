package net.minecraftforge.fml.client.config.element;

import com.electronwill.nightconfig.core.Config;
import net.minecraftforge.fml.client.config.ConfigScreen;
import net.minecraftforge.fml.client.config.element.category.CategoryElement;
import net.minecraftforge.fml.client.config.entry.ConfigListEntry;
import net.minecraftforge.fml.client.config.entry.ScreenElementConfigListEntry;
import net.minecraftforge.fml.client.config.entry.widget.ConfigButton;
import net.minecraftforge.fml.client.config.entry.widget.IConfigListEntryWidget;
import net.minecraftforge.fml.client.config.entry.widget.ScreenButton;

/**
 * Config Element for a ConfigValue<Config> (as opposed to a category)
 *
 * @author Cadiboo
 * @see CategoryElement
 */
public class ConfigConfigElement extends ConfigElement<Config> {

	public ConfigConfigElement(final ConfigElementContainer<Config> elementContainer) {
		super(elementContainer);
	}

	@Override
	public ConfigListEntry<Config> makeConfigListEntry(final ConfigScreen configScreen) {
		final IConfigListEntryWidget.Callback<Config> callback = new IConfigListEntryWidget.Callback<>(this::get, this::set, this::getDefault, this::isDefault, this::resetToDefault, this::isChanged, this::undoChanges, this::isValid, this::save);
		final ScreenButton<Config> widget = new ConfigButton(getLabel(), configScreen, callback);
		return new ScreenElementConfigListEntry<>(configScreen, widget, this);
	}

}
