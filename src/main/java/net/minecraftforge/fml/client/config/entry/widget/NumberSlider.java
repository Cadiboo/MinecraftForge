package net.minecraftforge.fml.client.config.entry.widget;

import net.minecraftforge.common.ForgeConfigSpec.Range;
import net.minecraftforge.fml.client.config.GuiSlider;

import java.util.function.Function;

/**
 * @author Cadiboo
 */
public class NumberSlider<T extends Number & Comparable<? super T>> extends GuiSlider {

	public NumberSlider(final WidgetValueReference<T> widgetValueReference, final Range<T> range, final Function<String, T> parser) {
		this("Number Slider", widgetValueReference, range, parser);
	}

	public NumberSlider(final String message, final WidgetValueReference<T> widgetValueReference, final Range<T> range, final Function<String, T> parser) {
		super(0, 0, message, range.getMin().doubleValue(), range.getMax().doubleValue(), widgetValueReference.get().doubleValue(), b -> {
		}, s -> {
			final String string = Double.toString(s.getValue());
			widgetValueReference.set(parser.apply(string));
		});
	}

}
