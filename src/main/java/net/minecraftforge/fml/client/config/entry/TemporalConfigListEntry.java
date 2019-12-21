package net.minecraftforge.fml.client.config.entry;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.Widget;
import net.minecraftforge.fml.client.config.ConfigEntryListWidget;
import net.minecraftforge.fml.client.config.ConfigScreen;

import java.time.DateTimeException;
import java.time.LocalDate;

/**
 * @author Cadiboo
 */
public class TemporalConfigListEntry extends ConfigListEntry {

	private final TemporalConfigValueElement temporalConfigValueElement;
	private final TextFieldWidget textFieldWidget;

	public TemporalConfigListEntry(final TemporalConfigValueElement configValueElement, final ConfigScreen configScreen, final ConfigEntryListWidget configEntryListScreen) {
		super(configScreen, configEntryListScreen, configValueElement);
		this.temporalConfigValueElement = configValueElement;
		this.children().add(this.textFieldWidget = new TextFieldWidget(Minecraft.getInstance().fontRenderer, 0, 0, 18, 18, configValueElement.getName()));
		this.textFieldWidget.setMaxStringLength("YYYY-MM-DD".length());
		this.textFieldWidget.setText(configValueElement.get().toString());
		this.textFieldWidget.setCursorPositionZero(); // Remove weird scroll bug
		this.textFieldWidget.func_212954_a(s -> {
			if (!this.isValidValue())
				return;

			final String[] split = s.split("-");

			final int year = Integer.parseInt(split[0]);
			final int month = Integer.parseInt(split[1]);
			final int day = Integer.parseInt(split[2]);
			temporalConfigValueElement.set(LocalDate.of(year, month, day));
		});
	}

	@Override
	public boolean isValidValue() {
		final String text = this.textFieldWidget.getText();

		if (!temporalConfigValueElement.getValidationPattern().asPredicate().test(text))
			return false;

		final String[] split = text.split("-");
		if (split.length != 3)
			return false;
		try {
			final int year = Integer.parseInt(split[0]);
			final int month = Integer.parseInt(split[1]);
			final int day = Integer.parseInt(split[2]);
			try {
				LocalDate.of(year, month, day);
			} catch (DateTimeException e) {
				return false;
			}
		} catch (NumberFormatException e) {
			return false;
		}
		return true;
	}

	@Override
	public Widget getWidget() {
		return textFieldWidget;
	}

	@Override
	public Object getCurrentValue() {
		return temporalConfigValueElement.get();
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
		return temporalConfigValueElement.isDefault();
	}

	@Override
	public void resetToDefault() {
		temporalConfigValueElement.entryConfigValue.resetToDefault();
		this.textFieldWidget.setText(temporalConfigValueElement.get().toString());
	}

	@Override
	public void undoChanges() {
		temporalConfigValueElement.entryConfigValue.undoChanges();
		this.textFieldWidget.setText(temporalConfigValueElement.get().toString());
	}

	@Override
	public boolean isChanged() {
		return temporalConfigValueElement.entryConfigValue.isChanged();
	}

	@Override
	public boolean save() {
		temporalConfigValueElement.entryConfigValue.saveAndLoad();
		return temporalConfigValueElement.requiresWorldRestart();
	}

	@Override
	public boolean requiresWorldRestart() {
		return temporalConfigValueElement.requiresWorldRestart();
	}

	@Override
	public boolean requiresMcRestart() {
		return temporalConfigValueElement.requiresMcRestart();
	}

}
