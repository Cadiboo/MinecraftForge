package net.minecraftforge.fml.client.config.entry.widget;

import net.minecraft.client.gui.widget.button.Button;
import net.minecraftforge.fml.client.config.GuiButtonExt;

/**
 * @author Cadiboo
 */
public class BooleanButton extends GuiButtonExt implements ConfigListEntryWidget<Boolean> {

	private final WidgetValueReference<Boolean> widgetValueReference;

	public BooleanButton(final WidgetValueReference<Boolean> widgetValueReference) {
		this("Boolean Button", widgetValueReference);
	}

	public BooleanButton(final String message, final WidgetValueReference<Boolean> widgetValueReference) {
		super(0, 0, 0, 0, message, button -> {
			widgetValueReference.set(!widgetValueReference.get());
			updateTextAndColor(button, widgetValueReference.get());
		});
		this.widgetValueReference = widgetValueReference;
		updateWidgetValue();
	}

	public static void updateTextAndColor(final Button button, final Boolean newValue) {
		button.setMessage(newValue.toString());
		button.setFGColor(getColor(newValue));
	}

	/**
	 * @return Green if true, Red if false
	 */
	public static int getColor(final boolean b) {
		return b ? 0x55FF55 : 0xFF5555;
	}

	public WidgetValueReference<Boolean> getWidgetValueReference() {
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

}
