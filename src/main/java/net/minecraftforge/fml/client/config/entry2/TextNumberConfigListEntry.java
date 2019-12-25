package net.minecraftforge.fml.client.config.entry2;

import net.minecraftforge.fml.client.config.ConfigScreen;
import net.minecraftforge.fml.client.config.element.NumberConfigElement;

/**
 * @author Cadiboo
 */
public class TextNumberConfigListEntry<T extends Number & Comparable<? super T>> extends TextConfigListEntry<T> {

	public TextNumberConfigListEntry(final ConfigScreen configScreen, final NumberConfigElement<T> configElement) {
		super(configScreen, configElement);
		getWidget().func_212954_a(s -> {
			if (!this.isValidValue())
				return;
			final NumberConfigElement<T> numberConfigElement = ((NumberConfigElement<T>) getConfigElement());
			numberConfigElement.set(numberConfigElement.parse(s));
		});
	}

	@Override
	public T parse(final String text) {
		return ((NumberConfigElement<T>) getConfigElement()).parse(text);
	}

}
