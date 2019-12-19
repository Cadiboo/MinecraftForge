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

import com.google.common.collect.Lists;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.IGuiEventListener;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.list.ExtendedList;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraftforge.fml.client.config.entry.CategoryEntry;
import net.minecraftforge.fml.client.config.entry.ConfigListEntry;
import net.minecraftforge.fml.client.config.entry.ConfigValue;
import net.minecraftforge.fml.client.config.entry.IConfigScreenListEntry;
import net.minecraftforge.fml.client.config.entry.IConfigValueElement;
import net.minecraftforge.fml.config.ConfigTracker;
import net.minecraftforge.fml.config.ModConfig;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nullable;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Pattern;

/**
 * This class implements the scrolling list functionality of the config GUI screens.
 * It also provides all the default control handlers for the various property types.
 *
 * @author bspkrs
 * @author Cadiboo
 */
public class ConfigEntryListWidget extends ExtendedList<ConfigListEntry> {

	/**
	 * The empty space between the entries and both sides of the screen.
	 */
	public static final int PADDING_X = 10;
	public final ConfigScreen owningScreen;

	public ConfigEntryListWidget(final Minecraft mcIn, final ConfigScreen owningScreen) {
		super(mcIn, owningScreen.width, owningScreen.height, owningScreen.getHeaderSize(), owningScreen.height - owningScreen.getFooterSize(), 20);
		this.owningScreen = owningScreen;
	}

	// For testing. Modified from WorldRenderer#drawAABB. TODO: Remove
	public static void drawCuboid(double minX, double minY, double maxX, double maxY, float red, float green, float blue, float alpha) {
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bufferbuilder = tessellator.getBuffer();
		bufferbuilder.begin(GL11.GL_LINE_STRIP, DefaultVertexFormats.POSITION_COLOR);
		bufferbuilder.pos(minX, minY, 0.0).color(red, green, blue, 0.0F).endVertex();
		bufferbuilder.pos(minX, minY, 0.0).color(red, green, blue, alpha).endVertex();
		bufferbuilder.pos(maxX, minY, 0.0).color(red, green, blue, alpha).endVertex();
		bufferbuilder.pos(maxX, minY, 0.0).color(red, green, blue, alpha).endVertex();
		bufferbuilder.pos(minX, minY, 0.0).color(red, green, blue, alpha).endVertex();
		bufferbuilder.pos(minX, minY, 0.0).color(red, green, blue, alpha).endVertex();
		bufferbuilder.pos(minX, maxY, 0.0).color(red, green, blue, alpha).endVertex();
		bufferbuilder.pos(maxX, maxY, 0.0).color(red, green, blue, alpha).endVertex();
		bufferbuilder.pos(maxX, maxY, 0.0).color(red, green, blue, alpha).endVertex();
		bufferbuilder.pos(minX, maxY, 0.0).color(red, green, blue, alpha).endVertex();
		bufferbuilder.pos(minX, maxY, 0.0).color(red, green, blue, alpha).endVertex();
		bufferbuilder.pos(minX, maxY, 0.0).color(red, green, blue, 0.0F).endVertex();
		bufferbuilder.pos(minX, minY, 0.0).color(red, green, blue, alpha).endVertex();
		bufferbuilder.pos(maxX, maxY, 0.0).color(red, green, blue, 0.0F).endVertex();
		bufferbuilder.pos(maxX, minY, 0.0).color(red, green, blue, alpha).endVertex();
		bufferbuilder.pos(maxX, maxY, 0.0).color(red, green, blue, 0.0F).endVertex();
		bufferbuilder.pos(maxX, minY, 0.0).color(red, green, blue, alpha).endVertex();
		bufferbuilder.pos(maxX, minY, 0.0).color(red, green, blue, 0.0F).endVertex();
		tessellator.draw();
	}

	/**
	 * First mimics the logic from {@link Screen#init(Minecraft, int, int)} (Set bounds, clear children, clear focused)
	 * then mimics the logic from {@link Screen#init()} (Add Widgets)
	 */
	public void init() {
		// Screen#init(Minecraft, int, int)
		// top
		this.y0 = owningScreen.getHeaderSize();
		// bottom
		this.y1 = owningScreen.height - owningScreen.getFooterSize();
		// left
		this.x0 = 0;
		// right
		this.x1 = owningScreen.width;

		this.width = this.x1 - this.x0;
		this.height = this.y1 - this.y0;

		this.children().clear();
		this.setFocused(null);

		// Screen#init()

		final AtomicReference<IConfigValueElement<Boolean>>configValueElementReference = new AtomicReference<>();

		final IConfigValueElement<Boolean> configValueElement = new IConfigValueElement<Boolean>() {

			final ConfigValue<Boolean> configValue = ConfigValue.of(Boolean.class, ConfigTracker.INSTANCE.getConfig(owningScreen.modContainer.getModId(), ModConfig.Type.COMMON).get(), "aBoolean", TestConfig.COMMON_SPEC.getSpec().get("aBoolean"));

			@Override
			public boolean isProperty() {
				return true;
			}

			@Override
			public IConfigScreenListEntry makeConfigEntry(final ConfigScreen configScreen, final ConfigEntryListWidget configEntryListScreen) {
				return new ConfigListEntry(configScreen, configEntryListScreen, configValueElementReference.get()) {
					@Override
					public boolean isDragging() {
						return false;
					}

					@Override
					public void setDragging(final boolean p_setDragging_1_) {

					}

					@Nullable
					@Override
					public IGuiEventListener getFocused() {
						return null;
					}

					@Override
					public void setFocused(@Nullable final IGuiEventListener p_setFocused_1_) {

					}

					@Override
					public Object getCurrentValue() {
						return configValue.getCurrentValue();
					}

					@Override
					public Object[] getCurrentValues() {
						return new Object[0];
					}

					@Override
					public void tick() {

					}

					@Override
					public boolean isDefault() {
						return configValue.isDefault();
					}

					@Override
					public void setToDefault() {
						configValue.resetToDefault();
					}

					@Override
					public void undoChanges() {
						configValue.undoChanges();
					}

					@Override
					public boolean isChanged() {
						return configValue.isChanged();
					}

					@Override
					public boolean save() {
						configValue.saveAndLoad();
						return configValue.requiresWorldRestart();
					}
				};
			}

			@Override
			public String getName() {
				return configValue.getLabel();
			}

			@Override
			public String getQualifiedName() {
				return null;
			}

			@Override
			public String getTranslationKey() {
				return configValue.getLabel();
			}

			@Override
			public String getComment() {
				return configValue.getComment();
			}

			@Override
			public List<IConfigValueElement> getChildElements() {
				return Lists.newArrayList();
			}

			@Override
			public boolean isList() {
				return false;
			}

			@Override
			public boolean isListLengthFixed() {
				return false;
			}

			@Override
			public int getMaxListLength() {
				return 0;
			}

			@Override
			public boolean isDefault() {
				return configValue.isDefault();
			}

			@Override
			public Boolean getDefault() {
				return configValue.getDefaultValue();
			}

			@Override
			public Boolean[] getDefaults() {
				return new Boolean[0];
			}

			@Override
			public void setToDefault() {
				configValue.resetToDefault();
			}

			@Override
			public boolean requiresWorldRestart() {
				return configValue.requiresWorldRestart();
			}

			@Override
			public boolean showInGui() {
				return true;
			}

			@Override
			public boolean requiresMcRestart() {
				return configValue.requiresMcRestart();
			}

			@Override
			public Boolean get() {
				return configValue.getCurrentValue();
			}

			@Override
			public Boolean[] getList() {
				return new Boolean[0];
			}

			@Override
			public void set(final Boolean value) {
				configValue.setCurrentValue(value);
			}

			@Override
			public void set(final Boolean[] aVal) {

			}

			@Override
			public String[] getValidValues() {
				return new String[0];
			}

			@Override
			public Boolean getMinValue() {
				return false;
			}

			@Override
			public Boolean getMaxValue() {
				return true;
			}

			@Override
			public Pattern getValidationPattern() {
				return null;
			}
		};
		configValueElementReference.set(configValueElement);
		this.children().add((ConfigListEntry) configValueElement.makeConfigEntry(owningScreen, this));
	}

	public int getRowWidth() {
		return getWidth() - PADDING_X * 2;
	}

	/**
	 * This method is a pass-through for IConfigEntry objects that contain {@link TextFieldWidget} elements.
	 * Called from the parent ConfigScreen.
	 */
	public void tick() {
		for (IConfigScreenListEntry entry : this.getListEntries())
			entry.tick();
	}

	/**
	 * This method is a pass-through for IConfigEntry objects that need to perform actions
	 * when the containing GUI is closed.
	 * Called from the parent ConfigScreen.
	 */
	public void onClose() {
		for (IConfigScreenListEntry entry : this.getListEntries())
			entry.onGuiClosed();
	}

	/**
	 * Saves all properties.
	 * This method returns true if any elements were changed that require a restart for proper handling.
	 *
	 * @return If any elements were changed that require the game to restart for proper handling
	 */
	public boolean save() {
		boolean requiresRestart = false;
		for (IConfigScreenListEntry entry : this.getListEntries())
			requiresRestart |= entry.save();
		return requiresRestart;
	}

	/**
	 * Checks if all IConfigEntry objects on this screen are set to default.
	 *
	 * @param applyToSubcategories If sub-category objects should be checked as well.
	 * @return If all IConfigEntry objects on this screen are set to default.
	 */
	public boolean areAllEntriesDefault(boolean applyToSubcategories) {
		for (IConfigScreenListEntry entry : this.getListEntries())
			if (applyToSubcategories || !(entry instanceof CategoryEntry))
				if (!entry.isDefault())
					return false;
		return true;
	}

	/**
	 * Resets all IConfigEntry objects on this screen to their default values.
	 *
	 * @param applyToSubcategories If sub-category objects should be reset as well.
	 */
	public void resetAllToDefault(boolean applyToSubcategories) {
		for (IConfigScreenListEntry entry : this.getListEntries())
			if (applyToSubcategories || !(entry instanceof CategoryEntry))
				entry.setToDefault();
	}

	/**
	 * Checks if any IConfigEntry objects on this screen are changed.
	 *
	 * @param applyToSubcategories If sub-category objects should be checked as well.
	 * @return f any IConfigEntry objects on this screen are changed.
	 */
	public boolean areAnyEntriesChanged(boolean applyToSubcategories) {
		for (IConfigScreenListEntry entry : this.getListEntries())
			if (applyToSubcategories || !(entry instanceof CategoryEntry))
				if (entry.isChanged())
					return true;
		return false;
	}

	/**
	 * Reverts changes to all IConfigEntry objects on this screen.
	 *
	 * @param applyToSubcategories If sub-category objects should be reverted as well.
	 */
	public void undoAllChanges(boolean applyToSubcategories) {
		for (IConfigScreenListEntry entry : this.getListEntries())
			if (applyToSubcategories || !(entry instanceof CategoryEntry))
				entry.undoChanges();
	}

	/**
	 * Checks if any IConfigEntry objects on this screen are enabled.
	 *
	 * @param applyToSubcategories If sub-category objects should be checked as well.
	 * @return If any IConfigEntry objects on this screen are enabled.
	 */
	public boolean areAnyEntriesEnabled(boolean applyToSubcategories) {
		for (IConfigScreenListEntry entry : this.getListEntries())
			if (applyToSubcategories || !(entry instanceof CategoryEntry))
				if (entry.enabled())
					return true;
		return false;
	}

//	/**
//	 * Calls the renderToolTip() method for all IConfigEntry objects on this screen.
//	 * This is called from the parent GuiConfig screen after drawing all other elements.
//	 */
//	public void renderScreenPost(int mouseX, int mouseY, float partialTicks) {
//		for (IConfigEntry entry : this.getListEntries())
//			entry.renderToolTip(mouseX, mouseY);
//	}

	public void postRender(final int mouseX, final int mouseY, final float partialTicks) {
		for (int item = 0; item < this.getItemCount(); item++) {
			int itemTop = this.getRowTop(item);
//			int itemBottom = this.getRowBottom(item); // Private, logic copied below
			int itemBottom = this.getRowTop(item) + this.itemHeight;
			if (itemTop <= this.y1 && itemBottom >= this.y0) {
				ConfigListEntry configListEntry = this.getEntry(item);
//				if (configListEntry.isHovered())
//					configListEntry.renderTooltip(mouseX, mouseY, partialTicks);
			}
		}
	}

	public Iterable<? extends IConfigScreenListEntry> getListEntries() {
		return children();
	}

}
