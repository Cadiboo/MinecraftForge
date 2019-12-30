package net.minecraftforge.fml.client.config.entry.widget;

/**
 * @author Cadiboo
 */
public class DoubleTextField extends ObjectTextField<Double> {

	public DoubleTextField(final Callback<Double> callback) {
		this("Double", callback);
	}

	public DoubleTextField(final String message, final Callback<Double> callback) {
		super(message, callback);
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
