package net.minecraftforge.fml.client.config.element.borked;

import net.minecraftforge.fml.client.config.ConfigEntryListWidget;
import net.minecraftforge.fml.client.config.ConfigScreen;
import net.minecraftforge.fml.client.config.element.ConfigElement;
import net.minecraftforge.fml.client.config.entry.ConfigElementContainer;
import net.minecraftforge.fml.client.config.entry2.ConfigListEntry;
import net.minecraftforge.fml.client.config.entry2.fucked.SliderNumberConfigListEntry;
import net.minecraftforge.fml.client.config.entry2.fucked.TextNumberConfigListEntry;

/**
 * @author Cadiboo
 */
public abstract class NumberConfigElement<T extends Number & Comparable<? super T>> extends ConfigElement<T> {

	public NumberConfigElement(final ConfigElementContainer<T> entryConfigValue) {
		super(entryConfigValue);
	}

	@Override
	public ConfigListEntry<T> makeWidgetThing(final ConfigScreen configScreen, final ConfigEntryListWidget configEntryListWidget) {
		if (hasSlidingControl()) {
			return makeSlider(configScreen);
		} else {
			return makeText(configScreen);
		}
	}

	public TextNumberConfigListEntry<T> makeText(final ConfigScreen configScreen) {
		return new TextNumberConfigListEntry<>(configScreen, this);
	}

	public SliderNumberConfigListEntry<T> makeSlider(final ConfigScreen configScreen) {
		return new SliderNumberConfigListEntry<>(configScreen, this);
	}

	public abstract T parse(final String text) throws NumberFormatException;

}
