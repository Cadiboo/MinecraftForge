package net.minecraftforge.fml.client.config.entry.widget;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.resources.I18n;

/**
 * @author Cadiboo
 */
public class InfoText<T> extends Widget implements IConfigListEntryWidget<T> {

	public InfoText(final String translationKey, String... formatArgs) {
		super(0, 0, 0, 0, I18n.format(translationKey, (Object[]) formatArgs));
		this.active = false;
		this.visible = false;
	}

	@Override
	public Callback<T> getCallback() {
		return null;
	}

	@Override
	public T getDefault() {
		return null;
	}

	@Override
	public boolean isDefault() {
		return true;
	}

	@Override
	public void resetToDefault() {
	}

	@Override
	public boolean isChanged() {
		return false;
	}

	@Override
	public void undoChanges() {
	}

	@Override
	public void save() {
	}

	@Override
	public boolean isValid() {
		return true;
	}

	@Override
	public boolean isWidgetValueValid() {
		return true;
	}

	@Override
	public void updateWidgetValue() {
	}

	@Override
	public void render(final int mouseX, final int mouseY, final float partialTicks) {
		Minecraft minecraft = Minecraft.getInstance();
		FontRenderer fontrenderer = minecraft.fontRenderer;
		this.drawCenteredString(fontrenderer, this.getMessage(), this.x + this.width / 2, this.y + (this.height - 8) / 2, 0xFF_FF_00_00);
	}

	@Override
	public void renderToolTip(final int mouseX, final int mouseY, final float partialTicks) {

	}

}
