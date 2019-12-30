package net.minecraftforge.fml.client.config.entry;

import net.minecraft.client.gui.INestedGuiEventHandler;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.Widget;
import net.minecraftforge.fml.client.config.ConfigEntryListWidget;
import net.minecraftforge.fml.client.config.entry.widget.IConfigListEntryWidget;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Provides an interface for defining {@link ConfigEntryListWidget} entry objects.
 *
 * @author Cadiboo
 */
public interface IConfigListEntry<T> extends INestedGuiEventHandler {

	/**
	 * Call {@link TextFieldWidget#tick()} for any TextFieldWidget objects in this entry.
	 */
	void tick();

	/**
	 * Handles drawing any tooltips that apply to this entry.
	 * Only called for visible entries.
	 */
	void renderToolTip(int mouseX, int mouseY, float partialTicks);

	/**
	 * @return The default value
	 */
	@Nullable
	default T getDefault() {
		return getWidget().getDefault();
	}

	/**
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
	 * @return If this value different from the initial value
	 */
	default boolean isChanged() {
		return getWidget().isChanged();
	}

	/**
	 * Sets this value to the initial value.
	 */
	default void undoChanges() {
		getWidget().undoChanges();
	}

	/**
	 * Handles saving any changes that have been made to this entry back to the underlying object.
	 * It is a good practice to check {@link #isChanged()} before performing the save action.
	 */
	default void save() {
		getWidget().save();
	}

	/**
	 * This method is called when the parent GUI is closed.
	 * Most handlers won't need this; it is provided for special cases.
	 */
	default void onGuiClosed() {
	}

	/**
	 * If the default value should be added to the tooltip.
	 * Categories ({@link #isCategory()}) and Lists return false.
	 */
	default boolean displayDefaultValue() {
		return true;
	}

	/**
	 * @return If this is backed by a ModConfig or Category
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
