package net.minecraftforge.fml.client.config.element;

import net.minecraftforge.fml.client.config.ConfigEntryListWidget;
import net.minecraftforge.fml.client.config.ConfigScreen;
import net.minecraftforge.fml.client.config.entry2.BooleanConfigListEntry;
import net.minecraftforge.fml.client.config.entry.ConfigElementContainer;
import net.minecraftforge.fml.client.config.entry2.ConfigListEntry;

/**
 * @author Cadiboo
 */
public class BooleanConfigElement extends ConfigElement<Boolean> {

	public BooleanConfigElement(final ConfigElementContainer<Boolean> entryConfigValue) {
		super(entryConfigValue);
	}

	@Override
	public ConfigListEntry<Boolean> makeWidgetThing(final ConfigScreen configScreen, final ConfigEntryListWidget configEntryListWidget) {
		return new BooleanConfigListEntry(configScreen, this);
	}

}
