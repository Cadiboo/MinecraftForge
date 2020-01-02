package net.minecraftforge.fml.client.config.element;

import net.minecraft.client.gui.widget.Widget;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.client.config.ConfigScreen;
import net.minecraftforge.fml.client.config.GuiSlider;
import net.minecraftforge.fml.client.config.entry.ElementConfigListEntry;
import net.minecraftforge.fml.client.config.entry.widget.IConfigListEntryWidget;
import net.minecraftforge.fml.client.config.entry.widget.ObjectTextField;
import net.minecraftforge.fml.client.config.entry.widget.number.NumberSlider;

/**
 * @author Cadiboo
 */
public abstract class NumberConfigElement<T extends Number & Comparable<? super T>> extends ConfigElement<T> {

	public NumberConfigElement(final ConfigElementContainer<T> entryConfigValue) {
		super(entryConfigValue);
	}

	@Override
	public ElementConfigListEntry<T> makeConfigListEntry(final ConfigScreen configScreen) {
		final IConfigListEntryWidget.Callback<T> callback = new IConfigListEntryWidget.Callback<>(this::get, this::set, this::getDefault, this::isDefault, this::resetToDefault, this::isChanged, this::undoChanges, this::isValid, this::save);
		final Widget widget;
		if (this.hasSlidingControl())
			widget = this.makeSlider(this.getLabel(), callback, this.getConfigElementContainer().getValueSpec().getRange());
		else
			widget = this.makeTextField(this.getLabel(), callback);
		return new ElementConfigListEntry<>(configScreen, cast(widget), this);
	}

	protected NumberSlider<T> makeSlider(final String label, final IConfigListEntryWidget.Callback<T> callback, final ForgeConfigSpec.Range<T> range) {
		return new NumberSlider<T>(label, callback, range) {
			@Override
			public T getValue(final GuiSlider slider) {
				final Class<?> clazz;
				if (range == null) // Should never happen
					clazz = Byte.class; // Leads to weird results but DOESN'T crash
				else clazz = range.getClazz();
				if (Double.class.isAssignableFrom(clazz))
					return (T) Double.valueOf(slider.getValue());
				else if (Float.class.isAssignableFrom(clazz))
					return (T) Float.valueOf((float) slider.getValue());
				else if (Long.class.isAssignableFrom(clazz))
					return (T) Long.valueOf((long) slider.getValue());
				else
					return parse(Integer.toString(slider.getValueInt()));
			}
		};
	}

	protected ObjectTextField<T> makeTextField(final String label, final IConfigListEntryWidget.Callback<T> callback) {
		return new ObjectTextField<T>(label, callback) {

			@Override
			public String toText(final T value) {
				return stringify(value);
			}

			@Override
			public T fromText(final String text) throws Exception {
				return parse(text);
			}
		};
	}

	private <W extends Widget & IConfigListEntryWidget<T>> W cast(final Widget widget) {
		// Generate ClassCastException if it's not actually assignable.
		((IConfigListEntryWidget<T>) widget).getCallback();
		return (W) widget;
	}

	public String stringify(final T value) {
		return value.toString();
	}

	public abstract T parse(final String text) throws NumberFormatException;

}
