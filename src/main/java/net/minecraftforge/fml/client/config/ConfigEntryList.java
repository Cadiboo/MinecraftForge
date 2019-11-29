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

import com.electronwill.nightconfig.core.Config;
import com.google.common.collect.Lists;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.IGuiEventListener;
import net.minecraft.client.gui.INestedGuiEventHandler;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.gui.widget.list.AbstractOptionList;
import net.minecraft.client.gui.widget.list.ExtendedList;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.ForgeConfigSpec.ValueSpec;
import net.minecraftforge.fml.config.ConfigTracker;
import net.minecraftforge.fml.config.ModConfig;
import org.apache.commons.lang3.tuple.Triple;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

import static net.minecraftforge.fml.client.config.GuiUtils.RESET_CHAR;
import static net.minecraftforge.fml.client.config.GuiUtils.UNDO_CHAR;

public class ConfigEntryList extends ExtendedList<ConfigEntryList.Entry> {

	public static final int TOP = 43;
	public static final int BOTTOM_PADDING = 32;
	public static final int MARGIN_X = 10;
	private static final Logger LOGGER = LogManager.getLogger();
	public final ConfigScreen configScreen;

	public ConfigEntryList(final ConfigScreen configScreen, final Minecraft minecraft) {
		super(minecraft, configScreen.width, configScreen.height - BOTTOM_PADDING, TOP, configScreen.height - BOTTOM_PADDING, 20);
		this.configScreen = configScreen;
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

	public int getRowWidth() {
		return getWidth() - MARGIN_X * 2;
	}

	@Override
	public void render(final int mouseX, final int mouseY, final float partialTicks) {
		super.render(mouseX, mouseY, partialTicks);
//		drawCuboid(x0, y0, x1, y1, 1, 0, 0, 1);
//		drawCuboid(x0 + 10, y0 + 10, x1 - 10, y1 - 10, 0.5F, 0, 0, 1);
//		drawCuboid(0, 0, width, height, 0F, 0.5F, 0, 1);
	}

	protected int getScrollbarPosition() {
		return getWidth() - MARGIN_X / 2;
	}

	@Override
	public boolean mouseClicked(final double p_mouseClicked_1_, final double p_mouseClicked_3_, final int p_mouseClicked_5_) {
		// TODO: there must be a better way to do this
		unfocus();
		return super.mouseClicked(p_mouseClicked_1_, p_mouseClicked_3_, p_mouseClicked_5_);
	}

	@Override
	protected void renderList(final int x, final int y, final int mouseX, final int mouseY, final float partialTicks) {
		super.renderList(x, y, mouseX, mouseY, partialTicks);

		if (!this.isHovered(mouseX, mouseY))
			return;

		boolean anythingRendered = false;

		int i = this.getItemCount();
		for (int j = 0; j < i; ++j) {
			int k = this.getRowTop(j);
//			int l = this.getRowBottom(j);
			int l = this.getRowTop(j) + this.itemHeight;
			if (l >= this.y0 && k <= this.y1) {
				final Entry e = this.getEntry(j);
				if (!(e instanceof ConfigValueEntry))
					continue;
				ConfigValueEntry entry = (ConfigValueEntry) e;
//				drawCuboid(entry.startX, entry.startY, entry.endX, entry.endY, 0F, 1F, 1F, 1F);
				final boolean hovered = mouseX >= entry.startX && mouseY >= entry.startY && mouseX < entry.endX && mouseY < entry.endY;
				if (hovered && entry.tooltip != null && !entry.tooltip.isEmpty()) {
					configScreen.drawToolTip(Arrays.asList(entry.tooltip.split("\n")), mouseX, mouseY);
					anythingRendered = true;
				}
			}
		}

		if (anythingRendered) {
			// These are enabled by configScreen.drawToolTip
			GlStateManager.disableLighting();
			GlStateManager.disableDepthTest();
		}
	}

	private boolean isHovered(int mouseX, int mouseY) {
		return mouseX >= x0 && mouseY >= y0 && mouseX < x1 && mouseY < y1;
	}

	public void unfocus() {
		this.children().forEach(this::unfocus);
	}

	/**
	 * Copied loosely from {@link Screen#init(Minecraft, int, int)}
	 * Updates position values & fills entry list
	 */
	public void init() {
		this.width = configScreen.width;
		this.height = configScreen.height - BOTTOM_PADDING;
		this.y0 = TOP;
		this.y1 = configScreen.height - BOTTOM_PADDING;
		this.x0 = 0;
		this.x1 = configScreen.width;

		this.clearEntries();
		this.setFocused(null);

		fill();
	}

	protected void fill() {
		String modId = configScreen.modContainer.getModId();
		for (final ModConfig.Type type : ModConfig.Type.values()) {
			if (type == ModConfig.Type.SERVER && !canPlayerEditServerConfig())
				continue;
			ConfigTracker.INSTANCE.getConfig(modId, type).ifPresent(modConfig -> {
				this.addEntry(new ModConfigEntry(I18n.format("fml.configgui.modConfig", modConfig.getFileName())));
				modConfig.getSpec().valueMap().forEach((configKey, configValue) -> addEntries(modConfig, configKey, configValue, ""));
			});
		}
	}

	// TODO: change this to be hierarchical based instead of all on once screen
	private void addEntries(final ModConfig modConfig, final String configKey, final Object configValue, final String parentName) {
		final String path = parentName + configKey;
		if (configValue instanceof ValueSpec) {
			ValueSpec valueSpec = (ValueSpec) configValue;
			final Triple<Widget, ConfigEntry<?>, Consumer<?>> tuple = ConfigEntryWidgets.makeWidgetForValue(modConfig, valueSpec, path);
			if (tuple != null) {
				this.addEntry(new ConfigValueEntry(tuple.getLeft(), tuple.getMiddle(), tuple.getRight(), path));
				return;
			}
			LOGGER.warn("Unable to make widget for valueSpec! Path: {}, Value: {}, Class: {}", path, valueSpec, valueSpec.getClazz());
			return;
		} else if (configValue instanceof Config) {
			Config config = (Config) configValue;
			this.addEntry(new ModConfigEntry(I18n.format("fml.configgui.category", path.replace(".", " > "))));
//			this.addEntry(new ModConfigCommentEntry(I18n.format("fml.configgui.category.comment", config.getComment()));
			config.valueMap().forEach((k, v) -> addEntries(modConfig, k, v, parentName + configKey + "."));
			return;
		}
		this.addEntry(new ModConfigEntry(TextFormatting.RED + String.format("Invalid config value %s for %s!", configValue == null ? "null" : configValue.getClass().getSimpleName(), path)));
		LOGGER.warn("Invalid config value for configValue! Path: {}, Value: {}", path, configValue);
	}

	private void unfocus(INestedGuiEventHandler o) {
		for (final IGuiEventListener child : o.children()) {
			if (child instanceof INestedGuiEventHandler)
				unfocus((INestedGuiEventHandler) child);
			else if (child instanceof Widget) {
//				((Widget) child).setFocused(false);
				if (child instanceof TextFieldWidget)
					((TextFieldWidget) child).setFocused2(false);
			}
		}
	}

	/**
	 * @return True if in singleplayer and not open to LAN
	 */
	private boolean canPlayerEditServerConfig() {
		if (minecraft.getIntegratedServer() == null)
			return false;
		if (!minecraft.isSingleplayer())
			return false;
		if (minecraft.getIntegratedServer().getPublic())
			return false;
		return true;
	}

	public void resetAllToDefault() {
		children().forEach(entry -> {
			if (entry instanceof ConfigValueEntry)
				((ConfigValueEntry) entry).resetToDefault();
		});
	}

	public void undoAllChanges() {
		children().forEach(entry -> {
			if (entry instanceof ConfigValueEntry)
				((ConfigValueEntry) entry).undoChanges();
		});
	}

	/**
	 * Called from the ConfigScreen's tick method (which is called from the main game loop) to update the list.
	 */
	public void tick() {
		children().forEach(entry -> entry.children().forEach(widget -> {
			if (widget instanceof TextFieldWidget)
				((TextFieldWidget) widget).tick();
		}));
	}

	public boolean anyRequireMcRestart() {
		for (Entry entry : children()) {
			if (entry instanceof ConfigValueEntry) {
				final ConfigEntry configEntry = ((ConfigValueEntry) entry).configEntry;
				if (configEntry.isChanged() && configEntry.requiresMcRestart())
					return true;
			}
		}
		return false;
	}

	public boolean anyRequireWorldRestart() {
		for (Entry entry : children()) {
			if (entry instanceof ConfigValueEntry) {
				final ConfigEntry configEntry = ((ConfigValueEntry) entry).configEntry;
				if (configEntry.isChanged() && configEntry.requiresWorldRestart())
					return true;
			}
		}
		return false;
	}

	public abstract static class Entry extends AbstractOptionList.Entry<ConfigEntryList.Entry> {

	}

	public static class ConfigTextFieldWidget<T> extends TextFieldWidget {

		final Function<String, T> convertT;
		final Function<T, String> convertS;
		private final ConfigEntry<T> configEntry;

		public ConfigTextFieldWidget(final ValueSpec valueSpec, final ConfigEntry<T> configEntry, Function<String, T> convertT, Function<T, String> convertS) {
			this(Minecraft.getInstance().fontRenderer, valueSpec, configEntry, convertT, convertS);
		}

		public ConfigTextFieldWidget(final FontRenderer font, final ValueSpec valueSpec, final ConfigEntry<T> configEntry, Function<String, T> convertT, Function<T, String> convertS) {
			super(font, 0, 0, 100, 20, configEntry.getLabel());
			this.configEntry = configEntry;
			this.convertT = convertT;
			this.convertS = convertS;
			convertAndSet(configEntry.getCurrentValue());
			this.setValidator(s -> {
				try {
					T t = this.convertT.apply(s);
					return valueSpec.test(t);
				} catch (Exception e) {
					return false;
				}
			});
		}

		private void save() {
			final String newValue = this.getText();
			final T setValue = convertT.apply(newValue);
			configEntry.setCurrentValue(setValue);
			configEntry.saveAndLoad();
			final T loadValue = configEntry.getCurrentValue();
			convertAndSet(loadValue);
		}

		public void convertAndSet(final T loadValue) {
			this.setText(convertS.apply(loadValue));
		}

		@Override
		public boolean keyPressed(final int keyCode, final int scanCode, final int modifiers) {
			boolean b = super.keyPressed(keyCode, scanCode, modifiers);
			if (keyCode == GLFW.GLFW_KEY_ENTER)
				this.save();
			return b;
		}

		@Override
		protected void onFocusedChanged(final boolean lostFocus) {
			final boolean oldFocused = this.isFocused();
			super.onFocusedChanged(lostFocus);
			if (oldFocused && lostFocus)
				this.save();
		}

		@Override
		public void render(final int mouseX, final int mouseY, final float partialTicks) {
			// Hacks because textboxes r weird. See ConfigValueEntry#render
			this.x += 2;
			this.y += 2;
			this.width -= 4;
			this.height -= 4;
			super.render(mouseX, mouseY, partialTicks);
		}

		@Override
		public void setFocused(final boolean newValue) {
			final boolean oldFocused = this.isFocused();
			super.setFocused(newValue);
			if (oldFocused && !newValue) this.save();
		}

	}

	public class ConfigValueEntry extends ConfigEntryList.Entry {

		private final Widget widget;
		private final String tooltip;
		private final Consumer widgetSetter;
		private final int depth;
		private final ConfigEntry configEntry;
		private final Widget undoChangesButton;
		private final Widget resetToDefaultButton;
		private int startX;
		private int startY;
		private int endX;
		private int endY;

		public ConfigValueEntry(final Widget widget, final ConfigEntry<?> configEntry, final Consumer<?> widgetSetter, final String path) {
			this.widget = widget;
			this.configEntry = configEntry;
			this.tooltip = configEntry.getComment();
			this.widgetSetter = widgetSetter;
			this.depth = (int) path.chars().filter(ch -> ch == '.').count();
			this.undoChangesButton = new GuiUnicodeGlyphButton(0, 0, 20, 20, "", UNDO_CHAR, 1, b -> ConfigValueEntry.this.undoChanges());
			this.resetToDefaultButton = new GuiUnicodeGlyphButton(0, 0, 20, 20, "", RESET_CHAR, 1, b -> ConfigValueEntry.this.resetToDefault());
		}

		public void render(int index, int startY, int startX, int width, int height, int mouseX, int mouseY, boolean isHovered, float partialTicks) {
			this.startX = startX;
			this.startY = startY;
			this.endX = startX + width;
			this.endY = startY + height;

			FontRenderer font = Minecraft.getInstance().fontRenderer;
			final String depthText = "> ";
			final int depthWidth = font.getStringWidth(depthText);
			for (int i = 0; i < depth; i++)
				font.drawStringWithShadow(depthText, startX + i * depthWidth, startY + (height - 8) / 2F, ModConfigEntry.COLOR);
			int padding = depth * depthWidth;
			startX += padding;
			width -= padding;

			String title = configEntry.getLabel() + " ";
			int ellipsisWidth = font.getStringWidth("...");
			int titleWidth = font.getStringWidth(title);
			int minWidgetWidth = 50;
			int maxWidth = width - height * 2 - minWidgetWidth;
			if (titleWidth > maxWidth && titleWidth > ellipsisWidth) {
				title = font.trimStringToWidth(title, maxWidth - ellipsisWidth).trim() + "...";
				titleWidth = font.getStringWidth(title);
			}
			font.drawStringWithShadow(title, startX, startY + (height - 8) / 2F, 0xFFFFFF);
			startX += titleWidth;
			width -= titleWidth;

			width -= height * 2;

			widget.x = startX;
			widget.y = startY;
			widget.setWidth(width);
			widget.setHeight(height);

			widget.render(mouseX, mouseY, partialTicks);

			undoChangesButton.active = !configEntry.getCurrentValue().equals(configEntry.getInitialValue());
			undoChangesButton.x = startX + width;
			undoChangesButton.y = startY;
			undoChangesButton.setWidth(height);
			undoChangesButton.setHeight(height);
			undoChangesButton.render(mouseX, mouseY, partialTicks);

			resetToDefaultButton.active = !configEntry.getCurrentValue().equals(configEntry.getDefaultValue());
			resetToDefaultButton.x = startX + width + height;
			resetToDefaultButton.y = startY;
			resetToDefaultButton.setWidth(height);
			resetToDefaultButton.setHeight(height);
			resetToDefaultButton.render(mouseX, mouseY, partialTicks);
		}

		public List<? extends IGuiEventListener> children() {
			return Lists.newArrayList(widget, resetToDefaultButton, undoChangesButton);
		}

		public void resetToDefault() {
			configEntry.resetToDefault();
			configEntry.saveAndLoad();
			widgetSetter.accept(configEntry.getCurrentValue());
		}

		public void undoChanges() {
			configEntry.undoChanges();
			configEntry.saveAndLoad();
			widgetSetter.accept(configEntry.getCurrentValue());
		}

//		public boolean changeFocus(boolean p_changeFocus_1_) {
//			return widget.changeFocus(p_changeFocus_1_);
//		}

	}

}
