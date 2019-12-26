package net.minecraftforge.fml.client.config;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fml.ModContainer;

/**
 * @author Cadiboo
 */
public class ListConfigScreen extends ConfigScreen {

	public ListConfigScreen(final ITextComponent titleIn, final Screen parentScreen, final ModContainer modContainer) {
		super(titleIn, parentScreen, modContainer);
	}

}
