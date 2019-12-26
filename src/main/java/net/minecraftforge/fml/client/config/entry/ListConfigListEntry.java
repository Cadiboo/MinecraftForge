package net.minecraftforge.fml.client.config.entry;

import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.fml.client.config.ConfigScreen;
import net.minecraftforge.fml.client.config.GuiButtonExt;
import net.minecraftforge.fml.client.config.HoverChecker;
import net.minecraftforge.fml.client.config.entry.widget.BooleanButton;
import net.minecraftforge.fml.client.config.entry.widget.ConfigListEntryWidget;

import java.util.Collections;
import java.util.List;

import static net.minecraftforge.fml.client.config.GuiUtils.UNDO_CHAR;

/**
 * @author Cadiboo
 */
public abstract class ListConfigListEntry<T> extends ConfigListEntry<T> {

	protected final GuiButtonExt undoChangesButton, addEntryBelowButton, removeEntryButton;
	protected final List<String> undoChangesButtonTooltip, addEntryBelowTooltip, removeEntryTooltip;
	protected final HoverChecker undoChangesButtonHoverChecker, addEntryBelowButtonHoverChecker, removeEntryButtonHoverChecker;

	public <W extends Widget & ConfigListEntryWidget<T>> ListConfigListEntry(final ConfigScreen owningScreen, final W widget) {
		super(owningScreen, widget);

		this.children().add(this.undoChangesButton = new GuiButtonExt(0, 0, 0, 0, UNDO_CHAR, b -> this.undoChanges()));
		this.children().add(this.addEntryBelowButton = new GuiButtonExt(0, 0, 0, 0, "+", b -> this.addEntryBelow()));
		this.children().add(this.removeEntryButton = new GuiButtonExt(0, 0, 0, 0, "-", b -> this.removeEntry()));

		this.undoChangesButtonTooltip = Collections.singletonList(I18n.format("fml.configgui.undoChanges.tooltip.single"));
		this.addEntryBelowTooltip = Collections.singletonList(I18n.format("fml.configgui.tooltip.addNewEntryBelow"));
		this.removeEntryTooltip = Collections.singletonList(I18n.format("fml.configgui.tooltip.removeEntry"));

		this.undoChangesButtonHoverChecker = new HoverChecker(this.undoChangesButton, 500);
		this.addEntryBelowButtonHoverChecker = new HoverChecker(this.addEntryBelowButton, 500);
		this.removeEntryButtonHoverChecker = new HoverChecker(this.removeEntryButton, 500);

		this.addEntryBelowButton.setFGColor(BooleanButton.getColor(true));
		this.removeEntryButton.setFGColor(BooleanButton.getColor(false));
	}

	public abstract void removeEntry();

	public abstract void addEntryBelow();

	@Override
	public void renderToolTip(final int mouseX, final int mouseY, final float partialTicks) {
		if (this.addEntryBelowButtonHoverChecker.checkHover(mouseX, mouseY, true))
			this.owningScreen.drawToolTip(addEntryBelowTooltip, mouseX, mouseY);
		if (this.removeEntryButtonHoverChecker.checkHover(mouseX, mouseY, true))
			this.owningScreen.drawToolTip(removeEntryTooltip, mouseX, mouseY);
		super.renderToolTip(mouseX, mouseY, partialTicks);
	}

	public int preRenderWidgets(final int startY, final int startX, final int width, final int height, final int buttonSize) {
		// Changing x coordinate.
		// After use it is the largest x coordinate before the buttons.
		// After use it is comparable to "startX + width - buttonsWidth"
		int posX = startX + width;

		posX -= BUTTON_SPACER + buttonSize;
		preRenderWidget(undoChangesButton, posX, startY, buttonSize, buttonSize);
		undoChangesButton.active &= isChanged();

		posX -= BUTTON_SPACER + buttonSize;
		preRenderWidget(addEntryBelowButton, posX, startY, buttonSize, buttonSize);
//		addEntryBelowButton.active &= !this.isListFixedLength();

		posX -= buttonSize + BUTTON_SPACER;
		preRenderWidget(removeEntryButton, posX, startY, buttonSize, buttonSize);
//		removeEntryButton.active &= !this.isListFixedLength();

		return posX;
	}

	@Override
	public boolean shouldRenderLabel() {
		return false;
	}

}
