package net.minecraftforge.fml.client.config.entry2;

import net.minecraft.client.gui.widget.Widget;
import net.minecraftforge.common.ForgeConfigSpec.Range;
import net.minecraftforge.fml.client.config.ConfigScreen;
import net.minecraftforge.fml.client.config.GuiSlider;
import net.minecraftforge.fml.client.config.element.ConfigElement;
import net.minecraftforge.fml.client.config.element.NumberConfigElement;

/**
 * @author Cadiboo
 */
public class SliderNumberConfigListEntry<T extends Number & Comparable<? super T>> extends ConfigListEntry<T> {

	private final GuiSlider slider;

	public SliderNumberConfigListEntry(final ConfigScreen configScreen, final NumberConfigElement<T> configElement) {
		super(configScreen, configElement);
		// FIXME shhhh
		final Range<T> range = ((ConfigElement<T>) getConfigElement()).getConfigElementContainer().getValueSpec().getRange();
		this.children().add(this.slider = new GuiSlider(0, 0, configElement.get().toString(), range.getMin().doubleValue(), range.getMax().doubleValue(), getConfigElement().get().doubleValue(), b -> {}, s -> {
			final String string = Double.toString(s.getValue());
			final T number = configElement.parse(string);
			getConfigElement().set(number);
		}));
	}

	@Override
	public boolean isValidValue() {
		return true;
	}

	@Override
	public Widget getWidget() {
		return slider;
	}

}
