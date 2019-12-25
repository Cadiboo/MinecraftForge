package net.minecraftforge.fml.client.config.entry2;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.Widget;
import net.minecraftforge.fml.client.config.ConfigScreen;
import net.minecraftforge.fml.client.config.GuiButtonExt;
import net.minecraftforge.fml.client.config.element.ConfigElement;

/**
 * @author Cadiboo
 */
public class ScreenConfigListEntry<T> extends ConfigListEntry<T> {

	private final GuiButtonExt button;

	public ScreenConfigListEntry(final ConfigScreen configScreen, final ConfigElement<T> configElement, final Screen nextScreen) {
		super(configScreen, configElement);
		this.children().add(this.button = new GuiButtonExt(0, 0, 0, 0, configElement.getLabel(), b -> Minecraft.getInstance().displayGuiScreen(nextScreen)));
		this.renderLabel = false;
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
	public boolean displayDefaultValue() {
		return false;
	}

}
