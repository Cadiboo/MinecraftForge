package net.minecraftforge.fml.client.config.entry.widget.number;

import net.minecraftforge.common.ForgeConfigSpec.Range;
import net.minecraftforge.fml.client.config.GuiSlider;
import net.minecraftforge.fml.client.config.entry.widget.IConfigListEntryWidget;

/**
 * @author Cadiboo
 */
public abstract class NumberSlider<T extends Number & Comparable<? super T>> extends GuiSlider implements IConfigListEntryWidget<T> {

	private final Callback<T> callback;

	public NumberSlider(final String message, final IConfigListEntryWidget.Callback<T> callback, final Range<T> range) {
		// public GuiSlider(int xPos, int yPos, int width, int height, String prefix, String suf, double minVal, double maxVal, double currentVal, boolean showDec, boolean drawStr, IPressable handler)
		super(
				0, 0, 0, 0,
				"", "",
				getMin(range), getMax(range), callback.get().doubleValue(),
				shouldShowDecimal(callback),
				true,
				b -> {
				}
		);
		this.callback = callback;
		this.parent = s -> callback.set(getValue(s));
	}

	private static boolean shouldShowDecimal(final Callback<? extends Number> callback) {
		final Number number = callback.get();
		return number instanceof Float || callback.get() instanceof Double;
	}

	private static double getMax(final Range<? extends Number> range) {
		if (range == null) // Should never happen
			return Double.MAX_VALUE; // Leads to weird results but DOESN'T crash
		return range.getMax().doubleValue();
	}

	private static double getMin(final Range<? extends Number> range) {
		if (range == null) // Should never happen
			return Double.MIN_VALUE; // Leads to weird results but DOESN'T crash
		return range.getMin().doubleValue();
	}

	@Override
	public Callback<T> getCallback() {
		return callback;
	}

	@Override
	public boolean isWidgetValueValid() {
		return true;
	}

	@Override
	public void updateWidgetValue() {
		this.setValue(callback.get().doubleValue());
		this.updateSlider();
	}

	public abstract T getValue(GuiSlider slider);

}
