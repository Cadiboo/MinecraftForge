package net.minecraftforge.fml.client.config.element;

import net.minecraftforge.fml.client.config.ConfigEntryListWidget;
import net.minecraftforge.fml.client.config.ConfigScreen;
import net.minecraftforge.fml.client.config.entry.ConfigElementContainer;
import net.minecraftforge.fml.client.config.entry2.ConfigListEntry;
import net.minecraftforge.fml.client.config.entry2.EnumConfigListEntry;

/**
 * @author Cadiboo
 */
public class EnumConfigElement<T extends Enum<?>> extends ConfigElement<T> {

	public EnumConfigElement(final ConfigElementContainer<T> entryConfigValue) {
		super(entryConfigValue);
	}

	@Override
	public ConfigListEntry<T> makeWidgetThing(final ConfigScreen configScreen, final ConfigEntryListWidget configEntryListWidget) {
		return new EnumConfigListEntry<>(configScreen, this);
	}

}
