package net.minecraftforge.fml.client.config.element;

import net.minecraftforge.fml.client.config.ConfigEntryListWidget;
import net.minecraftforge.fml.client.config.ConfigScreen;
import net.minecraftforge.fml.client.config.entry.ConfigElementContainer;
import net.minecraftforge.fml.client.config.entry2.ConfigListEntry;
import net.minecraftforge.fml.client.config.entry2.TextConfigListEntry;

/**
 * @author Cadiboo
 */
public class StringConfigElement extends ConfigElement<String> {

	public StringConfigElement(final ConfigElementContainer<String> entryConfigValue) {
		super(entryConfigValue);
	}

	@Override
	public ConfigListEntry<String> makeWidgetThing(final ConfigScreen configScreen, final ConfigEntryListWidget configEntryListWidget) {
		return new TextConfigListEntry<String>(configScreen, this) {
			@Override
			public String parse(final String text) {
				return text;
			}
		};
	}

}
