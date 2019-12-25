package net.minecraftforge.fml.client.config.entry;

import net.minecraft.client.gui.widget.Widget;
import net.minecraft.item.DyeColor;
import net.minecraftforge.common.ForgeConfigSpec.ConfigValue;
import net.minecraftforge.fml.client.config.ConfigScreen;
import net.minecraftforge.fml.client.config.GuiButtonExt;
import net.minecraftforge.fml.config.ModConfig;

import java.util.List;

/**
 * @author Cadiboo
 */
public class EnumConfigListEntry extends ConfigListEntry<Enum<?>> {

	private final ConfigElementContainer<Enum<?>> entryConfigValue;
	private final GuiButtonExt button;

	public EnumConfigListEntry(final ConfigScreen configScreen, final ModConfig modConfig, final List<String> path, final ConfigValue<Enum<?>> configValue) {
		super(configScreen);
		this.entryConfigValue = new ConfigElementContainer<>(path, modConfig, configValue);
		final Enum<?>[] enumConstants = entryConfigValue.getCurrentValue().getClass().getEnumConstants();
		this.children().add(this.button = new GuiButtonExt(0, 0, 18, 18, getLabel(), b -> {
			final Enum<?> newValue = enumConstants[(entryConfigValue.getCurrentValue().ordinal() + 1) % enumConstants.length];
			entryConfigValue.setCurrentValue(newValue);
			updateButtonText();
		}));
		updateButtonText();
	}

	public void updateButtonText() {
		final Enum<?> anEnum = this.entryConfigValue.getCurrentValue();
		this.button.setMessage(getDisplayString(anEnum));
		this.button.setFGColor(getColor(anEnum));
	}

	public int getColor(final Enum<?> anEnum) {
		if (anEnum instanceof DyeColor)
			return ((DyeColor) anEnum).getColorValue();
		return 0xFF_FF_FF; // white
	}

	private String getDisplayString(final Enum<?> anEnum) {
		return anEnum.toString();
	}

	@Override
	public boolean isValidValue() {
		return true;
	}

	@Override
	public Widget getWidget() {
		return button;
	}

	@Override
	protected ConfigElementContainer<Enum<?>> getBooleanConfigElement() {
		return entryConfigValue;
	}

}
