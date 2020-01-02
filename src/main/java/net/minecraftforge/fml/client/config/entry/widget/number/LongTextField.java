package net.minecraftforge.fml.client.config.entry.widget.number;

import net.minecraftforge.fml.client.config.entry.widget.ObjectTextField;

/**
 * @author Cadiboo
 */
public class LongTextField extends ObjectTextField<Long> {

	public LongTextField(final Callback<Long> callback) {
		this("Long", callback);
	}

	public LongTextField(final String message, final Callback<Long> callback) {
		super(message, callback);
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
