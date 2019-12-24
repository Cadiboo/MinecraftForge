package net.minecraftforge.fml.client.config.entry;

import net.minecraftforge.fml.client.config.ConfigScreen;

import java.util.List;
import java.util.Objects;

/**
 * @author Cadiboo
 */
public abstract class ListConfigListEntry<T> extends ConfigListEntry<List<?>> {

	public final T initial;
	public T obj;

	public ListConfigListEntry(final ConfigScreen owningScreen, final T obj) {
		super(owningScreen);
		this.obj = this.initial = obj;
		this.drawLabel = false;
	}

	@Override
	public boolean isValidValue() {
		return true;
	}

	@Override
	public boolean isDefault() {
		return Objects.equals(this.obj, this.initial);
	}

	@Override
	protected EntryConfigValue<List<?>> getEntryConfigValue() {
		return null;
	}

	@Override
	public void resetToDefault() {
		this.obj = this.initial;
	}

	@Override
	public boolean isChanged() {
		return !Objects.equals(this.obj, this.initial);
	}

	@Override
	public void undoChanges() {
		this.obj = this.initial;
	}

}
