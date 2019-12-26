package net.minecraftforge.fml.client.config.element.borked;

import net.minecraftforge.fml.client.config.ConfigEntryListWidget;
import net.minecraftforge.fml.client.config.ConfigScreen;
import net.minecraftforge.fml.client.config.element.ConfigElement;
import net.minecraftforge.fml.client.config.entry.ConfigElementContainer;
import net.minecraftforge.fml.client.config.entry2.ConfigListEntry;
import net.minecraftforge.fml.client.config.entry2.fucked.LocalDateTimeConfigListEntry;

import java.time.LocalDateTime;

/**
 * @author Cadiboo
 */
public class LocalDateTimeConfigElement extends ConfigElement<LocalDateTime> {

	public LocalDateTimeConfigElement(final ConfigElementContainer<LocalDateTime> entryConfigValue) {
		super(entryConfigValue);
	}

	@Override
	public ConfigListEntry<LocalDateTime> makeWidgetThing(final ConfigScreen configScreen, final ConfigEntryListWidget configEntryListWidget) {
		return new LocalDateTimeConfigListEntry(configScreen, this);
	}

}
