package net.minecraftforge.fml.client.config.entry2;

import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.fml.client.config.ConfigScreen;
import net.minecraftforge.fml.client.config.GuiButtonExt;
import net.minecraftforge.fml.client.config.HoverChecker;
import net.minecraftforge.fml.client.config.entry2.widget.ConfigListEntryWidget;

import java.util.Collections;
import java.util.List;

/**
 * @author Cadiboo
 */
public abstract class ListConfigListEntry<T> extends ConfigListEntry<T> {

	protected final GuiButtonExt addEntryBelowButton, deleteEntryButton;
	protected final List<String> addEntryBelowTooltip, deleteEntryTooltip;
	protected final HoverChecker addEntryBelowButtonHoverChecker, deleteEntryButtonHoverChecker;

	public <W extends Widget & ConfigListEntryWidget<T>> ListConfigListEntry(final ConfigScreen owningScreen, final W widget) {
		super(owningScreen, widget);
		this.children().add(this.addEntryBelowButton = new GuiButtonExt(0, 0, 0, 0, "+", b -> this.addEntryBelow()));
		this.children().add(this.deleteEntryButton = new GuiButtonExt(0, 0, 0, 0, "-", b -> this.deleteEntry()));

		this.addEntryBelowButtonHoverChecker = new HoverChecker(this.addEntryBelowButton, 500);
		this.deleteEntryButtonHoverChecker = new HoverChecker(this.deleteEntryButton, 500);

		this.addEntryBelowTooltip = Collections.singletonList(I18n.format("Add below"));
		this.deleteEntryTooltip = Collections.singletonList(I18n.format("Delete"));

		this.renderLabel = false;
	}

	private void deleteEntry() {
	}

	private void addEntryBelow() {
	}

	@Override
	public void renderToolTip(final int mouseX, final int mouseY, final float partialTicks) {
		super.renderToolTip(mouseX, mouseY, partialTicks);
		// TODO: this is only called for hovered elements anyway?
		boolean canHover = mouseY < this.owningScreen.getEntryList().getBottom() && mouseY > this.owningScreen.getEntryList().getTop();
		if (this.addEntryBelowButtonHoverChecker.checkHover(mouseX, mouseY, canHover))
			this.owningScreen.drawToolTip(addEntryBelowTooltip, mouseX, mouseY);

		if (this.deleteEntryButtonHoverChecker.checkHover(mouseX, mouseY, canHover))
			this.owningScreen.drawToolTip(deleteEntryTooltip, mouseX, mouseY);
	}

}
