package net.minecraftforge.fml.client.config;

import com.electronwill.nightconfig.core.Config;
import joptsimple.internal.Strings;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.client.config.element.BooleanConfigElement;
import net.minecraftforge.fml.client.config.element.ConfigConfigElement;
import net.minecraftforge.fml.client.config.element.ConfigElement;
import net.minecraftforge.fml.client.config.element.DummyConfigElement;
import net.minecraftforge.fml.client.config.element.ListConfigElement;
import net.minecraftforge.fml.client.config.entry.ConfigElementContainer;
import net.minecraftforge.fml.config.ModConfig;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author Cadiboo
 */
public class ConfigEntriesManager {

	/**
	 * @param obj Either a ConfigValue or a Config
	 * @see #getSpecConfigValues(ModConfig)
	 */
	public static ConfigElement<?> makeConfigElement(final ModConfig modConfig, final String name, final Object obj) {
		if (obj instanceof ForgeConfigSpec.ConfigValue) {
			final ForgeConfigSpec.ConfigValue<?> configValue = (ForgeConfigSpec.ConfigValue<?>) obj;
			// Because the obj is a ConfigValue the corresponding object in the ValueSpec map must be a ValueSpec
			final ForgeConfigSpec.ValueSpec valueSpec = (ForgeConfigSpec.ValueSpec) getValueSpec(modConfig, configValue.getPath());

			Class<?> clazz = valueSpec.getClazz();
			if (clazz == Object.class) {
				final Object actualValue = configValue.get();
				final Class<?> valueClass = actualValue.getClass();
				if (valueClass != null)
					clazz = valueClass;
				else {
					final Object defaultValue = valueSpec.getDefault();
					if (defaultValue != null)
						clazz = defaultValue.getClass();
				}
			}
			if (Boolean.class.isAssignableFrom(clazz)) {
				return new BooleanConfigElement(new ConfigElementContainer<>(configValue.getPath(), modConfig, (ForgeConfigSpec.ConfigValue<Boolean>) configValue));
//			} else if (Byte.class.isAssignableFrom(clazz)) {
//				return new NumberConfigElement<Byte>(new ConfigElementContainer<>(configValue.getPath(), modConfig, (ForgeConfigSpec.ConfigValue<Byte>) configValue)) {
//					@Override
//					public Byte parse(final String text) throws NumberFormatException {
//						return Byte.parseByte(text);
//					}
//				};
//			} else if (Integer.class.isAssignableFrom(clazz)) {
//				return new NumberConfigElement<Integer>(new ConfigElementContainer<>(configValue.getPath(), modConfig, (ForgeConfigSpec.ConfigValue<Integer>) configValue)) {
//					@Override
//					public Integer parse(final String text) throws NumberFormatException {
//						return Integer.parseInt(text);
//					}
//				};
//			} else if (Float.class.isAssignableFrom(clazz)) {
//				return new NumberConfigElement<Float>(new ConfigElementContainer<>(configValue.getPath(), modConfig, (ForgeConfigSpec.ConfigValue<Float>) configValue)) {
//					@Override
//					public Float parse(final String text) throws NumberFormatException {
//						return Float.parseFloat(text);
//					}
//				};
//			} else if (Long.class.isAssignableFrom(clazz)) {
//				return new NumberConfigElement<Long>(new ConfigElementContainer<>(configValue.getPath(), modConfig, (ForgeConfigSpec.ConfigValue<Long>) configValue)) {
//					@Override
//					public Long parse(final String text) throws NumberFormatException {
//						return Long.parseLong(text);
//					}
//				};
//			} else if (Double.class.isAssignableFrom(clazz)) {
//				return new NumberConfigElement<Double>(new ConfigElementContainer<>(configValue.getPath(), modConfig, (ForgeConfigSpec.ConfigValue<Double>) configValue)) {
//					@Override
//					public Double parse(final String text) throws NumberFormatException {
//						return Double.parseDouble(text);
//					}
//				};
//			} else if (String.class.isAssignableFrom(clazz)) {
//				return new StringConfigElement(new ConfigElementContainer<>(configValue.getPath(), modConfig, (ForgeConfigSpec.ConfigValue<String>) configValue));
//			} else if (Enum.class.isAssignableFrom(clazz)) {
//				return new EnumConfigElement<>(new ConfigElementContainer<>(configValue.getPath(), modConfig, (ForgeConfigSpec.ConfigValue<Enum<?>>) configValue));
//			} else if (List.class.isAssignableFrom(clazz)) {
//				return new ListConfigElement<>(new ConfigElementContainer<>(configValue.getPath(), modConfig, (ForgeConfigSpec.ConfigValue<List<?>>) configValue));
//			} else if (LocalTime.class.isAssignableFrom(clazz)) {
//				return new LocalTimeConfigElement(new ConfigElementContainer<>(configValue.getPath(), modConfig, (ForgeConfigSpec.ConfigValue<LocalTime>) configValue));
//			} else if (LocalDate.class.isAssignableFrom(clazz)) {
//				return new LocalDateConfigElement(new ConfigElementContainer<>(configValue.getPath(), modConfig, (ForgeConfigSpec.ConfigValue<LocalDate>) configValue));
//			} else if (LocalDateTime.class.isAssignableFrom(clazz)) {
//				return new LocalDateTimeConfigElement(new ConfigElementContainer<>(configValue.getPath(), modConfig, (ForgeConfigSpec.ConfigValue<LocalDateTime>) configValue));
//			} else if (OffsetDateTime.class.isAssignableFrom(clazz)) {
//				return new OffsetDateTimeConfigElement(new ConfigElementContainer<>(configValue.getPath(), modConfig, (ForgeConfigSpec.ConfigValue<OffsetDateTime>) configValue));
			} else {
				return new DummyConfigElement("(Unknown object " + clazz.getSimpleName() + ") " + name);
			}
		} else if (obj instanceof Config) {
			final Config config = (Config) obj;
			String translationKey = ""; // TODO: No clue how to get this for categories. Doesn't seem to exist currently?
			String label = I18n.format(translationKey);
			if (Objects.equals(translationKey, label))
				label = name;
			String comment = modConfig.getConfigData().getComment(name); // TODO: Only works for top-level categories?
			if (Strings.isNullOrEmpty(comment))
				comment = "";
			return new ConfigConfigElement(config, modConfig, label, translationKey, comment);
		} else {
			throw new IllegalStateException("How? " + name + ", " + obj);
		}
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
