package net.minecraftforge.fml.client.config.entry;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.Widget;
import net.minecraftforge.fml.client.config.ConfigScreen;

/**
 * @author Cadiboo
 */
public class StringListEntryConfigListEntry extends ListConfigListEntry<String> {

	private final TextFieldWidget textFieldWidget;

	public StringListEntryConfigListEntry(final ConfigScreen owningScreen, final String obj) {
		super(owningScreen, obj);
		this.children().add(this.textFieldWidget = new TextFieldWidget(Minecraft.getInstance().fontRenderer, 0, 0, 18, 18, getLabel()));
		this.textFieldWidget.setMaxStringLength(Integer.MAX_VALUE);
		this.textFieldWidget.setText(obj);
		this.textFieldWidget.setCursorPositionZero(); // Remove weird scroll bug
		this.textFieldWidget.func_212954_a(s -> this.obj = s);
	}

	@Override
	public Widget getWidget() {
		return textFieldWidget;
	}

}
