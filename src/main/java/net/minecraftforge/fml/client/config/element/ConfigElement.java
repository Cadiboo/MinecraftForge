package net.minecraftforge.fml.client.config.element;

import net.minecraftforge.fml.client.config.entry.ConfigElementContainer;

import javax.annotation.Nullable;

/**
 * @author Cadiboo
 */
public abstract class ConfigElement<T> implements IConfigElement<T> {

	private final ConfigElementContainer<T> entryConfigValue;

	public ConfigElement(final ConfigElementContainer<T> entryConfigValue) {
		// entryConfigValue can be null from DummyConfigElements or HolderConfigElements
		this.entryConfigValue = entryConfigValue;
	}

	public ConfigElementContainer<T> getConfigElementContainer() {
		return entryConfigValue;
	}

	@Override
	public String getLabel() {
		return getConfigElementContainer().getLabel();
	}

	@Override
	public String getTranslationKey() {
		return getConfigElementContainer().getTranslationKey();
	}

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
	public boolean isValid(T o) {
		return getConfigElementContainer().isValid(o);
	}

}
