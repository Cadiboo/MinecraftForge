package net.minecraftforge.fml.client.config;

import com.electronwill.nightconfig.core.CommentedConfig;
import com.electronwill.nightconfig.core.Config;
import com.electronwill.nightconfig.toml.TomlFormat;
import com.google.common.collect.Lists;
import joptsimple.internal.Strings;
import net.minecraft.item.DyeColor;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;
import net.minecraftforge.common.ForgeConfigSpec.ByteValue;
import net.minecraftforge.common.ForgeConfigSpec.ConfigValue;
import net.minecraftforge.common.ForgeConfigSpec.DoubleValue;
import net.minecraftforge.common.ForgeConfigSpec.EnumValue;
import net.minecraftforge.common.ForgeConfigSpec.FloatValue;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;
import net.minecraftforge.common.ForgeConfigSpec.LongValue;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.forgespi.language.IModInfo;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static net.minecraftforge.fml.loading.LogMarkers.FORGEMOD;

/**
 * TODO: REMOVE - TESTING ONLY
 *
 * @author Cadiboo
 */
@Mod.EventBusSubscriber(modid = "forge", bus = Mod.EventBusSubscriber.Bus.MOD)
public class TestConfig {

	public static final Common COMMON;
	public static final ForgeConfigSpec COMMON_SPEC;

	private static boolean aBoolean;
	private static byte aByte;
	private static int anInt;
	private static float aFloat;
	private static long aLong;
	private static double aDouble;
	private static DyeColor anEnum;
	private static String aString;
	// see com.electronwill.nightconfig.toml.TemporalParser
	private static LocalTime aLocalTime;
	private static LocalDate aLocalDate;
	private static LocalDateTime aLocalDateTime;
	private static OffsetDateTime anOffsetDateTime;
	// Why are nested configs possible D:<
	private static Config aConfig;

	private static List<Boolean> aBooleanList;
	private static List<Byte> aByteList;
	private static List<Integer> anIntegerList;
	private static List<Float> aFloatList;
	private static List<Long> aLongList;
	private static List<Double> aDoubleList;
	private static List<Enum<DyeColor>> anEnumList;
	private static List<String> aStringList;
	private static List<LocalTime> aLocalTimeList;
	private static List<LocalDate> aLocalDateList;
	private static List<LocalDateTime> aLocalDateTimeList;
	private static List<OffsetDateTime> anOffsetDateTimeList;
	private static List<? extends String> aStringListOld;
	private static List<List<String>> aStringListList;
	private static List<Config> aConfigList;

	private static boolean aBooleanInList;
	private static byte aByteInList;
	private static int anIntInList;
	private static float aFloatInList;
	private static long aLongInList;
	private static double aDoubleInList;
	private static DyeColor anEnumInList;
	private static String aStringInList;
	private static LocalTime aLocalTimeInList;
	private static LocalDate aLocalDateInList;
	private static LocalDateTime aLocalDateTimeInList;
	private static OffsetDateTime anOffsetDateTimeInList;
	private static Config aConfigInList;
	private static List<String> aStringListInList;

	private static boolean category0_aBoolean;
	private static int category0_anInt;

	private static boolean category0_category1_aBoolean;
	private static int category0_category1_anInt;

	static {
		final Pair<Common, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(Common::new);
		COMMON_SPEC = specPair.getRight();
		COMMON = specPair.getLeft();
	}

	@SubscribeEvent
	public static void onEvent(final ModConfig.ModConfigEvent configEvent) {
		if (configEvent.getConfig().getSpec() != COMMON_SPEC)
			return;
		LogManager.getLogger().fatal(FORGEMOD, "ModConfigEvent for TestConfig! " + configEvent.getClass().getSimpleName());
		bakeAndDebugConfig();
	}

	private static void bakeAndDebugConfig() {
		bakeAndDebug(() -> aBoolean, COMMON.aBoolean, $ -> aBoolean = $);
		bakeAndDebug(() -> aByte, COMMON.aByte, $ -> aByte = $);
		bakeAndDebug(() -> anInt, COMMON.anInteger, $ -> anInt = $);
		bakeAndDebug(() -> aFloat, COMMON.aFloat, $ -> aFloat = $);
		bakeAndDebug(() -> aLong, COMMON.aLong, $ -> aLong = $);
		bakeAndDebug(() -> aDouble, COMMON.aDouble, $ -> aDouble = $);
		bakeAndDebug(() -> anEnum, COMMON.anEnum, $ -> anEnum = $);
		bakeAndDebug(() -> aString, COMMON.aString, $ -> aString = $);
		bakeAndDebug(() -> aLocalTime, COMMON.aLocalTime, $ -> aLocalTime = $);
		bakeAndDebug(() -> aLocalDate, COMMON.aLocalDate, $ -> aLocalDate = $);
		bakeAndDebug(() -> aLocalDateTime, COMMON.aLocalDateTime, $ -> aLocalDateTime = $);
		bakeAndDebug(() -> anOffsetDateTime, COMMON.anOffsetDateTime, $ -> anOffsetDateTime = $);
		bakeAndDebug(() -> aConfig, COMMON.aConfig, $ -> aConfig = $);

		bakeAndDebug(() -> aBooleanList, COMMON.aBooleanList, $ -> aBooleanList = $);
		bakeAndDebug(() -> aByteList, COMMON.aByteList, $ -> aByteList = $);
		bakeAndDebug(() -> anIntegerList, COMMON.anIntegerList, $ -> anIntegerList = $);
		bakeAndDebug(() -> aFloatList, COMMON.aFloatList, $ -> aFloatList = $);
		bakeAndDebug(() -> aLongList, COMMON.aLongList, $ -> aLongList = $);
		bakeAndDebug(() -> aDoubleList, COMMON.aDoubleList, $ -> aDoubleList = $);
		bakeAndDebug(() -> anEnumList, COMMON.anEnumList, $ -> anEnumList = $);
		bakeAndDebug(() -> aStringList, COMMON.aStringList, $ -> aStringList = $);
		bakeAndDebug(() -> aLocalTimeList, COMMON.aLocalTimeList, $ -> aLocalTimeList = $);
		bakeAndDebug(() -> aLocalDateList, COMMON.aLocalDateList, $ -> aLocalDateList = $);
		bakeAndDebug(() -> aLocalDateTimeList, COMMON.aLocalDateTimeList, $ -> aLocalDateTimeList = $);
		bakeAndDebug(() -> anOffsetDateTimeList, COMMON.anOffsetDateTimeList, $ -> anOffsetDateTimeList = $);
		bakeAndDebug(() -> aConfigList, COMMON.aConfigList, $ -> aConfigList = $);
		bakeAndDebug(() -> aStringListOld, COMMON.aStringListOld, $ -> aStringListOld = $);
		bakeAndDebug(() -> aStringListList, COMMON.aStringListList, $ -> aStringListList = $);

		bakeAndDebug(() -> aBooleanInList, COMMON.aBooleanInList, $ -> aBooleanInList = $);
//		bakeAndDebug(() -> aByteInList, COMMON.aByteInList, $ -> aByteInList = $); // FIXME: aByteInList returns Integer not Byte
		bakeAndDebug(() -> anIntInList, COMMON.anIntegerInList, $ -> anIntInList = $);
//		bakeAndDebug(() -> aFloatInList, COMMON.aFloatInList, $ -> aFloatInList = $); // FIXME: aFloatInList returns Double not Float
//		bakeAndDebug(() -> aLongInList, COMMON.aLongInList, $ -> aLongInList = $); // FIXME: aLongInList returns Integer not Long
		bakeAndDebug(() -> aDoubleInList, COMMON.aDoubleInList, $ -> aDoubleInList = $);
//		bakeAndDebug(() -> anEnumInList, COMMON.anEnumInList, $ -> anEnumInList = $);  // FIXME: anEnumInList returns String not Enum<>
		bakeAndDebug(() -> aStringInList, COMMON.aStringInList, $ -> aStringInList = $);
		bakeAndDebug(() -> aLocalTimeInList, COMMON.aLocalTimeInList, $ -> aLocalTimeInList = $);
		bakeAndDebug(() -> aLocalDateInList, COMMON.aLocalDateInList, $ -> aLocalDateInList = $);
		bakeAndDebug(() -> aLocalDateTimeInList, COMMON.aLocalDateTimeInList, $ -> aLocalDateTimeInList = $);
		bakeAndDebug(() -> anOffsetDateTimeInList, COMMON.anOffsetDateTimeInList, $ -> anOffsetDateTimeInList = $);
		bakeAndDebug(() -> aConfigInList, COMMON.aConfigInList, $ -> aConfigInList = $);
		bakeAndDebug(() -> aStringListInList, COMMON.aStringListInList, $ -> aStringListInList = $);

		bakeAndDebug(() -> category0_aBoolean, COMMON.category0_aBoolean, $ -> category0_aBoolean = $);
		bakeAndDebug(() -> category0_anInt, COMMON.category0_anInt, $ -> category0_anInt = $);

		bakeAndDebug(() -> category0_category1_aBoolean, COMMON.category0_category1_aBoolean, $ -> category0_category1_aBoolean = $);
		bakeAndDebug(() -> category0_category1_anInt, COMMON.category0_category1_anInt, $ -> category0_category1_anInt = $);
	}

	private static <T> void bakeAndDebug(final Supplier<T> getter, final ConfigValue<T> configValue, final Consumer<T> setter) {
		T oldValue = getter.get();
		T newValue = configValue.get();
		String path = Strings.join(configValue.getPath(), "_");
//		LogManager.getLogger().warn(FORGEMOD, path + ": " + oldValue + ", " + newValue + ", " + Objects.equals(oldValue, newValue));
		LogManager.getLogger().warn(FORGEMOD, path + ": " + Objects.equals(oldValue, newValue));
		setter.accept(newValue);
	}

	public static class Common {

		private final BooleanValue aBoolean;
		private final ByteValue aByte;
		private final IntValue anInteger;
		private final FloatValue aFloat;
		private final LongValue aLong;
		private final DoubleValue aDouble;
		private final EnumValue<DyeColor> anEnum;
		private final ConfigValue<String> aString;
		private final ConfigValue<LocalTime> aLocalTime;
		private final ConfigValue<LocalDate> aLocalDate;
		private final ConfigValue<LocalDateTime> aLocalDateTime;
		private final ConfigValue<OffsetDateTime> anOffsetDateTime;
		private final ConfigValue<Config> aConfig;

		private final ConfigValue<List<Boolean>> aBooleanList;
		private final ConfigValue<List<Byte>> aByteList;
		private final ConfigValue<List<Integer>> anIntegerList;
		private final ConfigValue<List<Float>> aFloatList;
		private final ConfigValue<List<Long>> aLongList;
		private final ConfigValue<List<Double>> aDoubleList;
		private final ConfigValue<List<Enum<DyeColor>>> anEnumList;
		private final ConfigValue<List<String>> aStringList;
		private final ConfigValue<List<LocalTime>> aLocalTimeList;
		private final ConfigValue<List<LocalDate>> aLocalDateList;
		private final ConfigValue<List<LocalDateTime>> aLocalDateTimeList;
		private final ConfigValue<List<OffsetDateTime>> anOffsetDateTimeList;
		private final ConfigValue<List<Config>> aConfigList;
		private final ConfigValue<List<? extends String>> aStringListOld;
		private final ConfigValue<List<List<String>>> aStringListList;

		private final ConfigValue<Boolean> aBooleanInList;
		private final ConfigValue<Byte> aByteInList;
		private final ConfigValue<Integer> anIntegerInList;
		private final ConfigValue<Float> aFloatInList;
		private final ConfigValue<Long> aLongInList;
		private final ConfigValue<Double> aDoubleInList;
		private final ConfigValue<DyeColor> anEnumInList;
		private final ConfigValue<String> aStringInList;
		private final ConfigValue<LocalTime> aLocalTimeInList;
		private final ConfigValue<LocalDate> aLocalDateInList;
		private final ConfigValue<LocalDateTime> aLocalDateTimeInList;
		private final ConfigValue<OffsetDateTime> anOffsetDateTimeInList;
		private final ConfigValue<Config> aConfigInList;
		private final ConfigValue<List<String>> aStringListInList;

		private final BooleanValue category0_aBoolean;
		private final IntValue category0_anInt;

		private final BooleanValue category0_category1_aBoolean;
		private final IntValue category0_category1_anInt;

		Common(ForgeConfigSpec.Builder builder) {

			aBoolean = builder
					.comment("a Boolean")
					.translation("aBoolean")
					.worldRestart()
					.define("aBoolean", false);

			aByte = builder
					.comment("a Byte")
					.translation("aByte")
					.worldRestart()
					.defineInRange("aByte", (byte) 10, Byte.MIN_VALUE, Byte.MAX_VALUE);

			anInteger = builder
					.comment("an Int")
					.translation("anInt")
					.worldRestart()
					.defineInRange("anInt", 10, 0, 100);

			aFloat = builder
					.comment("a Float")
					.translation("aFloat")
					.worldRestart()
					.defineInRange("aFloat", 0.5F, 0.0F, 1.0F);

			aLong = builder
					.comment("a Long")
					.translation("aLong")
					.worldRestart()
					.defineInRange("aLong", Long.MAX_VALUE / 2L, 0L, Long.MAX_VALUE);

			aDouble = builder
					.comment("a Double")
					.translation("aDouble")
					.worldRestart()
					.defineInRange("aDouble", 0.05D, 0.0D, 1.0D);

			anEnum = builder
					.comment("an Enum")
					.translation("anEnum")
					.defineEnum("anEnum", DyeColor.WHITE);

			aString = builder
					.comment("a String")
					.translation("aString")
					.define("aString", "Hello, World!");

			// Negative years or years that have less/more than 4 digits break toml's parser

			aLocalTime = builder
					.comment("a LocalTime")
					.translation("aLocalTime")
					.define("aLocalTime", LocalTime.of(23, 59, 59));

			aLocalDate = builder
					.comment("a LocalDate")
					.translation("aLocalDate")
					.define("aLocalDate", LocalDate.of(2000, 1, 1));

			aLocalDateTime = builder
					.comment("a LocalDateTime")
					.translation("aLocalDateTime")
					.define("aLocalDateTime", LocalDateTime.of(2000, 1, 1, 23, 59));

			anOffsetDateTime = builder
					.comment("an OffsetDateTime")
					.translation("anOffsetDateTime")
					.define("anOffsetDateTime", OffsetDateTime.now());

			aConfig = builder
					.comment("a Config")
					.translation("aConfig")
					.define("aConfig", TomlFormat.instance().createConfig());

			builder.push("lists");
			builder.comment("List tests");
			{
				aBooleanList = builder
						.comment("a BooleanList")
						.translation("aBooleanList")
						.define("aBooleanList", Lists.newArrayList(true, false));

				aByteList = builder
						.comment("a ByteList")
						.translation("aByteList")
						.define("aByteList", Lists.newArrayList((byte) 0, Byte.MIN_VALUE, Byte.MAX_VALUE, (byte) 256));

				anIntegerList = builder
						.comment("an IntegerList")
						.translation("anIntegerList")
						.define("anIntegerList", Lists.newArrayList(0, Integer.MIN_VALUE, Integer.MAX_VALUE, 256));

				aFloatList = builder
						.comment("a FloatList")
						.translation("aFloatList")
						.define("aFloatList", Lists.newArrayList(0F, Float.MIN_VALUE, Float.MAX_VALUE, 256F));

				aLongList = builder
						.comment("a LongList")
						.translation("aLongList")
						.define("aLongList", Lists.newArrayList(0L, Long.MIN_VALUE, Long.MAX_VALUE, 256L));

				aDoubleList = builder
						.comment("a DoubleList")
						.translation("aDoubleList")
						.define("aDoubleList", Lists.newArrayList(0d, Double.MIN_VALUE, Double.MAX_VALUE, 256d));

				anEnumList = builder
						.comment("an EnumList")
						.translation("anEnumList")
						.define("anEnumList", Lists.newArrayList(DyeColor.BLACK, DyeColor.GREEN, DyeColor.CYAN, DyeColor.RED));

				aStringList = builder
						.comment("a StringList")
						.translation("aStringList")
						.define("aStringList", Lists.newArrayList("aStringList_value0", "aStringList_value1"));

				// Negative years or years that have less/more than 4 digits break toml's parser

				aLocalTimeList = builder
						.comment("a LocalTimeList")
						.translation("aLocalTimeList")
						.define("aLocalTimeList", Lists.newArrayList(LocalTime.of(0, 0, 0), LocalTime.of(23, 59, 59)));

				aLocalDateList = builder
						.comment("a LocalDateList")
						.translation("aLocalDateList")
						.define("aLocalDateList", Lists.newArrayList(LocalDate.of(1999, 1, 1), LocalDate.of(2000, 1, 1)));

				aLocalDateTimeList = builder
						.comment("a LocalDateTimeList")
						.translation("aLocalDateTimeList")
						.define("aLocalDateTimeList", Lists.newArrayList(LocalDateTime.of(1999, 1, 1, 10, 0), LocalDateTime.of(2000, 1, 1, 23, 59)));

				anOffsetDateTimeList = builder
						.comment("an OffsetDateTimeList")
						.translation("anOffsetDateTimeList")
						.define("anOffsetDateTimeList", Lists.newArrayList(OffsetDateTime.now(), OffsetDateTime.now(ZoneOffset.UTC)));

				aConfigList = builder
						.comment("a ConfigList")
						.translation("aConfigList")
						.define("aConfigList", Lists.newArrayList(TomlFormat.instance().createConfig(), TomlFormat.instance().createConfig(), TomlFormat.instance().createConfig(), TomlFormat.instance().createConfig()));

				aStringListOld = builder
						.comment("a StringListOld")
						.translation("aStringListOld")
						.defineList("aStringListOld", Lists.newArrayList("aStringListOld_value0", "aStringListOld_value1"), o -> o instanceof String);

				aStringListList = builder
						.comment("a StringListList")
						.translation("aStringListList")
						.define("aStringListList", Lists.newArrayList(Lists.newArrayList("Hello"), Lists.newArrayList("World")));
			}
			builder.pop();

			builder.push("inLists");
			builder.comment("In List tests");
			{
				aBooleanInList = builder
						.comment("a BooleanInList")
						.translation("aBooleanInList")
						.defineInList("aBooleanInList", true, Lists.newArrayList(true, false));

				aByteInList = builder
						.comment("a ByteInList")
						.translation("aByteInList")
						.defineInList("aByteInList", (byte) 256, Lists.newArrayList((byte) 0, Byte.MIN_VALUE, Byte.MAX_VALUE, (byte) 256));

				anIntegerInList = builder
						.comment("an IntegerInList")
						.translation("anIntegerInList")
						.defineInList("anIntegerInList", 256, Lists.newArrayList(0, Integer.MIN_VALUE, Integer.MAX_VALUE, 256));

				aFloatInList = builder
						.comment("a FloatInList")
						.translation("aFloatInList")
						.defineInList("aFloatInList", 256F,  Lists.newArrayList(0F, Float.MIN_VALUE, Float.MAX_VALUE, 256F));

				aLongInList = builder
						.comment("a LongInList")
						.translation("aLongInList")
						.defineInList("aLongInList", 256L, Lists.newArrayList(0L, Long.MIN_VALUE, Long.MAX_VALUE, 256L));

				aDoubleInList = builder
						.comment("a DoubleInList")
						.translation("aDoubleInList")
						.defineInList("aDoubleInList", 256d, Lists.newArrayList(0d, Double.MIN_VALUE, Double.MAX_VALUE, 256d));

				anEnumInList = builder
						.comment("an EnumInList")
						.translation("anEnumInList")
						.defineInList("anEnumInList", DyeColor.ORANGE, Lists.newArrayList(DyeColor.ORANGE, DyeColor.BLACK, DyeColor.GREEN, DyeColor.CYAN, DyeColor.RED));

				aStringInList = builder
						.comment("a StringInList")
						.translation("aStringInList")
						.defineInList("aStringInList", "aStringListInList_value0", Lists.newArrayList("aStringListInList_value0", "aStringListInList_value1"));

				// Negative years or years that have less/more than 4 digits break toml's parser

				aLocalTimeInList = builder
						.comment("a LocalTimeInList")
						.translation("aLocalTimeInList")
						.defineInList("aLocalTimeInList", LocalTime.of(0, 0, 0), Lists.newArrayList(LocalTime.of(0, 0, 0), LocalTime.of(23, 59, 59)));

				aLocalDateInList = builder
						.comment("a LocalDateInList")
						.translation("aLocalDateInList")
						.defineInList("aLocalDateInList", LocalDate.of(1999, 1, 1), Lists.newArrayList(LocalDate.of(1999, 1, 1), LocalDate.of(2000, 1, 1)));

				aLocalDateTimeInList = builder
						.comment("a LocalDateTimeInList")
						.translation("aLocalDateTimeInList")
						.defineInList("aLocalDateTimeInList",LocalDateTime.of(1999, 1, 1, 10, 0), Lists.newArrayList(LocalDateTime.of(1999, 1, 1, 10, 0), LocalDateTime.of(2000, 1, 1, 23, 59)));

				anOffsetDateTimeInList = builder
						.comment("an OffsetDateTimeInList")
						.translation("anOffsetDateTimeInList")
						.defineInList("anOffsetDateTimeInList", OffsetDateTime.of(2019, 12, 26, 21, 58, 10, 368, ZoneOffset.of("+11:00")), Lists.newArrayList(OffsetDateTime.of(2019, 12, 26, 21, 58, 10, 368, ZoneOffset.of("+11:00")), OffsetDateTime.now(), OffsetDateTime.now(ZoneOffset.UTC)));

				final CommentedConfig config = TomlFormat.instance().createConfig();
				aConfigInList = builder
						.comment("a ConfigInList")
						.translation("aConfigInList")
						.defineInList("aConfigInList", config, Lists.newArrayList(config, TomlFormat.instance().createConfig(), TomlFormat.instance().createConfig(), TomlFormat.instance().createConfig()));

				aStringListInList = builder
						.comment("a StringListInList")
						.translation("aStringListInList")
						.defineInList("aStringListInList", Lists.newArrayList("Hello"), Lists.newArrayList(Lists.newArrayList("Hello"), Lists.newArrayList("World")));
			}
			builder.pop();

			builder.push("category0");
			builder.comment("Category0 configuration settings");
			{
				category0_aBoolean = builder
						.comment("category0 > a Boolean")
						.translation("category0_aBoolean")
						.worldRestart()
						.define("category0_aBoolean", false);

				category0_anInt = builder
						.comment("category0 > an Int")
						.translation("category0_anInt")
						.worldRestart()
						.defineInRange("category0_anInt", 10, 0, 100);

				builder.push("category1");
				builder.comment("Category0 > category1 configuration settings");
				{
					category0_category1_aBoolean = builder
							.comment("category0 > category1 a Boolean")
							.translation("category0_category1_aBoolean")
							.worldRestart()
							.define("category0_category1_aBoolean", false);

					category0_category1_anInt = builder
							.comment("category0 > category1 > an Int")
							.translation("category0_category1_anInt")
							.worldRestart()
							.defineInRange("category0_category1_anInt", 10, 0, 100);
				}
				builder.pop();
			}
			builder.pop();
		}

	}

}
