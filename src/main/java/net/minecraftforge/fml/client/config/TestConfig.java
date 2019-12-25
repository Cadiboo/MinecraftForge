package net.minecraftforge.fml.client.config;

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
import java.time.ZoneId;
import java.time.ZoneOffset;
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
		bakeAndDebug(() -> aStringListOld, COMMON.aStringListOld, $ -> aStringListOld = $);
		bakeAndDebug(() -> aStringListList, COMMON.aStringListList, $ -> aStringListList = $);

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
		private final ConfigValue<List<? extends String>> aStringListOld;
		private final ConfigValue<List<List<String>>> aStringListList;

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
