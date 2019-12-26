package net.minecraftforge.fml.client.config.entry;

import net.minecraft.client.gui.INestedGuiEventHandler;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.Widget;
import net.minecraftforge.fml.client.config.ConfigEntryListWidget;
import net.minecraftforge.fml.client.config.entry2.widget.ConfigListEntryWidget;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Provides an interface for defining {@link ConfigEntryListWidget} entry objects.
 *
 * @author Cadiboo
 */
public interface IConfigListEntry2<T> extends INestedGuiEventHandler {

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
	 * Returns null for list widgets
	 *
	 * @return The result of formatting the translation key.
	 */
	@Nullable
	default String getLabel() {
		return getWidget().getLabel();
	}

	/**
	 * Returns null for widgets that are part of a List/Array
	 *
	 * @return The result of formatting the translation key.
	 */
	@Nullable
	default String getTranslationKey() {
		return getWidget().getTranslationKey();
	}

	/**
	 * Returns null for widgets that are part of a List/Array
	 *
	 * @return The result of formatting the translation key.
	 */
	@Nullable
	default String getComment() {
		return getWidget().getComment();
	}

	@Nonnull
	default T getDefault() {
		return getWidget().getDefault();
	}

	/**
	 * Is this value equal to the default value?
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
	 * Is this value different from the initial value?
	 */
	default boolean isChanged() {
		return getWidget().isChanged();
	}

	/**
	 * Sets this value to the default value.
	 */
	default void undoChanges() {
		getWidget().undoChanges();
	}

	/**
	 * Handles saving any changes that have been made to this entry back to the underlying object.
	 * It is a good practice to check isChanged() before performing the save action.
	 */
	default void save() {
		getWidget().save();
	}

	/**
	 * Whether or not this element is safe to modify while a world is running.
	 * For Categories return false if ANY properties in the category are modifiable
	 * while a world is running, true if all are not.
	 */
	default boolean requiresWorldRestart() {
		return false;
	}

	/**
	 * Whether or not this element requires Minecraft to be restarted when changed.
	 */
	default boolean requiresGameRestart() {
		return false;
	}

	/**
	 * This method is called when the parent GUI is closed.
	 * Most handlers won't need this; it is provided for special cases.
	 */
	default void onGuiClosed() {
	}

	/**
	 * If the default value should be added to the tooltip.
	 * Categories and Lists return false.
	 */
	default boolean displayDefaultValue() {
		return true;
	}

	default boolean isCategory() {
		return false;
	}

	<W extends Widget & ConfigListEntryWidget<T>> W getWidget();

}
