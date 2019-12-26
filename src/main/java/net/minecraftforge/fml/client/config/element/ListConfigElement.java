package net.minecraftforge.fml.client.config.element;

import net.minecraft.client.MainWindow;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.fml.client.config.ConfigEntryListWidget;
import net.minecraftforge.fml.client.config.ConfigScreen;
import net.minecraftforge.fml.client.config.ConfigTypesManager;
import net.minecraftforge.fml.client.config.ListConfigScreen;
import net.minecraftforge.fml.client.config.entry.ConfigListEntry;
import net.minecraftforge.fml.client.config.entry.ListConfigListEntry;
import net.minecraftforge.fml.client.config.entry.ScreenElementConfigListEntry;
import net.minecraftforge.fml.client.config.entry.widget.ConfigListEntryWidget;
import net.minecraftforge.fml.client.config.entry.widget.ScreenButton;
import net.minecraftforge.fml.client.config.entry.widget.WidgetValueReference;
import net.minecraftforge.fml.config.ModConfig;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Cadiboo
 */
public class ListConfigElement<T extends List<?>> extends ConfigElement<T> {

	private final int initialHash;
	private final Object firstOrNull;

	public ListConfigElement(final ConfigElementContainer<T> elementContainer) {
		super(elementContainer);
		final T list = get();
		this.initialHash = list.hashCode();
		if (list.isEmpty())
			firstOrNull = null;
		else
			firstOrNull = list.get(0);
	}

	protected <W extends Widget & ConfigListEntryWidget<Object>> List<W> makeWidgets(final List<?> list, final ModConfig modConfig) {
		final List<W> elements = new ArrayList<>();
		list.forEach(obj -> elements.add(ConfigTypesManager.makeWidget(this, obj)));
		return elements;
	}

	@Override
	public boolean isChanged() {
		final int currentHash = get().hashCode();
		return initialHash != currentHash;
	}

	@Override
	public void undoChanges() {
		// TODO: Dunno what to do here
	}

	@Override
	public ConfigListEntry<T> makeConfigListEntry(final ConfigScreen configScreen, final ConfigEntryListWidget configEntryListWidget) {
		final WidgetValueReference<T> widgetValueReference = new WidgetValueReference<>(this::get, this::set, this::getDefault, this::isDefault, this::resetToDefault, this::isChanged, this::undoChanges, this::isValid, this::save);
		final Screen screen = makeScreen(configScreen, configEntryListWidget);
		final ScreenButton<T> widget = new ScreenButton<>(getLabel(), widgetValueReference, screen);
		return new ScreenElementConfigListEntry<>(configScreen, widget, this);
	}

	public Screen makeScreen(final ConfigScreen owningScreen, final ConfigEntryListWidget configEntryListWidget) {
		final ConfigScreen configScreen = new ListConfigScreen(owningScreen.getTitle(), owningScreen, owningScreen.modContainer) {
			@Override
			public void init() {
				super.init();
				makeWidgets(get(), getConfigElementContainer().getModConfig())
						.forEach(w -> this.getEntryList().children()
								.add(new ListConfigListEntry(this, w) {
									     @Override
									     public void removeEntry() {
										     final List<?> list = ListConfigElement.this.get();
										     final WidgetValueReference<?> widgetValueReference = ((ConfigListEntryWidget<?>) this.getWidget()).getWidgetValueReference();
										     if (widgetValueReference == null)
											     return; // DummyConfigElement (Unsupported Object)
										     list.remove(widgetValueReference.get());
										     needsRefresh = true;
										     final Minecraft minecraft = Minecraft.getInstance();
										     final MainWindow mainWindow = minecraft.func_228018_at_();
										     init(minecraft, mainWindow.getScaledWidth(), mainWindow.getScaledHeight());
									     }

									     @Override
									     public void addEntryBelow() {
										     final List list = ListConfigElement.this.get();

										     final Object o;
										     if (list.isEmpty())
											     if (firstOrNull == null)
												     return;
											     else o = firstOrNull;
										     else
											     o = list.get(0);

										     list.add(o);
										     needsRefresh = true;
										     final Minecraft minecraft = Minecraft.getInstance();
										     final MainWindow mainWindow = minecraft.func_228018_at_();
										     init(minecraft, mainWindow.getScaledWidth(), mainWindow.getScaledHeight());
									     }
								     }
								)
						);
			}
		};
		final ITextComponent subtitle;
		if (owningScreen.getSubtitle() == null)
			subtitle = new StringTextComponent(getLabel());
		else
			subtitle = owningScreen.getSubtitle().deepCopy().appendSibling(new StringTextComponent(ConfigScreen.CATEGORY_DIVIDER + getLabel()));
		configScreen.setSubtitle(subtitle);

		return configScreen;
	}

}
