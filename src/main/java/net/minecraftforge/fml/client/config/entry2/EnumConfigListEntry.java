package net.minecraftforge.fml.client.config.entry2;

import net.minecraft.client.gui.widget.Widget;
import net.minecraft.item.DyeColor;
import net.minecraftforge.fml.client.config.ConfigScreen;
import net.minecraftforge.fml.client.config.GuiButtonExt;
import net.minecraftforge.fml.client.config.element.IConfigElement;

/**
 * @author Cadiboo
 */
public class EnumConfigListEntry<T extends Enum<?>> extends ConfigListEntry<T> {

	private final GuiButtonExt button;

	public EnumConfigListEntry(final ConfigScreen configScreen, final IConfigElement<T> configElement) {
		super(configScreen, configElement);
		final T[] enumConstants = (T[]) configElement.get().getClass().getEnumConstants();
		this.children().add(this.button = new GuiButtonExt(0, 0, 0, 0, getLabel(), b -> {
			final T newValue = enumConstants[(configElement.get().ordinal() + 1) % enumConstants.length];
			configElement.set(newValue);
			updateButtonText();
		}));
		updateButtonText();
	}

	@Override
	public void resetToDefault() {
		super.resetToDefault();
		this.updateButtonText();
	}

	@Override
	public void undoChanges() {
		super.resetToDefault();
		this.updateButtonText();
	}

	public void updateButtonText() {
		final T anEnum = this.getConfigElement().get();
		this.button.setMessage(getDisplayString(anEnum));
		this.button.setFGColor(getColor(anEnum));
	}

	public int getColor(final T anEnum) {
		if (anEnum instanceof DyeColor)
			return ((DyeColor) anEnum).getColorValue();
		else if (anEnum instanceof IColorable)
			return ((IColorable) anEnum).getColor();
		else
			return 0xFF_FF_FF; // White
	}

	public String getDisplayString(final Enum<?> anEnum) {
		return anEnum.toString();
	}

	@Override
	public boolean isValidValue() {
		return true;
	}

	@Override
	public Widget getWidget() {
		return button;
	}

	public interface IColorable {

		int getColor();

	}

}
