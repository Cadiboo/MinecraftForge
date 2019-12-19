package net.minecraftforge.fml.client.config.entry;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.gui.widget.list.AbstractList;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.client.config.ConfigEntryListWidget;
import net.minecraftforge.fml.client.config.ConfigScreen;
import net.minecraftforge.fml.client.config.GuiButtonExt;
import net.minecraftforge.fml.client.config.HoverChecker;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static net.minecraftforge.fml.client.config.GuiUtils.RESET_CHAR;
import static net.minecraftforge.fml.client.config.GuiUtils.UNDO_CHAR;

/**
 * Provides a base entry for others to extend.
 * Handles drawing the prop label (if drawLabel == true) and the Undo/Default buttons.
 */
public abstract class ConfigListEntry extends AbstractList.AbstractListEntry<ConfigListEntry> implements IConfigScreenListEntry {

	/**
	 * The size of the undoChangesButton and the resetToDefaultButton.
	 */
	public static final int BUTTON_SIZE = 20;
	/**
	 * The space around each button.
	 */
	public static final int BUTTON_MARGIN = 1;

	protected final ConfigScreen owningScreen;
	protected final ConfigEntryListWidget owningEntryList;
	protected final IConfigValueElement configElement;
	protected final Minecraft minecraft;
	protected final String name;
	protected final List<Widget> children = new ArrayList<>();
	protected final GuiButtonExt undoChangesButton;
	protected final GuiButtonExt resetToDefaultButton;
	protected final List<String> toolTip;
	protected final List<String> undoToolTip;
	protected final List<String> defaultToolTip;
	protected boolean isValidValue = true;
	protected HoverChecker tooltipHoverChecker;
	protected HoverChecker undoHoverChecker;
	protected HoverChecker defaultHoverChecker;
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
		}));
		this.children().add(this.resetToDefaultButton = new GuiButtonExt(0, 0, 18, 18, RESET_CHAR, b -> {
		}));


		this.undoHoverChecker = new HoverChecker(this.undoChangesButton, 800);
		this.defaultHoverChecker = new HoverChecker(this.resetToDefaultButton, 800);
		this.undoToolTip = Collections.singletonList(I18n.format("fml.configgui.tooltip.undoChanges"));
		this.defaultToolTip = Collections.singletonList(I18n.format("fml.configgui.tooltip.resetToDefault"));
		this.toolTip = new ArrayList<>();

		this.drawLabel = true;

//		String comment;
//
//		comment = I18n.format(configElement.getLanguageKey() + ".tooltip").replace("\\n", "\n");
//
//		if (!comment.equals(configElement.getLanguageKey() + ".tooltip"))
//			Collections.addAll(toolTip, (TextFormatting.GREEN + name + "\n" + TextFormatting.YELLOW + removeTag(comment, "[default:", "]")).split("\n"));
//		else if (configElement.getComment() != null && !configElement.getComment().trim().isEmpty())
//			Collections.addAll(toolTip, (TextFormatting.GREEN + name + "\n" + TextFormatting.YELLOW + removeTag(configElement.getComment(), "[default:", "]")).split("\n"));
//		else
//			Collections.addAll(toolTip, (TextFormatting.GREEN + name + "\n" + TextFormatting.RED + "No tooltip defined.").split("\n"));

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
		ConfigEntryListWidget.drawCuboid(startX, startY, startX + width, startY + height, 0, 1, 1, 1);
		boolean isChanged = isChanged();

		if (drawLabel) {
			String label = (!isValidValue ? TextFormatting.RED.toString() :
					(isChanged ? TextFormatting.WHITE.toString() : TextFormatting.GRAY.toString()))
					+ (isChanged ? TextFormatting.ITALIC.toString() : "") + this.name;
			this.minecraft.fontRenderer.drawString(
					label,
//					this.owningScreen.getEntryList().labelX,
					this.owningScreen.getEntryList().getLeft(),
					startY + height / 2F - this.minecraft.fontRenderer.FONT_HEIGHT / 2F,
					0xFFFFFF);
		}

		this.undoChangesButton.x = this.owningEntryList.getRowWidth() - BUTTON_MARGIN - BUTTON_SIZE - BUTTON_MARGIN - BUTTON_MARGIN - BUTTON_SIZE - BUTTON_MARGIN;
		this.undoChangesButton.y = startY;
		this.undoChangesButton.active = enabled() && isChanged;
		this.undoChangesButton.renderButton(mouseX, mouseY, partialTicks);

		this.resetToDefaultButton.x = this.owningEntryList.getRowWidth() - BUTTON_MARGIN - BUTTON_SIZE - BUTTON_MARGIN;
		this.resetToDefaultButton.y = startY;
		this.resetToDefaultButton.active = enabled() && !isDefault();
		this.resetToDefaultButton.renderButton(mouseX, mouseY, partialTicks);

		if (this.tooltipHoverChecker == null)
			this.tooltipHoverChecker = new HoverChecker(startY, startY + height, startX, this.owningScreen.getEntryList().getRight() - 8, 800);
		else
			this.tooltipHoverChecker.updateBounds(startY, startY + height, startX, this.owningScreen.getEntryList().getRight() - 8);
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
	public abstract void setToDefault();

	@Override
	public abstract void undoChanges();

	@Override
	public abstract boolean isChanged();

	@Override
	public abstract boolean save();

	@Override
	public void renderToolTip(int mouseX, int mouseY) {
		boolean canHover = mouseY < this.owningScreen.getEntryList().getBottom() && mouseY > this.owningScreen.getEntryList().getTop();
		if (toolTip != null && this.tooltipHoverChecker != null) {
			if (this.tooltipHoverChecker.checkHover(mouseX, mouseY, canHover))
				this.owningScreen.drawToolTip(toolTip, mouseX, mouseY);
		}

		if (this.undoHoverChecker.checkHover(mouseX, mouseY, canHover))
			this.owningScreen.drawToolTip(undoToolTip, mouseX, mouseY);

		if (this.defaultHoverChecker.checkHover(mouseX, mouseY, canHover))
			this.owningScreen.drawToolTip(defaultToolTip, mouseX, mouseY);
	}

	@Override
	public int getLabelWidth() {
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

	@Override
	public List<Widget> children() {
		return children;
	}

}
