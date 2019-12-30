package net.minecraftforge.fml.client.config.entry;

import net.minecraft.client.gui.widget.Widget;
import net.minecraftforge.fml.client.config.ConfigScreen;
import net.minecraftforge.fml.client.config.element.ConfigConfigElement;
import net.minecraftforge.fml.client.config.element.IConfigElement;
import net.minecraftforge.fml.client.config.element.ListConfigElement;
import net.minecraftforge.fml.client.config.element.category.CategoryElement;
import net.minecraftforge.fml.client.config.element.category.ConfigCategoryElement;
import net.minecraftforge.fml.client.config.element.category.ModConfigCategoryElement;
import net.minecraftforge.fml.client.config.entry.widget.IConfigListEntryWidget;

/**
 * A ConfigListEntry for an ElementConfigListEntry that displays a screen.
 * Used by Configs (ConfigValue), Lists (ConfigValue) and Categories (ModConfig, Config)
 *
 * @param <T> The type of the config object (e.g. Boolean/Float).
 * @author Cadiboo
 * @see ConfigConfigElement
 * @see ListConfigElement
 * @see ConfigCategoryElement
 * @see ModConfigCategoryElement
 */
public class ScreenElementConfigListEntry<T> extends ElementConfigListEntry<T> {

	public <W extends Widget & IConfigListEntryWidget<T>> ScreenElementConfigListEntry(final ConfigScreen configScreen, final W widget, final IConfigElement<T> configElement) {
		super(configScreen, widget, configElement);
	}

	@Override
	public boolean shouldRenderLabel() {
		return false;
	}

	@Override
	public boolean displayDefaultValue() {
		return false;
	}

	@Override
	public boolean isCategory() {
		return getConfigElement() instanceof CategoryElement;
	}

}
