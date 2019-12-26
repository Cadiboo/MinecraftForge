package net.minecraftforge.fml.client.config.element;

import com.electronwill.nightconfig.core.Config;
import joptsimple.internal.Strings;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.fml.client.config.ConfigEntryListWidget;
import net.minecraftforge.fml.client.config.ConfigScreen;
import net.minecraftforge.fml.client.config.ConfigTypesManager;
import net.minecraftforge.fml.client.config.ElementConfigScreen;
import net.minecraftforge.fml.client.config.entry.ConfigListEntry;
import net.minecraftforge.fml.client.config.entry.ScreenElementConfigListEntry;
import net.minecraftforge.fml.client.config.entry.widget.ScreenButton;
import net.minecraftforge.fml.client.config.entry.widget.WidgetValueReference;
import net.minecraftforge.fml.config.ModConfig;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author Cadiboo
 */
public class ConfigConfigElement extends CategoryConfigElement<Config> {

	private final Config config;
	private final String label;
	private final String translationKey;
	private final String comment;
	private final List<IConfigElement<?>> elements;

	public ConfigConfigElement(final Config config, final ModConfig modConfig, final String name) {
		this.config = config;
		String translationKey = ""; // TODO: No clue how to get this for categories. Doesn't seem to exist currently?
		String label = I18n.format(translationKey);
		if (Objects.equals(translationKey, label))
			label = name;
		String comment = modConfig.getConfigData().getComment(name); // TODO: Only works for top-level categories?
		if (Strings.isNullOrEmpty(comment))
			comment = "";
		this.label = label;
		this.translationKey = translationKey;
		this.comment = comment;
		this.elements = this.makeChildElementsList(config, modConfig);
	}

	@Nonnull
	public List<IConfigElement<?>> getConfigElements() {
		return elements;
	}

	protected List<IConfigElement<?>> makeChildElementsList(final Config config, final ModConfig modConfig) {
		final List<IConfigElement<?>> list = new ArrayList<>();
		// obj will always be a ConfigValue or a Config object
		config.valueMap().forEach((name, obj) -> list.add(ConfigTypesManager.makeConfigElement(modConfig, name, obj)));
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
	public Config getDefault() {
		return config;
	}

	@Override
	public Config get() {
		return config;
	}

	@Override
	public ConfigListEntry<Config> makeConfigListEntry(final ConfigScreen configScreen, final ConfigEntryListWidget configEntryListWidget) {
		final WidgetValueReference<Config> widgetValueReference = new WidgetValueReference<>(this::get, this::set, this::getDefault, this::isDefault, this::resetToDefault, this::isChanged, this::undoChanges, this::isValid, this::save);
		final Screen screen = makeScreen(configScreen);
		final ScreenButton<Config> widget = new ScreenButton<>(getLabel(), widgetValueReference, screen);
		return new ScreenElementConfigListEntry<>(configScreen, widget, this);
	}

	protected ConfigScreen makeScreen(final ConfigScreen owningScreen) {
		final ConfigScreen configScreen = new ElementConfigScreen(owningScreen.getTitle(), owningScreen, owningScreen.modContainer, getConfigElements());
		final ITextComponent subtitle;
		if (owningScreen.getSubtitle() == null)
			subtitle = new StringTextComponent(getLabel());
		else
			subtitle = owningScreen.getSubtitle().deepCopy().appendSibling(new StringTextComponent(ConfigScreen.CATEGORY_DIVIDER + getLabel()));
		configScreen.setSubtitle(subtitle);
		return configScreen;
	}

}
