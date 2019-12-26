package net.minecraftforge.fml.client.config.entry;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.widget.Widget;
import net.minecraftforge.fml.client.config.ConfigScreen;
import net.minecraftforge.fml.client.config.entry.widget.ConfigListEntryWidget;
import net.minecraftforge.fml.client.config.entry.widget.WidgetValueReference;

/**
 * @author Cadiboo
 */
public class DummyConfigListEntry<T> extends ConfigListEntry<T> {

	public DummyConfigListEntry(final ConfigScreen configScreen, final String label) {
		super(configScreen, new DummyWidget<>(label));
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

	public static class DummyWidget<T> extends Widget implements ConfigListEntryWidget<T> {

		public DummyWidget(final String label) {
			super(0, 0, 0, 0, label);
		}

		@Override
		public WidgetValueReference<T> getWidgetValueReference() {
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

	}

}
