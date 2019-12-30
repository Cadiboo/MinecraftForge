package net.minecraftforge.fml.client.config.entry.widget;

/**
 * @author Cadiboo
 */
public class ByteTextField extends ObjectTextField<Byte> {

	public ByteTextField(final Callback<Byte> callback) {
		this("Byte", callback);
	}

	public ByteTextField(final String message, final Callback<Byte> callback) {
		super(message, callback);
	}

	@Override
	public String toText(final Byte value) {
		return value.toString();
	}

	@Override
	public Byte fromText(final String text) throws Exception {
		return Byte.parseByte(text);
	}

}
