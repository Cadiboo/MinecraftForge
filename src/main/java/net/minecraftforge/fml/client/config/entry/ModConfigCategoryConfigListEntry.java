package net.minecraftforge.fml.client.config.entry;

import com.electronwill.nightconfig.core.CommentedConfig;
import com.electronwill.nightconfig.core.file.FileConfig;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.fml.client.config.ConfigScreen;
import net.minecraftforge.fml.client.config.element.ConfigElement;
import net.minecraftforge.fml.client.config.element.IConfigElement;
import net.minecraftforge.fml.config.ModConfig;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Cadiboo
 */
public class ModConfigCategoryConfigListEntry extends CategoryConfigListEntry<ModConfig> {

	private final ModConfig modConfig;

	public ModConfigCategoryConfigListEntry(final ConfigScreen owningScreen, final ModConfig modConfig) {
		super(owningScreen, StringUtils.capitalize(modConfig.getType().name().toLowerCase()));
		this.modConfig = modConfig;
	}

	/**
	 * This method is called in the constructor and is used to set the childScreen field.
	 */
	protected Screen buildChildScreen() {
		final ConfigScreen configScreen = new ConfigScreen(this.owningScreen.getTitle(), this.owningScreen, this.owningScreen.modContainer, this.makeChildElementsList());
		final ITextComponent subtitle;
		if (this.owningScreen.getSubtitle() == null)
			subtitle = new StringTextComponent(getLabel());
		else
			subtitle = this.owningScreen.getSubtitle().deepCopy().appendSibling(new StringTextComponent(" > " + getLabel()));
		configScreen.setSubtitle(subtitle);
		return configScreen;
	}

	@Override
	public boolean save() {
		final boolean requiresRestart = super.save();
		// TODO: make server config send packet to server?
		final CommentedConfig configData = this.modConfig.getConfigData();
		if (configData instanceof FileConfig) {
			this.modConfig.save();
			((FileConfig) configData).load();
		}
		modConfig.fireEvent(new ModConfig.ConfigReloading(modConfig));
		return requiresRestart;
	}

	protected List<IConfigElement<?>> makeChildElementsList() {
		// name -> ConfigValue|SimpleConfig
		final Map<String, Object> specConfigValues = ConfigScreen.getSpecConfigValues(modConfig);

		final List<IConfigElement<?>> list = new ArrayList<>();
		specConfigValues.forEach((name, obj) -> {
			final ConfigElement<?> configListEntry = ConfigScreen.makeConfigElement(modConfig, name, obj);
			list.add(configListEntry);
		});
		return list;
	}

}
