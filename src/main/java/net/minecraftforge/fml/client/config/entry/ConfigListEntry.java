package net.minecraftforge.fml.client.config.entry;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.gui.widget.list.AbstractOptionList;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.ForgeConfigSpec.Range;
import net.minecraftforge.common.ForgeConfigSpec.ValueSpec;
import net.minecraftforge.fml.client.config.ConfigScreen;
import net.minecraftforge.fml.client.config.GuiButtonExt;
import net.minecraftforge.fml.client.config.HoverChecker;

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
public abstract class ConfigListEntry<T> extends AbstractOptionList.Entry<ConfigListEntry<?>> {

	/**
	 * The space between each button.
	 */
	public static final int BUTTON_SPACER = 1;
	protected final ConfigScreen owningScreen;
	protected final Minecraft minecraft;
	protected final List<Widget> children = new ArrayList<>();
	protected final GuiButtonExt undoChangesButton, resetToDefaultButton;
	protected final List<String> undoToolTip, defaultToolTip;
	protected List<String> toolTip;
	protected HoverChecker tooltipHoverChecker, undoChangesButtonChecker, resetToDefaultButtonHoverChecker;
	protected boolean drawLabel;

	public ConfigListEntry(ConfigScreen owningScreen) {
		this.owningScreen = owningScreen;
		this.minecraft = Minecraft.getInstance();
		this.children().add(this.undoChangesButton = new GuiButtonExt(0, 0, 0, 0, UNDO_CHAR, b -> {
			this.undoChanges();
		}));
		this.children().add(this.resetToDefaultButton = new GuiButtonExt(0, 0, 0, 0, RESET_CHAR, b -> {
			this.resetToDefault();
		}));

		this.undoChangesButtonChecker = new HoverChecker(this.undoChangesButton, 500);
		this.resetToDefaultButtonHoverChecker = new HoverChecker(this.resetToDefaultButton, 500);

		this.undoToolTip = Collections.singletonList(I18n.format("fml.configgui.tooltip.undoChanges"));
		this.defaultToolTip = Collections.singletonList(I18n.format("fml.configgui.tooltip.resetToDefault"));

		this.drawLabel = true;
	}

	protected void makeTooltip(final ConfigScreen owningScreen) {
		this.toolTip = new ArrayList<>();
		final ConfigElementContainer<T> configValue = getBooleanConfigElement();
		if (configValue != null) {
			final String translateKey = configValue.getTranslationKey() + ".tooltip";
			final String comment = I18n.format(translateKey).replace("\\n", "\n");
			if (!comment.equals(translateKey))
				Collections.addAll(toolTip, (TextFormatting.GREEN + getLabel() + "\n" + TextFormatting.YELLOW + removeTag(comment, "[default:", "]")).split("\n"));
			else if (configValue.getComment() != null && !configValue.getComment().trim().isEmpty())
				Collections.addAll(toolTip, (TextFormatting.GREEN + getLabel() + "\n" + TextFormatting.YELLOW + removeTag(configValue.getComment(), "[default:", "]")).split("\n"));
			else
				Collections.addAll(toolTip, (TextFormatting.GREEN + getLabel() + "\n" + TextFormatting.RED + "No tooltip defined.").split("\n"));

			final ValueSpec valueSpec = configValue.getValueSpec();
			if (Number.class.isAssignableFrom(valueSpec.getClazz())) {
				final Range<?> range = valueSpec.getRange();
				Collections.addAll(toolTip, (TextFormatting.AQUA + I18n.format("fml.configgui.tooltip.defaultNumeric", range.getMin(), range.getMax(), configValue.getDefaultValue())).split("\n"));
			} else if (!this.isCategory())
				Collections.addAll(toolTip, (TextFormatting.AQUA + I18n.format("fml.configgui.tooltip.default", configValue.getDefaultValue())).split("\n"));

			if (configValue.requiresGameRestart() || owningScreen.doAllRequireMcRestart())
				toolTip.add(TextFormatting.RED + "[" + I18n.format("fml.configgui.gameRestartTitle") + "]");
		}
	}

	public boolean isCategory() {
		return false;
	}

	@Override
	public void render(final int index, final int startY, final int startX, final int width, final int height, final int mouseX, final int mouseY, final boolean isHovered, final float partialTicks) {
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);

		final boolean isChanged = isChanged();

		if (drawLabel) {
			String formatting = "" + TextFormatting.GRAY;
			if (isChanged)
				formatting += "" + TextFormatting.WHITE + TextFormatting.ITALIC;
			if (!isValidValue())
				formatting += TextFormatting.RED;
			this.minecraft.fontRenderer.drawString(
					formatting + getLabel(),
					startX,
					startY + height / 2F - this.minecraft.fontRenderer.FONT_HEIGHT / 2F,
					0xFFFFFF);
		}

		final int buttonSize = height;

		// Changing x coordinate.
		// After use it is the largest x coordinate before the buttons.
		// After use it is comparable to "startX + width - buttonsWidth"
		int posX = startX + width;

		posX -= BUTTON_SPACER + buttonSize;
		preRenderWidget(undoChangesButton, posX, startY, buttonSize, buttonSize);
		undoChangesButton.active &= isChanged;

		posX -= buttonSize + BUTTON_SPACER;
		preRenderWidget(resetToDefaultButton, posX, startY, buttonSize, buttonSize);
		resetToDefaultButton.active &= !isDefault();

		posX -= BUTTON_SPACER; // Add a tiny bit of space between the entry and the undo/reset buttons
		if (tooltipHoverChecker == null)
			tooltipHoverChecker = new HoverChecker(startY, startY + height, startX, posX, 500);
		else
			tooltipHoverChecker.updateBounds(startY, startY + height, startX, posX);

		final Widget widget = this.getWidget();
		if (widget != null) {
			int widgetX = startX;
			if (drawLabel)
				widgetX += this.owningScreen.getEntryList().getLongestLabelWidth();
			preRenderWidget(widget, widgetX, startY, posX - widgetX, height);
		}
		this.children().forEach(c -> c.render(mouseX, mouseY, partialTicks));
	}

	public abstract boolean isValidValue();

	@Nullable
	public abstract Widget getWidget();

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
		return !owningScreen.isWorldRunning() || !owningScreen.doAllRequireWorldRestart() && !this.requiresWorldRestart();
	}

	public void tick() {
		final Widget widget = getWidget();
		if (widget instanceof TextFieldWidget)
			((TextFieldWidget) widget).tick();
	}

	public String getLabel() {
		final ConfigElementContainer<T> configValue = getBooleanConfigElement();
		if (configValue != null)
			return configValue.getLabel();
		return "Unknown Label!";
	}

	public boolean isDefault() {
		final ConfigElementContainer<T> configValue = getBooleanConfigElement();
		if (configValue != null)
			return configValue.isDefault();
		return true;
	}

	protected abstract ConfigElementContainer<T> getBooleanConfigElement();

	public void resetToDefault() {
		final ConfigElementContainer<T> configValue = getBooleanConfigElement();
		if (configValue != null)
			configValue.resetToDefault();
	}

	public boolean isChanged() {
		final ConfigElementContainer<T> configValue = getBooleanConfigElement();
		if (configValue != null)
			return configValue.isChanged();
		return false;
	}

	public void undoChanges() {
		final ConfigElementContainer<T> configValue = getBooleanConfigElement();
		if (configValue != null)
			configValue.undoChanges();
	}

	public boolean save() {
		final ConfigElementContainer<T> configValue = getBooleanConfigElement();
		if (configValue != null)
			return configValue.save();
		return false;
	}

	public boolean requiresWorldRestart() {
		final ConfigElementContainer<T> configValue = getBooleanConfigElement();
		if (configValue != null)
			return configValue.requiresWorldRestart();
		return false;
	}

	public boolean requiresMcRestart() {
		final ConfigElementContainer<T> configValue = getBooleanConfigElement();
		if (configValue != null)
			return configValue.requiresGameRestart();
		return false;
	}

	public void renderToolTip(final int mouseX, final int mouseY, final float partialTicks) {
		boolean canHover = mouseY < this.owningScreen.getEntryList().getBottom() && mouseY > this.owningScreen.getEntryList().getTop();
		final List<String> toolTip = getToolTip();
		if (!toolTip.isEmpty() && this.tooltipHoverChecker != null)
			if (this.tooltipHoverChecker.checkHover(mouseX, mouseY, canHover))
				this.owningScreen.drawToolTip(toolTip, mouseX, mouseY);

		if (this.undoChangesButtonChecker.checkHover(mouseX, mouseY, canHover))
			this.owningScreen.drawToolTip(undoToolTip, mouseX, mouseY);

		if (this.resetToDefaultButtonHoverChecker.checkHover(mouseX, mouseY, canHover))
			this.owningScreen.drawToolTip(defaultToolTip, mouseX, mouseY);
	}

	@Nonnull
	public List<String> getToolTip() {
		if (toolTip == null)
			makeTooltip(owningScreen);
		return toolTip;
	}

	public int getLabelWidth() {
		if (!drawLabel)
			return 0;
		return this.minecraft.fontRenderer.getStringWidth(this.getLabel());
	}

	public void onGuiClosed() {
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
