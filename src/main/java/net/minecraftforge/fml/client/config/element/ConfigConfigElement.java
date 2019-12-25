package net.minecraftforge.fml.client.config.element;

import com.electronwill.nightconfig.core.Config;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.fml.client.config.ConfigEntryListWidget;
import net.minecraftforge.fml.client.config.ConfigScreen;
import net.minecraftforge.fml.config.ModConfig;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Cadiboo
 */
public class ConfigConfigElement extends HolderConfigElement<Config> {

	private final String label;
	private final String translationKey;
	private final String comment;

	public ConfigConfigElement(final Config config, final ModConfig modConfig, final String label, final String translationKey, final String comment) {
		super(config, makeChildElementsList(config, modConfig));
		this.label = label;
		this.translationKey = translationKey;
		this.comment = comment;
	}

	protected static List<IConfigElement<?>> makeChildElementsList(final Config config, final ModConfig modConfig) {
		final List<IConfigElement<?>> list = new ArrayList<>();
		// obj will always be a ConfigValue or a Config object
		config.valueMap().forEach((name, obj) -> list.add(ConfigScreen.makeConfigElement(modConfig, name, obj)));
		return list;
	}

	@Override
	public String getLabel() {
		return label;
	}

	@Override
	public String getTranslationKey() {
		return translationKey;
	}

	@Override
	public String getComment() {
		return comment;
	}

	@Override
	public boolean isCategory() {
		return true;
	}

	@Override
	protected ConfigScreen makeScreen(final ConfigScreen owningScreen, final ConfigEntryListWidget configEntryListWidget) {
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
