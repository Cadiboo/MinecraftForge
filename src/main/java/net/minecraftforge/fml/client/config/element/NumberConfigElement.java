package net.minecraftforge.fml.client.config.element;

import net.minecraft.client.gui.widget.Widget;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.client.config.ConfigEntryListWidget;
import net.minecraftforge.fml.client.config.ConfigScreen;
import net.minecraftforge.fml.client.config.entry.ConfigListEntry;
import net.minecraftforge.fml.client.config.entry.ElementConfigListEntry;
import net.minecraftforge.fml.client.config.entry.widget.ConfigListEntryWidget;
import net.minecraftforge.fml.client.config.entry.widget.NumberSlider;
import net.minecraftforge.fml.client.config.entry.widget.ObjectTextField;
import net.minecraftforge.fml.client.config.entry.widget.WidgetValueReference;

/**
 * @author Cadiboo
 */
public abstract class NumberConfigElement<T extends Number & Comparable<? super T>> extends ConfigElement<T> {

	public NumberConfigElement(final ConfigElementContainer<T> entryConfigValue) {
		super(entryConfigValue);
	}

	@Override
	public ConfigListEntry<T> makeConfigListEntry(final ConfigScreen configScreen, final ConfigEntryListWidget configEntryListWidget) {
		final WidgetValueReference<T> widgetValueReference = new WidgetValueReference<>(this::get, this::set, this::getDefault, this::isDefault, this::resetToDefault, this::isChanged, this::undoChanges, this::isValid, this::save);
		final Widget widget;
		if (this.hasSlidingControl())
			widget = makeSlider(widgetValueReference, this.getConfigElementContainer().getValueSpec().getRange());
		else
			widget = makeTextField(widgetValueReference);
		return new ElementConfigListEntry<>(configScreen, cast(widget), this);
	}

	protected NumberSlider<T> makeSlider(final WidgetValueReference<T> widgetValueReference, final ForgeConfigSpec.Range<T> range) {
		return new NumberSlider<>(widgetValueReference, range, this::parse);
	}

	private <W extends Widget & ConfigListEntryWidget<T>> W cast(final Widget widget) {
		return (W) widget;
	}

	protected ObjectTextField<T> makeTextField(final WidgetValueReference<T> widgetValueReference) {
		return new ObjectTextField<T>(widgetValueReference) {

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

	public String stringify(final T value) {
		return value.toString();
	}

	public abstract T parse(final String text) throws NumberFormatException;

}
