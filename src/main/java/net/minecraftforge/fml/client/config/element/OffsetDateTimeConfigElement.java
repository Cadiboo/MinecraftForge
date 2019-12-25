package net.minecraftforge.fml.client.config.element;

import net.minecraftforge.fml.client.config.ConfigEntryListWidget;
import net.minecraftforge.fml.client.config.ConfigScreen;
import net.minecraftforge.fml.client.config.entry.ConfigElementContainer;
import net.minecraftforge.fml.client.config.entry2.ConfigListEntry;
import net.minecraftforge.fml.client.config.entry2.OffsetDateTimeConfigListEntry;

import java.time.OffsetDateTime;

/**
 * @author Cadiboo
 */
public class OffsetDateTimeConfigElement extends ConfigElement<OffsetDateTime> {

	public OffsetDateTimeConfigElement(final ConfigElementContainer<OffsetDateTime> entryConfigValue) {
		super(entryConfigValue);
	}

	@Override
	public ConfigListEntry<OffsetDateTime> makeWidgetThing(final ConfigScreen configScreen, final ConfigEntryListWidget configEntryListWidget) {
		return new OffsetDateTimeConfigListEntry(configScreen, this);
	}

}
