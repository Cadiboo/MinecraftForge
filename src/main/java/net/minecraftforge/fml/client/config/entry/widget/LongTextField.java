package net.minecraftforge.fml.client.config.entry.widget;

public class LongTextField extends ObjectTextField<Long> {

	public LongTextField(final WidgetValueReference<Long> widgetValueReference) {
		super(widgetValueReference);
	}

	@Override
	public String toText(final Long value) {
		return value.toString();
	}

	@Override
	public Long fromText(final String text) throws Exception {
		return Long.parseLong(text);
	}

}
