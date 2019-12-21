package net.minecraftforge.fml.client.config.entry;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.Widget;
import net.minecraftforge.fml.client.config.ConfigEntryListWidget;
import net.minecraftforge.fml.client.config.ConfigScreen;

/**
 * @author Cadiboo
 */
public class StringConfigListEntry extends ConfigListEntry {

	private final StringConfigValueElement stringConfigValueElement;
	private final TextFieldWidget textFieldWidget;

	public StringConfigListEntry(final StringConfigValueElement configValueElement, final ConfigScreen configScreen, final ConfigEntryListWidget configEntryListScreen) {
		super(configScreen, configEntryListScreen, configValueElement);
		this.stringConfigValueElement = configValueElement;
		this.children().add(this.textFieldWidget = new TextFieldWidget(Minecraft.getInstance().fontRenderer, 0, 0, 18, 18, configValueElement.getName()));
		this.textFieldWidget.setMaxStringLength(Integer.MAX_VALUE);
		this.textFieldWidget.setText(configValueElement.get());
		this.textFieldWidget.setCursorPositionZero(); // Remove weird scroll bug
		this.textFieldWidget.func_212954_a(this.stringConfigValueElement::set);
	}

	@Override
	public boolean isValidValue() {
		return this.stringConfigValueElement.getValidationPattern().asPredicate().test(this.textFieldWidget.getText());
	}

	@Override
	public Widget getWidget() {
		return textFieldWidget;
	}

	@Override
	public Object getCurrentValue() {
		return stringConfigValueElement.get();
	}

	@Override
	public Object[] getCurrentValues() {
		return new Object[0];
	}

	@Override
	public void tick() {
		textFieldWidget.tick();
	}

	@Override
	public boolean isDefault() {
		return stringConfigValueElement.isDefault();
	}

	@Override
	public void resetToDefault() {
		stringConfigValueElement.entryConfigValue.resetToDefault();
		this.textFieldWidget.setText(stringConfigValueElement.get());
	}

	@Override
	public void undoChanges() {
		stringConfigValueElement.entryConfigValue.undoChanges();
		this.textFieldWidget.setText(stringConfigValueElement.get());
	}

	@Override
	public boolean isChanged() {
		return stringConfigValueElement.entryConfigValue.isChanged();
	}

	@Override
	public boolean save() {
		stringConfigValueElement.entryConfigValue.saveAndLoad();
		return stringConfigValueElement.entryConfigValue.requiresWorldRestart();
	}

	@Override
	public boolean requiresWorldRestart() {
		return stringConfigValueElement.entryConfigValue.requiresWorldRestart();
	}

	@Override
	public boolean requiresMcRestart() {
		return stringConfigValueElement.entryConfigValue.requiresMcRestart();
	}

}
