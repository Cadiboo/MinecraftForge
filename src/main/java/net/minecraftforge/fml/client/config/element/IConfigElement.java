package net.minecraftforge.fml.client.config.element;

import net.minecraftforge.fml.client.config.ConfigEntryListWidget;
import net.minecraftforge.fml.client.config.ConfigScreen;
import net.minecraftforge.fml.client.config.entry2.ConfigListEntry;

/**
 * Not the actual widget thing. Holds the value of the config thing.
 *
 * @author Cadiboo
 */
public interface IConfigElement<T> {

	/**
	 * @return The result of formatting the translation key
	 */
	String getLabel();

	String getTranslationKey();

	String getComment();

	/**
	 * Is this property value equal to the default value?
	 */
	boolean isDefault();

	/**
	 * Gets this property's default value.
	 */
	T getDefault();

	/**
	 * Sets this property's value to the default value.
	 */
	void resetToDefault();

	/**
	 * Has the value of this element changed?
	 *
	 * @return true if changes have been made to this element's value, false otherwise.
	 */
	boolean isChanged();

	/**
	 * Handles reverting any changes that have occurred to this element.
	 */
	void undoChanges();

	/**
	 * Whether or not this element is safe to modify while a world is running.
	 * For Categories return false if ANY properties in the category are modifiable
	 * while a world is running, true if all are not.
	 */
	boolean requiresWorldRestart();

	/**
	 * Whether or not this element should be allowed to show on config GUIs.
	 */
	default boolean showInGui() {
		return true;
	}

	/**
	 * Whether or not this element requires Minecraft to be restarted when changed.
	 */
	boolean requiresGameRestart();

	/**
	 * Gets this value.
	 */
	T get();

	/**
	 * Sets this value.
	 */
	void set(T value);

	void save();

	boolean isValid(T o);

	/**
	 * @return true if this element is going to have a slider attached
	 */
	default boolean hasSlidingControl() {
		return false;
	}

	ConfigListEntry<T> makeWidgetThing(ConfigScreen configScreen, ConfigEntryListWidget configEntryListWidget);

//	ConfigListEntry<T> makeListWidgetThing(ListConfigScreen configScreen, ConfigEntryListWidget configEntryListWidget);

	default boolean isCategory() {
		return false;
	}

}
