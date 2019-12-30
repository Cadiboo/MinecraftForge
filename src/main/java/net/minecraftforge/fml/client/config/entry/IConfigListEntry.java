package net.minecraftforge.fml.client.config.entry;

import net.minecraft.client.gui.INestedGuiEventHandler;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.gui.widget.list.AbstractList;
import net.minecraftforge.fml.client.config.ConfigEntryListWidget;
import net.minecraftforge.fml.client.config.element.category.CategoryElement;
import net.minecraftforge.fml.client.config.entry.widget.IConfigListEntryWidget;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Provides an interface for defining {@link ConfigEntryListWidget} entry objects.
 * In 1.12.2 -> 1.14.4 {@link AbstractList} now uses an abstract class ({@link AbstractList.AbstractListEntry})
 * instead of an interface for its entries but this interface still exists for documentation & abstraction.
 * <p>
 * It's also a hacky use of inheritance to delegate everything to the widget.
 *
 * @author Cadiboo
 */
public interface IConfigListEntry<T> extends INestedGuiEventHandler {

	/**
	 * Call {@link TextFieldWidget#tick()} for any TextFieldWidget objects in this entry.
	 */
	void tick();

	/**
	 * Handles rendering any tooltips that apply to this entry.
	 * Only called for visible entries.
	 */
	void renderToolTip(int mouseX, int mouseY, float partialTicks);

	/**
	 * By default delegates to the widget.
	 *
	 * @return The default value
	 */
	@Nullable
	default T getDefault() {
		return getWidget().getDefault();
	}

	/**
	 * By default delegates to the widget.
	 *
	 * @return If this value is equal to the default value
	 */
	default boolean isDefault() {
		return getWidget().isDefault();
	}

	/**
	 * Sets this value to the default value.
	 */
	default void resetToDefault() {
		getWidget().resetToDefault();
	}

	/**
	 * By default delegates to the widget.
	 *
	 * @return If this value different from the initial value
	 */
	default boolean isChanged() {
		return getWidget().isChanged();
	}

	/**
	 * By default delegates to the widget.
	 * Sets this value to the initial value.
	 */
	default void undoChanges() {
		getWidget().undoChanges();
	}

	/**
	 * By default delegates to the widget.
	 * Handles saving any changes that have been made to this entry back to the underlying object.
	 * It is a good practice to check {@link #isChanged()} before performing the save action.
	 */
	default void save() {
		getWidget().save();
	}

	/**
	 * Callback for when the parent ConfigScreen is closed.
	 * Most handlers won't need this; it is provided for special cases.
	 */
	default void onGuiClosed() {
	}

	/**
	 * If the default value should be added to the tooltip.
	 * Categories ({@link #isCategory()}), Lists and Configs return false.
	 */
	default boolean displayDefaultValue() {
		return true;
	}

	/**
	 * @return If this is backed by a Category (ModConfig or Config)
	 * @see CategoryElement
	 */
	default boolean isCategory() {
		return false;
	}

	/**
	 * @param <W> The type of the widget
	 * @return The widget
	 */
	@Nonnull
	<W extends Widget & IConfigListEntryWidget<T>> W getWidget();

}
