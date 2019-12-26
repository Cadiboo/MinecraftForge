package net.minecraftforge.fml.client.config.entry.widget;

public class IntegerTextField extends ObjectTextField<Integer> {

	public IntegerTextField(final WidgetValueReference<Integer> widgetValueReference) {
		super(widgetValueReference);
	}

	@Override
	public String toText(final Integer value) {
		return value.toString();
	}

	@Override
	public Integer fromText(final String text) throws Exception {
		return Integer.parseInt(text);
	}

}
