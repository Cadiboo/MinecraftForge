package net.minecraftforge.fml.client.config.entry;

import net.minecraftforge.fml.client.config.ConfigScreen;
import net.minecraftforge.fml.client.config.entry.widget.InfoText;

/**
 * A dummy ConfigListEntry that just displays text (Usually error text) and does nothing else.
 *
 * @author Cadiboo
 */
public class InfoTextConfigListEntry<T> extends ConfigListEntry<T> {

	public InfoTextConfigListEntry(final ConfigScreen configScreen, final String label) {
		super(configScreen, new InfoText<>(label));
	}

	@Override
	public void renderToolTip(final int mouseX, final int mouseY, final float partialTicks) {
	}

	@Override
	public int preRenderWidgets(final int startY, final int startX, final int width, final int height, final int buttonSize) {
		return width;
	}

	@Override
	public boolean shouldRenderLabel() {
		return false;
	}

	@Override
	public boolean displayDefaultValue() {
		return false;
	}

}
