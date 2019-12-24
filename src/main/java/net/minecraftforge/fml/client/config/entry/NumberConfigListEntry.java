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
public abstract class NumberConfigListEntry<T extends Number> extends ConfigListEntry<T> {

	private final EntryConfigValue<T> entryConfigValue;
	private final TextFieldWidget textFieldWidget;

	public NumberConfigListEntry(final ConfigScreen configScreen, final ModConfig modConfig, final List<String> path, final ConfigValue<T> configValue) {
		super(configScreen);
		this.entryConfigValue = new EntryConfigValue<>(path, modConfig, configValue);
		this.children().add(this.textFieldWidget = new TextFieldWidget(Minecraft.getInstance().fontRenderer, 0, 0, 18, 18, getLabel()));
		this.textFieldWidget.setMaxStringLength(Integer.MAX_VALUE);
		this.textFieldWidget.setText(entryConfigValue.getCurrentValue().toString());
		this.textFieldWidget.setCursorPositionZero(); // Remove weird scroll bug
		this.textFieldWidget.func_212954_a(s -> {
			if (!this.isValidValue())
				return;
			this.entryConfigValue.setCurrentValue(parse(s));
		});
	}

	@Override
	public boolean isValidValue() {
		try {
			T o = parse(this.textFieldWidget.getText());
			return entryConfigValue.getValueSpec().test(o);
		} catch (NumberFormatException e) {
			return false;
		}
	}

	@Override
	public Widget getWidget() {
		return textFieldWidget;
	}

	@Override
	public EntryConfigValue<T> getEntryConfigValue() {
		return entryConfigValue;
	}

	protected abstract T parse(final String text) throws NumberFormatException;

}
