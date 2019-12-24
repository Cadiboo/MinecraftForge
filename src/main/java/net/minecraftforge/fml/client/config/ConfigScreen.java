/*
 * Minecraft Forge
 * Copyright (c) 2016-2019.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation version 2.1
 * of the License.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */

package net.minecraftforge.fml.client.config;

import com.electronwill.nightconfig.core.Config;
import com.google.common.util.concurrent.Runnables;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.gui.widget.button.CheckboxButton;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.common.ForgeConfigSpec.ConfigValue;
import net.minecraftforge.fml.ExtensionPoint;
import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.client.config.entry.BooleanConfigListEntry;
import net.minecraftforge.fml.client.config.entry.ConfigCategoryConfigListEntry;
import net.minecraftforge.fml.client.config.entry.ConfigListEntry;
import net.minecraftforge.fml.client.config.entry.DummyConfigListEntry;
import net.minecraftforge.fml.client.config.entry.EnumConfigListEntry;
import net.minecraftforge.fml.client.config.entry.ListCategoryConfigListEntry;
import net.minecraftforge.fml.client.config.entry.ModConfigCategoryConfigListEntry;
import net.minecraftforge.fml.client.config.entry.NumberConfigListEntry;
import net.minecraftforge.fml.client.config.entry.StringConfigListEntry;
import net.minecraftforge.fml.client.config.entry.TemporalConfigListEntry;
import net.minecraftforge.fml.config.ConfigTracker;
import net.minecraftforge.fml.config.ModConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nullable;
import java.time.temporal.Temporal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import static net.minecraftforge.common.ForgeConfigSpec.ValueSpec;
import static net.minecraftforge.fml.client.config.GuiUtils.RESET_CHAR;
import static net.minecraftforge.fml.client.config.GuiUtils.UNDO_CHAR;

/**
 * This class is the base Screen for all config GUI screens.
 * It can be extended by mods to provide the top-level config screen that will be called when
 * the Config button is clicked from the Mods Menu list.
 *
 * @author bspkrs
 * @author Cadiboo
 */
public class ConfigScreen extends Screen {

	public static final int BUTTON_HEIGHT = 20;
	/**
	 * The empty space above & below the buttons at the bottom of the screen, as well as between each button.
	 */
	public static final int MARGIN = 5;

	private static final Logger LOGGER = LogManager.getLogger();

	/**
	 * A reference to the screen object that created this. Used for navigating between screens.
	 * If this is an instance of ConfigScreen, the config elements on this screen will NOT be saved when this is closed.
	 */
	public final Screen parentScreen;
	/**
	 * The mod that this config is for.
	 */
	public final ModContainer modContainer;
	/**
	 * A list of elements on this screen.
	 * Re-created when {@link #init()} is called if {@link #needsRefresh} is true.
	 */
	private final List<ConfigListEntry<?>> configElements;
	/**
	 * The initial list of elements on this screen. Same as the first value of {@link #configElements}
	 */
	private final List<ConfigListEntry<?>> initialConfigElements;
	/**
	 * If true then the entryList & configValueElements will be re-created next time {@link #init()} is called.
	 */
	public boolean needsRefresh = true;
	/**
	 * When clicked undoes all changes that the user made on this screen
	 * (sets all config entries on this screen to the value they had before any changes were made).
	 * If applyToSubcategoriesCheckBox is checked also undoes all changes to child screens.
	 */
	protected GuiButtonExt undoChangesButton;
	/**
	 * When clicked resets all config entries on this screen to their default values.
	 * If applyToSubcategoriesCheckBox is checked also resets all config entries on child screens.
	 */
	protected GuiButtonExt resetToDefaultButton;
	/**
	 * If the effects of clicking the undoChangesButton or the resetToDefaultButton should propagate to child screens.
	 */
	protected CheckboxButton applyToSubcategoriesCheckBox;
	/**
	 * Used to determine if specific tooltips should be rendered.
	 */
	protected HoverChecker undoChangesButtonHoverChecker, resetToDefaultButtonHoverChecker, applyToSubcategoriesCheckBoxHoverChecker;
	private ITextComponent subtitle;
	/**
	 * Displays all our {@link #configElements} in a scrollable list
	 */
	private ConfigEntryListWidget entryList;

	public ConfigScreen(final ITextComponent titleIn, final Screen parentScreen, final ModContainer modContainer, @Nullable Function<ConfigScreen, List<ConfigListEntry<?>>> makeConfigElements) {
		super(titleIn);
		this.parentScreen = parentScreen;
		this.modContainer = modContainer;
		if (makeConfigElements == null)
			this.configElements = this.initialConfigElements = makeElementsForMod();
		else
			this.configElements = this.initialConfigElements = makeConfigElements.apply(this);
	}

	public ConfigScreen(final ITextComponent titleIn, final Screen parentScreen, final ModContainer modContainer) {
		this(titleIn, parentScreen, modContainer, null);
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
						new ConfigScreen(new StringTextComponent(modContainer.getModInfo().getDisplayName()), screen, modContainer));
	}

	/**
	 * @param obj Either a ConfigValue or a Config
	 * @see #getSpecConfigValues(ModConfig)
	 */
	public static ConfigListEntry<?> makeConfigListEntry(final ConfigScreen configScreen, final ModConfig modConfig, final String name, final Object obj) {
		if (obj instanceof ConfigValue) {
			final ConfigValue<?> configValue = (ConfigValue<?>) obj;
			// Because the obj is a ConfigValue the corresponding object in the ValueSpec map must be a ValueSpec
			final ValueSpec valueSpec = (ValueSpec) getValueSpec(modConfig, configValue.getPath());

			Class<?> clazz = valueSpec.getClazz();
			if (clazz == Object.class) {
				final Object actualValue = configValue.get();
				final Class<?> valueClass = actualValue.getClass();
				if (valueClass != null)
					clazz = valueClass;
				else {
					final Object defaultValue = valueSpec.getDefault();
					if (defaultValue != null)
						clazz = defaultValue.getClass();
				}
			}
			if (Boolean.class.isAssignableFrom(clazz)) {
				return new BooleanConfigListEntry(configScreen, modConfig, configValue.getPath(), (ConfigValue<Boolean>) configValue);
			} else if (Byte.class.isAssignableFrom(clazz)) {
				return new NumberConfigListEntry<Byte>(configScreen, modConfig, configValue.getPath(), (ConfigValue<Byte>) configValue) {
					@Override
					protected Byte parse(final String text) {
						return Byte.parseByte(text);
					}
				};
			} else if (Integer.class.isAssignableFrom(clazz)) {
				return new NumberConfigListEntry<Integer>(configScreen, modConfig, configValue.getPath(), (ConfigValue<Integer>) configValue) {
					@Override
					protected Integer parse(final String text) {
						return Integer.parseInt(text);
					}
				};
			} else if (Float.class.isAssignableFrom(clazz)) {
				return new NumberConfigListEntry<Float>(configScreen, modConfig, configValue.getPath(), (ConfigValue<Float>) configValue) {
					@Override
					protected Float parse(final String text) {
						return Float.parseFloat(text);
					}
				};
			} else if (Long.class.isAssignableFrom(clazz)) {
				return new NumberConfigListEntry<Long>(configScreen, modConfig, configValue.getPath(), (ConfigValue<Long>) configValue) {
					@Override
					protected Long parse(final String text) {
						return Long.parseLong(text);
					}
				};
			} else if (Double.class.isAssignableFrom(clazz)) {
				return new NumberConfigListEntry<Double>(configScreen, modConfig, configValue.getPath(), (ConfigValue<Double>) configValue) {
					@Override
					protected Double parse(final String text) {
						return Double.parseDouble(text);
					}
				};
			} else if (String.class.isAssignableFrom(clazz)) {
				return new StringConfigListEntry(configScreen, modConfig, configValue.getPath(), (ConfigValue<String>) configValue);
			} else if (Enum.class.isAssignableFrom(clazz)) {
				return new EnumConfigListEntry(configScreen, modConfig, configValue.getPath(), (ConfigValue<Enum<?>>) configValue);
			} else if (List.class.isAssignableFrom(clazz)) {
				return new ListCategoryConfigListEntry(configScreen, modConfig, configValue.getPath(), (ConfigValue<List<?>>) configValue);
			} else if (Temporal.class.isAssignableFrom(clazz)) {
				return new TemporalConfigListEntry(configScreen, modConfig, configValue.getPath(), (ConfigValue<Temporal>) configValue);
			} else {
				return new DummyConfigListEntry(configScreen, name);
			}
		} else if (obj instanceof Config) {
			final Config config = (Config) obj;
			return new ConfigCategoryConfigListEntry(configScreen, name, modConfig, config);
		} else {
			throw new IllegalStateException("How? " + name + ", " + obj);
		}
	}

//	/**
//	 * @param parentScreen           the parent Screen object
//	 * @param modID                  the mod ID for the mod whose config settings will be editted
//	 * @param allRequireWorldRestart whether all config elements on this screen require a world restart
//	 * @param allRequireMcRestart    whether all config elements on this screen require a game restart
//	 * @param title                  the desired title for this screen. For consistency it is recommended that you pass the path of the config file being
//	 *                               edited.
//	 * @param configClasses          an array of classes annotated with {@code @Config} providing the configuration
//	 */
//	public ConfigScreen(Screen parentScreen, String modID, boolean allRequireWorldRestart, boolean allRequireMcRestart, String title, Class<?>... configClasses) {
//		this(parentScreen, collectConfigElements(configClasses), modID, null, allRequireWorldRestart, allRequireMcRestart, title, null);
//	}
//
//	/**
//	 * GuiConfig constructor that will use ConfigChangedEvent when editing is concluded. If a non-null value is passed for configID,
//	 * the OnConfigChanged and PostConfigChanged events will be posted when the Done button is pressed if any configElements were changed
//	 * (includes child screens). If configID is not defined, the events will be posted if the parent gui is null or if the parent gui
//	 * is not an instance of GuiConfig.
//	 *
//	 * @param parentScreen           the parent Screen object
//	 * @param configElements         a List of IConfigElement objects
//	 * @param modID                  the mod ID for the mod whose config settings will be edited
//	 * @param configID               an identifier that will be passed to the OnConfigChanged and PostConfigChanged events. Setting this value will force
//	 *                               the save action to be called when the Done button is pressed on this screen if any configElements were changed.
//	 * @param allRequireWorldRestart send true if all configElements on this screen require a world restart
//	 * @param allRequireMcRestart    send true if all configElements on this screen require MC to be restarted
//	 * @param title                  the desired title for this screen. For consistency it is recommended that you pass the path of the config file being
//	 *                               edited.
//	 */
//	public ConfigScreen(Screen parentScreen, List<IConfigElement> configElements, String modID, String configID,
//	                    boolean allRequireWorldRestart, boolean allRequireMcRestart, String title) {
//		this(parentScreen, configElements, modID, configID, allRequireWorldRestart, allRequireMcRestart, title, null);
//	}
//
//	/**
//	 * GuiConfig constructor that will use ConfigChangedEvent when editing is concluded. This constructor passes null for configID.
//	 * If configID is not defined, the events will be posted if the parent gui is null or if the parent gui is not an instance of GuiConfig.
//	 *
//	 * @param parentScreen           the parent Screen object
//	 * @param configElements         a List of IConfigElement objects
//	 * @param modID                  the mod ID for the mod whose config settings will be edited
//	 * @param allRequireWorldRestart send true if all configElements on this screen require a world restart
//	 * @param allRequireMcRestart    send true if all configElements on this screen require MC to be restarted
//	 * @param title                  the desired title for this screen. For consistency it is recommended that you pass the path of the config file being
//	 *                               edited.
//	 */
//	public ConfigScreen(Screen parentScreen, List<IConfigElement> configElements, String modID,
//	                    boolean allRequireWorldRestart, boolean allRequireMcRestart, String title) {
//		this(parentScreen, configElements, modID, null, allRequireWorldRestart, allRequireMcRestart, title, null);
//	}
//
//	/**
//	 * GuiConfig constructor that will use ConfigChangedEvent when editing is concluded. This constructor passes null for configID.
//	 * If configID is not defined, the events will be posted if the parent gui is null or if the parent gui is not an instance of GuiConfig.
//	 *
//	 * @param parentScreen           the parent Screen object
//	 * @param configElements         a List of IConfigElement objects
//	 * @param modID                  the mod ID for the mod whose config settings will be edited
//	 * @param allRequireWorldRestart send true if all configElements on this screen require a world restart
//	 * @param allRequireMcRestart    send true if all configElements on this screen require MC to be restarted
//	 * @param title                  the desired title for this screen. For consistency it is recommended that you pass the path of the config file being
//	 *                               edited.
//	 * @param titleLine2             the desired title second line for this screen. Typically this is used to send the category name of the category
//	 *                               currently being edited.
//	 */
//	public ConfigScreen(Screen parentScreen, List<IConfigElement> configElements, String modID,
//	                    boolean allRequireWorldRestart, boolean allRequireMcRestart, String title, String titleLine2) {
//		this(parentScreen, configElements, modID, null, allRequireWorldRestart, allRequireMcRestart, title, titleLine2);
//	}
//
//	/**
//	 * @param parentScreen           the parent Screen object
//	 * @param configElements         a List of IConfigElement objects
//	 * @param modID                  the mod ID for the mod whose config settings will be edited
//	 * @param configID               an identifier that will be passed to the OnConfigChanged and PostConfigChanged events
//	 * @param allRequireWorldRestart send true if all configElements on this screen require a world restart
//	 * @param allRequireMcRestart    send true if all configElements on this screen require MC to be restarted
//	 * @param title                  the desired title for this screen. For consistency it is recommended that you pass the path of the config file being
//	 *                               edited.
//	 * @param subtitle               the desired title second line for this screen. Typically this is used to send the category name of the category
//	 *                               currently being edited.
//	 */
//	public ConfigScreen(
//			Screen parentScreen, List<IConfigElement> configElements, String modID, @Nullable String configID, boolean allRequireWorldRestart, boolean allRequireMcRestart, String title, @Nullable String subtitle) {
//		this.minecraft = Minecraft.getInstance();
//		this.parentScreen = parentScreen;
//		this.configElements = configElements;
//		this.entryList = new GuiConfigEntries(this, minecraft);
//		this.initEntries = new ArrayList<IConfigEntry>(entryList.listEntries);
//		this.allRequireWorldRestart = allRequireWorldRestart;
//		IF:
//		if (!allRequireWorldRestart) {
//			for (IConfigElement element : configElements) {
//				if (!element.requiresWorldRestart()) ;
//				break IF;
//			}
//			allRequireWorldRestart = true;
//		}
//		this.allRequireMcRestart = allRequireMcRestart;
//		IF:
//		if (!allRequireMcRestart) {
//			for (IConfigElement element : configElements) {
//				if (!element.requiresMcRestart()) ;
//				break IF;
//			}
//			allRequireMcRestart = true;
//		}
//		this.modID = modID;
//		this.isWorldRunning = minecraft.world != null;
//		if (title != null)
//			this.title = title;
//		this.subtitle = subtitle;
//		if (this.subtitle != null && this.subtitle.startsWith(" > "))
//			this.subtitle = this.subtitle.replaceFirst(" > ", "");
//	}
//
//	private static List<IConfigElement> collectConfigElements(Class<?>[] configClasses) {
//		List<IConfigElement> toReturn;
//		if (configClasses.length == 1) {
//			toReturn = ConfigElement.from(configClasses[0]).getChildElements();
//		} else {
//			toReturn = new ArrayList<IConfigElement>();
//			for (Class<?> clazz : configClasses) {
//				toReturn.add(ConfigElement.from(clazz));
//			}
//		}
//		toReturn.sort(Comparator.comparing(e -> I18n.format(e.getLanguageKey())));
//		return toReturn;
//	}

	public static Map<String, Object> getSpecValueSpecs(final ModConfig modConfig) {
		// name -> ValueSpec|SimpleConfig
		return modConfig.getSpec().valueMap();
	}

	public static Map<String, Object> getSpecConfigValues(final ModConfig modConfig) {
		// name -> ConfigValue|SimpleConfig
		return modConfig.getSpec().getValues().valueMap();
	}

	public static Map<String, Object> getConfigValues(final ModConfig modConfig) {
		// name -> Object
		return modConfig.getConfigData().valueMap();
	}

	public static Object getValueSpec(final ModConfig modConfig, final List<String> path) {
		// name -> ValueSpec|SimpleConfig
		final Map<String, Object> specValueSpecs = getSpecValueSpecs(modConfig);

		// Either a ValueSpec or a SimpleConfig
		Object ret = specValueSpecs;

		for (final String s : path) {
			if (ret instanceof Map) // First iteration
				ret = ((Map<String, Object>) ret).get(s);
			else if (ret instanceof ValueSpec)
				return ret; // Uh, shouldn't happen? TODO: Throw error?
			else if (ret instanceof Config)
				ret = ((Config) ret).get(s);
		}
		return ret;
	}

	public static Object getConfigValue(final ModConfig modConfig, final List<String> path) {
		// name -> ConfigValue|SimpleConfig
		final Map<String, Object> specConfigVales = getSpecConfigValues(modConfig);

		// Either a ConfigValue or a SimpleConfig
		Object ret = specConfigVales;

		for (final String s : path) {
			if (ret instanceof Map) // First iteration
				ret = ((Map<String, Object>) ret).get(s);
			else if (ret instanceof ConfigValue)
				return ret; // Uh, shouldn't happen? TODO: Throw error?
			else if (ret instanceof Config)
				ret = ((Config) ret).get(s);
		}
		return ret;
	}

	/**
	 * @return True if in singleplayer and not open to LAN
	 */
	protected boolean canPlayerEditServerConfig() {
		final Minecraft minecraft = Minecraft.getInstance();
		if (minecraft.getIntegratedServer() == null)
			return false;
		if (!minecraft.isSingleplayer())
			return false;
		return !minecraft.getIntegratedServer().getPublic();
	}

	protected List<ConfigListEntry<?>> makeElementsForMod() {
		final List<ConfigListEntry<?>> list = new ArrayList<>();
		for (final ModConfig.Type type : ModConfig.Type.values())
			makeConfigElementForModConfigType(type).ifPresent(list::add);
		return list;
	}

	protected Optional<ModConfigCategoryConfigListEntry> makeConfigElementForModConfigType(final ModConfig.Type type) {

		if (type == ModConfig.Type.SERVER && !canPlayerEditServerConfig())
			return Optional.empty();

		final ModConfig modConfig = ConfigTracker.INSTANCE.getConfig(modContainer.getModId(), type).orElse(null);
		if (modConfig == null)
			return Optional.empty();

		return Optional.of(new ModConfigCategoryConfigListEntry(this, modConfig));
	}

	@Nullable
	public ConfigEntryListWidget getEntryList() {
		return entryList;
	}

	private void onDoneButtonClicked(final Button button) {
		boolean canClose = true;
		try {
			if (this.entryList.areAnyEntriesChanged(this.shouldApplyToSubcategories())) {
				if (parentScreen != null && parentScreen instanceof ConfigScreen) {
					// Mark as needing to re-init the entry list.
					// Why? Maybe to allow adding of stuff to the config? IDK
//					((ConfigScreen) this.parentScreen).needsRefresh = true;
				} else {
					boolean requiresMcRestart = this.entryList.save();
					boolean requiresWorldRestart = this.entryList.anyRequireWorldRestart();
					if (requiresMcRestart) {
						canClose = false;
						getMinecraft().displayGuiScreen(new GuiMessageDialog(parentScreen, "fml.configgui.gameRestartTitle", new StringTextComponent(I18n.format("fml.configgui.gameRestartRequired")), "fml.configgui.confirmMessage"));
					}
					if (requiresWorldRestart && Minecraft.getInstance().world != null) {
						canClose = false;
						getMinecraft().displayGuiScreen(new GuiMessageDialog(parentScreen, "fml.configgui.worldRestartTitle", new StringTextComponent(I18n.format("fml.configgui.worldRestartRequired")), "fml.configgui.confirmMessage"));
					}
				}
			}
		} catch (Throwable e) {
			LOGGER.error("Error performing GuiConfig action:", e);
		}
		if (canClose)
			this.onClose();
	}

	private boolean shouldApplyToSubcategories() {
		return this.applyToSubcategoriesCheckBox.func_212942_a();
	}

	private void onResetToDefaultButtonClicked(final Button button) {
		this.entryList.resetAllToDefault(shouldApplyToSubcategories());
	}

	private void onUndoChangesButtonClicked(final Button button) {
		this.entryList.undoAllChanges(shouldApplyToSubcategories());
	}

	/**
	 * Draws the screen and all the components in it.
	 */
	@Override
	public void render(int mouseX, int mouseY, float partialTicks) {
		this.renderBackground();
		this.entryList.render(mouseX, mouseY, partialTicks);
//		this.minecraft.getTextureManager().bindTexture(AtlasTexture.LOCATION_BLOCKS_TEXTURE);
//		innerBlit(0, 10000, 0, 10000, this.getBlitOffset(), 0, 100, 0, 100);
		GL11.glDisable(GL11.GL_SCISSOR_TEST);

		final int halfWidth = this.width / 2;

		this.drawCenteredString(font, this.title.getFormattedText(), halfWidth, 5, 0xFFFFFF);

		if (subtitle != null) {
			String title2 = subtitle.getFormattedText();
			int strWidth = font.getStringWidth(title2);
			int ellipsisWidth = font.getStringWidth("...");
			if (strWidth > width - 6 && strWidth > ellipsisWidth)
				title2 = font.trimStringToWidth(title2, width - 6 - ellipsisWidth).trim() + "...";
			this.drawCenteredString(font, title2, halfWidth, 20, 0x9D9D97);
		}

		super.render(mouseX, mouseY, partialTicks);
		this.entryList.postRender(mouseX, mouseY, partialTicks);

		if (this.undoChangesButtonHoverChecker.checkHover(mouseX, mouseY))
			this.drawToolTip(Arrays.asList(I18n.format("fml.configgui.tooltip.undoChanges").split("\n")), mouseX, mouseY);
		if (this.resetToDefaultButtonHoverChecker.checkHover(mouseX, mouseY))
			this.drawToolTip(Arrays.asList(I18n.format("fml.configgui.tooltip.resetToDefault").split("\n")), mouseX, mouseY);
		if (this.applyToSubcategoriesCheckBoxHoverChecker.checkHover(mouseX, mouseY))
			this.drawToolTip(Arrays.asList(I18n.format("fml.configgui.tooltip.applyToSubcategories").split("\n")), mouseX, mouseY);
	}

	/**
	 * Called when the screen is unloaded.
	 */
	@Override
	public void onClose() {
		this.entryList.onClose();
//		if (this.configID != null && this.parentScreen instanceof ConfigScreen) {
//			ConfigScreen parentConfigScreen = (ConfigScreen) this.parentScreen;
//			parentConfigScreen.needsRefresh = true;
//			parentConfigScreen.init();
//		}
		getMinecraft().displayGuiScreen(parentScreen);
	}

	/**
	 * Adds the buttons (and other controls) to the screen in question.
	 * Called when the GUI is displayed and when the window resizes, the buttonList is cleared beforehand.
	 */
	@Override
	public void init() {
		super.init();

		String doneText = I18n.format("gui.done");
		String undoText = I18n.format("fml.configgui.undoChanges");
		String resetText = I18n.format("fml.configgui.resetToDefault");
		String applyToSubcategoriesText = I18n.format("fml.configgui.applyToSubcategories");

		int undoGlyphWidth = font.getStringWidth(UNDO_CHAR) * 2;
		int resetGlyphWidth = font.getStringWidth(RESET_CHAR) * 2;

		int doneWidth = Math.max(MARGIN + font.getStringWidth(doneText) + MARGIN, 60); // Make the done button slightly bigger than neccesary so it doesn't look too small
		int undoWidth = MARGIN + font.getStringWidth(undoText) + undoGlyphWidth + MARGIN;
		int resetWidth = MARGIN + font.getStringWidth(resetText) + resetGlyphWidth + MARGIN;
		int applyToSubcategoriesWidth = MARGIN + font.getStringWidth(applyToSubcategoriesText) + MARGIN;
		int buttonsWidthHalf = (MARGIN + doneWidth + MARGIN + undoWidth + MARGIN + resetWidth + MARGIN + applyToSubcategoriesWidth + MARGIN) / 2;

		int xPos = this.width / 2 - buttonsWidthHalf;
		int yPos = this.height - MARGIN - BUTTON_HEIGHT;
		this.addButton(new GuiButtonExt(xPos, yPos, doneWidth, BUTTON_HEIGHT, doneText, this::onDoneButtonClicked));

		xPos += doneWidth + MARGIN;
		this.addButton(undoChangesButton = new GuiUnicodeGlyphButton(xPos, yPos, undoWidth, BUTTON_HEIGHT, undoText, UNDO_CHAR, 2.0F, this::onUndoChangesButtonClicked));

		xPos += undoWidth + MARGIN;
		this.addButton(resetToDefaultButton = new GuiUnicodeGlyphButton(xPos, yPos, resetWidth, BUTTON_HEIGHT, resetText, RESET_CHAR, 2.0F, this::onResetToDefaultButtonClicked));

		xPos += resetWidth + MARGIN;
		// Widgets are re-created each time so make the value persist if it exists.
		boolean shouldApplyToSubcategories = applyToSubcategoriesCheckBox != null && shouldApplyToSubcategories();
		this.addButton(applyToSubcategoriesCheckBox = new CheckboxButton(xPos, yPos, applyToSubcategoriesWidth, BUTTON_HEIGHT, applyToSubcategoriesText, shouldApplyToSubcategories));

		this.undoChangesButtonHoverChecker = new HoverChecker(undoChangesButton, 500);
		this.resetToDefaultButtonHoverChecker = new HoverChecker(resetToDefaultButton, 500);
		this.applyToSubcategoriesCheckBoxHoverChecker = new HoverChecker(applyToSubcategoriesCheckBox, 500);

		if (this.entryList == null || this.needsRefresh) {
			this.entryList = new ConfigEntryListWidget(this);
			this.needsRefresh = false;
		}
		this.children.add(entryList);

		entryList.init();

		this.setButtonsActive();
	}

	/**
	 * Called from the main game loop to update the screen.
	 */
	@Override
	public void tick() {
		super.tick();
		if (entryList != null) // hmmm
			this.entryList.tick();
		setButtonsActive();
	}

	public void setButtonsActive() {
		if (entryList == null)
			return;
		final boolean applyToSubcategories = shouldApplyToSubcategories();
		final boolean areAnyEntriesEnabled = this.entryList.areAnyEntriesEnabled(applyToSubcategories);
		this.undoChangesButton.active = areAnyEntriesEnabled && this.entryList.areAnyEntriesChanged(applyToSubcategories);
		this.resetToDefaultButton.active = areAnyEntriesEnabled && !this.entryList.areAllEntriesDefault(applyToSubcategories);
	}

	public void drawToolTip(List<String> stringList, int x, int y) {
		GuiUtils.drawHoveringText(stringList, x, y, width, height, 300, font);
	}

	public ITextComponent getSubtitle() {
		return subtitle;
	}

	public void setSubtitle(final ITextComponent subtitle) {
		this.subtitle = subtitle;
	}

	public boolean isWorldRunning() {
		return Minecraft.getInstance().world != null;
	}

	public boolean doAllRequireWorldRestart() {
		return false; // TODO
	}

	public boolean doAllRequireMcRestart() {
		return false; // TODO
	}

	public int getHeaderSize() {
		return getSubtitle() != null ? 33 : 23;
	}

	public int getFooterSize() {
		return MARGIN + BUTTON_HEIGHT + MARGIN;
	}

	public List<ConfigListEntry<?>> getConfigElements() {
		return configElements;
	}

}
