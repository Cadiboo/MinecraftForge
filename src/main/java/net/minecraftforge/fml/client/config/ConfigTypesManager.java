package net.minecraftforge.fml.client.config;

import com.electronwill.nightconfig.core.Config;
import com.google.common.collect.Lists;
import com.google.common.util.concurrent.Runnables;
import net.minecraft.client.gui.widget.Widget;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.client.config.element.BooleanConfigElement;
import net.minecraftforge.fml.client.config.element.ConfigConfigElement;
import net.minecraftforge.fml.client.config.element.ConfigElementContainer;
import net.minecraftforge.fml.client.config.element.DummyConfigElement;
import net.minecraftforge.fml.client.config.element.EnumConfigElement;
import net.minecraftforge.fml.client.config.element.IConfigElement;
import net.minecraftforge.fml.client.config.element.ListConfigElement;
import net.minecraftforge.fml.client.config.element.LocalDateConfigElement;
import net.minecraftforge.fml.client.config.element.LocalDateTimeConfigElement;
import net.minecraftforge.fml.client.config.element.LocalTimeConfigElement;
import net.minecraftforge.fml.client.config.element.NumberConfigElement;
import net.minecraftforge.fml.client.config.element.OffsetDateTimeConfigElement;
import net.minecraftforge.fml.client.config.element.StringConfigElement;
import net.minecraftforge.fml.client.config.entry.DummyConfigListEntry;
import net.minecraftforge.fml.client.config.entry.widget.BooleanButton;
import net.minecraftforge.fml.client.config.entry.widget.ByteTextField;
import net.minecraftforge.fml.client.config.entry.widget.ConfigListEntryWidget;
import net.minecraftforge.fml.client.config.entry.widget.DoubleTextField;
import net.minecraftforge.fml.client.config.entry.widget.EnumButton;
import net.minecraftforge.fml.client.config.entry.widget.FloatTextField;
import net.minecraftforge.fml.client.config.entry.widget.IntegerTextField;
import net.minecraftforge.fml.client.config.entry.widget.LocalDateTextField;
import net.minecraftforge.fml.client.config.entry.widget.LocalDateTimeTextField;
import net.minecraftforge.fml.client.config.entry.widget.LocalTimeTextField;
import net.minecraftforge.fml.client.config.entry.widget.LongTextField;
import net.minecraftforge.fml.client.config.entry.widget.OffsetDateTimeTextField;
import net.minecraftforge.fml.client.config.entry.widget.StringTextField;
import net.minecraftforge.fml.client.config.entry.widget.WidgetValueReference;
import net.minecraftforge.fml.config.ModConfig;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
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
 *
 * @author Cadiboo
 */
public class ConfigTypesManager {

	private static final Function NO_FACTORY = o -> null;
	private static final Map<Class<?>, Function<ConfigElementContainer<?>, IConfigElement<?>>> CONFIG_ELEMENTS = new HashMap<>();
	private static final Map<Class<?>, Function<WidgetValueReference<?>, ConfigListEntryWidget<?>>> WIDGETS = new HashMap<>();

	static {
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

		registerWidgetFactory(Boolean.class, BooleanButton::new);
		registerWidgetFactory(Byte.class, ByteTextField::new);
		registerWidgetFactory(Integer.class, IntegerTextField::new);
		registerWidgetFactory(Float.class, FloatTextField::new);
		registerWidgetFactory(Long.class, LongTextField::new);
		registerWidgetFactory(Double.class, DoubleTextField::new);
		registerWidgetFactory(String.class, StringTextField::new);
		registerWidgetFactory(Enum.class, EnumButton::new);
//		registerWidgetFactory(List.class, ListButton::new); // TODO
		registerWidgetFactory(List.class, $ -> new DummyConfigListEntry.DummyWidget("Unsupported nested list type: List"));
		registerWidgetFactory(LocalTime.class, LocalTimeTextField::new);
		registerWidgetFactory(LocalDate.class, LocalDateTextField::new);
		registerWidgetFactory(LocalDateTime.class, LocalDateTimeTextField::new);
		registerWidgetFactory(OffsetDateTime.class, OffsetDateTimeTextField::new);
//		registerWidgetFactory(Config.class, ConfigButton::new); // TODO
		registerWidgetFactory(Config.class, $ -> new DummyConfigListEntry.DummyWidget("Unsupported nested list type: Config"));
		// ModConfig should NEVER be an element in a config list.
		registerWidgetFactory(ModConfig.class, $ -> new DummyConfigListEntry.DummyWidget("Unsupported nested list type: ModConfig"));
	}

	@SuppressWarnings({"rawtypes", "unchecked"})
	public static <E> void registerElementFactory(final Class<E> clazz, final Function<ConfigElementContainer<E>, IConfigElement<E>> factory) {
		CONFIG_ELEMENTS.put(clazz, (Function) factory);
	}

	@SuppressWarnings({"rawtypes", "unchecked"})
	public static <C, W extends Widget & ConfigListEntryWidget<C>> void registerWidgetFactory(final Class<C> clazz, final Function<WidgetValueReference<C>, W> factory) {
		WIDGETS.put(clazz, (Function) factory);
	}

	/**
	 * @param obj Either a ConfigValue or a Config
	 * @see #getSpecConfigValues(ModConfig)
	 */
	@SuppressWarnings({"unchecked"})
	public static IConfigElement<?> makeConfigElement(final ModConfig modConfig, @Deprecated final String name, final Object obj) {
		if (obj instanceof ForgeConfigSpec.ConfigValue) {
			final ForgeConfigSpec.ConfigValue<?> configValue = (ForgeConfigSpec.ConfigValue<?>) obj;
			// Because the obj is a ConfigValue the corresponding object in the ValueSpec map must be a ValueSpec
			final ForgeConfigSpec.ValueSpec valueSpec = (ForgeConfigSpec.ValueSpec) getValueSpec(modConfig, configValue.getPath());

			// I give up. defineInList just isn't type safe.

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
//				final Object defaultValue = valueSpec.getDefault();
//				if (defaultValue != null) // Should NEVER happen
//					clazz = defaultValue.getClass();
//				else {
//					final Object actualValue = configValue.get();
//					final Class<?> valueClass = actualValue.getClass();
//					if (valueClass != Object.class)
//						clazz = valueClass;
//				}
			}

			if (clazz == null || clazz == Object.class)
				return new DummyConfigElement<>("(No element factory applicable for null class) " + name);

			Function<ConfigElementContainer<?>, IConfigElement<?>> factory = recursiveGetFactory(clazz, clazz, (Map) CONFIG_ELEMENTS);
			if (factory != null && factory != NO_FACTORY) {
				try {
					return factory.apply(new ConfigElementContainer(configValue.getPath(), modConfig, configValue));
				} catch (Exception e) {
					e.printStackTrace();
					return new DummyConfigElement<>("(An error occurred trying to create the element for object " + clazz.getSimpleName() + ") " + name);
				}
			} else {
				return new DummyConfigElement<>("(No element factory applicable for object " + clazz.getSimpleName() + ") " + name);
			}
		} else if (obj instanceof Config) {
			final Config config = (Config) obj;
			return new ConfigConfigElement(config, modConfig, name);
		} else {
			throw new IllegalStateException("How? " + name + ", " + obj);
		}
	}

	@SuppressWarnings({"unchecked", "rawtypes", "UnstableApiUsage"})
	public static <T extends List<?>, W> W makeWidget(@Nonnull final ListConfigElement<T> listConfigElement, @Nonnull final Object obj) {
		final Class<?> clazz = obj.getClass();
		final List list = listConfigElement.get();

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
		final Predicate isValid = o -> listConfigElement.isValid((T) Lists.newArrayList(getter.get()));

		Function<WidgetValueReference<?>, ConfigListEntryWidget<?>> factory = recursiveGetFactory(clazz, clazz, (Map) WIDGETS);
		if (factory != null && factory != NO_FACTORY) {
			try {
				return (W) factory.apply(new WidgetValueReference<>(getter, setter, defaultValueGetter, isDefault, resetToDefault, isChanged, undoChanges, isValid));
			} catch (Exception e) {
				e.printStackTrace();
				return (W) new DummyConfigListEntry.DummyWidget("An error occurred trying to create the widget for object " + clazz.getSimpleName());
			}
		} else {
			return (W) new DummyConfigListEntry.DummyWidget("No widget factory applicable for object " + clazz.getSimpleName());
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
			factories.put(clazz, NO_FACTORY);
		else
			factories.put(originalClass, factory);
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

}
