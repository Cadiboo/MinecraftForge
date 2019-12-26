package net.minecraftforge.fml.client.config.entry2.widget;

import net.minecraft.client.gui.widget.TextFieldWidget;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author Cadiboo
 */
public interface ConfigListEntryWidget<T> {

	WidgetValueReference<T> getWidgetValueReference();

	/**
	 * Call {@link TextFieldWidget#tick()} for any TextFieldWidget objects in this entry.
	 */
	default void tick() {
	}

	/**
	 * Handles drawing any tooltips that apply to this entry.
	 */
	default void renderToolTip(int mouseX, int mouseY, float partialTicks) {
	}

	/**
	 * Returns null for list widgets
	 *
	 * @return The result of formatting the translation key.
	 */
	@Nullable
	default String getLabel() {
		return null;
	}

	/**
	 * Returns null for widgets that are part of a List/Array
	 *
	 * @return The result of formatting the translation key.
	 */
	@Nullable
	default String getTranslationKey() {
		return null;
	}

	/**
	 * Returns null for widgets that are part of a List/Array
	 *
	 * @return The result of formatting the translation key.
	 */
	@Nullable
	default String getComment() {
		return null;
	}

	/**
	 * Gets the default value
	 */
	@Nonnull
	default T getDefault() {
		return getWidgetValueReference().getDefault();
	}

	/**
	 * Is this value equal to the default value?
	 */
	default boolean isDefault() {
		return getWidgetValueReference().isDefault();
	}

	/**
	 * Sets this value to the default value.
	 */
	default void resetToDefault() {
		getWidgetValueReference().resetToDefault();
		updateWidgetValue();
	}

	/**
	 * Is this value different from the initial value?
	 */
	default boolean isChanged() {
		return getWidgetValueReference().isChanged();
	}

	/**
	 * Sets this value to the initial value.
	 */
	default void undoChanges() {
		getWidgetValueReference().undoChanges();
		updateWidgetValue();
	}

	/**
	 * Handles saving any changes that have been made to this entry back to the underlying object.
	 * It is a good practice to check isChanged() before performing the save action.
	 */
	default void save() {
		getWidgetValueReference().save();
	}

	/**
	 * Is this value is a valid value?
	 */
	default boolean isValid() {
		return getWidgetValueReference().isValid() && isWidgetValueValid();
	}

	boolean isWidgetValueValid();

	void updateWidgetValue();

}
