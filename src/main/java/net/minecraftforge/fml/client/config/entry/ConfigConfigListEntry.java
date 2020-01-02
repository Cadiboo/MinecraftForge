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

package net.minecraftforge.fml.client.config.entry;

import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.common.ForgeConfigSpec.ConfigValue;
import net.minecraftforge.fml.client.config.ConfigScreen;
import net.minecraftforge.fml.client.config.GuiButtonExt;
import net.minecraftforge.fml.client.config.HoverChecker;
import net.minecraftforge.fml.client.config.entry.widget.IConfigListEntryWidget;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

import static net.minecraftforge.fml.client.config.GuiUtils.UNDO_CHAR;

/**
 * A ConfigListEntry for a Config ({@link ConfigValue})
 * Has the undo changes button on the right.
 *
 * @param <T> The type of the config object (e.g. Boolean/Float).
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
