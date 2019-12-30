package net.minecraftforge.fml.client.config.entry.widget;

public class StringTextField extends ObjectTextField<String> {

	public StringTextField(final Callback<String> callback) {
		this("String", callback);
	}

	public StringTextField(final String message, final Callback<String> callback) {
		super(message, callback);
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
