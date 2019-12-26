package net.minecraftforge.fml.client.config.entry2;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraftforge.fml.client.config.ConfigScreen;
import net.minecraftforge.fml.client.config.GuiButtonExt;
import net.minecraftforge.fml.client.config.element.ConfigElement;
import net.minecraftforge.fml.client.config.entry2.widget.ConfigListEntryWidget;
import net.minecraftforge.fml.client.config.entry2.widget.WidgetValueReference;

/**
 * @author Cadiboo
 */
public class ScreenConfigListEntry<T> extends ConfigListEntry<T> {

	public ScreenConfigListEntry(final ConfigScreen configScreen, final ConfigElement<T> configElement, final Screen nextScreen) {
		super(configScreen, new ScreenButton<>(configElement.getLabel(), b -> Minecraft.getInstance().displayGuiScreen(nextScreen)));
		this.renderLabel = false;
	}

	@Override
	public boolean displayDefaultValue() {
		return false;
	}

	private static class ScreenButton<T> extends GuiButtonExt implements ConfigListEntryWidget<T> {

		public ScreenButton(final String displayString, final IPressable handler) {
			super(0, 0, 0, 0, displayString, handler);
		}

		@Override
		public WidgetValueReference<T> getWidgetValueReference() {
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
		public boolean isValid() {
			return true;
		}

		@Override
		public boolean isWidgetValueValid() {
			return false;
		}

		@Override
		public void updateWidgetValue() {

		}

	}

}
