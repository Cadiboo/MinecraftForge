package net.minecraftforge.fml.client.config;

import com.google.common.util.concurrent.Runnables;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.fml.ExtensionPoint;
import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.client.config.element.IConfigElement;
import net.minecraftforge.fml.client.config.element.category.ModConfigCategoryElement;
import net.minecraftforge.fml.config.ConfigTracker;
import net.minecraftforge.fml.config.ModConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author Cadiboo
 */
public class ModConfigScreen extends ElementConfigScreen {

	public ModConfigScreen(final ITextComponent titleIn, final Screen parentScreen, final ModContainer modContainer) {
		super(titleIn, parentScreen, modContainer, makeElementsForMod(modContainer));
	}

	/**
	 * Makes a Runnable that will register a config gui factory for the ModContainer
	 * BUT ONLY IF a config gui factory does not already exist for the ModContainer.
	 *
	 * @param modContainer The ModContainer to possibly register a config gui factory for
	 * @return The runnable
	 */
	@SuppressWarnings("UnstableApiUsage") // Runnables is marked as unstable
	public static Runnable makeConfigGuiExtensionPoint(final ModContainer modContainer) {
		if (modContainer.getCustomExtension(ExtensionPoint.CONFIGGUIFACTORY).isPresent())
			return Runnables.doNothing();
		return () -> modContainer.registerExtensionPoint(ExtensionPoint.CONFIGGUIFACTORY,
				() -> (minecraft, screen) ->
						new ModConfigScreen(new StringTextComponent(modContainer.getModInfo().getDisplayName()), screen, modContainer));
	}

	public static List<IConfigElement<?>> makeElementsForMod(final ModContainer modContainer) {
		final List<IConfigElement<?>> list = new ArrayList<>();
		for (final ModConfig.Type type : ModConfig.Type.values())
			makeConfigElementForModConfigType(modContainer, type).ifPresent(list::add);
		return list;
	}

	public static Optional<ModConfigCategoryElement> makeConfigElementForModConfigType(final ModContainer modContainer, final ModConfig.Type type) {
		// TODO: @Config classes?
		// Probably don't need to as they will be gotten from getConfig
		return ConfigTracker.INSTANCE.getConfig(modContainer.getModId(), type)
				.map(ModConfigCategoryElement::new);
	}

}
