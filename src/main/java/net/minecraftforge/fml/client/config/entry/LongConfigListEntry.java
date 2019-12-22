package net.minecraftforge.fml.client.config.entry;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.Widget;
import net.minecraftforge.fml.client.config.ConfigEntryListWidget;
import net.minecraftforge.fml.client.config.ConfigScreen;

/**
 * @author Cadiboo
 */
public class LongConfigListEntry extends ConfigListEntry {

	private final LongConfigValueElement configValueElement;
	private final TextFieldWidget textFieldWidget;

	public LongConfigListEntry(final LongConfigValueElement configValueElement, final ConfigScreen configScreen, final ConfigEntryListWidget configEntryListScreen) {
		super(configScreen, configEntryListScreen, configValueElement);
		this.configValueElement = configValueElement;
		this.children().add(this.textFieldWidget = new TextFieldWidget(Minecraft.getInstance().fontRenderer, 0, 0, 18, 18, configValueElement.getName()));
		this.textFieldWidget.setMaxStringLength(Integer.MAX_VALUE);
		this.textFieldWidget.setText(configValueElement.get().toString());
		this.textFieldWidget.setCursorPositionZero(); // Remove weird scroll bug
		this.textFieldWidget.func_212954_a(s -> {
			if (!this.isValidValue())
				return;
			this.configValueElement.set(Long.valueOf(s));
		});
	}

	@Override
	public boolean isValidValue() {
		try {
			long o = Long.parseLong(this.textFieldWidget.getText());
			return configValueElement.entryConfigValue.getValueSpec().test(o);
		} catch (NumberFormatException e) {
			return false;
		}
	}

	@Override
	public Widget getWidget() {
		return textFieldWidget;
	}

	@Override
	public Object getCurrentValue() {
		return configValueElement.get();
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
		return configValueElement.isDefault();
	}

	@Override
	public void resetToDefault() {
		configValueElement.entryConfigValue.resetToDefault();
		this.textFieldWidget.setText(configValueElement.get().toString());
	}

	@Override
	public void undoChanges() {
		configValueElement.entryConfigValue.undoChanges();
		this.textFieldWidget.setText(configValueElement.get().toString());
	}

	@Override
	public boolean isChanged() {
		return configValueElement.entryConfigValue.isChanged();
	}

	@Override
	public boolean save() {
		configValueElement.entryConfigValue.saveAndLoad();
		return configValueElement.entryConfigValue.requiresWorldRestart();
	}

	@Override
	public boolean requiresWorldRestart() {
		return configValueElement.entryConfigValue.requiresWorldRestart();
	}

	@Override
	public boolean requiresMcRestart() {
		return configValueElement.entryConfigValue.requiresMcRestart();
	}

}
