package net.minecraftforge.fml.client.config.element.borked;

import net.minecraftforge.fml.client.config.ConfigEntryListWidget;
import net.minecraftforge.fml.client.config.ConfigScreen;
import net.minecraftforge.fml.client.config.element.ConfigElement;
import net.minecraftforge.fml.client.config.entry.ConfigElementContainer;
import net.minecraftforge.fml.client.config.entry2.ConfigListEntry;
import net.minecraftforge.fml.client.config.entry2.fucked.LocalTimeConfigListEntry;

import java.time.LocalTime;

/**
 * @author Cadiboo
 */
public class LocalTimeConfigElement extends ConfigElement<LocalTime> {

	public LocalTimeConfigElement(final ConfigElementContainer<LocalTime> entryConfigValue) {
		super(entryConfigValue);
	}

	@Override
	public ConfigListEntry<LocalTime> makeWidgetThing(final ConfigScreen configScreen, final ConfigEntryListWidget configEntryListWidget) {
		return new LocalTimeConfigListEntry(configScreen, this);
	}

}
