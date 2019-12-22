package net.minecraftforge.fml.client.config.entry;

import net.minecraft.client.gui.widget.Widget;
import net.minecraft.item.DyeColor;
import net.minecraftforge.fml.client.config.ConfigEntryListWidget;
import net.minecraftforge.fml.client.config.ConfigScreen;
import net.minecraftforge.fml.client.config.GuiButtonExt;

/**
 * @author Cadiboo
 */
public class EnumConfigListEntry extends ConfigListEntry {

	private final EnumConfigValueElement configValueElement;
	private final GuiButtonExt button;

	public EnumConfigListEntry(final EnumConfigValueElement configValueElement, final ConfigScreen configScreen, final ConfigEntryListWidget configEntryListScreen) {
		super(configScreen, configEntryListScreen, configValueElement);
		this.configValueElement = configValueElement;
		final Enum<?>[] enumConstants = configValueElement.get().getClass().getEnumConstants();
		this.children().add(this.button = new GuiButtonExt(0, 0, 18, 18, "You shouldn't see this", b -> {
			final Enum<?> newValue = enumConstants[(configValueElement.get().ordinal() + 1) % enumConstants.length];
			configValueElement.set(newValue);
			updateButtonText();
		}));
		updateButtonText();
	}

	public void updateButtonText() {
		final Enum<?> anEnum = this.configValueElement.get();
		this.button.setMessage(getDisplayString(anEnum));
		this.button.setFGColor(getColor(anEnum));
	}

	public int getColor(final Enum<?> anEnum) {
		if (anEnum instanceof DyeColor)
			return ((DyeColor) anEnum).getColorValue();
		return 0xFF_FF_FF; // white
	}

	private String getDisplayString(final Enum<?> anEnum) {
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

	@Override
	public Object getCurrentValue() {
		return configValueElement.get();
	}

	@Override
	public Object[] getCurrentValues() {
		return new Object[0];
	}

	@Override
	public void tick() {
	}

	@Override
	public boolean isDefault() {
		return configValueElement.isDefault();
	}

	@Override
	public void resetToDefault() {
		configValueElement.resetToDefault();
		updateButtonText();
	}

	@Override
	public void undoChanges() {
		configValueElement.entryConfigValue.undoChanges();
		updateButtonText();
	}

	@Override
	public boolean isChanged() {
		return configValueElement.entryConfigValue.isChanged();
	}

	@Override
	public boolean save() {
		configValueElement.entryConfigValue.saveAndLoad();
		updateButtonText();
		return configValueElement.requiresWorldRestart();
	}

	@Override
	public boolean requiresWorldRestart() {
		return configValueElement.requiresWorldRestart();
	}

	@Override
	public boolean requiresMcRestart() {
		return configValueElement.requiresMcRestart();
	}

}
