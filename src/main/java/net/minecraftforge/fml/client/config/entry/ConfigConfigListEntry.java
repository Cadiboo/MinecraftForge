package net.minecraftforge.fml.client.config.entry;

import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.fml.client.config.ConfigScreen;
import net.minecraftforge.fml.client.config.GuiButtonExt;
import net.minecraftforge.fml.client.config.HoverChecker;
import net.minecraftforge.fml.client.config.entry.widget.IConfigListEntryWidget;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

import static net.minecraftforge.fml.client.config.GuiUtils.UNDO_CHAR;

/**
 * @author Cadiboo
 */
public class ConfigConfigListEntry<T> extends ConfigListEntry<T> {

	@Nullable
	protected final GuiButtonExt undoChangesButton;
	@Nullable
	protected final List<String> undoChangesButtonTooltip;
	@Nullable
	protected final HoverChecker undoChangesButtonHoverChecker;

	public <W extends Widget & IConfigListEntryWidget<T>> ConfigConfigListEntry(final ConfigScreen owningScreen, final W widget) {
		this(owningScreen, widget, true);
	}

	public <W extends Widget & IConfigListEntryWidget<T>> ConfigConfigListEntry(final ConfigScreen owningScreen, final W widget, final boolean enableUndoChangesButton) {
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
	}

	@Override
	public void renderToolTip(final int mouseX, final int mouseY, final float partialTicks) {
		if (this.undoChangesButtonHoverChecker != null && this.undoChangesButtonHoverChecker.checkHover(mouseX, mouseY, true))
			this.owningScreen.drawToolTip(undoChangesButtonTooltip, mouseX, mouseY);
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
		return posX;
	}

	@Override
	public boolean shouldRenderLabel() {
		return false;
	}

}
