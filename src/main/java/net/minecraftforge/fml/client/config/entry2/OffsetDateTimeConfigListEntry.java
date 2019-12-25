package net.minecraftforge.fml.client.config.entry2;

import net.minecraftforge.fml.client.config.ConfigScreen;
import net.minecraftforge.fml.client.config.element.IConfigElement;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

/**
 * @author Cadiboo
 */
public class OffsetDateTimeConfigListEntry extends TextConfigListEntry<OffsetDateTime> {

	public OffsetDateTimeConfigListEntry(final ConfigScreen configScreen, final IConfigElement<OffsetDateTime> configElement) {
		super(configScreen, configElement);
		getWidget().setMaxStringLength(OffsetDateTime.MIN.toString().length());
		getWidget().func_212954_a(s -> {
			if (!this.isValidValue())
				return;
			getConfigElement().set(parse(s));
		});
	}

	@Override
	public OffsetDateTime parse(String text) {
		//     * <li>{@code uuuu-MM-dd'T'HH:mmXXXXX}</li>
		//     * <li>{@code uuuu-MM-dd'T'HH:mm:ssXXXXX}</li>
		//     * <li>{@code uuuu-MM-dd'T'HH:mm:ss.SSSXXXXX}</li>
		//     * <li>{@code uuuu-MM-dd'T'HH:mm:ss.SSSSSSXXXXX}</li>
		//     * <li>{@code uuuu-MM-dd'T'HH:mm:ss.SSSSSSSSSXXXXX}</li>

		final int beginIndex = text.length() - 5;

		final ZoneOffset offset = ZoneOffset.of(text.substring(beginIndex));

		text = text.substring(0, beginIndex);

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
			if (sS.length > 1)
				nano = Integer.parseInt(sS[1]);
			else
				nano = 0;
		} else
			second = nano = 0;
		return OffsetDateTime.of(year, month, day, hour, minute, second, nano, offset);
	}

}
