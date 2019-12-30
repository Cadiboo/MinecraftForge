package net.minecraftforge.fml.client.config;

import com.electronwill.nightconfig.core.CommentedConfig;
import com.electronwill.nightconfig.core.Config;
import com.google.common.collect.Lists;
import com.google.common.util.concurrent.Runnables;
import net.minecraft.client.gui.widget.Widget;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.client.config.element.BooleanConfigElement;
import net.minecraftforge.fml.client.config.element.ConfigConfigElement;
import net.minecraftforge.fml.client.config.element.ConfigElementContainer;
import net.minecraftforge.fml.client.config.element.EnumConfigElement;
import net.minecraftforge.fml.client.config.element.IConfigElement;
import net.minecraftforge.fml.client.config.element.InfoTextConfigElement;
import net.minecraftforge.fml.client.config.element.ListConfigElement;
import net.minecraftforge.fml.client.config.element.LocalDateConfigElement;
import net.minecraftforge.fml.client.config.element.LocalDateTimeConfigElement;
import net.minecraftforge.fml.client.config.element.LocalTimeConfigElement;
import net.minecraftforge.fml.client.config.element.NumberConfigElement;
import net.minecraftforge.fml.client.config.element.OffsetDateTimeConfigElement;
import net.minecraftforge.fml.client.config.element.StringConfigElement;
import net.minecraftforge.fml.client.config.element.category.ConfigCategoryElement;
import net.minecraftforge.fml.client.config.entry.widget.BooleanButton;
import net.minecraftforge.fml.client.config.entry.widget.ByteTextField;
import net.minecraftforge.fml.client.config.entry.widget.ConfigButton;
import net.minecraftforge.fml.client.config.entry.widget.DoubleTextField;
import net.minecraftforge.fml.client.config.entry.widget.EnumButton;
import net.minecraftforge.fml.client.config.entry.widget.FloatTextField;
import net.minecraftforge.fml.client.config.entry.widget.IConfigListEntryWidget;
import net.minecraftforge.fml.client.config.entry.widget.InfoText;
import net.minecraftforge.fml.client.config.entry.widget.IntegerTextField;
import net.minecraftforge.fml.client.config.entry.widget.ListButton;
import net.minecraftforge.fml.client.config.entry.widget.LocalDateTextField;
import net.minecraftforge.fml.client.config.entry.widget.LocalDateTimeTextField;
import net.minecraftforge.fml.client.config.entry.widget.LocalTimeTextField;
import net.minecraftforge.fml.client.config.entry.widget.LongTextField;
import net.minecraftforge.fml.client.config.entry.widget.OffsetDateTimeTextField;
import net.minecraftforge.fml.client.config.entry.widget.StringTextField;
import net.minecraftforge.fml.config.ModConfig;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * Run away, here be dragons.
 * RUN AWAY!!!
 * <p>
 * All the hacks necessary for the ConfigScreen to work.
 * Handles creating {@link IConfigElement}s and {@link IConfigListEntryWidget}s.
 * <p>
 * Lots of unchecked casts, lots of raw uses of generic classes.
 * <p>
 * Also has the method for sorting
 *
 * @author Cadiboo
 */
public class ConfigTypesManager {

	private static final Function<?, ?> NO_FACTORY = o -> null;
	private static final Map<Class<?>, Function<ConfigElementContainer<?>, IConfigElement<?>>> CONFIG_ELEMENTS = new HashMap<>();
	private static final Map<Class<?>, Function<IConfigListEntryWidget.Callback<?>, IConfigListEntryWidget<?>>> WIDGETS = new HashMap<>();
	private static Comparator<IConfigElement<?>> CONFIG_ELEMENTS_COMPARATOR = null;
	private static Comparator<?> WIDGETS_COMPARATOR = null;

	static {
		register();
	}

	private static void register() {
		CONFIG_ELEMENTS.clear();
		registerElementFactory(Boolean.class, BooleanConfigElement::new);
		registerElementFactory(Byte.class, $ -> new NumberConfigElement<Byte>($) {
			@Override
			public Byte parse(final String text) throws NumberFormatException {
				return Byte.parseByte(text);
			}
		});
		registerElementFactory(Byte.class, $ -> new NumberConfigElement<Byte>($) {
			@Override
			public Byte parse(final String text) throws NumberFormatException {
				return Byte.parseByte(text);
			}
		});
		registerElementFactory(Integer.class, $ -> new NumberConfigElement<Integer>($) {
			@Override
			public Integer parse(final String text) throws NumberFormatException {
				return Integer.parseInt(text);
			}
		});
		registerElementFactory(Float.class, $ -> new NumberConfigElement<Float>($) {
			@Override
			public Float parse(final String text) throws NumberFormatException {
				return Float.parseFloat(text);
			}
		});
		registerElementFactory(Long.class, $ -> new NumberConfigElement<Long>($) {
			@Override
			public Long parse(final String text) throws NumberFormatException {
				return Long.parseLong(text);
			}
		});
		registerElementFactory(Double.class, $ -> new NumberConfigElement<Double>($) {
			@Override
			public Double parse(final String text) throws NumberFormatException {
				return Double.parseDouble(text);
			}
		});
		registerElementFactory(String.class, StringConfigElement::new);
		registerElementFactory(Enum.class, EnumConfigElement::new);
		registerElementFactory(List.class, ListConfigElement::new);
		registerElementFactory(LocalTime.class, LocalTimeConfigElement::new);
		registerElementFactory(LocalDate.class, LocalDateConfigElement::new);
		registerElementFactory(LocalDateTime.class, LocalDateTimeConfigElement::new);
		registerElementFactory(OffsetDateTime.class, OffsetDateTimeConfigElement::new);
		registerElementFactory(Config.class, ConfigConfigElement::new);

		WIDGETS.clear();
		registerWidgetFactory(Boolean.class, BooleanButton::new);
		registerWidgetFactory(Byte.class, ByteTextField::new);
		registerWidgetFactory(Integer.class, IntegerTextField::new);
		registerWidgetFactory(Float.class, FloatTextField::new);
		registerWidgetFactory(Long.class, LongTextField::new);
		registerWidgetFactory(Double.class, DoubleTextField::new);
		registerWidgetFactory(String.class, StringTextField::new);
		registerWidgetFactory(Enum.class, EnumButton::new);
		registerWidgetFactory(List.class, callback -> new ListButton<>(((ScreenedCallback) callback).screen, callback));
		registerWidgetFactory(LocalTime.class, LocalTimeTextField::new);
		registerWidgetFactory(LocalDate.class, LocalDateTextField::new);
		registerWidgetFactory(LocalDateTime.class, LocalDateTimeTextField::new);
		registerWidgetFactory(OffsetDateTime.class, OffsetDateTimeTextField::new);
		registerWidgetFactory(Config.class, callback -> new ConfigButton(((ScreenedCallback) callback).screen, callback));
		// ModConfig should NEVER be an element in a config list.
		registerWidgetFactory(ModConfig.class, $ -> new InfoText<>("fml.configgui.list.unsupportedTypeUseConfig", "ModConfig"));

		setElementComparator(Comparator.comparing(IConfigElement::getLabel));

		setWidgetComparator(Comparator.comparing(Widget::getMessage));
	}

	/**
	 * Registers a factory for making config elements for the specific class.
	 */
	@SuppressWarnings({"rawtypes", "unchecked"})
	public static <E> void registerElementFactory(final Class<E> clazz, final Function<ConfigElementContainer<E>, IConfigElement<E>> factory) {
		CONFIG_ELEMENTS.put(clazz, (Function) factory);
	}

	/**
	 * Registers a factory for making widgets for the specific class.
	 *
	 * @param <C> The type of the class
	 * @param <W> The type of the widget
	 */
	@SuppressWarnings({"rawtypes", "unchecked"})
	public static <C, W extends Widget & IConfigListEntryWidget<C>> void registerWidgetFactory(final Class<C> clazz, final Function<IConfigListEntryWidget.Callback<C>, W> factory) {
		WIDGETS.put(clazz, (Function) factory);
	}

	/**
	 * Set the comparator that will be used to sort lists of config elements before displaying them on the screen.
	 */
	public static void setElementComparator(final Comparator<IConfigElement<?>> comparator) {
		CONFIG_ELEMENTS_COMPARATOR = comparator;
	}

	/**
	 * Set the comparator that will be used to sort lists of widgets before displaying them on the screen.
	 *
	 * @param <W> The type of the widget
	 */
	public static <W extends Widget & IConfigListEntryWidget<?>> void setWidgetComparator(final Comparator<W> comparator) {
		WIDGETS_COMPARATOR = comparator;
	}

	/**
	 * @param obj Either a ConfigValue or a Config
	 * @see #getSpecConfigValues(ModConfig)
	 */
	@SuppressWarnings({"unchecked"})
	public static IConfigElement<?> makeConfigElement(final ModConfig modConfig, final String path, final Object obj) {
		if (obj instanceof ForgeConfigSpec.ConfigValue) {
			final ForgeConfigSpec.ConfigValue<?> configValue = (ForgeConfigSpec.ConfigValue<?>) obj;
			// Because the obj is a ConfigValue the corresponding object in the ValueSpec map must be a ValueSpec
			final ForgeConfigSpec.ValueSpec valueSpec = (ForgeConfigSpec.ValueSpec) getValueSpec(modConfig, configValue.getPath());

			Class<?> clazz = valueSpec.getClazz();
			if (clazz == Object.class) {
				final Object actualValue = configValue.get();
				final Class<?> valueClass = actualValue.getClass();
				if (valueClass != Object.class)
					clazz = valueClass;
				else {
					final Object defaultValue = valueSpec.getDefault();
					if (defaultValue != null) // Should NEVER happen
						clazz = defaultValue.getClass();
				}
			}

			if (clazz == null || clazz == Object.class)
				return new InfoTextConfigElement<>("(No element factory applicable for null class) " + path);

			Function<ConfigElementContainer<?>, IConfigElement<?>> factory = recursiveGetFactory(clazz, clazz, (Map) CONFIG_ELEMENTS);
			if (factory != null && factory != NO_FACTORY) {
				try {
					return factory.apply(new ConfigElementContainer(configValue.getPath(), modConfig, configValue));
				} catch (Exception e) {
					e.printStackTrace();
					return new InfoTextConfigElement<>("(An error occurred trying to create the element for object " + clazz.getSimpleName() + ") " + path);
				}
			} else {
				return new InfoTextConfigElement<>("(No element factory applicable for object " + clazz.getSimpleName() + ") " + path);
			}
		} else if (obj instanceof Config) {
			final Config config = (Config) obj;
			final List<String> split = ForgeConfigSpec.split(path);
			split.remove(split.size() - 1);
			final @Nullable Object parentConfig;
			if (split.isEmpty())
				parentConfig = modConfig.getConfigData();
			else
				parentConfig = getValue(modConfig, split);
			if (parentConfig instanceof CommentedConfig)
				return new ConfigCategoryElement(config, modConfig, ((CommentedConfig) parentConfig), path);
			else
				return new ConfigCategoryElement(config, modConfig, null, path);
		} else {
			throw new IllegalStateException("How? " + path + ", " + obj);
//			return new InfoTextConfigElement<>("Uh, this is an error... " + path + ", " + obj);
		}
	}

	@SuppressWarnings({"unchecked", "rawtypes", "UnstableApiUsage"})
	public static <W> W makeWidget(@Nonnull final List list, @Nonnull final ConfigScreen configScreen, @Nonnull final Predicate<Object> isValidPredicate, @Nonnull final Object obj) {
		final Class<?> clazz = obj.getClass();

		if (clazz == null || clazz == Object.class)
			return (W) new InfoText("fml.configgui.list.nullTypeUseConfig");

		Function<IConfigListEntryWidget.Callback<?>, IConfigListEntryWidget<?>> factory = recursiveGetFactory(clazz, clazz, (Map) WIDGETS);
		if (factory != null && factory != NO_FACTORY) {
			try {
				// TODO: Do these indices better? What about duplicate values? Pass in index as param?
				AtomicReference<Object> atomicReference = new AtomicReference<>();
				atomicReference.set(obj);
				Supplier getter = () -> list.get(list.indexOf(atomicReference.get()));
				Consumer setter = newObj -> {
					final int i = list.indexOf(getter.get());
					list.set(i, newObj);
					atomicReference.set(newObj);
				};
				final Supplier defaultValueGetter = () -> obj;
				final BooleanSupplier isDefault = () -> true;
				final Runnable resetToDefault = Runnables.doNothing();
				final BooleanSupplier isChanged = () -> !Objects.equals(getter.get(), obj);
				final Runnable undoChanges = () -> setter.accept(obj);
				// Ew
				final Predicate isValid = o -> isValidPredicate.test(Lists.newArrayList(getter.get()));
				return (W) factory.apply(new ScreenedCallback<>(getter, setter, defaultValueGetter, isDefault, resetToDefault, isChanged, undoChanges, isValid, configScreen));
			} catch (Exception e) {
				e.printStackTrace();
				return (W) new InfoText("fml.configgui.list.errorForTypeUseConfig" + clazz.getSimpleName());
			}
		} else {
			return (W) new InfoText("fml.configgui.list.unsupportedTypeUseConfig", clazz.getSimpleName());
		}
	}

	@SuppressWarnings({"unchecked", "rawtypes", "UnstableApiUsage"})
	public static <W> W makeWidget(@Nonnull final Config config, @Nonnull final ConfigScreen configScreen, @Nonnull final Predicate<Object> isValidPredicate, @Nonnull final String path, @Nonnull final Object obj) {
		final Class<?> clazz = obj.getClass();

		if (clazz == null || clazz == Object.class)
			return (W) new InfoText("fml.configgui.list.nullTypeUseConfig");

		Function<IConfigListEntryWidget.Callback<?>, IConfigListEntryWidget<?>> factory = recursiveGetFactory(clazz, clazz, (Map) WIDGETS);
		if (factory != null && factory != NO_FACTORY) {
			try {
				Supplier getter = () -> config.get(path);
				Consumer setter = newObj -> config.set(path, newObj);
				final Supplier defaultValueGetter = () -> obj;
				final BooleanSupplier isDefault = () -> true;
				final Runnable resetToDefault = Runnables.doNothing();
				final BooleanSupplier isChanged = () -> !Objects.equals(getter.get(), obj);
				final Runnable undoChanges = () -> setter.accept(obj);
				// Ew
				final Predicate isValid = o -> {
					final Config temp = config.configFormat().createConfig();
					temp.add(path, getter.get());
					return isValidPredicate.test(config);
				};
				return (W) factory.apply(new ScreenedCallback<>(getter, setter, defaultValueGetter, isDefault, resetToDefault, isChanged, undoChanges, isValid, configScreen));
			} catch (Exception e) {
				e.printStackTrace();
				return (W) new InfoText("fml.configgui.list.errorForTypeUseConfig" + clazz.getSimpleName());
			}
		} else {
			return (W) new InfoText("fml.configgui.list.unsupportedTypeUseConfig", clazz.getSimpleName());
		}
	}

	@Nullable
	private static <T extends Function<A, B>, A, B> T recursiveGetFactory(final Class<?> clazz, final Class<?> originalClass, final Map<Class<?>, Function> factories) {
		if (clazz == null || clazz == Object.class)
			return null;
		Function factory = factories.get(clazz);
		if (factory == null)
			factory = recursiveGetFactory(clazz.getSuperclass(), clazz, factories);
		if (factory == null) {
			for (final Class<?> anInterface : clazz.getInterfaces()) {
				factory = recursiveGetFactory(anInterface, clazz, factories);
				if (factory != null)
					break;
			}
		}
		if (factory == null)
			factories.put(clazz, NO_FACTORY); // A factory does NOT exist, avoid checking hierarchy as extensively in the future.
		else
			factories.put(originalClass, factory); // A factory DOES exist, avoid checking the class' entire hierarchy in the future.
		return (T) factory;
	}

	public static Map<String, Object> getSpecValueSpecs(final ModConfig modConfig) {
		// name -> ValueSpec|SimpleConfig
		return modConfig.getSpec().valueMap();
	}

	public static Map<String, Object> getSpecConfigValues(final ModConfig modConfig) {
		// name -> ConfigValue|SimpleConfig
		return modConfig.getSpec().getValues().valueMap();
	}

	public static Map<String, Object> getConfigValues(final ModConfig modConfig) {
		// name -> Object
		return modConfig.getConfigData().valueMap();
	}

	public static Object getValueSpec(final ModConfig modConfig, final List<String> path) {
		// name -> ValueSpec|SimpleConfig
		final Map<String, Object> specValueSpecs = getSpecValueSpecs(modConfig);

		// Either a ValueSpec or a SimpleConfig
		Object ret = specValueSpecs;

		for (final String s : path) {
			if (ret instanceof Map) // First iteration
				ret = ((Map<String, Object>) ret).get(s);
			else if (ret instanceof ForgeConfigSpec.ValueSpec)
				return ret; // Uh, shouldn't happen? TODO: Throw error?
			else if (ret instanceof Config)
				ret = ((Config) ret).get(s);
		}
		return ret;
	}

	public static Object getConfigValue(final ModConfig modConfig, final List<String> path) {
		// name -> ConfigValue|SimpleConfig
		final Map<String, Object> specConfigVales = getSpecConfigValues(modConfig);

		// Either a ConfigValue or a SimpleConfig
		Object ret = specConfigVales;

		for (final String s : path) {
			if (ret instanceof Map) // First iteration
				ret = ((Map<String, Object>) ret).get(s);
			else if (ret instanceof ForgeConfigSpec.ConfigValue)
				return ret; // Uh, shouldn't happen? TODO: Throw error?
			else if (ret instanceof Config)
				ret = ((Config) ret).get(s);
		}
		return ret;
	}

	public static Object getValue(final ModConfig modConfig, final List<String> path) {
		// name -> Object
		final Map<String, Object> specConfigVales = getConfigValues(modConfig);

		// Either a ConfigValue or a SimpleConfig
		Object ret = specConfigVales;

		for (final String s : path) {
			if (ret instanceof Map) // First iteration
				ret = ((Map<String, Object>) ret).get(s);
//			else if (ret instanceof Something)
//				return ret; // Uh, shouldn't happen? TODO: Throw error?
			else if (ret instanceof Config)
				ret = ((Config) ret).get(s);
		}
		return ret;
	}

	public static void sortElements(final List<IConfigElement<?>> configElements) {
		configElements.sort(CONFIG_ELEMENTS_COMPARATOR);
	}

	public static <W extends Widget & IConfigListEntryWidget<?>> void sortWidgets(final List<W> widgets) {
		widgets.sort(Comparator.comparing(Widget::getMessage));
	}

	/**
	 *
	 * @param <T>
	 */
	public static class ScreenedCallback<T> extends IConfigListEntryWidget.Callback<T> {

		public final ConfigScreen screen;

		public ScreenedCallback(final Supplier<T> getter, final Consumer<T> setter, final Supplier<T> defaultValueGetter, final BooleanSupplier isDefault, final Runnable resetToDefault, final BooleanSupplier isChanged, final Runnable undoChanges, final Predicate<Object> isValid, final ConfigScreen screen) {
			super(getter, setter, defaultValueGetter, isDefault, resetToDefault, isChanged, undoChanges, isValid);
			this.screen = screen;
		}

		public ScreenedCallback(final Supplier<T> getter, final Consumer<T> setter, final Supplier<T> defaultValueGetter, final BooleanSupplier isDefault, final Runnable resetToDefault, final BooleanSupplier isChanged, final Runnable undoChanges, final Predicate<Object> isValid, @Nullable final Runnable save, final ConfigScreen screen) {
			super(getter, setter, defaultValueGetter, isDefault, resetToDefault, isChanged, undoChanges, isValid, save);
			this.screen = screen;
		}

	}

}
