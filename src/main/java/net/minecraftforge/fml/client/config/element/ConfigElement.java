package net.minecraftforge.fml.client.config.element;

import net.minecraftforge.common.ForgeConfigSpec;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author Cadiboo
 */
public abstract class ConfigElement<T> implements IConfigElement<T> {

	@Nonnull
	private final ConfigElementContainer<T> configElementContainer;

	public ConfigElement(@Nonnull final ConfigElementContainer<T> configElementContainer) {
		this.configElementContainer = configElementContainer;
	}

	@Nonnull
	public ConfigElementContainer<T> getConfigElementContainer() {
		return configElementContainer;
	}

	@Override
	public String getLabel() {
		return getConfigElementContainer().getLabel();
	}

	@Override
	public String getTranslationKey() {
		return getConfigElementContainer().getTranslationKey();
	}

	@Nullable
	@Override
	public String getComment() {
		return getConfigElementContainer().getComment();
	}

	@Override
	public boolean isDefault() {
		return getConfigElementContainer().isDefault();
	}

	@Override
	public T getDefault() {
		return getConfigElementContainer().getDefaultValue();
	}

	@Override
	public void resetToDefault() {
		getConfigElementContainer().resetToDefault();
	}

	@Override
	public boolean isChanged() {
		return getConfigElementContainer().isChanged();
	}

	@Override
	public void undoChanges() {
		getConfigElementContainer().undoChanges();
	}

	@Override
	public boolean requiresWorldRestart() {
		return getConfigElementContainer().requiresWorldRestart();
	}

	@Override
	public boolean requiresGameRestart() {
		return getConfigElementContainer().requiresGameRestart();
	}

	@Override
	public T get() {
		return getConfigElementContainer().getCurrentValue();
	}

	@Override
	public void set(final T value) {
		getConfigElementContainer().setCurrentValue(value);
	}

	@Override
	public void save() {
		getConfigElementContainer().save();
	}

	@Override
	public boolean isValid(final Object o) {
		return getConfigElementContainer().isValid(o);
	}

	@Nullable
	@Override
	public ForgeConfigSpec.Range<?> getRange() {
		return getConfigElementContainer().getRange();
	}

}
