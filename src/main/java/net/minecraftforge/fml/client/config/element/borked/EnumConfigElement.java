package net.minecraftforge.fml.client.config.element.borked;

import net.minecraftforge.fml.client.config.ConfigEntryListWidget;
import net.minecraftforge.fml.client.config.ConfigScreen;
import net.minecraftforge.fml.client.config.element.ConfigElement;
import net.minecraftforge.fml.client.config.entry.ConfigElementContainer;
import net.minecraftforge.fml.client.config.entry2.ConfigListEntry;
import net.minecraftforge.fml.client.config.entry2.fucked.EnumConfigListEntry;

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
