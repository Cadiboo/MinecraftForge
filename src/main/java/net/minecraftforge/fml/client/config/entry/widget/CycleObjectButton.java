package net.minecraftforge.fml.client.config.entry.widget;

import com.google.common.collect.Lists;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraftforge.fml.client.config.GuiButtonExt;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @author Cadiboo
 */
public abstract class CycleObjectButton<T> extends GuiButtonExt implements ConfigListEntryWidget<T> {

	private final WidgetValueReference<T> widgetValueReference;
	private boolean hasChanged = true;
	private boolean isValid = false;

	public CycleObjectButton(final WidgetValueReference<T> widgetValueReference, final Collection<T> acceptableValues) {
		this("Cycle Object Button", widgetValueReference, acceptableValues);
	}

	public CycleObjectButton(final String message, final WidgetValueReference<T> widgetValueReference, final Collection<T> acceptableValues) {
		super(0, 0, 0, 0, message, button -> {
			final ArrayList<T> listAcceptableValues = Lists.newArrayList(acceptableValues);
			final T currentValue = widgetValueReference.get();
			final T newValue = listAcceptableValues.get(listAcceptableValues.indexOf(currentValue) % listAcceptableValues.size());
			widgetValueReference.set(newValue);
			updateText(button, widgetValueReference.get());
		});
		this.widgetValueReference = widgetValueReference;
		updateWidgetValue();
	}

	public static void updateText(final Button button, final Object newValue) {
		button.setMessage(newValue.toString());
	}

	public WidgetValueReference<T> getWidgetValueReference() {
		return widgetValueReference;
	}

	@Override
	public boolean isWidgetValueValid() {
		return true;
	}

	@Override
	public void updateWidgetValue() {
		updateText(this, getWidgetValueReference().get());
	}

}
