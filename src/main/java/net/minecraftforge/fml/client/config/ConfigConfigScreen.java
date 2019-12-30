package net.minecraftforge.fml.client.config;

import com.electronwill.nightconfig.core.AbstractConfig;
import com.electronwill.nightconfig.core.Config;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.fml.client.config.entry.ConfigConfigListEntry;
import net.minecraftforge.fml.client.config.entry.ConfigListEntry;
import net.minecraftforge.fml.client.config.entry.widget.IConfigListEntryWidget;
import net.minecraftforge.fml.client.config.entry.widget.InfoText;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

/**
 * A ConfigScreen for a Config (ConfigValue).
 *
 * @author Cadiboo
 */
public class ConfigConfigScreen extends ConfigScreen {

	private final IConfigListEntryWidget.Callback<Config> callback;

	// Original is used as the value for resetToDefault
	private final Config original;
	// Clone is used for everything else
	private final Config clone;
	private final boolean isUnmodifiable;

	public ConfigConfigScreen(final ConfigScreen owningScreen, final IConfigListEntryWidget.Callback<Config> callback) {
		super(owningScreen.getTitle(), owningScreen, owningScreen.modContainer);
		this.callback = callback;
		this.original = callback.get();
		this.isUnmodifiable = isUnmodifiable(original);
		if (this.original instanceof AbstractConfig)
			this.clone = ((AbstractConfig) original).clone();
		else
			throw new IllegalStateException("Could not clone Config");
		this.callback.set(clone);

	}

	public static boolean isUnmodifiable(final Config original) {
		// Yeah... what? Config extends UnmodifiableConfig.
		// Maybe I should accept UnmodifiableConfig instead of Config as params?
		// Looks like the config passed in is never unmodifiable though, which makes
		// sense as this is a gui for modifying configs.
		return !(original instanceof Config);
	}

	public boolean isUnmodifiable() {
		return isUnmodifiable;
	}

	@Override
	public void init() {
		super.init();

		final Config config = callback.get();

		final ArrayList<? extends Widget> widgets = makeWidgets(config, this, o -> callback.isValid());
		if (!isUnmodifiable() && widgets.isEmpty()) {
			final Widget w = new InfoText<>(I18n.format("fml.configgui.noElements"));
			this.getEntryList().children().add(new ConfigConfigListEntry(this, w, false));
			return;
		}
		final List<ConfigListEntry<?>> configListEntries = this.getEntryList().children();
		final boolean isModifiable = !isUnmodifiable();
		widgets.forEach(w -> configListEntries.add(new ConfigConfigListEntry(this, w, isModifiable)));
	}

	protected <W extends Widget & IConfigListEntryWidget<Object>> ArrayList<W> makeWidgets(final Config config, final ConfigScreen configScreen, final Predicate<Object> elementValidator) {
		final ArrayList<W> elements = new ArrayList<>();
		config.valueMap().forEach((path, obj) -> elements.add(ConfigTypesManager.makeWidget(config, configScreen, elementValidator, path, obj)));
		return elements;
	}

}
