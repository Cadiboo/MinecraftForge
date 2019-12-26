package net.minecraftforge.fml.client.config.entry.widget;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraftforge.fml.client.config.GuiButtonExt;

/**
 * @author Cadiboo
 */
public class ScreenButton<T> extends GuiButtonExt implements ConfigListEntryWidget<T> {

	private final WidgetValueReference<T> widgetValueReference;

//	public ScreenButton(final WidgetValueReference<T> widgetValueReference, final Screen screen) {
//		this("Screen Button", widgetValueReference, screen);
//	}

	public ScreenButton(final String message, final WidgetValueReference<T> widgetValueReference, final Screen screen) {
		super(0, 0, 0, 0, message, button -> Minecraft.getInstance().displayGuiScreen(screen));
		this.widgetValueReference = widgetValueReference;
	}

	public WidgetValueReference<T> getWidgetValueReference() {
		return widgetValueReference;
	}

	@Override
	public boolean isWidgetValueValid() {
		return true;
	}

	@Override
	public void updateWidgetValue() {
	}

}