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
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.client.MainWindow;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.fml.client.config.entry.ConfigListEntry;
import net.minecraftforge.fml.client.config.entry.ListConfigListEntry;
import net.minecraftforge.fml.client.config.entry.widget.IConfigListEntryWidget;
import net.minecraftforge.fml.client.config.entry.widget.InfoText;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

/**
 * @author Cadiboo
 */
public class ListConfigScreen<T extends List<?>> extends ConfigScreen {

	private final IConfigListEntryWidget.Callback<T> callback;

	// Original is used as the value for resetToDefault
	private final T original;
	// Clone is used for everything else
	private final T clone;
	private final boolean isFixedSize;
	@Nullable
	private Object addObj;

	public ListConfigScreen(final ConfigScreen owningScreen, final IConfigListEntryWidget.Callback<T> callback) {
		super(owningScreen.getTitle(), owningScreen, owningScreen.modContainer);
		this.callback = callback;
		this.original = callback.get();
		this.isFixedSize = isFixedSize(original);
		if (this.original instanceof ArrayList)
			this.clone = (T) ((ArrayList<?>) original).clone();
//		else if (original.getClass().getName().equals("java.util.Arrays$ArrayList"))
//			this.clone = (T) Arrays.asList(original.toArray());
		else
			this.clone = (T) Lists.newArrayList(original);
		this.callback.set(clone);
		this.addObj = getInsertValue();

	}

	public static boolean isFixedSize(@Nonnull final List<?> list) {
//		return list.getClass().getName().equals("java.util.Arrays$ArrayList");
		return false;
	}

	public boolean isFixedSize() {
		return isFixedSize;
	}

	public Object getInsertValue() {
		if (isFixedSize)
			return addObj = null;

		if (addObj != null)
			return addObj;

		if (!clone.isEmpty())
			addObj = clone.get(0);
		else if (!original.isEmpty())
			addObj = original.get(0);
		else {
			final T aDefault = callback.getDefault();
			if (!aDefault.isEmpty())
				addObj = aDefault.get(0);
		}
		return addObj;
	}

	@Override
	public void init() {
		super.init();

		final Object2IntMap<Object> indices = new Object2IntOpenHashMap<>();

		final T list = callback.get();

		final ArrayList<? extends Widget> widgets = makeWidgets(list, this, o -> callback.isValid());
		for (int i = 0; i < widgets.size(); i++) {
			final Widget widget = widgets.get(i);
			indices.put(widget, i);
		}
		if (!isFixedSize(list) && widgets.isEmpty()) {
			final Widget w = new InfoText<>(I18n.format("fml.configgui.noElements"));
			this.getEntryList().children().add(new ListConfigListEntry(this, w, false, true, false) {
				public void removeEntry() {
				}

				@Override
				public void addEntryBelow() {
					if (isFixedSize())
						return;
					Object addObj = getInsertValue();
					if (addObj == null)
						return;
					try {
						((List) clone).add(addObj);
					} catch (Exception e) {
						e.printStackTrace();
					}
					// Don't need to worry about updating the indices, we refresh the screen
					shouldRefreshEntryList = true;
					final Minecraft minecraft = Minecraft.getInstance();
					final MainWindow mainWindow = minecraft.func_228018_at_();
					init(minecraft, mainWindow.getScaledWidth(), mainWindow.getScaledHeight());
				}
			});
			return;
		}
		final List<ConfigListEntry<?>> configListEntries = this.getEntryList().children();
		widgets.forEach(w -> {
			final boolean isModifiable = !isFixedSize();
			configListEntries.add(
					new ListConfigListEntry(this, w, true, isModifiable, isModifiable) {
						@Override
						public void removeEntry() {
							if (!canModify())
								return;
							try {
								clone.remove(indices.getInt(this.getWidget()));
							} catch (Exception e) {
								e.printStackTrace();
							}
							// Don't need to worry about updating the indices, we refresh the screen
							shouldRefreshEntryList = true;
							final Minecraft minecraft = Minecraft.getInstance();
							final MainWindow mainWindow = minecraft.func_228018_at_();
							init(minecraft, mainWindow.getScaledWidth(), mainWindow.getScaledHeight());
						}

						@Override
						public void addEntryBelow() {
							if (!canModify())
								return;
							Object addObj = getInsertValue();
							if (addObj == null)
								return;
							try {
								((List) clone).add(indices.getInt(getWidget()) + 1, addObj);
							} catch (Exception e) {
								e.printStackTrace();
							}
							// Don't need to worry about updating the indices, we refresh the screen
							shouldRefreshEntryList = true;
							final Minecraft minecraft = Minecraft.getInstance();
							final MainWindow mainWindow = minecraft.func_228018_at_();
							init(minecraft, mainWindow.getScaledWidth(), mainWindow.getScaledHeight());
						}

						private boolean canModify() {
							if (isFixedSize())
								return false;
							// DummyConfigElement (Unsupported Object) has null
							return ((IConfigListEntryWidget<?>) this.getWidget()).getCallback() != null;
						}
					}
			);
		});
	}

	protected <W extends Widget & IConfigListEntryWidget<Object>> ArrayList<W> makeWidgets(final List<?> list, final ConfigScreen configScreen, final Predicate<Object> elementValidator) {
		final ArrayList<W> widgets = new ArrayList<>();
		list.forEach(obj -> widgets.add(ConfigTypesManager.makeWidget(list, configScreen, elementValidator, obj)));
		ConfigTypesManager.sortWidgets(widgets);
		return widgets;
	}

}
