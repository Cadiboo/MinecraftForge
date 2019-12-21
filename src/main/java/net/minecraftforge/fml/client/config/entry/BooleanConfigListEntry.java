package net.minecraftforge.fml.client.config.entry;

import net.minecraft.client.gui.widget.Widget;
import net.minecraftforge.fml.client.config.ConfigEntryListWidget;
import net.minecraftforge.fml.client.config.ConfigScreen;
import net.minecraftforge.fml.client.config.GuiButtonExt;

/**
 * @author Cadiboo
 */
public class BooleanConfigListEntry extends ConfigListEntry {

	private final BooleanConfigValueElement booleanConfigValueElement;
	private final GuiButtonExt button;

	public BooleanConfigListEntry(final BooleanConfigValueElement configValueElement, final ConfigScreen configScreen, final ConfigEntryListWidget configEntryListScreen) {
		super(configScreen, configEntryListScreen, configValueElement);
		this.booleanConfigValueElement = configValueElement;
		this.children().add(this.button = new GuiButtonExt(0, 0, 18, 18, configValueElement.get().toString(), b -> {
			configValueElement.set(!configValueElement.get());
			b.setMessage(configValueElement.get().toString());
		}));
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
		return booleanConfigValueElement.entryConfigValue.getCurrentValue();
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
		return booleanConfigValueElement.entryConfigValue.isDefault();
	}

	@Override
	public void resetToDefault() {
		booleanConfigValueElement.entryConfigValue.resetToDefault();
	}

	@Override
	public void undoChanges() {
		booleanConfigValueElement.entryConfigValue.undoChanges();
	}

	@Override
	public boolean isChanged() {
		return booleanConfigValueElement.entryConfigValue.isChanged();
	}

	@Override
	public boolean save() {
		booleanConfigValueElement.entryConfigValue.saveAndLoad();
		return booleanConfigValueElement.entryConfigValue.requiresWorldRestart();
	}

	@Override
	public boolean requiresWorldRestart() {
		return booleanConfigValueElement.entryConfigValue.requiresWorldRestart();
	}

	@Override
	public boolean requiresMcRestart() {
		return booleanConfigValueElement.entryConfigValue.requiresMcRestart();
	}

}
