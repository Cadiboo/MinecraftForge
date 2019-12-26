package net.minecraftforge.fml.client.config.element;

import net.minecraftforge.fml.client.config.ConfigEntryListWidget;
import net.minecraftforge.fml.client.config.ConfigScreen;
import net.minecraftforge.fml.client.config.entry.ConfigElementContainer;
import net.minecraftforge.fml.client.config.entry2.ConfigListEntry;
import net.minecraftforge.fml.client.config.entry2.widget.BooleanButton;
import net.minecraftforge.fml.client.config.entry2.widget.WidgetValueReference;

/**
 * @author Cadiboo
 */
public class BooleanConfigElement extends ConfigElement<Boolean> {

	public BooleanConfigElement(final ConfigElementContainer<Boolean> entryConfigValue) {
		super(entryConfigValue);
	}

	@Override
	public ConfigListEntry<Boolean> makeWidgetThing(final ConfigScreen configScreen, final ConfigEntryListWidget configEntryListWidget) {
		final WidgetValueReference<Boolean> booleanWidgetValueReference = new WidgetValueReference<>(this::get, this::set, this::getDefault, this::isDefault, this::resetToDefault, this::isChanged, this::undoChanges, this::isValid, this::save);
		final BooleanButton widget = new BooleanButton(booleanWidgetValueReference);
		return new ConfigListEntry<>(configScreen, widget);
	}

//	@Override
//	public ConfigListEntry<Boolean> makeListWidgetThing(final ListConfigScreen listConfigScreen, final ConfigEntryListWidget configEntryListWidget) {
//		final WidgetValueReference<Boolean> booleanWidgetValueReference = new WidgetValueReference<>(this::get, this::set, this::getDefault, this::isDefault, this::resetToDefault, this::isChanged, this::undoChanges, this::isValid, this::save);
//		final BooleanButton widget = new BooleanButton(booleanWidgetValueReference);
//		return new ConfigListEntry<>(listConfigScreen, widget);
//	}

}
