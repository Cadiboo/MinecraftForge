package net.minecraftforge.fml.client.config.element;

import net.minecraft.client.gui.screen.Screen;
import net.minecraftforge.fml.client.config.ConfigEntryListWidget;
import net.minecraftforge.fml.client.config.ConfigScreen;
import net.minecraftforge.fml.client.config.entry2.ConfigListEntry;
import net.minecraftforge.fml.client.config.entry2.ScreenConfigListEntry;

import java.util.List;

/**
 * @author Cadiboo
 */
public abstract class HolderConfigElement<T> extends ConfigElement<T> {

	private final T obj;
	private final List<IConfigElement<?>> configElements;

	public HolderConfigElement(final T obj, final List<IConfigElement<?>> configElements) {
		super(null);
		this.obj = obj;
		this.configElements = configElements;
	}

	@Override
	public abstract String getLabel();

	@Override
	public abstract String getTranslationKey();

	@Override
	public abstract String getComment();

	@Override
	public boolean isDefault() {
		for (final IConfigElement<?> configElement : getConfigElements())
			if (!configElement.isDefault())
				return false;
		return true;
	}

	@Override
	public T getDefault() {
		return obj;
	}

	@Override
	public void resetToDefault() {
		for (final IConfigElement<?> configElement : getConfigElements())
			configElement.resetToDefault();
	}

	@Override
	public boolean isChanged() {
		for (final IConfigElement<?> configElement : getConfigElements())
			if (configElement.isChanged())
				return true;
		return false;
	}

	@Override
	public void undoChanges() {
		for (final IConfigElement<?> configElement : getConfigElements())
			configElement.undoChanges();
	}

	@Override
	public boolean requiresWorldRestart() {
		for (final IConfigElement<?> configElement : getConfigElements())
			if (configElement.requiresWorldRestart())
				return true;
		return false;
	}

	@Override
	public boolean requiresGameRestart() {
		for (final IConfigElement<?> configElement : getConfigElements())
			if (configElement.requiresGameRestart())
				return true;
		return false;
	}

	@Override
	public T get() {
		return obj;
	}

	@Override
	public void set(final T value) {
	}

	@Override
	public void save() {
		for (final IConfigElement<?> configElement : getConfigElements())
			configElement.save();
	}

	@Override
	public ConfigListEntry<T> makeWidgetThing(final ConfigScreen configScreen, final ConfigEntryListWidget configEntryListWidget) {
		final Screen screen = makeScreen(configScreen, configEntryListWidget);
		return new ScreenConfigListEntry<>(configScreen, this, screen);
	}

	@Override
	public abstract boolean isCategory();

	protected abstract Screen makeScreen(final ConfigScreen owningScreen, final ConfigEntryListWidget configEntryListWidget);

	public List<IConfigElement<?>> getConfigElements() {
		return configElements;
	}

}
