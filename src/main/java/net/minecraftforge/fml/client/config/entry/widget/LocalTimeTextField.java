package net.minecraftforge.fml.client.config.entry.widget;

import java.time.LocalTime;

/**
 * @author Cadiboo
 */
public class LocalTimeTextField extends ObjectTextField<LocalTime> {

	public LocalTimeTextField(final WidgetValueReference<LocalTime> widgetValueReference) {
		super(widgetValueReference);
	}

	@Override
	public String toText(final LocalTime value) {
		return value.toString();
	}

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
