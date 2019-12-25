package net.minecraftforge.fml.client.config.entry;

import net.minecraft.client.gui.INestedGuiEventHandler;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraftforge.fml.client.config.ConfigEntryListWidget;
import net.minecraftforge.fml.client.config.element.IConfigElement;

/**
 * Provides an interface for defining {@link ConfigEntryListWidget} entry objects.
 *
 * @author Cadiboo
 */
public interface IConfigListEntry<T> extends INestedGuiEventHandler {

	/**
	 * Gets the IConfigElement object owned by this entry.
	 */
	IConfigElement<T> getConfigElement();

	/**
	 * Call {@link TextFieldWidget#tick()} for any TextFieldWidget objects in this entry.
	 */
	void tick();

	/**
	 * Handles drawing any tooltips that apply to this entry.
	 * TODO: \/
	 * This method is called after all other GUI elements have been drawn to the screen,
	 * so it could also be used to draw any GUI element that needs to be drawn after
	 * all entries have had drawEntry() called.
	 */
	void renderToolTip(int mouseX, int mouseY, float partialTicks);

	/**
	 * @return The result of formatting the translation key
	 */
	default String getLabel() {
		return getConfigElement().getLabel();
	}

	default String getTranslationKey() {
		return getConfigElement().getTranslationKey();
	}

	default String getComment() {
		return getConfigElement().getComment();
	}

	/**
	 * Is this property value equal to the default value?
	 */
	default boolean isDefault() {
		return getConfigElement().isDefault();
	}

	/**
	 * Gets this property's default value.
	 */
	default T getDefault() {
		return getConfigElement().getDefault();
	}

	/**
	 * Sets this property's value to the default value.
	 */
	default void resetToDefault() {
		getConfigElement().resetToDefault();
	}

	/**
	 * Is this property value equal to the initial value?
	 */
	default boolean isChanged() {
		return getConfigElement().isChanged();
	}

	/**
	 * Sets this property's value to the default value.
	 */
	default void undoChanges() {
		getConfigElement().undoChanges();
	}

	/**
	 * Handles saving any changes that have been made to this entry back to the underlying object.
	 * It is a good practice to check isChanged() before performing the save action.
	 */
	default void save() {
		getConfigElement().save();
	}

	/**
	 * Whether or not this element is safe to modify while a world is running.
	 * For Categories return false if ANY properties in the category are modifiable
	 * while a world is running, true if all are not.
	 */
	default boolean requiresWorldRestart() {
		return getConfigElement().requiresWorldRestart();
	}

	/**
	 * Whether or not this element requires Minecraft to be restarted when changed.
	 */
	default boolean requiresGameRestart() {
		return getConfigElement().requiresGameRestart();
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
		return getConfigElement().isCategory();
	}

}
