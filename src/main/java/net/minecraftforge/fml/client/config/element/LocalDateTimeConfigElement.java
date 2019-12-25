package net.minecraftforge.fml.client.config.element;

import net.minecraftforge.fml.client.config.ConfigEntryListWidget;
import net.minecraftforge.fml.client.config.ConfigScreen;
import net.minecraftforge.fml.client.config.entry.ConfigElementContainer;
import net.minecraftforge.fml.client.config.entry2.ConfigListEntry;
import net.minecraftforge.fml.client.config.entry2.LocalDateConfigListEntry;
import net.minecraftforge.fml.client.config.entry2.LocalDateTimeConfigListEntry;

import java.time.LocalDate;
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
