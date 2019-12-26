package net.minecraftforge.fml.client.config.entry.widget;

public class FloatTextField extends ObjectTextField<Float> {

	public FloatTextField(final WidgetValueReference<Float> widgetValueReference) {
		super(widgetValueReference);
	}

	@Override
	public String toText(final Float value) {
		return value.toString();
	}

	@Override
	public Float fromText(final String text) throws Exception {
		return Float.parseFloat(text);
	}

}
