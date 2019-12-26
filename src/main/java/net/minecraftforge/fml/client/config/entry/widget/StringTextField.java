package net.minecraftforge.fml.client.config.entry.widget;

public class StringTextField extends ObjectTextField<String> {

	public StringTextField(final WidgetValueReference<String> widgetValueReference) {
		super(widgetValueReference);
	}

	@Override
	public String toText(final String value) {
		return value;
	}

	@Override
	public String fromText(final String text) throws Exception {
		return text;
	}

}
