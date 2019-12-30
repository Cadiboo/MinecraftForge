package net.minecraftforge.fml.client.config.element.category;

import com.google.common.collect.Lists;
import net.minecraft.client.Minecraft;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.fml.client.config.ConfigScreen;
import net.minecraftforge.fml.client.config.ConfigTypesManager;
import net.minecraftforge.fml.client.config.ElementConfigScreen;
import net.minecraftforge.fml.client.config.element.IConfigElement;
import net.minecraftforge.fml.client.config.entry.ConfigListEntry;
import net.minecraftforge.fml.client.config.entry.ScreenElementConfigListEntry;
import net.minecraftforge.fml.client.config.entry.widget.IConfigListEntryWidget;
import net.minecraftforge.fml.client.config.entry.widget.ScreenButton;
import net.minecraftforge.fml.config.ModConfig;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Category element for a ModConfig.
 * Has some hacks because it is a top level element and isn't actually written to/read from the config.
 * (ModConfig is just a wrapper around the NightConfig library + ForgeConfigSpec)
 *
 * @author Cadiboo
 */
public class ModConfigCategoryElement extends CategoryElement<ModConfig> {

	private static final Map<ModConfig.Type, String> COMMENTS;
	static {
		final Map<ModConfig.Type, String> map = new HashMap<>();

		final String commonComment = javadocToString("/**\n" +
				" * Common mod config for configuration that needs to be loaded on both environments.\n" +
				" * Loaded on both servers and clients.\n" +
				" * Stored in the global config directory.\n" +
				" * Not synced.\n" +
				" * Suffix is \"-common\" by default.\n" +
				" */");
		final String clientComment = javadocToString("/**\n" +
				" * Client config is for configuration affecting the ONLY client state such as graphical options.\n" +
				" * Only loaded on the client side.\n" +
				" * Stored in the global config directory.\n" +
				" * Not synced.\n" +
				" * Suffix is \"-client\" by default.\n" +
				" */");
		final String playerComment = javadocToString("/**\n" +
				" * Player type config is configuration that is associated with a player.\n" +
				" * Preferences around machine states, for example.\n" +
				" */");
		final String serverComment = javadocToString("/**\n" +
				" * Server type config is configuration that is associated with a server instance.\n" +
				" * Only loaded during server startup.\n" +
				" * Stored in a server/save specific \"serverconfig\" directory.\n" +
				" * Synced to clients during connection.\n" +
				" * Suffix is \"-server\" by default.\n" +
				" */");

		map.put(ModConfig.Type.COMMON, commonComment);
		map.put(ModConfig.Type.CLIENT, clientComment);
//		map.put(ModConfig.Type.PLAYER, playerComment);
		map.put(ModConfig.Type.SERVER, serverComment);

		COMMENTS = map;
	}

	private final ModConfig modConfig;
	private final String label;
	private final String translationKey;
	private final String comment;
	private final List<IConfigElement<?>> configElements;

	public ModConfigCategoryElement(final ModConfig modConfig) {
		this.modConfig = modConfig;
		final ModConfig.Type type = modConfig.getType();
		final String str = type.name().toLowerCase();
		this.label = StringUtils.capitalize(str);
		this.translationKey = "fml.configgui.modConfigType." + str;
		this.comment = COMMENTS.get(type);
		this.configElements = makeConfigElements(modConfig);
	}

	public static String javadocToString(final String javadoc) {
		try {
			return Arrays.stream(javadoc.split("\n"))
					.map(s -> s.substring(3)) // Remove "/**" or " * " or " */"
					.filter(s -> !s.isEmpty())
					.collect(Collectors.joining("\n"));
		} catch (Exception e) {
			e.printStackTrace();
			return "Unable to parse javadoc! (" + e.getClass().getSimpleName() + " - " + e.getMessage() + ")";
		}
	}

	/**
	 * @return True if in singleplayer and not open to LAN
	 */
	public static boolean canPlayerEditServerConfig() {
		final Minecraft minecraft = Minecraft.getInstance();
		if (minecraft.getIntegratedServer() == null)
			return false;
		if (!minecraft.isSingleplayer())
			return false;
		return !minecraft.getIntegratedServer().getPublic();
	}

	protected List<IConfigElement<?>> makeConfigElements(final ModConfig modConfig) {
		if (!canDisplay())
			return Lists.newArrayList();
		final List<IConfigElement<?>> configElements = new ArrayList<>();
		// name -> ConfigValue|SimpleConfig
		final Map<String, Object> specConfigValues = ConfigTypesManager.getSpecConfigValues(modConfig);
		specConfigValues.forEach((name, obj) -> configElements.add(ConfigTypesManager.makeConfigElement(modConfig, name, obj)));
		ConfigTypesManager.sortElements(configElements);
		return configElements;
	}

	public boolean canDisplay() {
		if (modConfig.getType() == ModConfig.Type.SERVER && !canPlayerEditServerConfig())
			return false;
		return true;
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
	public ModConfig getDefault() {
		return modConfig;
	}

	@Override
	public ModConfig get() {
		return modConfig;
	}

	@Override
	public ConfigListEntry<ModConfig> makeConfigListEntry(final ConfigScreen configScreen) {
		final IConfigListEntryWidget.Callback<ModConfig> callback = new IConfigListEntryWidget.Callback<>(this::get, this::set, this::getDefault, this::isDefault, this::resetToDefault, this::isChanged, this::undoChanges, this::isValid, this::save);
		final ScreenButton<ModConfig> widget;
		if (canDisplay())
			widget = new ScreenButton<>(getLabel(), callback, makeScreen(configScreen));
		else // Do nothing when pressed
			widget = new ScreenButton<ModConfig>(getLabel(), callback, b -> {
			}) {
				@Override
				public void render(final int p_render_1_, final int p_render_2_, final float p_render_3_) {
					this.active = false;
					super.render(p_render_1_, p_render_2_, p_render_3_);
				}
			};
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

	@Nonnull
	@Override
	public List<IConfigElement<?>> getConfigElements() {
		return configElements;
	}

}
