package net.minecraftforge.fml.client.config.entry;

import com.electronwill.nightconfig.core.Config;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.fml.client.config.ConfigScreen;
import net.minecraftforge.fml.client.config.element.ConfigElement;
import net.minecraftforge.fml.client.config.element.IConfigElement;
import net.minecraftforge.fml.config.ModConfig;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Cadiboo
 */
public class ConfigCategoryConfigListEntry extends CategoryConfigListEntry<ModConfig> {

	private final ModConfig modConfig;
	private final Config config;

	public ConfigCategoryConfigListEntry(final ConfigScreen owningScreen, final String label, final ModConfig modConfig, final Config config) {
		super(owningScreen, label);
		this.modConfig = modConfig;
		this.config = config;
	}

	/**
	 * This method is called in the constructor and is used to set the childScreen field.
	 */
	protected Screen buildChildScreen() {
		final ConfigScreen configScreen = new ConfigScreen(this.owningScreen.getTitle(), this.owningScreen, this.owningScreen.modContainer, this.makeChildElementsList());
		final ITextComponent subtitle;
		if (this.owningScreen.getSubtitle() == null)
			subtitle = new StringTextComponent(getLabel());
		else
			subtitle = this.owningScreen.getSubtitle().deepCopy().appendSibling(new StringTextComponent(" > " + getLabel()));
		configScreen.setSubtitle(subtitle);
		return configScreen;
	}

	protected List<IConfigElement<?>> makeChildElementsList() {
		final List<IConfigElement<?>> list = new ArrayList<>();
		// obj will always be a ConfigValue or a Config object
		config.valueMap().forEach((name, obj) -> list.add(ConfigScreen.makeConfigElement(modConfig, name, obj)));
		return list;
	}

}
