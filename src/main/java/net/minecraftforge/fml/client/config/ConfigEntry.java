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

package net.minecraftforge.fml.client.config;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.config.ModConfig;

/**
 * @author Cadiboo
 */
public class ConfigEntry<T> {

	private final T initialValue;
	private final T defaultValue;
	private final ModConfig modConfig;
	private final String path;
	private final String label;
	private final String comment;
	private T currentValue;
	private boolean isDirty;
	private final boolean requiresMcRestart;
	private final boolean requiresWorldRestart;

	public ConfigEntry(final T initialValue, final T defaultValue, final ModConfig modConfig, final String path, final ForgeConfigSpec.ValueSpec valueSpec) {
		this.initialValue = initialValue;
		this.defaultValue = defaultValue;
		this.modConfig = modConfig;
		this.path = path;
		this.currentValue = initialValue;
		this.label = I18n.format(valueSpec.getTranslationKey());
		this.comment = valueSpec.getComment();
		this.requiresMcRestart = false; // TODO: requiresMcRestart appears not to exist anymore?
		this.requiresWorldRestart = valueSpec.needsWorldRestart(); // TODO: requiresMcRestart appears not to exist anymore?
	}

	public static <T> ConfigEntry<T> of(final Class<T> clazz, final ModConfig modConfig, final String path, final ForgeConfigSpec.ValueSpec valueSpec) {
		if (Enum.class.isAssignableFrom(valueSpec.getClazz()))
			return (ConfigEntry<T>) ofEnum(modConfig, path, valueSpec);
		final CommentedFileConfig configData = (CommentedFileConfig) modConfig.getConfigData();
		final T initialValue = ensureCorrect(configData.get(path), clazz);
		final T defaultValue = (T) valueSpec.getDefault();
		return new ConfigEntry<>(initialValue, defaultValue, modConfig, path, valueSpec);
	}

	public static <T> T ensureCorrect(final T value, final Class<?> clazz) {
		final Class<?> valueClass = value.getClass();
		if (valueClass.isAssignableFrom(clazz))
			return value;
		// TOML stores Floats as Doubles
		if (clazz == Float.class && valueClass == Double.class)
			return (T) Float.valueOf(((Double) value).floatValue());
		// TOML stores Enums as Strings
		if (Enum.class.isAssignableFrom(clazz))
			return (T) Enum.valueOf((Class<? extends Enum>) clazz, (String) value);
		return value;
	}

	public static <T> T saveAndLoad(final ModConfig modConfig, final String path, final T setValue) {
		final CommentedFileConfig configData = (CommentedFileConfig) modConfig.getConfigData();
		configData.set(path, setValue);
		modConfig.save();
		configData.load();
		modConfig.fireEvent(new ModConfig.ConfigReloading(modConfig));
		return configData.get(path);
	}

	public static ConfigEntry<Enum> ofEnum(final ModConfig modConfig, final String path, final ForgeConfigSpec.ValueSpec valueSpec) {
		final CommentedFileConfig configData = (CommentedFileConfig) modConfig.getConfigData();
		final Enum initialValue = ensureCorrect(configData.get(path), valueSpec.getClazz());
		final Enum defaultValue = (Enum) valueSpec.getDefault();
		return new ConfigEntry<>(initialValue, defaultValue, modConfig, path, valueSpec);
	}

	public String getLabel() {
		return label;
	}

	public String getComment() {
		return comment;
	}

	public T getInitialValue() {
		return initialValue;
	}

	public T getDefaultValue() {
		return defaultValue;
	}

	public T getCurrentValue() {
		return currentValue;
	}

	public void setCurrentValue(final T newValue) {
		isDirty = true;
		this.currentValue = ensureCorrect(newValue, this.defaultValue.getClass());
	}

	public void undoChanges() {
		this.setCurrentValue(this.initialValue);
	}

	public boolean isChanged() {
		return !getCurrentValue().equals(getInitialValue());
	}

	public boolean isDefault() {
		return !getCurrentValue().equals(getDefaultValue());
	}

	public boolean isDirty() {
		return isDirty;
	}

	public void saveAndLoad() {
		if (!isDirty)
			return;
		this.setCurrentValue(saveAndLoad(modConfig, path, this.getCurrentValue()));
		isDirty = false;
	}

	public void resetToDefault() {
		this.setCurrentValue(this.defaultValue);
	}

	public String getPath() {
		return path;
	}

	public boolean requiresMcRestart() {
		return requiresMcRestart;
	}

	public boolean requiresWorldRestart() {
		return requiresWorldRestart;
	}

}
