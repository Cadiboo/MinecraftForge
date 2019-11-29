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
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.IngameMenuScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ExtensionPoint;
import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.client.gui.GuiModList;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;

import static net.minecraftforge.api.distmarker.Dist.CLIENT;
import static net.minecraftforge.fml.client.config.GuiUtils.RESET_CHAR;
import static net.minecraftforge.fml.client.config.GuiUtils.UNDO_CHAR;
import static net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus.FORGE;

@Mod.EventBusSubscriber(modid = "forge", value = CLIENT, bus = FORGE)
public class ConfigScreen extends Screen {

	private static final Logger LOGGER = LogManager.getLogger();
	public final ModContainer modContainer;
	private final Screen parentScreen;
	private HoverChecker undoHoverChecker, resetHoverChecker;
	private ConfigEntryList configEntryList;
	private ITextComponent subtitle;

	public ConfigScreen(final Minecraft minecraft, final Screen parentScreen, final ModContainer modContainer) {
		super(new TranslationTextComponent("fml.configgui.title", modContainer.getModInfo().getDisplayName()));
		this.minecraft = minecraft;
		this.parentScreen = parentScreen;
		this.modContainer = modContainer;
//		this.subtitle = new StringTextComponent("A Subtitle that is long. So long it needs an ellipsis! Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed mollis lacinia magna. Orci varius natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Sed sagittis luctus odio eu tempus. Interdum et malesuada fames ac ante ipsum primis in faucibus. Pellentesque volutpat ligula eget lacus auctor sagittis. In hac habitasse platea dictumst. Nunc gravida elit vitae sem vehicula efficitur. Donec mattis ipsum et arcu lobortis, eleifend sagittis sem rutrum. Cras pharetra quam eget posuere fermentum. Sed id tincidunt justo. Lorem ipsum dolor sit amet, consectetur adipiscing elit.");
	}

	public ConfigScreen(final Screen parentScreen, final ModContainer modContainer) {
		this(Minecraft.getInstance(), parentScreen, modContainer);
	}

	public static Runnable makeConfigGuiExtensionPoint(final ModContainer modContainer) {
		if (modContainer.getCustomExtension(ExtensionPoint.CONFIGGUIFACTORY).isPresent())
			return Runnables.doNothing();

		return () -> modContainer.registerExtensionPoint(ExtensionPoint.CONFIGGUIFACTORY,
				() -> (minecraft, screen) -> new ConfigScreen(minecraft, screen, modContainer));
	}

	@SubscribeEvent
	public static void onInitGuiEvent(final GuiScreenEvent.InitGuiEvent event) {
		final Screen gui = event.getGui();
		if (gui instanceof IngameMenuScreen) {
			int maxY = 0;
			for (final Widget button : event.getWidgetList()) {
				maxY = Math.max(button.y, maxY);
			}
			event.addWidget(new IngameModListButton(gui, maxY + 24));
		}
	}

	public ITextComponent getSubtitle() {
		return subtitle;
	}

	public void setSubtitle(final ITextComponent subtitle) {
		this.subtitle = subtitle;
	}

	private void onDoneClick(final Button button) {
		boolean canClose = true;
		try {
			this.configEntryList.unfocus();
			boolean requiresMcRestart = this.configEntryList.anyRequireMcRestart();
			boolean requiresWorldRestart = this.configEntryList.anyRequireWorldRestart();

			if (requiresMcRestart) {
				canClose = false;
				getMinecraft().displayGuiScreen(new GuiMessageDialog(parentScreen, "fml.configgui.gameRestartTitle", new StringTextComponent(I18n.format("fml.configgui.gameRestartRequired")), "fml.configgui.confirmMessage"));
			}
			if (requiresWorldRestart) {
				if (Minecraft.getInstance().world != null) {
					canClose = false;
					getMinecraft().displayGuiScreen(new GuiMessageDialog(parentScreen, "fml.configgui.worldRestartTitle", new StringTextComponent(I18n.format("fml.configgui.worldRestartRequired")), "fml.configgui.confirmMessage"));
				}
			}
		} catch (Throwable e) {
			LOGGER.error("Error performing GuiConfig action:", e);
		}

		if (canClose)
			this.onClose();
	}

	private void onResetClick(final Button button) {
		this.configEntryList.resetAllToDefault();
	}

	private void onUndoClick(final Button button) {
		this.configEntryList.undoAllChanges();
	}

	/**
	 * Draws the screen and all the components in it.
	 */
	@Override
	public void render(final int mouseX, final int mouseY, final float partialTicks) {
		this.renderDirtBackground(0);
		this.configEntryList.render(mouseX, mouseY, partialTicks);
		// hide overflow from configEntryList. TODO: use GLScissors?
		{
			GlStateManager.pushMatrix();
			GlStateManager.translated(0, this.configEntryList.getBottom(), 0);
			this.renderDirtBackground(0);
			GlStateManager.popMatrix();
		}

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
//		this.configEntryList.postRender(mouseX, mouseY, partialTicks);
		if (this.undoHoverChecker.checkHover(mouseX, mouseY))
			this.drawToolTip(Arrays.asList(I18n.format("fml.configgui.tooltip.undoAllChanges").split("\n")), mouseX, mouseY);
		if (this.resetHoverChecker.checkHover(mouseX, mouseY))
			this.drawToolTip(Arrays.asList(I18n.format("fml.configgui.tooltip.resetAllToDefault").split("\n")), mouseX, mouseY);
	}

	@Override
	public void onClose() {
		getMinecraft().displayGuiScreen(parentScreen);
	}

	/**
	 * Called to add all the buttons & stuff
	 * Called when the screen is opened AND every time the screen resizes
	 */
	@Override
	protected void init() {
		super.init();

		String doneText = I18n.format("gui.done");
		String undoText = I18n.format("fml.configgui.undoAllChanges");
		String resetText = I18n.format("fml.configgui.resetAllToDefault");

		final int buttonHeight = 20;
		final int padding = 5;

		int undoGlyphWidth = font.getStringWidth(UNDO_CHAR) * 2;
		int resetGlyphWidth = font.getStringWidth(RESET_CHAR) * 2;

		int doneWidth = Math.max(padding + font.getStringWidth(doneText) + padding, 100);
		int undoWidth = padding + font.getStringWidth(undoText) + undoGlyphWidth + padding;
		int resetWidth = padding + font.getStringWidth(resetText) + resetGlyphWidth + padding;
		int buttonWidthHalf = (padding + doneWidth + padding + undoWidth + padding + resetWidth + padding) / 2;

		int xPos = this.width / 2 - buttonWidthHalf;
		int yPos = this.height - 29;
		this.addButton(new GuiButtonExt(xPos, yPos, doneWidth, buttonHeight, doneText, this::onDoneClick));

		xPos += doneWidth + padding;
		final Button undoButton = new GuiUnicodeGlyphButton(xPos, yPos, undoWidth, buttonHeight, undoText, UNDO_CHAR, 2.0F, this::onUndoClick);
		this.addButton(undoButton);

		xPos += undoWidth + padding;
		final Button resetButton = new GuiUnicodeGlyphButton(xPos, yPos, resetWidth, buttonHeight, resetText, RESET_CHAR, 2.0F, this::onResetClick);
		this.addButton(resetButton);

		this.undoHoverChecker = new HoverChecker(undoButton, 500);
		this.resetHoverChecker = new HoverChecker(resetButton, 500);

		if (this.configEntryList == null)
			this.configEntryList = new ConfigEntryList(this, getMinecraft());
		this.children.add(configEntryList);

		configEntryList.init();
	}

	/**
	 * Called from the main game loop to update the screen.
	 */
	@Override
	public void tick() {
		super.tick();
		this.configEntryList.tick();
	}

	public void drawToolTip(List<String> stringList, int x, int y) {
		GuiUtils.drawHoveringText(stringList, x, y, width, height, 300, font);
	}

	@Nullable
	public ConfigEntryList getConfigEntryList() {
		return configEntryList;
	}

	private static class IngameModListButton extends GuiButtonExt {

		public IngameModListButton(final Screen gui, final int y) {
			super(gui.width / 2 - 102, y, 204, 20, I18n.format("fml.menu.mods"), button -> Minecraft.getInstance().displayGuiScreen(new GuiModList(gui)));
		}

	}

}
