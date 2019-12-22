package net.minecraftforge.fml.client.config.entry;

import net.minecraftforge.fml.client.config.ConfigEntryListWidget;
import net.minecraftforge.fml.client.config.ConfigScreen;
import net.minecraftforge.fml.config.ModConfig;

/**
 * @author Cadiboo
 */
public class ModConfigConfigListEntry extends CategoryConfigListEntry {

	public ModConfigConfigListEntry(final ConfigScreen owningScreen, final ConfigEntryListWidget owningEntryList, final IConfigValueElement<ModConfig> configValueElement) {
		super(owningScreen, owningEntryList, configValueElement);
	}

}
