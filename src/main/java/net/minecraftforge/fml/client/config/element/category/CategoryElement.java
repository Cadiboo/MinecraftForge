package net.minecraftforge.fml.client.config.element.category;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.client.config.element.IConfigElement;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

/**
 * A category element represents a ModConfig or a Config.
 * It is not backed by a ConfigValue and is a bit of a hack
 * and bridge between night config and forge's config spec system.
 * See the package-info.java for more information.
 *
 * @author Cadiboo
 */
public abstract class CategoryElement<T> implements IConfigElement<T> {

	@Override
	public boolean isDefault() {
		for (final IConfigElement<?> configElement : getConfigElements())
			if (!configElement.isDefault())
				return false;
		return true;
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
	public void set(final T value) {
		// No op
	}

	@Override
	public void save() {
		for (final IConfigElement<?> configElement : getConfigElements())
			configElement.save();
	}

	@Override
	public boolean isValid(final Object o) {
		for (final IConfigElement configElement : getConfigElements())
			if (!configElement.isValid(configElement.get()))
				return false;
		return true;
	}

	@Override
	public boolean isCategory() {
		return true;
	}

	@Nullable
	@Override
	public ForgeConfigSpec.Range<?> getRange() {
		return null;
	}

	@Nonnull
	public abstract List<IConfigElement<?>> getConfigElements();

}
