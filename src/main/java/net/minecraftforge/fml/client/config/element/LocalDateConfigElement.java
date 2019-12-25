package net.minecraftforge.fml.client.config.element;

import net.minecraftforge.fml.client.config.ConfigEntryListWidget;
import net.minecraftforge.fml.client.config.ConfigScreen;
import net.minecraftforge.fml.client.config.entry.ConfigElementContainer;
import net.minecraftforge.fml.client.config.entry2.ConfigListEntry;
import net.minecraftforge.fml.client.config.entry2.LocalDateConfigListEntry;

import java.time.LocalDate;

/**
 * @author Cadiboo
 */
public class LocalDateConfigElement extends ConfigElement<LocalDate> {

	public LocalDateConfigElement(final ConfigElementContainer<LocalDate> entryConfigValue) {
		super(entryConfigValue);
	}

	@Override
	public ConfigListEntry<LocalDate> makeWidgetThing(final ConfigScreen configScreen, final ConfigEntryListWidget configEntryListWidget) {
		return new LocalDateConfigListEntry(configScreen, this);
	}

}
