package net.minecraftforge.fml.client.config.entry2.widget;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.TextFieldWidget;

/**
 * @author Cadiboo
 */
public abstract class ObjectTextField<T> extends TextFieldWidget implements ConfigListEntryWidget<T> {

	private final WidgetValueReference<T> widgetValueReference;

	public ObjectTextField(final WidgetValueReference<T> widgetValueReference) {
		this("Object Text Field", widgetValueReference);
	}

	public ObjectTextField(final String message, final WidgetValueReference<T> widgetValueReference) {
		super(Minecraft.getInstance().fontRenderer, 0, 0, 0, 0, message);
		this.widgetValueReference = widgetValueReference;
		this.setMaxStringLength(Integer.MAX_VALUE);
		this.setText(toText(widgetValueReference.get()));
		this.setCursorPositionZero(); // Remove weird scroll bug
		this.func_212954_a(newText -> {
			if (!isWidgetValueValid())
				return;
			T t = null;
			try {
				t = fromText(newText);
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (t != null) // Object wasn't initialised OR Config doesn't accept null values
				widgetValueReference.set(t);
		});
	}

	public WidgetValueReference<T> getWidgetValueReference() {
		return widgetValueReference;
	}

	@Override

	public boolean isWidgetValueValid() {
		try {
			fromText(this.getText());
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	@Override
	public void updateWidgetValue() {
		this.setText(toText(getWidgetValueReference().get()));
	}

	public abstract String toText(final T value);

	public abstract T fromText(final String text) throws Exception;

}
