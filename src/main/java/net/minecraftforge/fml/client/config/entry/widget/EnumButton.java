package net.minecraftforge.fml.client.config.entry.widget;

import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.item.DyeColor;
import net.minecraftforge.fml.client.config.GuiButtonExt;

/**
 * @author Cadiboo
 */
public class EnumButton<T extends Enum<?>> extends GuiButtonExt implements ConfigListEntryWidget<T> {

	private final WidgetValueReference<T> widgetValueReference;

	public EnumButton(final WidgetValueReference<T> widgetValueReference) {
		this("Enum Button", widgetValueReference);
	}

	public EnumButton(final String message, final WidgetValueReference<T> widgetValueReference) {
		super(0, 0, 0, 0, "Enum", button -> {
			final T currentValue = widgetValueReference.get();
			final T[] enumConstants = (T[]) currentValue.getClass().getEnumConstants();
			final T newValue = enumConstants[(currentValue.ordinal() + 1) % enumConstants.length];
			widgetValueReference.set(newValue);
			updateTextAndColor(button, widgetValueReference.get());
		});
		this.widgetValueReference = widgetValueReference;
		updateTextAndColor(this, widgetValueReference.get());
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
	public WidgetValueReference<T> getWidgetValueReference() {
		return widgetValueReference;
	}

	@Override
	public boolean isWidgetValueValid() {
		return true;
	}

	@Override
	public void updateWidgetValue() {
		updateTextAndColor(this, getWidgetValueReference().get());
	}

	public interface IColorable {

		int getColor();

	}

}
