package net.minecraftforge.fml.client.config.entry2.fucked;

import net.minecraftforge.fml.client.config.ConfigScreen;
import net.minecraftforge.fml.client.config.element.IConfigElement;

import java.time.LocalTime;

/**
 * @author Cadiboo
 */
public class LocalTimeConfigListEntry extends TextConfigListEntry<LocalTime> {

	public LocalTimeConfigListEntry(final ConfigScreen configScreen, final IConfigElement<LocalTime> configElement) {
		super(configScreen, configElement);
		getWidget().setMaxStringLength(LocalTime.MIN.toString().length());
		getWidget().func_212954_a(s -> {
			if (!this.isValidValue())
				return;
			getConfigElement().set(parse(s));
		});
	}

	@Override
	public LocalTime parse(final String text) {
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
