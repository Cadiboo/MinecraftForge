package net.minecraftforge.fml.client.config.entry;

import net.minecraft.client.gui.widget.Widget;
import net.minecraftforge.fml.client.config.ConfigEntryListWidget;
import net.minecraftforge.fml.client.config.ConfigScreen;

/**
 * @author Cadiboo
 */
public class DummyConfigListEntry extends ConfigListEntry {

	public DummyConfigListEntry(final ConfigScreen configScreen, final ConfigEntryListWidget configEntryListScreen, final DummyConfigValueElement configElement) {
		super(configScreen, configEntryListScreen, configElement);
	}

	@Override
	public boolean isValidValue() {
		return true;
	}

	@Override
	public Widget getWidget() {
		return null;
	}

	@Override
	public Object getCurrentValue() {
		return "NULL";
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
		return true;
	}

	@Override
	public void resetToDefault() {
	}

	@Override
	public void undoChanges() {
	}

	@Override
	public boolean isChanged() {
		return false;
	}

	@Override
	public boolean save() {
		return false;
	}

	@Override
	public boolean requiresWorldRestart() {
		return false;
	}

	@Override
	public boolean requiresMcRestart() {
		return false;
	}

}
