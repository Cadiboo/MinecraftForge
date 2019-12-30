package net.minecraftforge.fml.client.config.entry.widget;

import java.time.LocalDate;

/**
 * @author Cadiboo
 */
public class LocalDateTextField extends ObjectTextField<LocalDate> {

	public LocalDateTextField(final Callback<LocalDate> callback) {
		this("LocalDate", callback);
	}

	public LocalDateTextField(final String message, final Callback<LocalDate> callback) {
		super(message, callback);
	}

	@Override
	public String toText(final LocalDate value) {
		return value.toString();
	}

	@Override
	public LocalDate fromText(final String text) throws Exception {
		final String[] split = text.split("-");
		final int year = Integer.parseInt(split[0]);
		final int month = Integer.parseInt(split[1]);
		final int day = Integer.parseInt(split[2]);
		return LocalDate.of(year, month, day);
	}

}
