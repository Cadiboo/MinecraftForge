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

import com.electronwill.nightconfig.core.CommentedConfig;
import com.electronwill.nightconfig.core.file.FileConfig;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.ConfigValue;
import net.minecraftforge.common.ForgeConfigSpec.ValueSpec;
import net.minecraftforge.fml.config.ModConfig;

import java.util.List;

/**
 * A config value that can be saved and loaded.
 * A wrapper around a path + {@link ValueSpec} + {@link ModConfig}
 * Pipes get and save through {@link ConfigValue}
 *
 * @author Cadiboo
 */
public class EntryConfigValue<T> {

	private final T initialValue;
	private final ModConfig modConfig;
	private final ValueSpec valueSpec;
	private final ConfigValue<T> configValue;
	private final String label;

	private T currentValue;
	private boolean isDirty;

	public EntryConfigValue(final List<String> path, final ModConfig modConfig, final ValueSpec valueSpec, final ConfigValue<T> configValue) {
		this.modConfig = modConfig;
		this.valueSpec = valueSpec;
		this.configValue = configValue;
		this.initialValue = this.configValue.get();
		this.label = I18n.format(valueSpec.getTranslationKey());

		this.currentValue = this.initialValue;
		this.isDirty = false;
	}

	public EntryConfigValue(final String path, final ModConfig modConfig, final ValueSpec valueSpec, final ConfigValue<T> configValue) {
		this(ForgeConfigSpec.split(path), modConfig, valueSpec, configValue);
	}

	/**
	 * @return The value of this entry before any changes were been made to it by the user.
	 */
	public T getInitialValue() {
		return initialValue;
	}

	/**
	 * @return The {@link ModConfig}.
	 */
	public ModConfig getModConfig() {
		return modConfig;
	}

	/**
	 * @return The {@link ValueSpec}.
	 */
	public ValueSpec getValueSpec() {
		return valueSpec;
	}

	/**
	 * @return The {@link ConfigValue}.
	 */
	public ConfigValue<T> getConfigValue() {
		return configValue;
	}

	/**
	 * @return The result of formatting this ValueSpec's translation key.
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * @return The current value of this entry.
	 */
	public T getCurrentValue() {
		return currentValue;
	}

	/**
	 * Sets this config value's current value to the new value and marks
	 * this config value as needing to be saved.
	 *
	 * @param newValue The new value
	 */
	public void setCurrentValue(final T newValue) {
		this.currentValue = newValue;
		this.isDirty = true;
	}

	/**
	 * @return The default value of this entry.
	 */
	public T getDefaultValue() {
		return (T) getValueSpec().getDefault();
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

		final ConfigValue<T> configValue = getConfigValue();
		final ModConfig modConfig = getModConfig();
		final CommentedConfig configData = modConfig.getConfigData();

		configValue.set(this.getCurrentValue());
		configValue.save();
		if (configData instanceof FileConfig) // load the data if its from a file
			((FileConfig) configData).load();
		modConfig.fireEvent(new ModConfig.ConfigReloading(modConfig));
		this.setCurrentValue(configValue.get());

		this.isDirty = false;
	}

	/**
	 * TODO: requiresMcRestart appears not to exist anymore?
	 *
	 * @return If this config value requires Minecraft to be restarted when it is changed.
	 */
	public boolean requiresMcRestart() {
		return false;
	}

	/**
	 * For Categories return false if ANY properties in the category are modifiable
	 * while a world is running, true if all are not.
	 *
	 * @return Whether or not this config value is safe to modify while a world is running.
	 */
	public boolean requiresWorldRestart() {
		return getValueSpec().needsWorldRestart();
	}

	/**
	 * @return The comment for this config value.
	 */
	public String getComment() {
		return getValueSpec().getComment();
	}

}
