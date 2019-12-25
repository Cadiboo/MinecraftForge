package net.minecraftforge.fml.client.config.entry;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.Widget;
import net.minecraftforge.common.ForgeConfigSpec.ConfigValue;
import net.minecraftforge.fml.client.config.ConfigScreen;
import net.minecraftforge.fml.config.ModConfig;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.temporal.Temporal;
import java.util.List;

/**
 * @author Cadiboo
 */
public class TemporalConfigListEntry extends ConfigListEntry<Temporal> {

	private final TextFieldWidget textFieldWidget;
	private final ConfigElementContainer<Temporal> entryConfigValue;

	public TemporalConfigListEntry(final ConfigScreen configScreen, final ModConfig modConfig, final List<String> path, final ConfigValue<Temporal> configValue) {
		super(configScreen);
		this.entryConfigValue = new ConfigElementContainer<>(path, modConfig, configValue);
		this.children().add(this.textFieldWidget = new TextFieldWidget(Minecraft.getInstance().fontRenderer, 0, 0, 0, 18, getLabel()));
		this.textFieldWidget.setMaxStringLength(LocalDate.MIN.toString().length());
		this.textFieldWidget.setText(getBooleanConfigElement().getCurrentValue().toString());
		this.textFieldWidget.setCursorPositionZero(); // Remove weird scroll bug
		this.textFieldWidget.func_212954_a(s -> {
			if (!this.isValidValue())
				return;

			final String[] split = s.split("-");

			final int year = Integer.parseInt(split[0]);
			final int month = Integer.parseInt(split[1]);
			final int day = Integer.parseInt(split[2]);
			this.getBooleanConfigElement().setCurrentValue(LocalDate.of(year, month, day));
		});
	}

	@Override
	public boolean isValidValue() {
		final String text = this.textFieldWidget.getText();

		final String[] split = text.split("-");
		if (split.length != 3)
			return false;
		try {
			final int year = Integer.parseInt(split[0]);
			final int month = Integer.parseInt(split[1]);
			final int day = Integer.parseInt(split[2]);
			try {
				LocalDate o = LocalDate.of(year, month, day);
				return getBooleanConfigElement().getValueSpec().test(o);
			} catch (DateTimeException e) {
				return false;
			}
		} catch (NumberFormatException e) {
			return false;
		}
	}

	@Override
	public Widget getWidget() {
		return textFieldWidget;
	}

	@Override
	protected ConfigElementContainer<Temporal> getBooleanConfigElement() {
		return entryConfigValue;
	}

}
