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
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.client.config.element.InfoTextConfigElement;
import net.minecraftforge.fml.client.config.element.IConfigElement;
import net.minecraftforge.fml.client.config.entry.ConfigListEntry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Objects;

/**
 * This class is the base Screen for all config GUI screens.
 * It can be extended by mods to provide the top-level config screen that will be called when
 * the Config button is clicked from the Mods Menu list.
 *
 * @author bspkrs
 * @author Cadiboo
 */
public class ElementConfigScreen extends ConfigScreen {

	private static final Logger LOGGER = LogManager.getLogger();

	/**
	 * A list of elements on this screen.
	 * Re-created when {@link #init()} is called if {@link #needsRefresh} is true.
	 */
	private final List<IConfigElement<?>> configElements;
	/**
	 * TODO: Why does this exist?
	 * The initial list of elements on this screen.
	 * Same as the first value of {@link #configElements}
	 */
	private final List<IConfigElement<?>> initialConfigElements;

	public ElementConfigScreen(final ITextComponent titleIn, final Screen parentScreen, final ModContainer modContainer, @Nonnull final List<IConfigElement<?>> configElements) {
		super(titleIn, parentScreen, modContainer);
		this.configElements = this.initialConfigElements = configElements;
	}

	@Override
	protected void onDoneButtonClicked(final Button button) {
		boolean canClose = true;
		try {
			final ConfigEntryListWidget entryList = this.getEntryList();
			if (entryList != null && entryList.areAnyEntriesChanged(true)) {
				if (parentScreen != null && parentScreen instanceof ElementConfigScreen) {
					// Mark as needing to re-init the entry list.
					// Why? Maybe to allow adding of stuff to the config? IDK
					((ConfigScreen) this.parentScreen).needsRefresh = true;
				} else {
					entryList.save();
					boolean requiresGameRestart = this.anyRequireGameRestart();
					boolean requiresWorldRestart = this.anyRequireWorldRestart();
					if (requiresGameRestart) {
						canClose = false;
						getMinecraft().displayGuiScreen(new GuiMessageDialog(parentScreen, "fml.configgui.gameRestartTitle", new StringTextComponent(I18n.format("fml.configgui.gameRestartRequired")), "fml.configgui.confirmMessage"));
					}
					if (requiresWorldRestart && Minecraft.getInstance().world != null) {
						canClose = false;
						getMinecraft().displayGuiScreen(new GuiMessageDialog(parentScreen, "fml.configgui.worldRestartTitle", new StringTextComponent(I18n.format("fml.configgui.worldRestartRequired")), "fml.configgui.confirmMessage"));
					}
				}
			}
		} catch (Exception e) {
			LOGGER.error("Error performing ConfigScreen action:", e);
		}
		if (canClose)
			this.onClose();
	}

	@Override
	public void init() {
		super.init();
		final ConfigEntryListWidget entryList = Objects.requireNonNull(getEntryList());
		final List<ConfigListEntry<?>> children = entryList.children();
		final List<IConfigElement<?>> configElements = getConfigElements();
		if (configElements.isEmpty()) {
			children.add(new InfoTextConfigElement<>("fml.configgui.noElements")
					.makeConfigListEntry(this));
			return;
		}
		configElements.stream()
				.filter(IConfigElement::showInGui)
				.map(element -> element.makeConfigListEntry(this))
				.map(configListEntry -> Objects.requireNonNull(configListEntry, "ConfigListEntry (Widget)"))
				.forEach(children::add);
	}

	public boolean anyRequireGameRestart() {
		for (IConfigElement<?> entry : this.getConfigElements())
			if (entry.requiresGameRestart())
				return true;
		return false;
	}

	public boolean anyRequireWorldRestart() {
		for (IConfigElement<?> entry : this.getConfigElements())
			if (entry.requiresWorldRestart())
				return true;
		return false;
	}

	public List<IConfigElement<?>> getConfigElements() {
		return configElements;
	}

}
