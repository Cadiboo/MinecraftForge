package net.minecraftforge.fml.client.config.entry2.fucked;

import net.minecraftforge.fml.client.config.ConfigScreen;
import net.minecraftforge.fml.client.config.element.IConfigElement;

import java.time.LocalDate;

/**
 * @author Cadiboo
 */
public class LocalDateConfigListEntry extends TextConfigListEntry<LocalDate> {

	public LocalDateConfigListEntry(final ConfigScreen configScreen, final IConfigElement<LocalDate> configElement) {
		super(configScreen, configElement);
		getWidget().setMaxStringLength(LocalDate.MIN.toString().length());
		getWidget().func_212954_a(s -> {
			if (!this.isValidValue())
				return;
			getConfigElement().set(parse(s));
		});
	}

	@Override
	public LocalDate parse(final String text) {
		final String[] split = text.split("-");
		final int year = Integer.parseInt(split[0]);
		final int month = Integer.parseInt(split[1]);
		final int day = Integer.parseInt(split[2]);
		return LocalDate.of(year, month, day);
	}

}
