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

package net.minecraftforge.fml.client.config.element;

import com.electronwill.nightconfig.core.CommentedConfig;
import com.electronwill.nightconfig.core.file.FileConfig;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.ConfigValue;
import net.minecraftforge.common.ForgeConfigSpec.Range;
import net.minecraftforge.common.ForgeConfigSpec.ValueSpec;
import net.minecraftforge.fml.config.ModConfig;

import javax.annotation.Nullable;
import java.util.List;

/**
 * A config value that can be saved and loaded.
 * A wrapper around a path + {@link ValueSpec} + {@link ModConfig}.
 * Pipes get and save through {@link ConfigValue}.
 *
 * @author Cadiboo
 */
public class ConfigElementContainer<T> {

	private final T initialValue;
	private final ModConfig modConfig;
	private final ValueSpec valueSpec;
	private final ConfigValue<T> configValue;
	private final String label;

	private T currentValue;
	private boolean isDirty;

	public ConfigElementContainer(final List<String> path, final ModConfig modConfig, final ConfigValue<T> configValue) {
		this.modConfig = modConfig;
		this.valueSpec = modConfig.getSpec().get(path);
		this.configValue = configValue;
		this.initialValue = this.configValue.get();
		this.label = I18n.format(valueSpec.getTranslationKey());

		this.currentValue = this.initialValue;
		this.isDirty = false;
	}

	public ConfigElementContainer(final String path, final ModConfig modConfig, final ConfigValue<T> configValue) {
		this(ForgeConfigSpec.split(path), modConfig, configValue);
	}

	/**
	 * @return The value of this element before any changes were been made to it by the user.
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
	 * @return The current value of this element.
	 */
	public T getCurrentValue() {
		return currentValue;
	}

	/**
	 * Sets this config element's current value to the new value and marks
	 * this config value as needing to be saved.
	 *
	 * @param newValue The new value
	 */
	public void setCurrentValue(final T newValue) {
		this.currentValue = newValue;
		this.isDirty = true;
	}

	/**
	 * @return The default value of this element.
	 */
	public T getDefaultValue() {
		return (T) getValueSpec().getDefault();
	}

	/**
	 * @return If this config element has been changed but not saved.
	 */
	public boolean isDirty() {
		return isDirty;
	}

	/**
	 * Sets this config element's current value to its initial value.
	 */
	public void undoChanges() {
		setCurrentValue(getInitialValue());
	}

	/**
	 * @return If this config element's current value is NOT equal to its initial value.
	 */
	public boolean isChanged() {
		return !getCurrentValue().equals(getInitialValue());
	}

	/**
	 * Sets this config element's current value to its default value.
	 */
	public void resetToDefault() {
		setCurrentValue(getDefaultValue());
	}

	/**
	 * @return If this config element's current value is equal to its default value.
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
	 * Saves the value if it has changed and has not yet been saved.
	 */
	public boolean save() {
		if (!isDirty())
			return false;

		final ConfigValue<T> configValue = getConfigValue();

		configValue.set(this.getCurrentValue());
		configValue.save();
		this.isDirty = false;
		return this.requiresGameRestart();
	}

	/**
	 * TODO: requiresGameRestart appears not to exist anymore?
	 *
	 * @return If this config element requires Minecraft to be restarted when it is changed.
	 */
	public boolean requiresGameRestart() {
		return false;
	}

	/**
	 * For Categories return false if ANY properties in the category are modifiable
	 * while a world is running, true if all are not.
	 *
	 * @return Whether or not this config element is safe to modify while a world is running.
	 */
	public boolean requiresWorldRestart() {
		return getValueSpec().needsWorldRestart();
	}

	/**
	 * @return The comment for this config element.
	 */
	public String getComment() {
		return getValueSpec().getComment();
	}

	/**
	 * @return The comment for this config element.
	 */
	public String getTranslationKey() {
		return getValueSpec().getTranslationKey();
	}

	/**
	 * @param o The object to test
	 * @return If the object is a valid object that can be used
	 */
	public boolean isValid(final Object o) {
		return getValueSpec().test(o);
	}

	/**
	 * @return The {@link Range} for this element ONLY if it is a number.
	 */
	@Nullable
	public <V extends Comparable<? super V>> Range<V> getRange() {
		final ValueSpec valueSpec = getValueSpec();
		if (!Number.class.isAssignableFrom(valueSpec.getClazz()))
			return null;
		return valueSpec.getRange();
	}

}
