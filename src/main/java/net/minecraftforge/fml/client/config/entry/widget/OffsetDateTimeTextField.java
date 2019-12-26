package net.minecraftforge.fml.client.config.entry.widget;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

/**
 * @author Cadiboo
 */
public class OffsetDateTimeTextField extends ObjectTextField<OffsetDateTime> {

	public OffsetDateTimeTextField(final WidgetValueReference<OffsetDateTime> widgetValueReference) {
		super(widgetValueReference);
	}

	@Override
	public String toText(final OffsetDateTime value) {
		return value.toString();
	}

	@Override
	public OffsetDateTime fromText(String text) throws Exception {
		// * <li>{@code uuuu-MM-dd'T'HH:mmXXXXX}</li>
		// * <li>{@code uuuu-MM-dd'T'HH:mm:ssXXXXX}</li>
		// * <li>{@code uuuu-MM-dd'T'HH:mm:ss.SSSXXXXX}</li>
		// * <li>{@code uuuu-MM-dd'T'HH:mm:ss.SSSSSSXXXXX}</li>
		// * <li>{@code uuuu-MM-dd'T'HH:mm:ss.SSSSSSSSSXXXXX}</li>

		int zoneStart = text.length() - 1;
		while (true) {
			final char c = text.charAt(zoneStart);
			if (c == '+' || c == '-')
				break;
			--zoneStart;
		}

		final ZoneOffset offset = ZoneOffset.of(text.substring(zoneStart));

		text = text.substring(0, zoneStart);

		final String[] split = text.split("T");

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
			if (sS.length > 1) {
				final String S = sS[1];
				final int mul = (int) Math.pow(10, "SSSSSSSSS".length() - S.length());
				nano = Integer.parseInt(S) * mul;
			} else
				nano = 0;
		} else
			second = nano = 0;
		return OffsetDateTime.of(year, month, day, hour, minute, second, nano, offset);

	}

}
