package net.minecraftforge.fml.client.config.entry.widget;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.client.config.ConfigScreen;
import net.minecraftforge.fml.client.config.ListConfigScreen;

import java.util.List;

/**
 * @author Cadiboo
 */
public class ListButton<T extends List<?>> extends ScreenButton<T> {

	public ListButton(final ConfigScreen configScreen, final Callback<T> callback) {
		this("List", configScreen, callback);
	}

	public ListButton(final String message, final ConfigScreen configScreen, final Callback<T> callback) {
		super(message, callback, makeScreen(configScreen, callback, message));
	}

	public static <L extends List<?>>Screen makeScreen(final ConfigScreen owningScreen, final Callback<L> callback, final String label) {
		final ListConfigScreen<L> configScreen = new ListConfigScreen<>(owningScreen, callback);
		final ITextComponent subtitle;
		if (owningScreen.getSubtitle() == null)
			subtitle = new StringTextComponent(label);
		else
			subtitle = owningScreen.getSubtitle().deepCopy().appendSibling(new StringTextComponent(ConfigScreen.CATEGORY_DIVIDER + label));
		if (configScreen.isFixedSize())
			subtitle.appendText(" ").appendSibling(new StringTextComponent("(" + I18n.format("fml.configgui.list.fixedSize") + ")").applyTextStyle(TextFormatting.RED));
		configScreen.setSubtitle(subtitle);

		return configScreen;
	}

}
