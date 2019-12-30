package net.minecraftforge.fml.client.config.entry.widget;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraftforge.fml.client.config.GuiButtonExt;

/**
 * @author Cadiboo
 */
public class ScreenButton<T> extends GuiButtonExt implements ConfigListEntryWidget<T> {

	private final Callback<T> callback;

//	public ScreenButton(final WidgetValueReference<T> callback, final Screen screen) {
//		this("Screen Button", callback, screen);
//	}

	public ScreenButton(final String message, final Callback<T> callback, final Screen screen) {
		super(0, 0, 0, 0, message, button -> Minecraft.getInstance().displayGuiScreen(screen));
		this.callback = callback;
	}

	public Callback<T> getCallback() {
		return callback;
	}

	@Override
	public boolean isWidgetValueValid() {
		return true;
	}

	@Override
	public void updateWidgetValue() {
	}

	@Override
	public void render(final int p_render_1_, final int p_render_2_, final float p_render_3_) {
		if (isValid())
			this.setFGColor(0);
		else
			this.setFGColor(BooleanButton.getColor(false));
		super.render(p_render_1_, p_render_2_, p_render_3_);
	}

}
