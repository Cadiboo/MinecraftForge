package net.minecraftforge.fml.client.config.entry.widget;

public class DoubleTextField extends ObjectTextField<Double> {

	public DoubleTextField(final WidgetValueReference<Double> widgetValueReference) {
		super(widgetValueReference);
	}

	@Override
	public String toText(final Double value) {
		return value.toString();
	}

	@Override
	public Double fromText(final String text) throws Exception {
		return Double.parseDouble(text);
	}

}
