package net.minecraftforge.fml.client.config.entry.widget;

import net.minecraftforge.common.ForgeConfigSpec.Range;
import net.minecraftforge.fml.client.config.GuiSlider;

import java.util.function.Function;

/**
 * @author Cadiboo
 */
public class NumberSlider<T extends Number & Comparable<? super T>> extends GuiSlider {

	public NumberSlider(final ConfigListEntryWidget.Callback<T> callback, final Range<T> range, final Function<String, T> parser) {
		this("Number Slider", callback, range, parser);
	}

	public NumberSlider(final String message, final ConfigListEntryWidget.Callback<T> callback, final Range<T> range, final Function<String, T> parser) {
		super(0, 0, message, range.getMin().doubleValue(), range.getMax().doubleValue(), callback.get().doubleValue(), b -> {
		}, s -> {
			final String string = Double.toString(s.getValue());
			callback.set(parser.apply(string));
		});
	}

}
