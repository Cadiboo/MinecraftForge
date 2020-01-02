package net.minecraftforge.fml.client.config.entry.widget.number;

import net.minecraftforge.fml.client.config.entry.widget.ObjectTextField;

/**
 * @author Cadiboo
 */
public class FloatTextField extends ObjectTextField<Float> {

	public FloatTextField(final Callback<Float> callback) {
		this("Float", callback);
	}

	public FloatTextField(final String message, final Callback<Float> callback) {
		super(message, callback);
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
