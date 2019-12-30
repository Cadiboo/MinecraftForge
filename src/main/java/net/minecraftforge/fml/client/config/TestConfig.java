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
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;

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
	private static List<Config> aConfigList;
	private static List<String> aStringWeirdList;
	private static List<? extends String> aStringListOld;

	private static List<List<Boolean>> aBooleanListList;
	private static List<List<Byte>> aByteListList;
	private static List<List<Integer>> anIntegerListList;
	private static List<List<Float>> aFloatListList;
	private static List<List<Long>> aLongListList;
	private static List<List<Double>> aDoubleListList;
	private static List<List<Enum<DyeColor>>> anEnumListList;
	private static List<List<String>> aStringListList;
	private static List<List<LocalTime>> aLocalTimeListList;
	private static List<List<LocalDate>> aLocalDateListList;
	private static List<List<LocalDateTime>> aLocalDateTimeListList;
	private static List<List<OffsetDateTime>> anOffsetDateTimeListList;
	private static List<List<Config>> aConfigListList;
	private static List<List<String>> aStringWeirdListWeirdList;
	private static List<? extends List<String>> aStringListOldList;

	private static Config aBooleanConfig;
	private static Config aByteConfig;
	private static Config anIntegerConfig;
	private static Config aFloatConfig;
	private static Config aLongConfig;
	private static Config aDoubleConfig;
	private static Config anEnumConfig;
	private static Config aStringConfig;
	private static Config aLocalTimeConfig;
	private static Config aLocalDateConfig;
	private static Config aLocalDateTimeConfig;
	private static Config anOffsetDateTimeConfig;
	private static Config aConfigConfig;

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

	// 10x list
	private static List<List<List<List<List<List<List<List<List<List<String>>>>>>>>>> aVeryNestedStringList;
	// 10x config
	private static Config aVeryNestedStringConfig;
	private static String aPathDefinedString;

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
		bakeAndDebug(() -> aStringWeirdList, COMMON.aStringWeirdList, $ -> aStringWeirdList = $);
		bakeAndDebug(() -> aStringListOld, COMMON.aStringListOld, $ -> aStringListOld = $);
		bakeAndDebug(() -> aBooleanListList, COMMON.aBooleanListList, $ -> aBooleanListList = $);
		bakeAndDebug(() -> aByteListList, COMMON.aByteListList, $ -> aByteListList = $);
		bakeAndDebug(() -> anIntegerListList, COMMON.anIntegerListList, $ -> anIntegerListList = $);
		bakeAndDebug(() -> aFloatListList, COMMON.aFloatListList, $ -> aFloatListList = $);
		bakeAndDebug(() -> aLongListList, COMMON.aLongListList, $ -> aLongListList = $);
		bakeAndDebug(() -> aDoubleListList, COMMON.aDoubleListList, $ -> aDoubleListList = $);
		bakeAndDebug(() -> anEnumListList, COMMON.anEnumListList, $ -> anEnumListList = $);
		bakeAndDebug(() -> aStringListList, COMMON.aStringListList, $ -> aStringListList = $);
		bakeAndDebug(() -> aLocalTimeListList, COMMON.aLocalTimeListList, $ -> aLocalTimeListList = $);
		bakeAndDebug(() -> aLocalDateListList, COMMON.aLocalDateListList, $ -> aLocalDateListList = $);
		bakeAndDebug(() -> aLocalDateTimeListList, COMMON.aLocalDateTimeListList, $ -> aLocalDateTimeListList = $);
		bakeAndDebug(() -> anOffsetDateTimeListList, COMMON.anOffsetDateTimeListList, $ -> anOffsetDateTimeListList = $);
		bakeAndDebug(() -> aConfigListList, COMMON.aConfigListList, $ -> aConfigListList = $);
		bakeAndDebug(() -> aStringWeirdListWeirdList, COMMON.aStringWeirdListWeirdList, $ -> aStringWeirdListWeirdList = $);
		bakeAndDebug(() -> aStringListOldList, COMMON.aStringListOldList, $ -> aStringListOldList = $);

		bakeAndDebug(() -> aBooleanInList, COMMON.aBooleanInList, $ -> aBooleanInList = $);
		bakeAndDebug(() -> aByteInList, COMMON.aByteInList, $ -> aByteInList = $);
		bakeAndDebug(() -> anIntInList, COMMON.anIntegerInList, $ -> anIntInList = $);
		bakeAndDebug(() -> aFloatInList, COMMON.aFloatInList, $ -> aFloatInList = $);
		bakeAndDebug(() -> aLongInList, COMMON.aLongInList, $ -> aLongInList = $);
		bakeAndDebug(() -> aDoubleInList, COMMON.aDoubleInList, $ -> aDoubleInList = $);
		bakeAndDebug(() -> anEnumInList, COMMON.anEnumInList, $ -> anEnumInList = $);
		bakeAndDebug(() -> aStringInList, COMMON.aStringInList, $ -> aStringInList = $);
		bakeAndDebug(() -> aLocalTimeInList, COMMON.aLocalTimeInList, $ -> aLocalTimeInList = $);
		bakeAndDebug(() -> aLocalDateInList, COMMON.aLocalDateInList, $ -> aLocalDateInList = $);
		bakeAndDebug(() -> aLocalDateTimeInList, COMMON.aLocalDateTimeInList, $ -> aLocalDateTimeInList = $);
		bakeAndDebug(() -> anOffsetDateTimeInList, COMMON.anOffsetDateTimeInList, $ -> anOffsetDateTimeInList = $);
		bakeAndDebug(() -> aConfigInList, COMMON.aConfigInList, $ -> aConfigInList = $);
		bakeAndDebug(() -> aStringListInList, COMMON.aStringListInList, $ -> aStringListInList = $);

		bakeAndDebug(() -> aBooleanConfig, COMMON.aBooleanConfig, $ -> aBooleanConfig = $);
		bakeAndDebug(() -> aByteConfig, COMMON.aByteConfig, $ -> aByteConfig = $);
		bakeAndDebug(() -> anIntegerConfig, COMMON.anIntegerConfig, $ -> anIntegerConfig = $);
		bakeAndDebug(() -> aFloatConfig, COMMON.aFloatConfig, $ -> aFloatConfig = $);
		bakeAndDebug(() -> aLongConfig, COMMON.aLongConfig, $ -> aLongConfig = $);
		bakeAndDebug(() -> aDoubleConfig, COMMON.aDoubleConfig, $ -> aDoubleConfig = $);
		bakeAndDebug(() -> anEnumConfig, COMMON.anEnumConfig, $ -> anEnumConfig = $);
		bakeAndDebug(() -> aStringConfig, COMMON.aStringConfig, $ -> aStringConfig = $);
		bakeAndDebug(() -> aLocalTimeConfig, COMMON.aLocalTimeConfig, $ -> aLocalTimeConfig = $);
		bakeAndDebug(() -> aLocalDateConfig, COMMON.aLocalDateConfig, $ -> aLocalDateConfig = $);
		bakeAndDebug(() -> aLocalDateTimeConfig, COMMON.aLocalDateTimeConfig, $ -> aLocalDateTimeConfig = $);
		bakeAndDebug(() -> anOffsetDateTimeConfig, COMMON.anOffsetDateTimeConfig, $ -> anOffsetDateTimeConfig = $);
		bakeAndDebug(() -> aConfigConfig, COMMON.aConfigConfig, $ -> aConfigConfig = $);

		bakeAndDebug(() -> aVeryNestedStringList, COMMON.aVeryNestedStringList, $ -> aVeryNestedStringList = $);
		bakeAndDebug(() -> aVeryNestedStringConfig, COMMON.aVeryNestedStringConfig, $ -> aVeryNestedStringConfig = $);
		bakeAndDebug(() -> aPathDefinedString, COMMON.aPathDefinedString, $ -> aPathDefinedString = $);

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

	@SafeVarargs
	private static <E> List<E> newList(E... elements) {
		return Lists.newArrayList(elements);
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
		private final ConfigValue<List<String>> aStringWeirdList;
		private final ConfigValue<List<? extends String>> aStringListOld;

		private final ConfigValue<List<List<Boolean>>> aBooleanListList;
		private final ConfigValue<List<List<Byte>>> aByteListList;
		private final ConfigValue<List<List<Integer>>> anIntegerListList;
		private final ConfigValue<List<List<Float>>> aFloatListList;
		private final ConfigValue<List<List<Long>>> aLongListList;
		private final ConfigValue<List<List<Double>>> aDoubleListList;
		private final ConfigValue<List<List<Enum<DyeColor>>>> anEnumListList;
		private final ConfigValue<List<List<String>>> aStringListList;
		private final ConfigValue<List<List<LocalTime>>> aLocalTimeListList;
		private final ConfigValue<List<List<LocalDate>>> aLocalDateListList;
		private final ConfigValue<List<List<LocalDateTime>>> aLocalDateTimeListList;
		private final ConfigValue<List<List<OffsetDateTime>>> anOffsetDateTimeListList;
		private final ConfigValue<List<List<Config>>> aConfigListList;
		private final ConfigValue<List<List<String>>> aStringWeirdListWeirdList;
		private final ConfigValue<List<? extends List<String>>> aStringListOldList;

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

		private final ConfigValue<Config> aBooleanConfig;
		private final ConfigValue<Config> aByteConfig;
		private final ConfigValue<Config> anIntegerConfig;
		private final ConfigValue<Config> aFloatConfig;
		private final ConfigValue<Config> aLongConfig;
		private final ConfigValue<Config> aDoubleConfig;
		private final ConfigValue<Config> anEnumConfig;
		private final ConfigValue<Config> aStringConfig;
		private final ConfigValue<Config> aLocalTimeConfig;
		private final ConfigValue<Config> aLocalDateConfig;
		private final ConfigValue<Config> aLocalDateTimeConfig;
		private final ConfigValue<Config> anOffsetDateTimeConfig;
		private final ConfigValue<Config> aConfigConfig;

		// 10x List
		private final ConfigValue<List<List<List<List<List<List<List<List<List<List<String>>>>>>>>>>> aVeryNestedStringList;
		// 10x Config
		private final ConfigValue<Config> aVeryNestedStringConfig;
		private final ConfigValue<String> aPathDefinedString;

		private final BooleanValue category0_aBoolean;
		private final IntValue category0_anInt;

		private final BooleanValue category0_category1_aBoolean;
		private final IntValue category0_category1_anInt;

		Common(ForgeConfigSpec.Builder builder) {

			aBoolean = builder
					.comment("a Boolean")
					.translation("aBoolean")
					.worldRestart()
					.define("aBoolean", true);

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
					.define("aConfig", newConfig(true, "hello", 0));

			builder.comment("List tests")
					.push("lists");
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
						.define("aConfigList", Lists.newArrayList(newConfig("foo"), newConfig("bar"), newConfig("baz"), newConfig("fuz")));

				aStringWeirdList = builder
						.comment("a StringWeirdList")
						.translation("aStringWeirdList")
						.define("aStringWeirdList", Arrays.asList("aStringWeirdList_value0", "aStringWeirdList_value1"));

				aStringListOld = builder
						.comment("a StringListOld")
						.translation("aStringListOld")
						.defineList("aStringListOld", Lists.newArrayList("aStringListOld_value0", "aStringListOld_value1"), o -> o instanceof String);
			}
			builder.pop();

			builder.comment("Nested List tests")
					.push("nestedlists");
			{
				aBooleanListList = builder
						.comment("a BooleanList")
						.translation("aBooleanList")
						.define("aBooleanList", Lists.newArrayList(Lists.newArrayList(true, false), Lists.newArrayList(true, false), Lists.newArrayList(true, false)));

				aByteListList = builder
						.comment("a ByteList")
						.translation("aByteList")
						.define("aByteList", Lists.newArrayList(Lists.newArrayList((byte) 0, Byte.MIN_VALUE, Byte.MAX_VALUE, (byte) 256), Lists.newArrayList((byte) 0, Byte.MIN_VALUE, Byte.MAX_VALUE, (byte) 256), Lists.newArrayList((byte) 0, Byte.MIN_VALUE, Byte.MAX_VALUE, (byte) 256)));

				anIntegerListList = builder
						.comment("an IntegerList")
						.translation("anIntegerList")
						.define("anIntegerList", Lists.newArrayList(Lists.newArrayList(0, Integer.MIN_VALUE, Integer.MAX_VALUE, 256), Lists.newArrayList(0, Integer.MIN_VALUE, Integer.MAX_VALUE, 256), Lists.newArrayList(0, Integer.MIN_VALUE, Integer.MAX_VALUE, 256)));

				aFloatListList = builder
						.comment("a FloatList")
						.translation("aFloatList")
						.define("aFloatList", Lists.newArrayList(Lists.newArrayList(0F, Float.MIN_VALUE, Float.MAX_VALUE, 256F), Lists.newArrayList(0F, Float.MIN_VALUE, Float.MAX_VALUE, 256F), Lists.newArrayList(0F, Float.MIN_VALUE, Float.MAX_VALUE, 256F)));

				aLongListList = builder
						.comment("a LongList")
						.translation("aLongList")
						.define("aLongList", Lists.newArrayList(Lists.newArrayList(0L, Long.MIN_VALUE, Long.MAX_VALUE, 256L), Lists.newArrayList(0L, Long.MIN_VALUE, Long.MAX_VALUE, 256L), Lists.newArrayList(0L, Long.MIN_VALUE, Long.MAX_VALUE, 256L)));

				aDoubleListList = builder
						.comment("a DoubleList")
						.translation("aDoubleList")
						.define("aDoubleList", Lists.newArrayList(Lists.newArrayList(0d, Double.MIN_VALUE, Double.MAX_VALUE, 256d), Lists.newArrayList(0d, Double.MIN_VALUE, Double.MAX_VALUE, 256d), Lists.newArrayList(0d, Double.MIN_VALUE, Double.MAX_VALUE, 256d)));

				anEnumListList = builder
						.comment("an EnumList")
						.translation("anEnumList")
						.define("anEnumList", Lists.newArrayList(Lists.newArrayList(DyeColor.BLACK, DyeColor.GREEN, DyeColor.CYAN, DyeColor.RED), Lists.newArrayList(DyeColor.BLACK, DyeColor.GREEN, DyeColor.CYAN, DyeColor.RED), Lists.newArrayList(DyeColor.BLACK, DyeColor.GREEN, DyeColor.CYAN, DyeColor.RED)));

				aStringListList = builder
						.comment("a StringListList")
						.translation("aStringListList")
						.define("aStringListList", Lists.newArrayList(Lists.newArrayList("Hello", "World!"), Lists.newArrayList("World", "Hello")));

				// Negative years or years that have less/more than 4 digits break toml's parser

				aLocalTimeListList = builder
						.comment("a LocalTimeList")
						.translation("aLocalTimeList")
						.define("aLocalTimeList", Lists.newArrayList(Lists.newArrayList(LocalTime.of(0, 0, 0), LocalTime.of(23, 59, 59)), Lists.newArrayList(LocalTime.of(0, 0, 0), LocalTime.of(23, 59, 59)), Lists.newArrayList(LocalTime.of(0, 0, 0), LocalTime.of(23, 59, 59))));

				aLocalDateListList = builder
						.comment("a LocalDateList")
						.translation("aLocalDateList")
						.define("aLocalDateList", Lists.newArrayList(Lists.newArrayList(LocalDate.of(1999, 1, 1), LocalDate.of(2000, 1, 1)), Lists.newArrayList(LocalDate.of(1999, 1, 1), LocalDate.of(2000, 1, 1)), Lists.newArrayList(LocalDate.of(1999, 1, 1), LocalDate.of(2000, 1, 1))));

				aLocalDateTimeListList = builder
						.comment("a LocalDateTimeList")
						.translation("aLocalDateTimeList")
						.define("aLocalDateTimeList", Lists.newArrayList(Lists.newArrayList(LocalDateTime.of(1999, 1, 1, 10, 0), LocalDateTime.of(2000, 1, 1, 23, 59)), Lists.newArrayList(LocalDateTime.of(1999, 1, 1, 10, 0), LocalDateTime.of(2000, 1, 1, 23, 59)), Lists.newArrayList(LocalDateTime.of(1999, 1, 1, 10, 0), LocalDateTime.of(2000, 1, 1, 23, 59))));

				anOffsetDateTimeListList = builder
						.comment("an OffsetDateTimeList")
						.translation("anOffsetDateTimeList")
						.define("anOffsetDateTimeList", Lists.newArrayList(Lists.newArrayList(OffsetDateTime.now(), OffsetDateTime.now(ZoneOffset.UTC)), Lists.newArrayList(OffsetDateTime.now(), OffsetDateTime.now(ZoneOffset.UTC)), Lists.newArrayList(OffsetDateTime.now(), OffsetDateTime.now(ZoneOffset.UTC))));

				aConfigListList = builder
						.comment("a ConfigList")
						.translation("aConfigList")
						.define("aConfigList", Lists.newArrayList(Lists.newArrayList(newConfig("foo"), newConfig("bar"), newConfig("baz"), newConfig("lumen")), Lists.newArrayList(newConfig("F"), newConfig("O"), newConfig("O"), newConfig("B")), Lists.newArrayList(newConfig("A"), newConfig("R"), newConfig("Baz"), newConfig("Im not very inventive in my testing stuff naming sorry"))));

				aStringWeirdListWeirdList = builder
						.comment("a StringWeirdListWeirdList")
						.translation("aStringWeirdListWeirdList")
						.define("aStringWeirdListWeirdList", Arrays.asList(Arrays.asList("aStringWeirdListWeirdList_value0", "aStringWeirdListWeirdList_value1"), Arrays.asList("aStringWeirdListWeirdList_value0", "aStringWeirdListWeirdList_value1"), Arrays.asList("aStringWeirdListWeirdList_value0", "aStringWeirdListWeirdList_value1")));

				aStringListOldList = builder
						.comment("a StringListOld")
						.translation("aStringListOld")
						.defineList("aStringListOld", newList(newList("aStringListOld_value0", "aStringListOld_value1"), newList("aStringListOld_value0", "aStringListOld_value1"), newList("aStringListOld_value0", "aStringListOld_value1")), o -> o instanceof List || o instanceof String);
			}
			builder.pop();

			builder.comment("In List tests")
					.push("inLists");
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
						.defineInList("aFloatInList", 256F, Lists.newArrayList(0F, Float.MIN_VALUE, Float.MAX_VALUE, 256F));

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
						.defineInList("aLocalDateTimeInList", LocalDateTime.of(1999, 1, 1, 10, 0), Lists.newArrayList(LocalDateTime.of(1999, 1, 1, 10, 0), LocalDateTime.of(2000, 1, 1, 23, 59)));

				anOffsetDateTimeInList = builder
						.comment("an OffsetDateTimeInList")
						.translation("anOffsetDateTimeInList")
						.defineInList("anOffsetDateTimeInList", OffsetDateTime.of(2019, 12, 26, 21, 58, 10, 368, ZoneOffset.of("+11:00")), Lists.newArrayList(OffsetDateTime.of(2019, 12, 26, 21, 58, 10, 368, ZoneOffset.of("+11:00")), OffsetDateTime.now(), OffsetDateTime.now(ZoneOffset.UTC)));

				final CommentedConfig config = newConfig("defaultValue yay :D");
				aConfigInList = builder
						.comment("a ConfigInList")
						.translation("aConfigInList")
						.defineInList("aConfigInList", config, Lists.newArrayList(config, newConfig(1111, 2222, 3333, 4444), newConfig("How is this going to work???"), newConfig("erm.....")));

				aStringListInList = builder
						.comment("a StringListInList")
						.translation("aStringListInList")
						.defineInList("aStringListInList", Lists.newArrayList("Hello"), Lists.newArrayList(Lists.newArrayList("Hello"), Lists.newArrayList("World")));
			}
			builder.pop();

			builder.comment("Config tests")
					.push("configs");
			{
				aBooleanConfig = builder
						.comment("a BooleanConfig")
						.translation("aBooleanConfig")
						.define("aBooleanConfig", newConfig(true, false));

				aByteConfig = builder
						.comment("a ByteConfig")
						.translation("aByteConfig")
						.define("aByteConfig", newConfig((byte) 0, Byte.MIN_VALUE, Byte.MAX_VALUE, (byte) 256));

				anIntegerConfig = builder
						.comment("an IntegerConfig")
						.translation("anIntegerConfig")
						.define("anIntegerConfig", newConfig(0, Integer.MIN_VALUE, Integer.MAX_VALUE, 256));

				aFloatConfig = builder
						.comment("a FloatConfig")
						.translation("aFloatConfig")
						.define("aFloatConfig", newConfig(0F, Float.MIN_VALUE, Float.MAX_VALUE, 256F));

				aLongConfig = builder
						.comment("a LongConfig")
						.translation("aLongConfig")
						.define("aLongConfig", newConfig(0L, Long.MIN_VALUE, Long.MAX_VALUE, 256L));

				aDoubleConfig = builder
						.comment("a DoubleConfig")
						.translation("aDoubleConfig")
						.define("aDoubleConfig", newConfig(0d, Double.MIN_VALUE, Double.MAX_VALUE, 256d));

				anEnumConfig = builder
						.comment("an EnumConfig")
						.translation("anEnumConfig")
						.define("anEnumConfig", newConfig(DyeColor.BLACK, DyeColor.GREEN, DyeColor.CYAN, DyeColor.RED));

				aStringConfig = builder
						.comment("a StringConfig")
						.translation("aStringConfig")
						.define("aStringConfig", newConfig("aStringConfig_value0", "aStringConfig_value1"));

				// Negative years or years that have less/more than 4 digits break toml's parser

				aLocalTimeConfig = builder
						.comment("a LocalTimeConfig")
						.translation("aLocalTimeConfig")
						.define("aLocalTimeConfig", newConfig(LocalTime.of(0, 0, 0), LocalTime.of(23, 59, 59)));

				aLocalDateConfig = builder
						.comment("a LocalDateConfig")
						.translation("aLocalDateConfig")
						.define("aLocalDateConfig", newConfig(LocalDate.of(1999, 1, 1), LocalDate.of(2000, 1, 1)));

				aLocalDateTimeConfig = builder
						.comment("a LocalDateTimeConfig")
						.translation("aLocalDateTimeConfig")
						.define("aLocalDateTimeConfig", newConfig(LocalDateTime.of(1999, 1, 1, 10, 0), LocalDateTime.of(2000, 1, 1, 23, 59)));

				anOffsetDateTimeConfig = builder
						.comment("an OffsetDateTimeConfig")
						.translation("anOffsetDateTimeConfig")
						.define("anOffsetDateTimeConfig", newConfig(OffsetDateTime.now(), OffsetDateTime.now(ZoneOffset.UTC)));

				aConfigConfig = builder
						.comment("a ConfigConfig")
						.translation("aConfigConfig")
						.define("aConfigConfig", newConfig(newConfig(true, false), newConfig("hello", "world"), newConfig(Lists.newArrayList("Hello!!!", "World___"), Lists.newArrayList("Hello______!!!", "World__!!_!!")), newConfig("foo")));
			}
			builder.pop();

			aVeryNestedStringList = builder
					.comment("a very nested string list (10x Lists)")
					.translation("aVeryNestedStringList")
					// 10x List
					.define("aVeryNestedStringList", newList(newList(newList(newList(newList(newList(newList(newList(newList(newList("Hello Lists!")))))))))));


			aVeryNestedStringConfig = builder
					.comment("a very nested string config (10x Configs)")
					.translation("aVeryNestedStringConfig")
					// 10x Config
					.define("aVeryNestedStringConfig", newConfig(newConfig(newConfig(newConfig(newConfig(newConfig(newConfig(newConfig(newConfig(newList("Hello Configs!")))))))))));

			aPathDefinedString = builder
					.comment("a pathDefinedString")
					.translation("aPathDefinedString")
					.define("aPathDefinedString0.aPathDefinedString1.aPathDefinedString2", "Did this work?");

			builder.comment("Category0 configuration settings")
					.push("category0");
			{
				category0_aBoolean = builder
						.comment("category0 > a Boolean")
						.translation("category0_aBoolean")
						.worldRestart()
						.define("category0_aBoolean", true);

				category0_anInt = builder
						.comment("category0 > an Int")
						.translation("category0_anInt")
						.worldRestart()
						.defineInRange("category0_anInt", 10, 0, 100);

				builder.comment("Category0 > category1 configuration settings")
						.push("category1");
				{
					category0_category1_aBoolean = builder
							.comment("category0 > category1 a Boolean")
							.translation("category0_category1_aBoolean")
							.worldRestart()
							.define("category0_category1_aBoolean", true);

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

		@SafeVarargs
		private static <T> CommentedConfig newConfig(final T... elements) {
			final CommentedConfig config = TomlFormat.newConfig();
			for (final T element : elements)
				config.add(Integer.toString(element.hashCode()), element);
			return config;
		}

	}

}
