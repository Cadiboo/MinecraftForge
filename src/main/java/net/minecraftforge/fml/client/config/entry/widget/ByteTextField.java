package net.minecraftforge.fml.client.config.entry.widget;

public class ByteTextField extends ObjectTextField<Byte> {

	public ByteTextField(final WidgetValueReference<Byte> widgetValueReference) {
		super(widgetValueReference);
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
