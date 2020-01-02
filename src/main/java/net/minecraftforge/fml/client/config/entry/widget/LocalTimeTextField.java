package net.minecraftforge.fml.client.config.entry.widget;

import java.time.LocalTime;

/**
 * @author Cadiboo
 */
public class LocalTimeTextField extends ObjectTextField<LocalTime> {

	public LocalTimeTextField(final Callback<LocalTime> callback) {
		this("LocalTime", callback);
	}

	public LocalTimeTextField(final String message, final Callback<LocalTime> callback) {
		super(message, callback);
	}

	@Override
	public String toText(final LocalTime value) {
		return value.toString();
	}

	/**
	 * Tries to parse the text to a LocalDate.
	 * TOML Local Times are in RFC 3339 format.
	 * <p>
	 * Examples:
	 * 07:32:00
	 * 00:32:00.999999
	 *
	 * @see "https://github.com/toml-lang/toml#local-time"
	 */
	@Override
	public LocalTime fromText(final String text) throws Exception {
		final String[] hms = text.split(":");

		final int hour = Integer.parseInt(hms[0]);
		final int minute = Integer.parseInt(hms[1]);
		final int second;
		final int nano;
		if (hms.length > 2) {
			final String[] sS = hms[2].split("\\.");
			second = Integer.parseInt(sS[0]);
			if (sS.length > 1)
				nano = Integer.parseInt(sS[1]);
			else
				nano = 0;
		} else
			second = nano = 0;
		return LocalTime.of(hour, minute, second, nano);
	}

}
