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

import net.minecraft.client.MainWindow;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.Widget;
import net.minecraftforge.fml.client.config.ConfigEntryListWidget;
import net.minecraftforge.fml.client.config.ConfigScreen;
import net.minecraftforge.fml.client.config.GuiButtonExt;

/**
 * Provides an entry that consists of a GuiButton for navigating to the child category ConfigScreen screen.
 */
public abstract class CategoryConfigListEntry<T> extends ConfigListEntry<T> {

	protected final GuiButtonExt selectCategoryButton;
	protected final String label;
	private Screen childScreen;

	public CategoryConfigListEntry(final ConfigScreen owningScreen, final String label) {
		super(owningScreen);
		this.label = label;

		this.children().add(this.selectCategoryButton = new GuiButtonExt(0, 0, 300, 18, getLabel(), b -> Minecraft.getInstance().displayGuiScreen(getChildScreen())));

		this.drawLabel = false;
	}

	public Screen getChildScreen() {
		if (this.childScreen == null) {
			this.childScreen = this.buildChildScreen();
			final Minecraft minecraft = Minecraft.getInstance();
			final MainWindow mainWindow = minecraft.func_228018_at_();
			this.childScreen.init(minecraft, mainWindow.getScaledWidth(), mainWindow.getScaledHeight());
		}
		return this.childScreen;
	}

	protected abstract Screen buildChildScreen();

	@Override
	public boolean isCategory() {
		return true;
	}

	@Override
	public boolean isValidValue() {
		return true;
	}

	@Override
	public Widget getWidget() {
		return selectCategoryButton;
	}

	@Override
	public boolean enabled() {
		return true;
	}

	@Override
	public void tick() {
		super.tick();
		getChildScreen().tick();
	}

	@Override
	public String getLabel() {
		return label;
	}

	@Override
	public boolean isDefault() {
		final Screen childScreen = getChildScreen();
		if (childScreen instanceof ConfigScreen && ((ConfigScreen) childScreen).getEntryList() != null)
			return ((ConfigScreen) childScreen).getEntryList().areAllEntriesDefault(true);

		return true;
	}

	@Override
	protected ConfigElementContainer<T> getBooleanConfigElement() {
		return null;
	}

	@Override
	public void resetToDefault() {
		final Screen childScreen = getChildScreen();
		if (childScreen instanceof ConfigScreen && ((ConfigScreen) childScreen).getEntryList() != null)
			((ConfigScreen) childScreen).getEntryList().resetAllToDefault(true);
	}

	@Override
	public boolean isChanged() {
		final Screen childScreen = getChildScreen();
		if (childScreen instanceof ConfigScreen && ((ConfigScreen) childScreen).getEntryList() != null)
			return ((ConfigScreen) childScreen).getEntryList().areAnyEntriesChanged(true);
		else
			return false;
	}

	@Override
	public void undoChanges() {
		final Screen childScreen = getChildScreen();
		if (childScreen instanceof ConfigScreen && ((ConfigScreen) childScreen).getEntryList() != null)
			((ConfigScreen) childScreen).getEntryList().undoAllChanges(true);
	}

	@Override
	public boolean save() {
		boolean requiresRestart = false;

		final Screen childScreen = getChildScreen();
		if (childScreen instanceof ConfigScreen && ((ConfigScreen) childScreen).getEntryList() != null) {
			requiresRestart = this.requiresMcRestart() &&
					((ConfigScreen) childScreen).getEntryList().areAnyEntriesChanged(true);

//			final ConfigEntryListWidget entryList = ((ConfigScreen) childScreen).getEntryList();
//			if (entryList.save())
//				requiresRestart = true;
		}

		return requiresRestart;
	}

	@Override
	public boolean requiresWorldRestart() {
		final Screen childScreen = getChildScreen();
		if (childScreen instanceof ConfigScreen && ((ConfigScreen) childScreen).getEntryList() != null)
			return ((ConfigScreen) childScreen).getEntryList().anyRequireWorldRestart();
		else
			return false;
	}

	@Override
	public boolean requiresMcRestart() {
		final Screen childScreen = getChildScreen();
		if (childScreen instanceof ConfigScreen && ((ConfigScreen) childScreen).getEntryList() != null)
			return ((ConfigScreen) childScreen).getEntryList().anyRequireGameRestart();
		else
			return false;
	}

}
