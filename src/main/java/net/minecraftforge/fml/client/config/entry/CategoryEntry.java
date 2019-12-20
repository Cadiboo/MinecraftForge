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

import net.minecraftforge.fml.client.config.ConfigEntryListWidget;
import net.minecraftforge.fml.client.config.ConfigScreen;

/**
 * Provides an entry that consists of a GuiButton for navigating to the child category ConfigScreen screen.
 */
public abstract class CategoryEntry extends ConfigListEntry {
//
//	protected final GuiButtonExt btnSelectCategory;
//	protected Screen childScreen;
//
	public CategoryEntry(ConfigScreen owningScreen, ConfigEntryListWidget owningEntryList, IConfigValueElement configElement) {
		super(owningScreen, owningEntryList, configElement);

////		this.childScreen = this.buildChildScreen();
////
//		this.btnSelectCategory = new GuiButtonExt(0, 0, 300, 18, I18n.format(name), b -> Minecraft.getInstance().displayGuiScreen(childScreen));
//		this.tooltipHoverChecker = new HoverChecker(this.btnSelectCategory, 800);

		this.drawLabel = false;
	}
//
//	/**
//	 * This method is called in the constructor and is used to set the childScreen field.
//	 */
//	protected Screen buildChildScreen() {
//		return new ConfigScreen(this.owningScreen.getMinecraft(), this.owningScreen, this.owningScreen.modContainer);
////		return new ConfigScreen(this.owningScreen, this.configElement.getChildElements(), this.owningScreen.modID,
////				owningScreen.doAllRequireWorldRestart() || this.configElement.requiresWorldRestart(),
////				owningScreen.doAllRequireMcRestart() || this.configElement.requiresMcRestart(), this.owningScreen.title,
////				((this.owningScreen.getSubtitle() == null ? "" : this.owningScreen.getSubtitle()) + " > " + this.name));
//	}
//
//	@Override
//	public void render(int slotIndex, int y, int x, int listWidth, int slotHeight, int mouseX, int mouseY, boolean isSelected, float partial) {
//		this.btnSelectCategory.x = listWidth / 2 - 150;
//		this.btnSelectCategory.y = y;
//		this.btnSelectCategory.active = enabled();
//		this.btnSelectCategory.render(mouseX, mouseY, partial);
//
//		super.render(slotIndex, y, x, listWidth, slotHeight, mouseX, mouseY, isSelected, partial);
//	}
//
//	@Override
//	public String getCurrentValue() {
//		return "";
//	}
//
//	@Override
//	public String[] getCurrentValues() {
//		return new String[]{getCurrentValue()};
//	}
//
//	@Override
//	public boolean enabled() {
//		return true;
//	}
//
//	@Override
//	public void keyTyped(char eventChar, int eventKey) {
//	}
//
//	@Override
//	public void updateCursorCounter() {
//	}
//
//	@Override
//	public void mouseClicked(int x, int y, int mouseEvent) {
//	}
//
//	@Override
//	public boolean isDefault() {
//		if (childScreen instanceof ConfigScreen && ((ConfigScreen) childScreen).getEntryList() != null)
//			return ((ConfigScreen) childScreen).getEntryList().areAllEntriesDefault(true);
//
//		return true;
//	}
//
//	@Override
//	public void setToDefault() {
//		if (childScreen instanceof ConfigScreen && ((ConfigScreen) childScreen).getEntryList() != null)
//			((ConfigScreen) childScreen).getEntryList().resetAllToDefault(true);
//	}
//
//	@Override
//	public void undoChanges() {
//		if (childScreen instanceof ConfigScreen && ((ConfigScreen) childScreen).getEntryList() != null)
//			((ConfigScreen) childScreen).getEntryList().undoAllChanges(true);
//	}
//
//	@Override
//	public boolean isChanged() {
//		if (childScreen instanceof ConfigScreen && ((ConfigScreen) childScreen).getEntryList() != null)
//			return ((ConfigScreen) childScreen).getEntryList().hasChangedEntry(true);
//		else
//			return false;
//	}
//
//	@Override
//	public boolean saveConfigElement() {
//		boolean requiresRestart = false;
//
//		if (childScreen instanceof ConfigScreen && ((ConfigScreen) childScreen).getEntryList() != null) {
//			requiresRestart = configElement.requiresMcRestart() && ((ConfigScreen) childScreen).getEntryList().hasChangedEntry(true);
//
//			if (((ConfigScreen) childScreen).getEntryList().save())
//				requiresRestart = true;
//		}
//
//		return requiresRestart;
//	}
//
//	@Override
//	public int getLabelWidth() {
//		return 0;
//	}
//
//	@Override
//	public int getEntryRightBound() {
//		return this.owningEntryList.getWidth() / 2 + 155 + 22 + 18;
//	}
//
//	@Override
//	public void renderToolTip(int mouseX, int mouseY) {
//		boolean canHover = mouseY < this.owningScreen.getEntryList().getBottom() && mouseY > this.owningScreen.getEntryList().getTop();
//
//		if (this.tooltipHoverChecker.checkHover(mouseX, mouseY, canHover))
//			this.owningScreen.drawToolTip(toolTip, mouseX, mouseY);
//
//		super.renderToolTip(mouseX, mouseY);
//	}
 
}
