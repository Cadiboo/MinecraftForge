package net.minecraftforge.fml.client.config.entry;

import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.common.ForgeConfigSpec.ConfigValue;
import net.minecraftforge.fml.client.config.ConfigScreen;
import net.minecraftforge.fml.client.config.GuiButtonExt;
import net.minecraftforge.fml.client.config.HoverChecker;
import net.minecraftforge.fml.client.config.entry.widget.BooleanButton;
import net.minecraftforge.fml.client.config.entry.widget.IConfigListEntryWidget;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

import static net.minecraftforge.fml.client.config.GuiUtils.UNDO_CHAR;

/**
 * A ConfigListEntry for a List ({@link ConfigValue})
 * Has the undo changes, add entry below and remove entry buttons on the right.
 *
 * @param <T> The type of the config object (e.g. Boolean/Float).
 * @author Cadiboo
 */
public abstract class ListConfigListEntry<T> extends ConfigListEntry<T> {

	@Nullable
	protected final GuiButtonExt undoChangesButton, addEntryBelowButton, removeEntryButton;
	@Nullable
	protected final List<String> undoChangesButtonTooltip, addEntryBelowTooltip, removeEntryTooltip;
	@Nullable
	protected final HoverChecker undoChangesButtonHoverChecker, addEntryBelowButtonHoverChecker, removeEntryButtonHoverChecker;

	public <W extends Widget & IConfigListEntryWidget<T>> ListConfigListEntry(final ConfigScreen owningScreen, final W widget) {
		this(owningScreen, widget, true, true, true);
	}

	public <W extends Widget & IConfigListEntryWidget<T>> ListConfigListEntry(final ConfigScreen owningScreen, final W widget, final boolean enableUndoChangesButton, final boolean enableAddNewEntryBelowButton, final boolean enableRemoveEntryButton) {
		super(owningScreen, widget);

		if (enableUndoChangesButton) {
			this.children().add(this.undoChangesButton = new GuiButtonExt(0, 0, 0, 0, UNDO_CHAR, b -> this.undoChanges()));
			this.undoChangesButtonTooltip = Collections.singletonList(I18n.format("fml.configgui.undoChanges.tooltip.single"));
			this.undoChangesButtonHoverChecker = new HoverChecker(this.undoChangesButton, 500);
		} else {
			this.undoChangesButton = null;
			this.undoChangesButtonTooltip = null;
			this.undoChangesButtonHoverChecker = null;
		}
		if (enableAddNewEntryBelowButton) {
			this.children().add(this.addEntryBelowButton = new GuiButtonExt(0, 0, 0, 0, "+", b -> this.addEntryBelow()));
			this.addEntryBelowTooltip = Collections.singletonList(I18n.format("fml.configgui.tooltip.addNewEntryBelow"));
			this.addEntryBelowButtonHoverChecker = new HoverChecker(this.addEntryBelowButton, 500);
			this.addEntryBelowButton.setFGColor(BooleanButton.getColor(true));
		} else {
			this.addEntryBelowButton = null;
			this.addEntryBelowTooltip = null;
			this.addEntryBelowButtonHoverChecker = null;
		}
		if (enableRemoveEntryButton) {
			this.children().add(this.removeEntryButton = new GuiButtonExt(0, 0, 0, 0, "-", b -> this.removeEntry()));
			this.removeEntryTooltip = Collections.singletonList(I18n.format("fml.configgui.tooltip.removeEntry"));
			this.removeEntryButtonHoverChecker = new HoverChecker(this.removeEntryButton, 500);
			this.removeEntryButton.setFGColor(BooleanButton.getColor(false));
		} else {
			this.removeEntryButton = null;
			this.removeEntryTooltip = null;
			this.removeEntryButtonHoverChecker = null;
		}
	}

	public abstract void removeEntry();

	public abstract void addEntryBelow();

	@Override
	public void renderToolTip(final int mouseX, final int mouseY, final float partialTicks) {
		if (this.undoChangesButtonHoverChecker != null && this.undoChangesButtonHoverChecker.checkHover(mouseX, mouseY, true))
			this.owningScreen.drawToolTip(undoChangesButtonTooltip, mouseX, mouseY);
		if (this.addEntryBelowButtonHoverChecker != null && this.addEntryBelowButtonHoverChecker.checkHover(mouseX, mouseY, true))
			this.owningScreen.drawToolTip(addEntryBelowTooltip, mouseX, mouseY);
		if (this.removeEntryButtonHoverChecker != null && this.removeEntryButtonHoverChecker.checkHover(mouseX, mouseY, true))
			this.owningScreen.drawToolTip(removeEntryTooltip, mouseX, mouseY);
		super.renderToolTip(mouseX, mouseY, partialTicks);
	}

	public int preRenderWidgets(final int startY, final int startX, final int width, final int height, final int buttonSize) {
		// Changing x coordinate.
		// After use it is the largest x coordinate before the buttons.
		// After use it is comparable to "startX + width - buttonsWidth"
		int posX = startX + width;
		if (undoChangesButton != null) {
			posX -= BUTTON_SPACER + buttonSize;
			preRenderWidget(undoChangesButton, posX, startY, buttonSize, buttonSize);
			undoChangesButton.active &= isChanged();
		}
		if (addEntryBelowButton != null) {
			posX -= BUTTON_SPACER + buttonSize;
			preRenderWidget(addEntryBelowButton, posX, startY, buttonSize, buttonSize);
		}
		if (removeEntryButton != null) {
			posX -= buttonSize + BUTTON_SPACER;
			preRenderWidget(removeEntryButton, posX, startY, buttonSize, buttonSize);
		}
		return posX;
	}

	@Override
	public boolean shouldRenderLabel() {
		return false;
	}

}
