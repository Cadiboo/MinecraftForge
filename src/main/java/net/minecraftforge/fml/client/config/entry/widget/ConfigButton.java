package net.minecraftforge.fml.client.config.entry.widget;

import com.electronwill.nightconfig.core.UnmodifiableConfig;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.client.config.ConfigConfigScreen;
import net.minecraftforge.fml.client.config.ConfigScreen;

/**
 * @author Cadiboo
 */
public class ConfigButton extends ScreenButton<UnmodifiableConfig> {

	public ConfigButton(final ConfigScreen configScreen, final Callback<UnmodifiableConfig> callback) {
		this("Config", configScreen, callback);
	}

	public ConfigButton(final String message, final ConfigScreen configScreen, final Callback<UnmodifiableConfig> callback) {
		super(message, callback, makeScreen(configScreen, callback, message));
	}

	public static Screen makeScreen(final ConfigScreen owningScreen, final Callback<UnmodifiableConfig> callback, final String label) {
		final ConfigConfigScreen configScreen = new ConfigConfigScreen(owningScreen, callback);
		final ITextComponent subtitle;
		if (owningScreen.getSubtitle() == null)
			subtitle = new StringTextComponent(label);
		else
			subtitle = owningScreen.getSubtitle().deepCopy().appendSibling(new StringTextComponent(ConfigScreen.CATEGORY_DIVIDER + label));
		if (configScreen.isUnmodifiable())
			subtitle.appendText(" ").appendSibling(new StringTextComponent("(" + I18n.format("fml.configgui.config.unmodifiable") + ")").applyTextStyle(TextFormatting.RED));
		configScreen.setSubtitle(subtitle);
		return configScreen;
	}

}
