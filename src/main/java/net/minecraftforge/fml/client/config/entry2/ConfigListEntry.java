package net.minecraftforge.fml.client.config.entry2;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.gui.widget.list.AbstractOptionList;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.client.config.ConfigEntryListWidget;
import net.minecraftforge.fml.client.config.ConfigScreen;
import net.minecraftforge.fml.client.config.GuiButtonExt;
import net.minecraftforge.fml.client.config.GuiUtils;
import net.minecraftforge.fml.client.config.HoverChecker;
import net.minecraftforge.fml.client.config.entry.IConfigListEntry2;
import net.minecraftforge.fml.client.config.entry2.widget.ConfigListEntryWidget;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static net.minecraftforge.fml.client.config.GuiUtils.RESET_CHAR;
import static net.minecraftforge.fml.client.config.GuiUtils.UNDO_CHAR;

/**
 * Provides a base entry for others to extend.
 * Handles drawing the prop label (if drawLabel == true) and the Undo/Default buttons.
 *
 * @param <T> The type of the config object. For example Boolean or Float
 */
public class ConfigListEntry<T> extends AbstractOptionList.Entry<ConfigListEntry<?>> implements IConfigListEntry2<T> {

	/**
	 * The space between each button.
	 */
	public static final int BUTTON_SPACER = 1;
	protected final ConfigScreen owningScreen;
	protected final Minecraft minecraft;
	protected final List<Widget> children = new ArrayList<>();
	protected final GuiButtonExt undoChangesButton, resetToDefaultButton;
	protected final List<String> undoToolTip, defaultToolTip;
	protected final HoverChecker undoChangesButtonHoverChecker, resetToDefaultButtonHoverChecker;
	private final ConfigListEntryWidget<T> widget;
	protected List<String> toolTip;
	protected HoverChecker tooltipHoverChecker;
	protected boolean renderLabel;

	public <W extends Widget & ConfigListEntryWidget<T>> ConfigListEntry(@Nonnull final ConfigScreen owningScreen, @Nonnull final W widget) {
		this.owningScreen = owningScreen;
		this.widget = widget;
		this.minecraft = Minecraft.getInstance();
		this.children().add(widget);
		this.children().add(this.undoChangesButton = new GuiButtonExt(0, 0, 0, 0, UNDO_CHAR, b -> this.undoChanges()));
		this.children().add(this.resetToDefaultButton = new GuiButtonExt(0, 0, 0, 0, RESET_CHAR, b -> this.resetToDefault()));

		this.undoChangesButtonHoverChecker = new HoverChecker(this.undoChangesButton, 500);
		this.resetToDefaultButtonHoverChecker = new HoverChecker(this.resetToDefaultButton, 500);

		this.undoToolTip = Collections.singletonList(I18n.format("fml.configgui.tooltip.undoChanges"));
		this.defaultToolTip = Collections.singletonList(I18n.format("fml.configgui.tooltip.resetToDefault"));

		this.renderLabel = true;
	}

	@Override
	public void tick() {
		final Widget widget = getWidget();
		if (widget instanceof TextFieldWidget)
			((TextFieldWidget) widget).tick();
	}

	@Override
	public void renderToolTip(final int mouseX, final int mouseY, final float partialTicks) {
		// TODO: this is only called for hovered elements anyway?
		boolean canHover = mouseY < this.owningScreen.getEntryList().getBottom() && mouseY > this.owningScreen.getEntryList().getTop();
		final List<String> toolTip = getToolTip();
		if (!toolTip.isEmpty() && this.tooltipHoverChecker != null)
			if (this.tooltipHoverChecker.checkHover(mouseX, mouseY, canHover))
				this.owningScreen.drawToolTip(toolTip, mouseX, mouseY);

		if (this.undoChangesButtonHoverChecker.checkHover(mouseX, mouseY, canHover))
			this.owningScreen.drawToolTip(undoToolTip, mouseX, mouseY);

		if (this.resetToDefaultButtonHoverChecker.checkHover(mouseX, mouseY, canHover))
			this.owningScreen.drawToolTip(defaultToolTip, mouseX, mouseY);
	}

	@Nonnull
	public final <W extends Widget & ConfigListEntryWidget<T>> W getWidget() {
		return (W) widget;
	}

	/**
	 * If we have a translation key use it. If we don't try to use the comment. If we don't have a comment use "No tooltip defined"
	 *
	 * @param owningScreen
	 */
	protected void makeTooltip(final ConfigScreen owningScreen) {
		this.toolTip = new ArrayList<>();

		final String label = getLabel();
		if (label == null)
			return; //

		toolTip.add(TextFormatting.GREEN + label);

		// Try for translation, with comment as backup

		boolean commentDone = false;

		@Nullable final String translationKey = getTranslationKey();
		if (translationKey != null) {
			final String tooltipTranslationKey = translationKey + ".tooltip";
			final String translated = I18n.format(tooltipTranslationKey);
			if (!translated.equals(tooltipTranslationKey)) {
				for (final String s : translated.replace("\\n", "\n").split("\n"))
					toolTip.add(TextFormatting.YELLOW + s);
				commentDone = true;
			}
		}
		if (!commentDone) {
			final String comment = getComment();
			if (comment != null)
				for (final String s : comment.split("\n"))
					toolTip.add(TextFormatting.YELLOW + s);
			else
				for (final String s : I18n.format("fml.configgui.noTooltip").replace("\\n", "\n").split("\n"))
					toolTip.add(TextFormatting.RED + s);
		}

		final T defaultValue = this.getDefault();
		final ForgeConfigSpec.Range<?> range = this.getRange();
		if (range != null) {
			for (final String s : I18n.format("fml.configgui.tooltip.rangeWithDefault", range.getMin(), range.getMax(), defaultValue).replace("\\n", "\n").split("\n"))
				toolTip.add(TextFormatting.AQUA + s);
		} else if (!this.displayDefaultValue())
			for (final String s : I18n.format("fml.configgui.tooltip.default", defaultValue).replace("\\n", "\n").split("\n"))
				toolTip.add(TextFormatting.AQUA + s);

		if (requiresGameRestart())
			toolTip.add(TextFormatting.RED + "[" + I18n.format("fml.configgui.gameRestartTitle") + "]");
		if (requiresWorldRestart())
			toolTip.add(TextFormatting.RED + "[" + I18n.format("fml.configgui.worldRestartTitle") + "]");
	}

	@Nullable
	public ForgeConfigSpec.Range<?> getRange() {
		return null;
	}

	@Override
	public void render(final int index, final int startY, final int startX, final int width, final int height, final int mouseX, final int mouseY, final boolean isHovered, final float partialTicks) {
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);

		if (renderLabel) {
			final String label = getLabel();
			if (label != null) {
				final boolean isValidValue = getWidget().isValid();
				String formatting = "" + TextFormatting.GRAY;
				if (isChanged())
					formatting += "" + TextFormatting.WHITE + TextFormatting.ITALIC;
				if (isValidValue)
					formatting += TextFormatting.RED;
				final FontRenderer font = this.minecraft.fontRenderer;
				final String trimmedLabel = GuiUtils.trimStringToSize(font, label, ConfigEntryListWidget.MAX_LABEL_WIDTH);
				font.drawString(
						formatting + trimmedLabel,
						startX,
						startY + height / 2F - font.FONT_HEIGHT / 2F,
						0xFFFFFF
				);
			}
		}

		int buttonsStartPosX = preRenderWidgets(startY, startX, width, height, height);

		buttonsStartPosX -= 2; // Add a tiny bit of space between the entry and the buttons

		if (tooltipHoverChecker == null)
			tooltipHoverChecker = new HoverChecker(startY, startY + height, startX, buttonsStartPosX, 500);
		else
			tooltipHoverChecker.updateBounds(startY, startY + height, startX, buttonsStartPosX);

		final Widget widget = this.getWidget();
		int widgetX = startX;
		if (renderLabel)
			widgetX += this.owningScreen.getEntryList().getLongestLabelWidth();
		preRenderWidget(widget, widgetX, startY, buttonsStartPosX - widgetX, height);
		this.children().forEach(c -> c.render(mouseX, mouseY, partialTicks));
	}

	private int preRenderWidgets(final int startY, final int startX, final int width, final int height, final int buttonSize) {
		// Changing x coordinate.
		// After use it is the largest x coordinate before the buttons.
		// After use it is comparable to "startX + width - buttonsWidth"
		int posX = startX + width;

		posX -= BUTTON_SPACER + buttonSize;
		preRenderWidget(undoChangesButton, posX, startY, buttonSize, buttonSize);
//		undoChangesButton.active &= isChanged();

		posX -= buttonSize + BUTTON_SPACER;
		preRenderWidget(resetToDefaultButton, posX, startY, buttonSize, buttonSize);
//		resetToDefaultButton.active &= !isDefault();

		return posX;
	}

	protected void preRenderWidget(final Widget widget, final int x, final int y, final int width, final int height) {
		// TextFieldWidget render larger than they should be (reeeee)
		if (widget instanceof TextFieldWidget) {
			widget.x = x + 2;
			widget.y = y + 2;
			widget.setWidth(width - 4);
			widget.setHeight(height - 4);
		} else {
			widget.x = x;
			widget.y = y;
			widget.setWidth(width);
			widget.setHeight(height);
		}
		widget.active = enabled();
	}

	public boolean enabled() {
		return true;
		// TODO ????
//		return !owningScreen.isWorldRunning() || !owningScreen.doAllRequireWorldRestart() && !this.requiresWorldRestart();
	}

	@Nonnull
	public List<String> getToolTip() {
		if (toolTip == null)
			makeTooltip(owningScreen);
		return toolTip;
	}

	public int getLabelWidth() {
//		if (!renderLabel)
		return 0;
//		return this.minecraft.fontRenderer.getStringWidth(this.getLabel());
	}

	/**
	 * Get string surrounding tagged area.
	 */
	private String removeTag(String target, String tagStart, String tagEnd) {
		int tagStartPosition = target.indexOf(tagStart);
		int tagEndPosition = target.indexOf(tagEnd, tagStartPosition + tagStart.length());

		if (-1 == tagStartPosition || -1 == tagEndPosition)
			return target;

		String taglessResult = target.substring(0, tagStartPosition);
		taglessResult += target.substring(tagEndPosition + 1);

		return taglessResult;
	}

	@Nonnull
	@Override
	public List<Widget> children() {
		return children;
	}

}
