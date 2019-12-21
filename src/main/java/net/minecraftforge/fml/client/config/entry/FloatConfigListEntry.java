package net.minecraftforge.fml.client.config.entry;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.Widget;
import net.minecraftforge.fml.client.config.ConfigEntryListWidget;
import net.minecraftforge.fml.client.config.ConfigScreen;

/**
 * @author Cadiboo
 */
public class FloatConfigListEntry extends ConfigListEntry {

	private final FloatConfigValueElement longConfigValueElement;
	private final TextFieldWidget textFieldWidget;

	public FloatConfigListEntry(final FloatConfigValueElement configValueElement, final ConfigScreen configScreen, final ConfigEntryListWidget configEntryListScreen) {
		super(configScreen, configEntryListScreen, configValueElement);
		this.longConfigValueElement = configValueElement;
		this.children().add(this.textFieldWidget = new TextFieldWidget(Minecraft.getInstance().fontRenderer, 0, 0, 18, 18, configValueElement.getName()));
		this.textFieldWidget.setMaxStringLength(Integer.MAX_VALUE);
		this.textFieldWidget.setText(configValueElement.get().toString());
		this.textFieldWidget.setCursorPositionZero(); // Remove weird scroll bug
		this.textFieldWidget.func_212954_a(s -> {
			if (!this.isValidValue())
				return;
			longConfigValueElement.set(Float.valueOf(s));
		});
	}

	@Override
	public boolean isValidValue() {
		try {
			Float.parseFloat(this.textFieldWidget.getText());
			return true;
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
		return longConfigValueElement.get();
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
		return longConfigValueElement.isDefault();
	}

	@Override
	public void resetToDefault() {
		longConfigValueElement.entryConfigValue.resetToDefault();
		this.textFieldWidget.setText(longConfigValueElement.get().toString());
	}

	@Override
	public void undoChanges() {
		longConfigValueElement.entryConfigValue.undoChanges();
		this.textFieldWidget.setText(longConfigValueElement.get().toString());
	}

	@Override
	public boolean isChanged() {
		return longConfigValueElement.entryConfigValue.isChanged();
	}

	@Override
	public boolean save() {
		longConfigValueElement.entryConfigValue.saveAndLoad();
		return longConfigValueElement.entryConfigValue.requiresWorldRestart();
	}

	@Override
	public boolean requiresWorldRestart() {
		return longConfigValueElement.entryConfigValue.requiresWorldRestart();
	}

	@Override
	public boolean requiresMcRestart() {
		return longConfigValueElement.entryConfigValue.requiresMcRestart();
	}

}
