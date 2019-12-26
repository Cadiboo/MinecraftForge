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
import net.minecraftforge.fml.client.config.element.IConfigElement;
import net.minecraftforge.fml.client.config.element.ModConfigConfigElement;
import net.minecraftforge.fml.config.ConfigTracker;
import net.minecraftforge.fml.config.ModConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

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
	private final List<IConfigElement<?>> configElements;
	/**
	 * The initial list of elements on this screen. Same as the first value of {@link #configElements}
	 */
	private final List<IConfigElement<?>> initialConfigElements;
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

	public ConfigScreen(final ITextComponent titleIn, final Screen parentScreen, final ModContainer modContainer, @Nullable final List<IConfigElement<?>> configElements) {
		super(titleIn);
		this.parentScreen = parentScreen;
		this.modContainer = modContainer;
		if (configElements == null)
			this.configElements = this.initialConfigElements = makeElementsForMod();
		else
			this.configElements = this.initialConfigElements = configElements;
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

	protected List<IConfigElement<?>> makeElementsForMod() {
		final List<IConfigElement<?>> list = new ArrayList<>();
		for (final ModConfig.Type type : ModConfig.Type.values())
			makeConfigElementForModConfigType(type).ifPresent(list::add);
		return list;
	}

	protected Optional<ModConfigConfigElement> makeConfigElementForModConfigType(final ModConfig.Type type) {
		if (type == ModConfig.Type.SERVER && !canPlayerEditServerConfig())
			return Optional.empty();
		return ConfigTracker.INSTANCE.getConfig(modContainer.getModId(), type)
				.map(ModConfigConfigElement::new);
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
					this.entryList.save();
					boolean requiresGameRestart = this.entryList.anyRequireGameRestart();
					boolean requiresWorldRestart = this.entryList.anyRequireWorldRestart();
					if (requiresGameRestart) {
						canClose = false;
						getMinecraft().displayGuiScreen(new GuiMessageDialog(parentScreen, "fml.configgui.gameRestartTitle", new StringTextComponent(I18n.format("fml.configgui.gameRestartRequired")), "fml.configgui.confirmMessage"));
					}
					if (requiresWorldRestart && Minecraft.getInstance().world != null) {
						canClose = false;
						getMinecraft().displayGuiScreen(new GuiMessageDialog(parentScreen, "fml.configgui.worldRestartTitle", new StringTextComponent(I18n.format("fml.configgui.worldRestartRequired")), "fml.configgui.confirmMessage"));
					}
				}
			}
		} catch (Exception e) {
			LOGGER.error("Error performing ConfigScreen action:", e);
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

		final int halfWidth = this.width / 2;

		this.drawCenteredString(font, this.title.getFormattedText(), halfWidth, 5, 0xFFFFFF);

		if (subtitle != null) {
			final String trimmed = GuiUtils.trimStringToSize(font, subtitle.getFormattedText(), width - 6);
			this.drawCenteredString(font, trimmed, halfWidth, 20, 0x9D9D97);
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
		if (entryList != null) // TODO hmmm
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

	public List<IConfigElement<?>> getConfigElements() {
		return configElements;
	}

}
