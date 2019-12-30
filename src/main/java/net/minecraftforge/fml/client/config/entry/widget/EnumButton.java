package net.minecraftforge.fml.client.config.entry.widget;

import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.item.DyeColor;
import net.minecraftforge.fml.client.config.GuiButtonExt;

/**
 * @author Cadiboo
 */
public class EnumButton<T extends Enum<?>> extends GuiButtonExt implements ConfigListEntryWidget<T> {

	private final Callback<T> callback;

	public EnumButton(final Callback<T> callback) {
		this("Enum", callback);
	}

	public EnumButton(final String message, final Callback<T> callback) {
		super(0, 0, 0, 0, message, button -> nextValue(callback, button, 0));
		this.callback = callback;
		updateTextAndColor(this, callback.get());
	}

	private static <T extends Enum<?>> void nextValue(final Callback<T> callback, final Button button, final int _try) {
		final T currentValue = callback.get();
		final T[] enumConstants = (T[]) currentValue.getClass().getEnumConstants();
		if (_try >= enumConstants.length)
			return;
		final T newValue = enumConstants[(currentValue.ordinal() + 1) % enumConstants.length];
		callback.set(newValue);
		if (!callback.isValid())
			nextValue(callback, button, _try + 1);
		else
			updateTextAndColor(button, callback.get());
	}

	public static void updateTextAndColor(final Button button, final Enum<?> newValue) {
		button.setMessage(newValue.toString());
		button.setFGColor(getColor(newValue));
	}

	public static int getColor(final Enum<?> anEnum) {
		if (anEnum instanceof DyeColor)
			return ((DyeColor) anEnum).getColorValue();
		else if (anEnum instanceof IColorable)
			return ((IColorable) anEnum).getColor();
		else
			return 0xFF_FF_FF; // White
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
		updateTextAndColor(this, getCallback().get());
	}

	public interface IColorable {

		int getColor();

	}

}
