package net.minecraftforge.fml.client.config.element;

import net.minecraftforge.common.ForgeConfigSpec.Range;
import net.minecraftforge.fml.client.config.ConfigEntryListWidget;
import net.minecraftforge.fml.client.config.ConfigScreen;
import net.minecraftforge.fml.client.config.entry.ConfigListEntry;
import net.minecraftforge.fml.client.config.entry.ElementConfigListEntry;

import javax.annotation.Nullable;

/**
 * Not the actual widget thing.
 * Holds the value of the config thing.
 * The widget updates this value.
 *
 * @author Cadiboo
 */
public interface IConfigElement<T> {

	/**
	 * @return The result of formatting the translation key
	 */
	String getLabel();

	/**
	 * The translation key for this element is also used as a lookup for the localised comment.
	 * Can return null or empty.
	 *
	 * @return The translation key for this element
	 * @see ElementConfigListEntry#makeTooltip()
	 */
	@Nullable
	String getTranslationKey();

	@Nullable
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

	/**
	 * Handles saving any changes that have been made to this entry back to the underlying object.
	 * It is a good practice to check {@link #isChanged()} before performing the save action.
	 */
	void save();

	/**
	 * @param o The object to check
	 * @return If the object is valid for this element
	 */
	boolean isValid(T o);

	/**
	 * @return If this element is going to have a slider attached (Only for numbers)
	 */
	default boolean hasSlidingControl() {
		return false;
	}

	/**
	 * @return A ConfigListEntry backed by this element
	 */
	ConfigListEntry<T> makeConfigListEntry(ConfigScreen configScreen, ConfigEntryListWidget configEntryListWidget);

	/**
	 * @return If this is backed by a ModConfig or ConfigCategory
	 */
	default boolean isCategory() {
		return false;
	}

	/**
	 * @return The range of this element (Only for numbers)
	 */
	@Nullable
	Range<?> getRange();

}
