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

package net.minecraftforge.fml.client.config.entry;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.common.ForgeConfigSpec.ValueSpec;
import net.minecraftforge.fml.config.ModConfig;

/**
 * A config value that can be saved and loaded.
 * A wrapper around a ValueSpec + ModConfig + path
 *
 * @author Cadiboo
 */
public class ConfigValue<T> {

	private final T initialValue;
	private final T defaultValue;
	private final ModConfig modConfig;
	private final String path;
	private final ValueSpec valueSpec;
	private final String label;
	private final String comment;
	private final boolean requiresMcRestart;
	private final boolean requiresWorldRestart;
	private T currentValue;
	private boolean isDirty;

	public ConfigValue(final T initialValue, final T defaultValue, final ModConfig modConfig, final String path, final ValueSpec valueSpec) {
		this.initialValue = initialValue;
		this.defaultValue = defaultValue;
		this.modConfig = modConfig;
		this.path = path;
		this.valueSpec = valueSpec;
		this.label = I18n.format(valueSpec.getTranslationKey());
		this.comment = valueSpec.getComment();
		this.requiresMcRestart = false; // TODO: requiresMcRestart appears not to exist anymore?
		this.requiresWorldRestart = valueSpec.needsWorldRestart();
		this.currentValue = initialValue;
		this.isDirty = false;
	}

	/**
	 * Factory method
	 */
	public static <T> ConfigValue<T> of(final Class<T> clazz, final ModConfig modConfig, final String path, final ValueSpec valueSpec) {
		if (Enum.class.isAssignableFrom(valueSpec.getClazz()))
			return (ConfigValue<T>) ofEnum(modConfig, path, valueSpec);
		final CommentedFileConfig configData = (CommentedFileConfig) modConfig.getConfigData();
		final T initialValue = ensureCorrectClass(configData.get(path), clazz);
		final T defaultValue = (T) valueSpec.getDefault();
		return new ConfigValue<>(initialValue, defaultValue, modConfig, path, valueSpec);
	}

	// TODO: is this needed?

	/**
	 * Factory method for enums
	 */
	public static ConfigValue<Enum> ofEnum(final ModConfig modConfig, final String path, final ValueSpec valueSpec) {
		final CommentedFileConfig configData = (CommentedFileConfig) modConfig.getConfigData();
		final Enum initialValue = ensureCorrectClass(configData.get(path), valueSpec.getClazz());
		final Enum defaultValue = (Enum) valueSpec.getDefault();
		return new ConfigValue<>(initialValue, defaultValue, modConfig, path, valueSpec);
	}

	/**
	 * NightConfig's TOML writer stores some values with different types so when they are read and
	 * converted back to their expected types a class cast exception occurs.
	 * Examples:
	 * Floats are stored as Doubles
	 * Enums are stored as Strings by their name {@link Enum#name()}
	 *
	 * @param value The value to ensure correctness of
	 * @param clazz The class of the value (Should be assignable from T)
	 * @param <T>   The expected type of the input value and the type of the return value
	 * @return The corrected value
	 * @see "https://github.com/TheElectronWill/night-config/blob/master/toml/src/main/java/com/electronwill/nightconfig/toml/ValueWriter.java#L23-L62"
	 */
	public static <T> T ensureCorrectClass(final T value, final Class<?> clazz) {
		final Class<?> valueClass = value.getClass();
		if (valueClass.isAssignableFrom(clazz))
			return value;
		// TOML stores Floats as Doubles
		if (clazz == Float.class && valueClass == Double.class)
			return (T) Float.valueOf(((Double) value).floatValue());
		// TOML stores Enums as Strings
		if (Enum.class.isAssignableFrom(clazz))
			return (T) Enum.valueOf((Class<? extends Enum>) clazz, (String) value);
		// throw new IllegalStateException("Value is not assignable from class: " + value + ", " + valueClass + ", " + clazz);
		return value;
	}

	/**
	 * Saves a value at the specified path on the specified ModConfig
	 * then loads the value from the path and fires the ModConfig.ConfigReloading event
	 * then loads the value from the path and returns it.
	 *
	 * @param modConfig The ModConfig to perform the action on
	 * @param path      The path at which to perform the action on the ModConfig
	 * @param setValue  The value to set
	 * @param <T>       The generic type of the set value (and the expected generic type of the loaded value)
	 * @return The RAW loaded value (Should be passed through {@link #ensureCorrectClass(Object, Class)} before typed usage)
	 */
	public static <T> T saveAndLoad(final ModConfig modConfig, final String path, final T setValue) {
		final CommentedFileConfig configData = (CommentedFileConfig) modConfig.getConfigData();
		configData.set(path, setValue);
		modConfig.save();
		configData.load();
		modConfig.fireEvent(new ModConfig.ConfigReloading(modConfig));
		return configData.get(path);
	}

	/**
	 * @return The value of this ConfigValue before any changes have been made to it by the user.
	 */
	public T getInitialValue() {
		return initialValue;
	}

	/**
	 * @return The default value of this ConfigValue as set by its {@link #getValueSpec() ValueSpec}.
	 */
	public T getDefaultValue() {
		return defaultValue;
	}

	/**
	 * @return The {@link ModConfig} for this ConfigValue.
	 */
	public ModConfig getModConfig() {
		return modConfig;
	}

	/**
	 * @return The path of this config value inside its {@link #getModConfig() ModConfig}.
	 */
	public String getPath() {
		return path;
	}

	/**
	 * @return The {@link ValueSpec} for this ConfigValue.
	 */
	public ValueSpec getValueSpec() {
		return valueSpec;
	}

	/**
	 * @return The result of formatting this ValueSpec's translation key.
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * @return The comment for this config value.
	 */
	public String getComment() {
		return comment;
	}

	/**
	 * TODO: requiresMcRestart appears not to exist anymore?
	 *
	 * @return If this config value requires Minecraft to be restarted when it is changed.
	 */
	public boolean requiresMcRestart() {
		return requiresMcRestart;
	}

	/**
	 * For Categories return false if ANY properties in the category are modifiable
	 * while a world is running, true if all are not.
	 *
	 * @return Whether or not this config value is safe to modify while a world is running.
	 */
	public boolean requiresWorldRestart() {
		return requiresWorldRestart;
	}

	public T getCurrentValue() {
		return currentValue;
	}

	/**
	 * Sets this config value's current value to the (corrected) new value and marks
	 * this config value as needing to be saved.
	 *
	 * @param newValue The new value
	 */
	public void setCurrentValue(final T newValue) {
		this.currentValue = ensureCorrectClass(newValue, getDefaultValue().getClass());
		this.isDirty = true;
	}

	/**
	 * @return If this config value has been changed but not saved.
	 */
	public boolean isDirty() {
		return isDirty;
	}

	/**
	 * Sets this config value's current value to its initial value.
	 */
	public void undoChanges() {
		setCurrentValue(getInitialValue());
	}

	/**
	 * @return If this config value's current value is NOT equal to its initial value.
	 */
	public boolean isChanged() {
		return !getCurrentValue().equals(getInitialValue());
	}

	/**
	 * Sets this config value's current value to its default value.
	 */
	public void resetToDefault() {
		setCurrentValue(getDefaultValue());
	}

	/**
	 * @return If this config value's current value is equal to its default value.
	 */
	public boolean isDefault() {
		return getCurrentValue().equals(getDefaultValue());
	}

	/**
	 * Saves and loads the value if it has changed and has not yet been saved.
	 */
	public void saveAndLoad() {
		if (!isDirty())
			return;
		this.setCurrentValue(saveAndLoad(modConfig, getPath(), this.getCurrentValue()));
		this.isDirty = false;
	}

}
