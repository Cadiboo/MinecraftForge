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

import net.minecraft.client.gui.INestedGuiEventHandler;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraftforge.fml.client.config.ConfigEntryListWidget;

/**
 * Provides an interface for defining {@link ConfigEntryListWidget} entry objects.
 */
public interface IConfigScreenListEntry extends INestedGuiEventHandler {

	/**
	 * Gets the IConfigElement object owned by this entry.
	 */
	IConfigValueElement getConfigElement();

	/**
	 * Gets the name of the ConfigElement owned by this entry.
	 */
	String getName();

	/**
	 * Gets the current value of this entry.
	 */
	Object getCurrentValue();

	/**
	 * Gets the current values of this list entry.
	 */
	Object[] getCurrentValues();

	/**
	 * Is this list entry enabled?
	 *
	 * @return true if this entry's controls should be enabled, false otherwise.
	 */
	boolean enabled();

	/**
	 * Call {@link TextFieldWidget#tick()} for any TextFieldWidget objects in this entry.
	 */
	void tick();

	/**
	 * Is this entry's value equal to the default value? Generally true should be returned if this entry is not a property or category
	 * entry.
	 *
	 * @return true if this entry's value is equal to this entry's default value.
	 */
	boolean isDefault();

	/**
	 * Sets this entry's value to the default value.
	 */
	void resetToDefault();

	/**
	 * Handles reverting any changes that have occurred to this entry.
	 */
	void undoChanges();

	/**
	 * Has the value of this entry changed?
	 *
	 * @return true if changes have been made to this entry's value, false otherwise.
	 */
	boolean isChanged();

	/**
	 * Handles saving any changes that have been made to this entry back to the underlying object.
	 * It is a good practice to check isChanged() before performing the save action.
	 *
	 * @return true if the element has changed AND REQUIRES A RESTART. TODO: WHAT TYPE OF RESTART!!!!
	 */
	boolean save();

	/**
	 * Handles drawing any tooltips that apply to this entry.
	 * This method is called after all other GUI elements have been drawn to the screen,
	 * so it could also be used to draw any GUI element that needs to be drawn after
	 * all entries have had drawEntry() called.
	 */
	void renderToolTip(int mouseX, int mouseY, float partialTicks);

	/**
	 * Gets this entry's label width.
	 */
	int getLabelWidth();

	/**
	 * Gets this entry's right-hand x boundary. This value is used to control where the scroll bar is placed.
	 */
	int getEntryRightBound();

	/**
	 * This method is called when the parent GUI is closed. Most handlers won't need this; it is provided for special cases.
	 */
	void onGuiClosed();

	/**
	 * FIXME
	 * Whether or not this element is safe to modify while a world is running.
	 * For Categories return false if ANY properties in the category are modifiable
	 * while a world is running, true if all are not.
	 */
	boolean requiresWorldRestart();

	/**
	 * FIXME
	 * Whether or not this element requires Minecraft to be restarted when changed.
	 */
	boolean requiresMcRestart();

}