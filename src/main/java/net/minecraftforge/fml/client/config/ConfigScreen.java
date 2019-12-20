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

import com.google.common.util.concurrent.Runnables;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.gui.widget.button.CheckboxButton;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.fml.ExtensionPoint;
import net.minecraftforge.fml.ModContainer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;

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
	/**
	 * The longest size a label of a config element can be before it is trimmed with an ellipsis.
	 * If it is trimmed, the new text (including the ellipsis) will always be equal to this value.
	 */
	public static final int MAX_LABEL_WIDTH = 200;

	private static final Logger LOGGER = LogManager.getLogger();

	/**
	 * A reference to the screen object that created this. Used for navigating between screens.
	 */
	public final Screen parentScreen;
	/**
	 * The mod that this config is for.
	 */
	public final ModContainer modContainer;
	/**
	 * The size of the largest label of all the config elements on this screen.
	 * Always smaller than or equal to {@link #MAX_LABEL_WIDTH};
	 * Used for
	 */
	private final int longestLabelWidth;
	/**
	 * If true then the entryList will be re-created next time {@link #init()} is called.
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
	private ConfigEntryListWidget entryList;

	public ConfigScreen(final ITextComponent titleIn, final Screen parentScreen, final ModContainer modContainer) {
		super(titleIn);
		this.parentScreen = parentScreen;
		this.modContainer = modContainer;
		this.longestLabelWidth = 100; //TODO
	}

	public ConfigScreen(final ITextComponent title, final Screen parentScreen, final ModContainer modContainer, final Minecraft minecraft) {
		this(title, parentScreen, modContainer);
		this.minecraft = minecraft;
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
						new ConfigScreen(new StringTextComponent(modContainer.getModInfo().getDisplayName()), screen, modContainer, minecraft));
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

	@Nullable
	public ConfigEntryListWidget getEntryList() {
		return entryList;
	}

	private void onDoneButtonClicked(final Button button) {
		boolean canClose = true;
		try {
//			if ((configID != null || this.parentScreen == null || !(this.parentScreen instanceof ConfigScreen))
//					&& (this.entryList.hasChangedEntry(true))) {
//				boolean requiresMcRestart = this.entryList.saveConfigElements();
//
//				if (Loader.isModLoaded(modID)) {
//					ConfigChangedEvent event = new OnConfigChangedEvent(modID, configID, isWorldRunning, requiresMcRestart);
//					MinecraftForge.EVENT_BUS.post(event);
//					if (!event.getResult().equals(Result.DENY))
//						MinecraftForge.EVENT_BUS.post(new PostConfigChangedEvent(modID, configID, isWorldRunning, requiresMcRestart));
//
//					if (requiresMcRestart) {
//						flag = false;
//						minecraft.displayScreen(new GuiMessageDialog(parentScreen, "fml.configgui.gameRestartTitle",
//								new TextComponentString(I18n.format("fml.configgui.gameRestartRequired")), "fml.configgui.confirmRestartMessage"));
//					}
//
//					if (this.parentScreen instanceof ConfigScreen)
//						((ConfigScreen) this.parentScreen).needsRefresh = true;
//				}
//			}

//

//			this.entryList.unfocus();
//			boolean requiresMcRestart = this.entryList.anyRequireMcRestart();
//			boolean requiresWorldRestart = this.entryList.anyRequireWorldRestart();
//
//			if (requiresMcRestart) {
//				canClose = false;
//				getMinecraft().displayGuiScreen(new GuiMessageDialog(parentScreen, "fml.configgui.gameRestartTitle", new StringTextComponent(I18n.format("fml.configgui.gameRestartRequired")), "fml.configgui.confirmMessage"));
//			}
//			if (requiresWorldRestart && Minecraft.getInstance().world != null) {
//				canClose = false;
//				getMinecraft().displayGuiScreen(new GuiMessageDialog(parentScreen, "fml.configgui.worldRestartTitle", new StringTextComponent(I18n.format("fml.configgui.worldRestartRequired")), "fml.configgui.confirmMessage"));
//			}
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

		// GLScissors to hide overflow from entryList
		{
			GL11.glEnable(GL11.GL_SCISSOR_TEST);
			double scale = getMinecraft().mainWindow.getGuiScaleFactor();
			int fbHeight = getMinecraft().mainWindow.getFramebufferHeight();
			// Scissors coords are relative to the bottom left of the screen
			int scissorsX = (int) (entryList.getLeft() * scale);
			int scissorsY = (int) (fbHeight - (entryList.getBottom() * scale));
			int scissorsWidth = (int) (entryList.getWidth() * scale);
			int scissorsHeight = (int) (entryList.getHeight() * scale);
			GL11.glScissor(scissorsX, scissorsY, scissorsWidth, scissorsHeight);
		}
		this.entryList.render(mouseX, mouseY, partialTicks);
//		this.minecraft.getTextureManager().bindTexture(AtlasTexture.LOCATION_BLOCKS_TEXTURE);
//		innerBlit(0, 10000, 0, 10000, this.blitOffset, 0, 100, 0, 100);
		GL11.glDisable(GL11.GL_SCISSOR_TEST);

		final int halfWidth = this.width / 2;

		this.drawCenteredString(font, this.title.getFormattedText(), halfWidth, 8, 0xFFFFFF);

		if (subtitle != null) {
			String title2 = subtitle.getFormattedText();
			int strWidth = font.getStringWidth(title2);
			int ellipsisWidth = font.getStringWidth("...");
			if (strWidth > width - 6 && strWidth > ellipsisWidth)
				title2 = font.trimStringToWidth(title2, width - 6 - ellipsisWidth).trim() + "...";
			this.drawCenteredString(font, title2, halfWidth, 25, 0xFFFFFF);
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
//			ConfigScreen parentGuiConfig = (ConfigScreen) this.parentScreen;
//			parentGuiConfig.needsRefresh = true;
//			parentGuiConfig.init();
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
		this.addButton(applyToSubcategoriesCheckBox = new CheckboxButton(xPos, yPos, applyToSubcategoriesWidth, BUTTON_HEIGHT, applyToSubcategoriesText, false));

		this.undoChangesButtonHoverChecker = new HoverChecker(undoChangesButton, 500);
		this.resetToDefaultButtonHoverChecker = new HoverChecker(resetToDefaultButton, 500);
		this.applyToSubcategoriesCheckBoxHoverChecker = new HoverChecker(applyToSubcategoriesCheckBox, 500);

		if (this.entryList == null || this.needsRefresh)
			this.entryList = new ConfigEntryListWidget(getMinecraft(), this);
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
		this.entryList.tick();
		setButtonsActive();
	}

	public void setButtonsActive() {
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

}
