package net.minecraftforge.fml.client.config.entry2.fucked;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraftforge.fml.client.config.ConfigScreen;
import net.minecraftforge.fml.client.config.element.ConfigElement;
import net.minecraftforge.fml.client.config.element.IConfigElement;
import net.minecraftforge.fml.client.config.entry2.ConfigListEntry;

import javax.annotation.Nonnull;

/**
 * @author Cadiboo
 */
public abstract class TextConfigListEntry<T> extends ConfigListEntry<T> {

	private final TextFieldWidget textFieldWidget;

	public TextConfigListEntry(final ConfigScreen configScreen, final IConfigElement<T> configElement) {
		super(configScreen, configElement);
		this.children().add(this.textFieldWidget = new TextFieldWidget(Minecraft.getInstance().fontRenderer, 0, 0, 0, 0, getLabel()));
		this.textFieldWidget.setMaxStringLength(Integer.MAX_VALUE);
		this.textFieldWidget.setText(getConfigElement().get().toString());
		this.textFieldWidget.setCursorPositionZero(); // Remove weird scroll bug
	}

	@Override
	public boolean isValidValue() {
		final ConfigElement<T> configElement = (ConfigElement<T>) getConfigElement();
		try {
			T o = parse(this.textFieldWidget.getText());
			return configElement.getConfigElementContainer().getValueSpec().test(o);
		} catch (Exception e) {
			return false;
		}
	}

	@Nonnull
	@Override
	public TextFieldWidget getWidget() {
		return textFieldWidget;
	}

	public abstract T parse(final String text);

}
