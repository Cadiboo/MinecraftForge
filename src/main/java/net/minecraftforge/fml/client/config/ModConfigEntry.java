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

package net.minecraftforge.fml.client.config;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.IGuiEventListener;

import java.util.Collections;
import java.util.List;

/**
 * A header for each ModConfig of the mod
 */
public class ModConfigEntry extends ConfigEntryList.Entry {

	public static final int COLOR = 0xAAAAAA;
	private final String labelText;
	private final int labelWidth;

	public ModConfigEntry(String name) {
		this.labelText = name;
		this.labelWidth = Minecraft.getInstance().fontRenderer.getStringWidth(name);
	}

	public void render(int index, int startY, int startX, int width, int height, int mouseX, int mouseY, boolean isHovered, float partialTicks) {
		Minecraft minecraft = Minecraft.getInstance();
		minecraft.fontRenderer.drawString(this.labelText, minecraft.currentScreen.width / 2F - this.labelWidth / 2F, startY + (height - 8F) / 2, COLOR);
//		ConfigEntryList.drawCuboid(startX, startY, startX + width, startY + height, 0F, 1F, 1F, 1F);
	}

	public List<? extends IGuiEventListener> children() {
		return Collections.emptyList();
	}

	public boolean changeFocus(boolean p_changeFocus_1_) {
		return false;
	}

}
