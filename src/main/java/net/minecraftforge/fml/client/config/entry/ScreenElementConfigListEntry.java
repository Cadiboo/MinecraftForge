package net.minecraftforge.fml.client.config.entry;

import net.minecraft.client.gui.widget.Widget;
import net.minecraftforge.fml.client.config.ConfigScreen;
import net.minecraftforge.fml.client.config.element.IConfigElement;
import net.minecraftforge.fml.client.config.entry.widget.IConfigListEntryWidget;

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
	public boolean isCategory() { // TODO Hmmmmmmmm this entire class is a bit suspicious. I don't feel like it should exist
		return !(getConfigElement() instanceof ListConfigListEntry);
	}

}
