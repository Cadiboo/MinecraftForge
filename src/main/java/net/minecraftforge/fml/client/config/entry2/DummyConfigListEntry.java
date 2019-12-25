package net.minecraftforge.fml.client.config.entry2;

import net.minecraft.client.gui.widget.Widget;
import net.minecraftforge.fml.client.config.ConfigScreen;
import net.minecraftforge.fml.client.config.element.DummyConfigElement;

/**
 * @author Cadiboo
 */
public class DummyConfigListEntry extends ConfigListEntry<String> {

	public DummyConfigListEntry(final ConfigScreen configScreen, final DummyConfigElement configElement) {
		super(configScreen, configElement);
	}

	@Override
	public boolean isValidValue() {
		return true;
	}

	@Override
	public Widget getWidget() {
		return null;
	}

}
