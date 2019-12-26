package net.minecraftforge.fml.client.config.entry2.fucked;

import net.minecraft.client.gui.widget.Widget;
import net.minecraftforge.fml.client.config.ConfigScreen;
import net.minecraftforge.fml.client.config.GuiButtonExt;
import net.minecraftforge.fml.client.config.element.ConfigElement;
import net.minecraftforge.fml.client.config.element.IConfigElement;
import net.minecraftforge.fml.client.config.entry2.ConfigListEntry;
import net.minecraftforge.fml.client.config.entry2.widget.EnumButton;

/**
 * @author Cadiboo
 */
public class EnumConfigListEntry<T extends Enum<?>> extends ConfigListEntry<T> {

	private final GuiButtonExt button;

	public EnumConfigListEntry(final ConfigScreen configScreen, final ConfigElement<T> element) {
		super(configScreen, element);
		this.children().add(this.button = new EnumButton<>(element::get, element::set, element::isDefault, element::resetToDefault, element::isChanged, element::resetToDefault, $ -> element.getConfigElementContainer().getValueSpec().test($)));
	}

	@Override
	public boolean isValidValue() {
		return true;
	}

	@Override
	public Widget getWidget() {
		return button;
	}

}
