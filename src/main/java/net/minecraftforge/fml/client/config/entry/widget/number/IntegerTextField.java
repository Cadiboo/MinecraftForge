package net.minecraftforge.fml.client.config.entry.widget.number;

import net.minecraftforge.fml.client.config.entry.widget.ObjectTextField;

/**
 * @author Cadiboo
 */
public class IntegerTextField extends ObjectTextField<Integer> {

	public IntegerTextField(final Callback<Integer> callback) {
		this("Integer", callback);
	}

	public IntegerTextField(final String message, final Callback<Integer> callback) {
		super(message, callback);
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
