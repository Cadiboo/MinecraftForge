package net.minecraftforge.fml.client.config.element;

import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.fml.client.config.ConfigEntriesManager;
import net.minecraftforge.fml.client.config.ConfigEntryListWidget;
import net.minecraftforge.fml.client.config.ConfigScreen;
import net.minecraftforge.fml.config.ModConfig;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * TODO: NO, have the list of config elements in here, create the screen in the Entry
 *
 * @author Cadiboo
 */
public class ModConfigConfigElement extends HolderConfigElement<ModConfig> {

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

	private final String label;
	private final String comment;

	public ModConfigConfigElement(final ModConfig modConfig) {
		super(modConfig, makeConfigElements(modConfig));
		final ModConfig.Type type = modConfig.getType();
		this.label = StringUtils.capitalize(type.name().toLowerCase());
		this.comment = COMMENTS.get(type);
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

	protected static List<IConfigElement<?>> makeConfigElements(final ModConfig modConfig) {
		final List<IConfigElement<?>> configElements = new ArrayList<>();
		// name -> ConfigValue|SimpleConfig
		final Map<String, Object> specConfigValues = ConfigEntriesManager.getSpecConfigValues(modConfig);
		specConfigValues.forEach((name, obj) -> configElements.add(ConfigEntriesManager.makeConfigElement(modConfig, name, obj)));
		return configElements;
	}

	@Override
	public String getLabel() {
		return label;
	}

	@Override
	public String getTranslationKey() {
		return label;
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
