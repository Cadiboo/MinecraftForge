package net.minecraftforge.fml.client.config.entry;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.common.ForgeConfigSpec.ConfigValue;
import net.minecraftforge.common.ForgeConfigSpec.ValueSpec;
import net.minecraftforge.fml.client.config.ConfigScreen;
import net.minecraftforge.fml.client.config.element.ConfigElement;
import net.minecraftforge.fml.client.config.element.IConfigElement;
import net.minecraftforge.fml.config.ModConfig;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Cadiboo
 */
public class ListCategoryConfigListEntry extends CategoryConfigListEntry<List<?>> {

	private final ConfigElementContainer<List<?>> entryConfigValue;
	private final ModConfig modConfig;

	public ListCategoryConfigListEntry(final ConfigScreen owningScreen, final ModConfig modConfig, final List<String> path, final ConfigValue<List<?>> configValue) {
		super(owningScreen, I18n.format(((ValueSpec) modConfig.getSpec().get(path)).getTranslationKey()));
		this.modConfig = modConfig;
		this.entryConfigValue = new ConfigElementContainer<>(path, modConfig, configValue);
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
	public boolean isDefault() {
		return super.isDefault() && getBooleanConfigElement().isDefault();
	}

	@Override
	public ConfigElementContainer<List<?>> getBooleanConfigElement() {
		return entryConfigValue;
	}

	@Override
	public void resetToDefault() {
		super.resetToDefault();
		getBooleanConfigElement().resetToDefault();
	}

	@Override
	public boolean isChanged() {
		return super.isChanged() || getBooleanConfigElement().isChanged();
	}

	@Override
	public void undoChanges() {
		super.undoChanges();
		getBooleanConfigElement().undoChanges();
	}

	@Override
	public boolean save() {
		return getBooleanConfigElement().save();
	}

	@Override
	public boolean requiresWorldRestart() {
		return getBooleanConfigElement().requiresWorldRestart();
	}

	@Override
	public boolean requiresMcRestart() {
		return getBooleanConfigElement().requiresGameRestart();
	}

	protected List<IConfigElement<?>> makeChildElementsList() {
		final List<IConfigElement<?>> list = new ArrayList<>();
//		entryConfigValue.getCurrentValue().forEach((obj) -> {
////			ListConfigListEntry<?> configListEntry = null;
//			ConfigElement<?> configListEntry = null;
//			if (obj instanceof Boolean) {
//				configListEntry = new BooleanListEntryConfigListEntry(configScreen, (Boolean) obj);
//			} else if (obj instanceof String) {
//				configListEntry = new StringListEntryConfigListEntry(configScreen, (String) obj);
//			} else {
//				configListEntry = new DummyConfigListEntry(configScreen, "(Unknown object \"" + obj.getClass().getSimpleName() + "\") " + obj);
//			}
//			list.add(configListEntry);
//		});
		return list;
	}

	@Override
	public boolean isCategory() {
		return false;
	}

}
