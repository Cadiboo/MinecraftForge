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
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MainWindow;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.gui.widget.list.ExtendedList;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.ConfigValue;
import net.minecraftforge.common.ForgeConfigSpec.ValueSpec;
import net.minecraftforge.fml.client.config.entry.BooleanConfigValueElement;
import net.minecraftforge.fml.client.config.entry.ByteConfigValueElement;
import net.minecraftforge.fml.client.config.entry.CategoryEntry;
import net.minecraftforge.fml.client.config.entry.ConfigListEntry;
import net.minecraftforge.fml.client.config.entry.DoubleConfigValueElement;
import net.minecraftforge.fml.client.config.entry.DummyConfigValueElement;
import net.minecraftforge.fml.client.config.entry.FloatConfigValueElement;
import net.minecraftforge.fml.client.config.entry.IConfigScreenListEntry;
import net.minecraftforge.fml.client.config.entry.IntegerConfigValueElement;
import net.minecraftforge.fml.client.config.entry.LongConfigValueElement;
import net.minecraftforge.fml.client.config.entry.ModConfigEntry;
import net.minecraftforge.fml.client.config.entry.StringConfigValueElement;
import net.minecraftforge.fml.client.config.entry.TemporalConfigValueElement;
import net.minecraftforge.fml.config.ConfigTracker;
import net.minecraftforge.fml.config.ModConfig;
import org.lwjgl.opengl.GL11;

import java.time.temporal.Temporal;
import java.util.Map;
import java.util.Optional;

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
	/**
	 * The longest size a label of a config element can be before it is trimmed with an ellipsis.
	 * If it is trimmed, the new text (including the ellipsis) will always be equal to this value.
	 */
	public static final int MAX_LABEL_WIDTH = 200;

	public final ConfigScreen owningScreen;
	/**
	 * The size of the largest label of all the config elements on this screen.
	 * Always smaller than or equal to {@link #MAX_LABEL_WIDTH};
	 * Used for rendering all widgets at the same x position
	 */
	private int longestLabelWidth;

	public ConfigEntryListWidget(final Minecraft mcIn, final ConfigScreen owningScreen) {
		super(mcIn, owningScreen.width, owningScreen.height, owningScreen.getHeaderSize(), owningScreen.height - owningScreen.getFooterSize(), 20);
		this.owningScreen = owningScreen;
	}

	public int getLongestLabelWidth0() {
		return longestLabelWidth;
	}

	// +2 for nice spacing
	public int getLongestLabelWidth() {
		return getLongestLabelWidth0() + 2;
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
		compute(ModConfig.Type.COMMON);

		this.children().forEach(configListEntry -> {
			final int labelWidth = configListEntry.getLabelWidth();
			if (longestLabelWidth < labelWidth)
				longestLabelWidth = labelWidth;
		});
	}

	// TODO: move to ConfigScreen? Anyway, shouldn't be here
	private Optional<ModConfigEntry> compute(final ModConfig.Type type) {

		if (type == ModConfig.Type.SERVER && !canPlayerEditServerConfig())
			return Optional.empty();

		final ModConfig modConfig = ConfigTracker.INSTANCE.getConfig(owningScreen.modContainer.getModId(), type).orElse(null);
		if (modConfig == null)
			return Optional.empty();

		// name -> ConfigValue|SimpleConfig
		final Map<String, Object> specConfigValues = modConfig.getSpec().getValues().valueMap();
		// name -> ValueSpec|SimpleConfig
		final Map<String, Object> specValueSpecs = modConfig.getSpec().valueMap();
		// name -> Object
		final Map<String, Object> configValues = modConfig.getConfigData().valueMap();

		specConfigValues.forEach((name, obj) -> {
			if (obj instanceof ConfigValue) {
				final ConfigValue<?> configValue = (ConfigValue<?>) obj;
				// Because the obj is a ConfigValue the corresponding object in the ValueSpec map must be a ValueSpec
				final ValueSpec valueSpec = (ValueSpec) specValueSpecs.get(name);

				Class<?> clazz = valueSpec.getClazz();
				if (clazz == Object.class) {
					final Object actualValue = configValue.get();
					final Class<?> valueClass = actualValue.getClass();
					if (valueClass != null)
						clazz = valueClass;
					else {
						final Object defaultValue = valueSpec.getDefault();
						if (defaultValue != null)
							clazz = defaultValue.getClass();
					}
				}
				if (Boolean.class.isAssignableFrom(clazz)) {
					this.children().add(new BooleanConfigValueElement(configValue.getPath(), modConfig, (ConfigValue<Boolean>) configValue)
							.makeConfigEntry(owningScreen, this));
				} else if (Byte.class.isAssignableFrom(clazz)) {
					this.children().add(new ByteConfigValueElement(configValue.getPath(), modConfig, (ConfigValue<Byte>) configValue)
							.makeConfigEntry(owningScreen, this));
				} else if (Integer.class.isAssignableFrom(clazz)) {
					this.children().add(new IntegerConfigValueElement(configValue.getPath(), modConfig, (ConfigValue<Integer>) configValue)
							.makeConfigEntry(owningScreen, this));
				} else if (Float.class.isAssignableFrom(clazz)) {
					this.children().add(new FloatConfigValueElement(configValue.getPath(), modConfig, (ConfigValue<Float>) configValue)
							.makeConfigEntry(owningScreen, this));
				} else if (Long.class.isAssignableFrom(clazz)) {
					this.children().add(new LongConfigValueElement(configValue.getPath(), modConfig, (ConfigValue<Long>) configValue)
							.makeConfigEntry(owningScreen, this));
				} else if (Double.class.isAssignableFrom(clazz)) {
					this.children().add(new DoubleConfigValueElement(configValue.getPath(), modConfig, (ConfigValue<Double>) configValue)
							.makeConfigEntry(owningScreen, this));
				} else if (String.class.isAssignableFrom(clazz)) {
					this.children().add(new StringConfigValueElement(configValue.getPath(), modConfig, (ConfigValue<String>) configValue)
							.makeConfigEntry(owningScreen, this));
//				} else if (Enum.class.isAssignableFrom(clazz)) {
//					this.children().add(new EnumConfigValueElement(configValue.getPath(), modConfig, (ConfigValue<Enum>) configValue)
//							.makeConfigEntry(owningScreen, this));
//				} else if (List.class.isAssignableFrom(clazz)) {
//					this.children().add(new ListConfigValueElement(configValue.getPath(), modConfig, (ConfigValue<List>) configValue)
//							.makeConfigEntry(owningScreen, this));
				} else if (Temporal.class.isAssignableFrom(clazz)) {
					this.children().add(new TemporalConfigValueElement(configValue.getPath(), modConfig, (ConfigValue<Temporal>) configValue)
							.makeConfigEntry(owningScreen, this));
				} else {
					this.children().add(new DummyConfigValueElement(name)
							.makeConfigEntry(owningScreen, this));
				}
			} else if (obj instanceof Config) {
				final Config config = (Config) obj;
				// Because the obj is a Config the corresponding object in the ValueSpec map must be a Config
				final Config valueSpec = (Config) specValueSpecs.get(name);

				this.children().add(new DummyConfigValueElement("Category: " + name)
						.makeConfigEntry(owningScreen, this));

				valueSpec.valueMap().forEach((name2, obj2) -> {
					// TODO: repeat with these
					final String path = ForgeConfigSpec.DOT_JOINER.join(name, name2);
					final DummyConfigValueElement configValueElement2 = new DummyConfigValueElement(path);
					this.children().add(configValueElement2.makeConfigEntry(owningScreen, this));
				});
			} else {
				throw new IllegalStateException("How? " + name + ", " + obj);
			}
		});
		return Optional.empty();
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

	public int getRowWidth() {
		return getWidth() - PADDING_X * 2;
	}

	@Override
	protected void renderBackground() {
		this.fillGradient(this.getLeft(), this.getTop(), this.getRight(), this.getBottom(), 0xC0101010, 0xD0101010);
	}

	@Override
	public void render(final int p_render_1_, final int p_render_2_, final float p_render_3_) {
		// GLScissors to hide overflow from entryList
		GL11.glEnable(GL11.GL_SCISSOR_TEST);

		final MainWindow mainWindow = minecraft.func_228018_at_();
		double scale = mainWindow.getGuiScaleFactor();
		// Scissors coords are relative to the bottom left of the screen
		int scissorsX = (int) (this.getLeft() * scale);
		int scissorsY = (int) (mainWindow.getFramebufferHeight() - (this.getBottom() * scale));
		int scissorsWidth = (int) (this.getWidth() * scale);
		int scissorsHeight = (int) (this.getHeight() * scale);

		GL11.glScissor(scissorsX, scissorsY, scissorsWidth, scissorsHeight);

//		super.render(p_render_1_, p_render_2_, p_render_3_);
		// super.render but with the background rendering we don't want commented out
		{
			this.renderBackground();
			int i = this.getScrollbarPosition();
			int j = i + 6;
			Tessellator tessellator = Tessellator.getInstance();
			BufferBuilder bufferbuilder = tessellator.getBuffer();
//			this.minecraft.getTextureManager().bindTexture(AbstractGui.BACKGROUND_LOCATION);
//			RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
//			float f = 32.0F;
//			bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
//			bufferbuilder.func_225582_a_((double)this.x0, (double)this.y1, 0.0D).func_225583_a_((float)this.x0 / 32.0F, (float)(this.y1 + (int)this.getScrollAmount()) / 32.0F).func_225586_a_(32, 32, 32, 255).endVertex();
//			bufferbuilder.func_225582_a_((double)this.x1, (double)this.y1, 0.0D).func_225583_a_((float)this.x1 / 32.0F, (float)(this.y1 + (int)this.getScrollAmount()) / 32.0F).func_225586_a_(32, 32, 32, 255).endVertex();
//			bufferbuilder.func_225582_a_((double)this.x1, (double)this.y0, 0.0D).func_225583_a_((float)this.x1 / 32.0F, (float)(this.y0 + (int)this.getScrollAmount()) / 32.0F).func_225586_a_(32, 32, 32, 255).endVertex();
//			bufferbuilder.func_225582_a_((double)this.x0, (double)this.y0, 0.0D).func_225583_a_((float)this.x0 / 32.0F, (float)(this.y0 + (int)this.getScrollAmount()) / 32.0F).func_225586_a_(32, 32, 32, 255).endVertex();
//			tessellator.draw();
			int k = this.getRowLeft();
			int l = this.y0 + 4 - (int) this.getScrollAmount();
			if (this.renderHeader) {
				this.renderHeader(k, l, tessellator);
			}

			this.renderList(k, l, p_render_1_, p_render_2_, p_render_3_);
			RenderSystem.disableDepthTest();
//			this.renderHoleBackground(0, this.y0, 255, 255);
//			this.renderHoleBackground(this.y1, this.height, 255, 255);
			RenderSystem.enableBlend();
			RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ZERO, GlStateManager.DestFactor.ONE);
			RenderSystem.disableAlphaTest();
			RenderSystem.shadeModel(7425);
			RenderSystem.disableTexture();
			int i1 = 4;
			// Top shadow
			bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
			bufferbuilder.func_225582_a_((double) this.x0, (double) (this.y0 + 4), 0.0D).func_225583_a_(0.0F, 1.0F).func_225586_a_(0, 0, 0, 0).endVertex();
			bufferbuilder.func_225582_a_((double) this.x1, (double) (this.y0 + 4), 0.0D).func_225583_a_(1.0F, 1.0F).func_225586_a_(0, 0, 0, 0).endVertex();
			bufferbuilder.func_225582_a_((double) this.x1, (double) this.y0, 0.0D).func_225583_a_(1.0F, 0.0F).func_225586_a_(0, 0, 0, 255).endVertex();
			bufferbuilder.func_225582_a_((double) this.x0, (double) this.y0, 0.0D).func_225583_a_(0.0F, 0.0F).func_225586_a_(0, 0, 0, 255).endVertex();
			tessellator.draw();
			// Bottom shadow
			bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
			bufferbuilder.func_225582_a_((double) this.x0, (double) this.y1, 0.0D).func_225583_a_(0.0F, 1.0F).func_225586_a_(0, 0, 0, 255).endVertex();
			bufferbuilder.func_225582_a_((double) this.x1, (double) this.y1, 0.0D).func_225583_a_(1.0F, 1.0F).func_225586_a_(0, 0, 0, 255).endVertex();
			bufferbuilder.func_225582_a_((double) this.x1, (double) (this.y1 - 4), 0.0D).func_225583_a_(1.0F, 0.0F).func_225586_a_(0, 0, 0, 0).endVertex();
			bufferbuilder.func_225582_a_((double) this.x0, (double) (this.y1 - 4), 0.0D).func_225583_a_(0.0F, 0.0F).func_225586_a_(0, 0, 0, 0).endVertex();
			tessellator.draw();
//			int j1 = this.getMaxScroll(); // private, logic copied
			int j1 = Math.max(0, this.getMaxPosition() - (this.y1 - this.y0 - 4));
			if (j1 > 0) {
				int k1 = (int) ((float) ((this.y1 - this.y0) * (this.y1 - this.y0)) / (float) this.getMaxPosition());
				k1 = MathHelper.clamp(k1, 32, this.y1 - this.y0 - 8);
				int l1 = (int) this.getScrollAmount() * (this.y1 - this.y0 - k1) / j1 + this.y0;
				if (l1 < this.y0) {
					l1 = this.y0;
				}

				bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
				bufferbuilder.func_225582_a_((double) i, (double) this.y1, 0.0D).func_225583_a_(0.0F, 1.0F).func_225586_a_(0, 0, 0, 255).endVertex();
				bufferbuilder.func_225582_a_((double) j, (double) this.y1, 0.0D).func_225583_a_(1.0F, 1.0F).func_225586_a_(0, 0, 0, 255).endVertex();
				bufferbuilder.func_225582_a_((double) j, (double) this.y0, 0.0D).func_225583_a_(1.0F, 0.0F).func_225586_a_(0, 0, 0, 255).endVertex();
				bufferbuilder.func_225582_a_((double) i, (double) this.y0, 0.0D).func_225583_a_(0.0F, 0.0F).func_225586_a_(0, 0, 0, 255).endVertex();
				tessellator.draw();
				bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
				bufferbuilder.func_225582_a_((double) i, (double) (l1 + k1), 0.0D).func_225583_a_(0.0F, 1.0F).func_225586_a_(128, 128, 128, 255).endVertex();
				bufferbuilder.func_225582_a_((double) j, (double) (l1 + k1), 0.0D).func_225583_a_(1.0F, 1.0F).func_225586_a_(128, 128, 128, 255).endVertex();
				bufferbuilder.func_225582_a_((double) j, (double) l1, 0.0D).func_225583_a_(1.0F, 0.0F).func_225586_a_(128, 128, 128, 255).endVertex();
				bufferbuilder.func_225582_a_((double) i, (double) l1, 0.0D).func_225583_a_(0.0F, 0.0F).func_225586_a_(128, 128, 128, 255).endVertex();
				tessellator.draw();
				bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
				bufferbuilder.func_225582_a_((double) i, (double) (l1 + k1 - 1), 0.0D).func_225583_a_(0.0F, 1.0F).func_225586_a_(192, 192, 192, 255).endVertex();
				bufferbuilder.func_225582_a_((double) (j - 1), (double) (l1 + k1 - 1), 0.0D).func_225583_a_(1.0F, 1.0F).func_225586_a_(192, 192, 192, 255).endVertex();
				bufferbuilder.func_225582_a_((double) (j - 1), (double) l1, 0.0D).func_225583_a_(1.0F, 0.0F).func_225586_a_(192, 192, 192, 255).endVertex();
				bufferbuilder.func_225582_a_((double) i, (double) l1, 0.0D).func_225583_a_(0.0F, 0.0F).func_225586_a_(192, 192, 192, 255).endVertex();
				tessellator.draw();
			}

			this.renderDecorations(p_render_1_, p_render_2_);
			RenderSystem.enableTexture();
			RenderSystem.shadeModel(7424);
			RenderSystem.enableAlphaTest();
			RenderSystem.disableBlend();
		}

		GL11.glDisable(GL11.GL_SCISSOR_TEST);
	}

	@Override
	protected int getScrollbarPosition() {
		return getRight() - 6; // 6 = scrollbar width
	}

	@Override
	public boolean mouseClicked(final double p_mouseClicked_1_, final double p_mouseClicked_3_, final int p_mouseClicked_5_) {
		this.children().forEach(configListEntry -> {
			final Widget widget = configListEntry.getWidget();
			if (widget instanceof TextFieldWidget)
				((TextFieldWidget) widget).setFocused2(false);
		});
		return super.mouseClicked(p_mouseClicked_1_, p_mouseClicked_3_, p_mouseClicked_5_);
	}

	@Override
	protected void renderHoleBackground(final int p_renderHoleBackground_1_, final int p_renderHoleBackground_2_, final int p_renderHoleBackground_3_, final int p_renderHoleBackground_4_) {
		// No-op We use GLScissors instead of hiding overflow afterwards by rendering over the top
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
				entry.resetToDefault();
	}

	/**
	 * Checks if any IConfigEntry objects on this screen are changed.
	 *
	 * @param applyToSubcategories If sub-category objects should be checked as well.
	 * @return If any IConfigEntry objects on this screen are changed.
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

	public boolean anyRequireMcRestart() {
		for (IConfigScreenListEntry entry : this.getListEntries())
			if (entry.requiresMcRestart())
				return true;
		return false;
	}

	public boolean anyRequireWorldRestart() {
		for (IConfigScreenListEntry entry : this.getListEntries())
			if (entry.requiresWorldRestart())
				return true;
		return false;
	}

	/**
	 * @param mouseX       The x coordinate of the mouse pointer on the screen
	 * @param mouseY       The y coordinate of the mouse pointer on the screen
	 * @param partialTicks The partial render ticks elapsed
	 */
	public void postRender(final int mouseX, final int mouseY, final float partialTicks) {
		for (int item = 0; item < this.getItemCount(); item++) {
			int itemTop = this.getRowTop(item);
//			int itemBottom = this.getRowBottom(item); // Private, logic copied below
			int itemBottom = this.getRowTop(item) + this.itemHeight;
			if (itemTop <= this.y1 && itemBottom >= this.y0) {
				ConfigListEntry configListEntry = this.getEntry(item);
				configListEntry.renderToolTip(mouseX, mouseY, partialTicks);
			}
		}
	}

	public Iterable<? extends IConfigScreenListEntry> getListEntries() {
		return children();
	}

}
