package net.minecraftforge.fml.client.config.entry;

import net.minecraft.client.gui.widget.Widget;
import net.minecraftforge.fml.client.config.ConfigScreen;
import net.minecraftforge.fml.client.config.GuiButtonExt;

import javax.annotation.Nullable;

/**
 * @author Cadiboo
 */
public class BooleanListEntryConfigListEntry extends ListConfigListEntry<Boolean> {

	final GuiButtonExt button;

	public BooleanListEntryConfigListEntry(final ConfigScreen owningScreen, final Boolean obj) {
		super(owningScreen, obj);
		this.children().add(this.button = new GuiButtonExt(0, 0, 18, 18, this.obj.toString(), b -> {
			this.obj = !this.obj;
			b.setMessage(this.obj.toString());
			b.setFGColor(BooleanConfigListEntry.getColor(this.obj));
		}));
		this.button.setFGColor(BooleanConfigListEntry.getColor(this.obj));
	}

	@Nullable
	@Override
	public Widget getWidget() {
		return button;
	}

}
