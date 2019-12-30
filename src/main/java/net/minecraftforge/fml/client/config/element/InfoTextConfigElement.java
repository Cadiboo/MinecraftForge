package net.minecraftforge.fml.client.config.element;

import net.minecraft.client.resources.I18n;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.client.config.ConfigScreen;
import net.minecraftforge.fml.client.config.entry.ConfigListEntry;
import net.minecraftforge.fml.client.config.entry.InfoTextConfigListEntry;

import javax.annotation.Nullable;

/**
 * A dummy IConfigElement that just displays text (Usually error text) and does nothing else.
 *
 * @author Cadiboo
 */
public class InfoTextConfigElement<T> implements IConfigElement<T> {

	private final String label;
	private final String translationKey;

	public InfoTextConfigElement(final String translationKey) {
		this.translationKey = translationKey;
		this.label = I18n.format(translationKey);
	}

	@Override
	public String getLabel() {
		return label;
	}

	@Override
	public String getTranslationKey() {
		return translationKey;
	}

	@Override
	public String getComment() {
		return label;
	}

	@Override
	public boolean isDefault() {
		return true;
	}

	@Override
	public T getDefault() {
		return null;
	}

	@Override
	public void resetToDefault() {
	}

	@Override
	public boolean isChanged() {
		return false;
	}

	@Override
	public void undoChanges() {
	}

	@Override
	public boolean requiresWorldRestart() {
		return false;
	}

	@Override
	public boolean requiresGameRestart() {
		return false;
	}

	@Override
	public T get() {
		return null;
	}

	@Override
	public void set(final T value) {
	}

	@Override
	public void save() {
	}

	@Override
	public boolean isValid(final T o) {
		return true;
	}

	@Override
	public ConfigListEntry<T> makeConfigListEntry(final ConfigScreen configScreen) {
		return new InfoTextConfigListEntry<>(configScreen, this.getLabel());
	}

	@Nullable
	@Override
	public ForgeConfigSpec.Range<?> getRange() {
		return null;
	}

}
