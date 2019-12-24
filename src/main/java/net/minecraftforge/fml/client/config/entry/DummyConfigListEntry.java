package net.minecraftforge.fml.client.config.entry;

import net.minecraft.client.gui.widget.Widget;
import net.minecraftforge.fml.client.config.ConfigScreen;

/**
 * @author Cadiboo
 */
public class DummyConfigListEntry extends ConfigListEntry<String> {

	private final String name;

	public DummyConfigListEntry(final ConfigScreen configScreen, final String name) {
		super(configScreen);
		this.name = name;
	}

	@Override
	public boolean isValidValue() {
		return true;
	}

	@Override
	public Widget getWidget() {
		return null;
	}

	@Override
	public String getLabel() {
		return name;
	}

	@Override
	protected EntryConfigValue<String> getEntryConfigValue() {
		return null;
	}

}
