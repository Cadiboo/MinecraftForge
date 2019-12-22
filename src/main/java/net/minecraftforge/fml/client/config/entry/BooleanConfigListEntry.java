package net.minecraftforge.fml.client.config.entry;

import net.minecraft.client.gui.widget.Widget;
import net.minecraftforge.fml.client.config.ConfigEntryListWidget;
import net.minecraftforge.fml.client.config.ConfigScreen;
import net.minecraftforge.fml.client.config.GuiButtonExt;

/**
 * @author Cadiboo
 */
public class BooleanConfigListEntry extends ConfigListEntry {

	private final BooleanConfigValueElement configValueElement;
	private final GuiButtonExt button;

	public BooleanConfigListEntry(final BooleanConfigValueElement configValueElement, final ConfigScreen configScreen, final ConfigEntryListWidget configEntryListScreen) {
		super(configScreen, configEntryListScreen, configValueElement);
		this.configValueElement = configValueElement;
		this.children().add(this.button = new GuiButtonExt(0, 0, 18, 18, configValueElement.get().toString(), b -> {
			configValueElement.set(!configValueElement.get());
			b.setMessage(configValueElement.get().toString());
			b.setFGColor(getColor(configValueElement.get()));
		}));
		this.button.setFGColor(getColor(configValueElement.get()));
	}

	private int getColor(final boolean b) {
		return b ? 0x55FF55 : 0xFF5555; // green or red
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
	}

	@Override
	public void undoChanges() {
		configValueElement.entryConfigValue.undoChanges();
	}

	@Override
	public boolean isChanged() {
		return configValueElement.entryConfigValue.isChanged();
	}

	@Override
	public boolean save() {
		configValueElement.entryConfigValue.saveAndLoad();
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
