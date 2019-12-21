package net.minecraftforge.fml.client.config.entry;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.gui.widget.list.AbstractOptionList;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.client.config.ConfigEntryListWidget;
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
 */
public abstract class ConfigListEntry extends AbstractOptionList.Entry<ConfigListEntry> implements IConfigScreenListEntry {

	/**
	 * The size of the undoChangesButton and the resetToDefaultButton.
	 */
	public static final int BUTTON_SIZE = 20;
	/**
	 * The space between each button.
	 */
	public static final int BUTTON_SPACER = 1;

	protected final ConfigScreen owningScreen;
	protected final ConfigEntryListWidget owningEntryList;
	protected final IConfigValueElement configElement;
	protected final Minecraft minecraft;
	protected final String name;
	protected final List<Widget> children = new ArrayList<>();
	protected final GuiButtonExt undoChangesButton, resetToDefaultButton;
	protected final List<String> toolTip, undoToolTip, defaultToolTip;
	protected HoverChecker tooltipHoverChecker, undoChangesButtonChecker, resetToDefaultButtonHoverChecker;
	protected boolean drawLabel;

	public ConfigListEntry(ConfigScreen owningScreen, ConfigEntryListWidget owningEntryList, IConfigValueElement configElement) {
		this.owningScreen = owningScreen;
		this.owningEntryList = owningEntryList;
		this.configElement = configElement;
		this.minecraft = Minecraft.getInstance();
//		String trans = I18n.format(configElement.getLanguageKey());
//		if (!trans.equals(configElement.getLanguageKey()))
//			this.name = trans;
//		else
		this.name = configElement.getName();
		this.children().add(this.undoChangesButton = new GuiButtonExt(0, 0, 18, 18, UNDO_CHAR, b -> {
			this.undoChanges();
		}));
		this.children().add(this.resetToDefaultButton = new GuiButtonExt(0, 0, 18, 18, RESET_CHAR, b -> {
			this.resetToDefault();
		}));

		this.undoChangesButtonChecker = new HoverChecker(this.undoChangesButton, 500);
		this.resetToDefaultButtonHoverChecker = new HoverChecker(this.resetToDefaultButton, 500);

		this.undoToolTip = Collections.singletonList(I18n.format("fml.configgui.tooltip.undoChanges"));
		this.defaultToolTip = Collections.singletonList(I18n.format("fml.configgui.tooltip.resetToDefault"));
		this.toolTip = new ArrayList<>();

		this.drawLabel = true;

		String comment;

		comment = I18n.format(configElement.getTranslationKey() + ".tooltip").replace("\\n", "\n");

		if (!comment.equals(configElement.getTranslationKey() + ".tooltip"))
			Collections.addAll(toolTip, (TextFormatting.GREEN + name + "\n" + TextFormatting.YELLOW + removeTag(comment, "[default:", "]")).split("\n"));
		else if (configElement.getComment() != null && !configElement.getComment().trim().isEmpty())
			Collections.addAll(toolTip, (TextFormatting.GREEN + name + "\n" + TextFormatting.YELLOW + removeTag(configElement.getComment(), "[default:", "]")).split("\n"));
		else
			Collections.addAll(toolTip, (TextFormatting.GREEN + name + "\n" + TextFormatting.RED + "No tooltip defined.").split("\n"));

//		if ((configElement.getType() == ConfigGuiType.INTEGER
//				&& (Integer.valueOf(configElement.getMinValue().toString()) != Integer.MIN_VALUE || Integer.valueOf(configElement.getMaxValue().toString()) != Integer.MAX_VALUE))
//				|| (configElement.getType() == ConfigGuiType.DOUBLE
//				&& (Double.valueOf(configElement.getMinValue().toString()) != -Double.MAX_VALUE || Double.valueOf(configElement.getMaxValue().toString()) != Double.MAX_VALUE)))
//			Collections.addAll(toolTip, (TextFormatting.AQUA + I18n.format("fml.configgui.tooltip.defaultNumeric", configElement.getMinValue(), configElement.getMaxValue(), configElement.getDefault())).split("\n"));
//		else if (configElement.getType() != ConfigGuiType.CONFIG_CATEGORY)
//			Collections.addAll(toolTip, (TextFormatting.AQUA + I18n.format("fml.configgui.tooltip.default", configElement.getDefault())).split("\n"));

		if (configElement.requiresMcRestart() || owningScreen.doAllRequireMcRestart())
			toolTip.add(TextFormatting.RED + "[" + I18n.format("fml.configgui.gameRestartTitle") + "]");
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
					formatting + this.name,
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
			final int widgetX = startX + this.owningEntryList.getLongestLabelWidth();
			preRenderWidget(widget, widgetX, startY, posX - widgetX, height);
		}

		this.children().forEach(c -> c.render(mouseX, mouseY, partialTicks));
	}

	public abstract boolean isValidValue();

	@Nullable
	public abstract Widget getWidget();

	protected void preRenderWidget(final Widget widget, final int x, final int y, final int width, final int height) {
		// TextFieldWidget render larger than they should be
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

	@Override
	public IConfigValueElement getConfigElement() {
		return configElement;
	}

	@Override
	public String getName() {
		return configElement.getName();
	}

	@Override
	public abstract Object getCurrentValue();

	@Override
	public abstract Object[] getCurrentValues();

	@Override
	public boolean enabled() {
		return !owningScreen.isWorldRunning() || !owningScreen.doAllRequireWorldRestart() && !configElement.requiresWorldRestart();
	}

	@Override
	public abstract void tick();

	@Override
	public abstract boolean isDefault();

	@Override
	public abstract void resetToDefault();

	@Override
	public abstract void undoChanges();

	@Override
	public abstract boolean isChanged();

	@Override
	public abstract boolean save();

	@Override
	public void renderToolTip(final int mouseX, final int mouseY, final float partialTicks) {
		boolean canHover = mouseY < this.owningScreen.getEntryList().getBottom() && mouseY > this.owningScreen.getEntryList().getTop();
		if (toolTip != null && this.tooltipHoverChecker != null)
			if (this.tooltipHoverChecker.checkHover(mouseX, mouseY, canHover))
				this.owningScreen.drawToolTip(toolTip, mouseX, mouseY);

		if (this.undoChangesButtonChecker.checkHover(mouseX, mouseY, canHover))
			this.owningScreen.drawToolTip(undoToolTip, mouseX, mouseY);

		if (this.resetToDefaultButtonHoverChecker.checkHover(mouseX, mouseY, canHover))
			this.owningScreen.drawToolTip(defaultToolTip, mouseX, mouseY);
	}

	@Override
	public int getLabelWidth() {
		if (!drawLabel)
			return 0;
		return this.minecraft.fontRenderer.getStringWidth(this.name);
	}

	@Override
	public int getEntryRightBound() {
		return this.owningEntryList.getRowWidth();
	}

	@Override
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
