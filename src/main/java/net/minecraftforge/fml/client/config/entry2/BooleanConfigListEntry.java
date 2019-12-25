package net.minecraftforge.fml.client.config.entry2;

import net.minecraft.client.gui.widget.Widget;
import net.minecraftforge.fml.client.config.ConfigScreen;
import net.minecraftforge.fml.client.config.GuiButtonExt;
import net.minecraftforge.fml.client.config.element.BooleanConfigElement;

/**
 * @author Cadiboo
 */
public class BooleanConfigListEntry extends ConfigListEntry<Boolean> {

	private final GuiButtonExt button;

	public BooleanConfigListEntry(final ConfigScreen configScreen, final BooleanConfigElement configElement) {
		super(configScreen, configElement);
		this.children().add(this.button = new GuiButtonExt(0, 0, 0, 0, configElement.get().toString(), b -> {
			configElement.set(!configElement.get());
			b.setMessage(configElement.get().toString());
			b.setFGColor(getColor(configElement.get()));
		}));
		this.button.setFGColor(getColor(configElement.get()));
	}

	public static int getColor(final boolean b) {
		return b ? 0x55FF55 : 0xFF5555; // green or red
	}

	@Override
	public boolean isValidValue() {
		return true;
	}

	@Override
	public Widget getWidget() {
		return button;
	}

}
