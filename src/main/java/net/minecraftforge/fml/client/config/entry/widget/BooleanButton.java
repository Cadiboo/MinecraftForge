package net.minecraftforge.fml.client.config.entry.widget;

import net.minecraft.client.gui.widget.button.Button;
import net.minecraftforge.fml.client.config.GuiButtonExt;

/**
 * @author Cadiboo
 */
public class BooleanButton extends GuiButtonExt implements IConfigListEntryWidget<Boolean> {

	private final Callback<Boolean> callback;

	public BooleanButton(final Callback<Boolean> callback) {
		this("Boolean", callback);
	}

	public BooleanButton(final String message, final Callback<Boolean> callback) {
		super(0, 0, 0, 0, message, button -> {
			callback.set(!callback.get());
			if (!callback.isValid())
				callback.set(!callback.get());
			updateTextAndColor(button, callback.get());
		});
		this.callback = callback;
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

	public Callback<Boolean> getCallback() {
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

}
