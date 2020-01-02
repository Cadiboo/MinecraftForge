package net.minecraftforge.fml.client.config.entry.widget;

import java.time.LocalDateTime;

/**
 * @author Cadiboo
 */
public class LocalDateTimeTextField extends ObjectTextField<LocalDateTime> {

	public LocalDateTimeTextField(final Callback<LocalDateTime> callback) {
		this("LocalDateTime", callback);
	}

	public LocalDateTimeTextField(final String message, final Callback<LocalDateTime> callback) {
		super(message, callback);
	}

	@Override
	public String toText(final LocalDateTime value) {
		return value.toString();
	}

	/**
	 * Tries to parse the text to a LocalDate.
	 * TOML Local Date Times are in RFC 3339 format.
	 * <p>
	 * Examples:
	 * 1979-05-27T07:32:00
	 * 1979-05-27T00:32:00.999999
	 *
	 * @see "https://github.com/toml-lang/toml#local-date-time"
	 */
	@Override
	public LocalDateTime fromText(final String text) throws Exception {
		final String[] split = text.split("[Tt ]"); // See com.electronwill.nightconfig.toml.TemporalParser.ALLOWED_DT_SEPARATORS

		final String[] ymd = split[0].split("-");
		final int year = Integer.parseInt(ymd[0]);
		final int month = Integer.parseInt(ymd[1]);
		final int day = Integer.parseInt(ymd[2]);

		final String[] hms = split[1].split(":");

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
		return LocalDateTime.of(year, month, day, hour, minute, second, nano);
	}

}
