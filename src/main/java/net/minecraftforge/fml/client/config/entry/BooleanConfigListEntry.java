package net.minecraftforge.fml.client.config.entry;

import net.minecraft.client.gui.widget.Widget;
import net.minecraftforge.common.ForgeConfigSpec.ConfigValue;
import net.minecraftforge.fml.client.config.ConfigScreen;
import net.minecraftforge.fml.client.config.GuiButtonExt;
import net.minecraftforge.fml.config.ModConfig;

import java.util.List;

/**
 * @author Cadiboo
 */
public class BooleanConfigListEntry extends ConfigListEntry<Boolean> {

	private final EntryConfigValue<Boolean> entryConfigValue;
	private final GuiButtonExt button;

	public BooleanConfigListEntry(final ConfigScreen configScreen, final ModConfig modConfig, final List<String> path, final ConfigValue<Boolean> configValue) {
		super(configScreen);
		this.entryConfigValue = new EntryConfigValue<>(path, modConfig, configValue);
		this.children().add(this.button = new GuiButtonExt(0, 0, 18, 18, entryConfigValue.getCurrentValue().toString(), b -> {
			entryConfigValue.setCurrentValue(!entryConfigValue.getCurrentValue());
			b.setMessage(entryConfigValue.getCurrentValue().toString());
			b.setFGColor(getColor(entryConfigValue.getCurrentValue()));
		}));
		this.button.setFGColor(getColor(entryConfigValue.getCurrentValue()));
	}

	public static int getColor(final boolean b) {
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
	protected EntryConfigValue<Boolean> getEntryConfigValue() {
		return entryConfigValue;
	}

}
