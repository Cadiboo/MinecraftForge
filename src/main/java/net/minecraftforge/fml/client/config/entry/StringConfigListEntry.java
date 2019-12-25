package net.minecraftforge.fml.client.config.entry;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.Widget;
import net.minecraftforge.common.ForgeConfigSpec.ConfigValue;
import net.minecraftforge.fml.client.config.ConfigScreen;
import net.minecraftforge.fml.config.ModConfig;

import java.util.List;

/**
 * @author Cadiboo
 */
public class StringConfigListEntry extends ConfigListEntry<String> {

	private final TextFieldWidget textFieldWidget;
	private final ConfigElementContainer<String> entryConfigValue;

	public StringConfigListEntry(final ConfigScreen configScreen, final ModConfig modConfig, final List<String> path, final ConfigValue<String> configValue) {
		super(configScreen);
		this.entryConfigValue = new ConfigElementContainer<>(path, modConfig, configValue);
		this.children().add(this.textFieldWidget = new TextFieldWidget(Minecraft.getInstance().fontRenderer, 0, 0, 18, 18, getLabel()));
		this.textFieldWidget.setMaxStringLength(Integer.MAX_VALUE);
		this.textFieldWidget.setText(getBooleanConfigElement().getCurrentValue());
		this.textFieldWidget.setCursorPositionZero(); // Remove weird scroll bug
		this.textFieldWidget.func_212954_a(s -> {
			if (!this.isValidValue())
				return;
			this.getBooleanConfigElement().setCurrentValue(s);
		});
	}

	@Override
	public boolean isValidValue() {
		return getBooleanConfigElement().getValueSpec().test(this.textFieldWidget.getText());
	}

	@Override
	public Widget getWidget() {
		return textFieldWidget;
	}

	@Override
	protected ConfigElementContainer<String> getBooleanConfigElement() {
		return entryConfigValue;
	}

}
