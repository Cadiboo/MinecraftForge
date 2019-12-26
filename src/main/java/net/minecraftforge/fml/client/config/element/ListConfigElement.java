package net.minecraftforge.fml.client.config.element;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.fml.client.config.ConfigEntryListWidget;
import net.minecraftforge.fml.client.config.ConfigScreen;
import net.minecraftforge.fml.client.config.entry.ConfigElementContainer;
import net.minecraftforge.fml.config.ModConfig;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Cadiboo
 */
public class ListConfigElement<T extends List<?>> extends HolderConfigElement<T> {

	private final ConfigElementContainer<T> elementContainer;

	public ListConfigElement(final ConfigElementContainer<T> elementContainer) {
		super(elementContainer.getDefaultValue(), Collections.emptyList());
		this.elementContainer = elementContainer;
	}

	protected static List<IConfigElement<?>> makeChildElementsList(final List<?> list, final ModConfig modConfig) {
		final List<IConfigElement<?>> elements = new ArrayList<>();
//		list.forEach(obj -> elements.add(ConfigScreen.makeConfigElement(modConfig, "obj name", obj)));
		return elements;
	}

	@Override
	public String getLabel() {
		return elementContainer.getLabel();
	}

	@Override
	public String getTranslationKey() {
		return elementContainer.getTranslationKey();
	}

	@Override
	public String getComment() {
		return elementContainer.getComment();
	}

	@Override
	public boolean isCategory() {
		return false;
	}

	@Override
	protected Screen makeScreen(final ConfigScreen owningScreen, final ConfigEntryListWidget configEntryListWidget) {
		final ConfigScreen configScreen = new ConfigScreen(owningScreen.getTitle(), owningScreen, owningScreen.modContainer, getConfigElements());
		final ITextComponent subtitle;
		if (owningScreen.getSubtitle() == null)
			subtitle = new StringTextComponent(getLabel());
		else
			subtitle = owningScreen.getSubtitle().deepCopy().appendSibling(new StringTextComponent(" > " + getLabel()));
		configScreen.setSubtitle(subtitle);
		return configScreen;
	}

}
