/*
 * Minecraft Forge
 * Copyright (c) 2016-2019.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation version 2.1
 * of the License.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */

package net.minecraftforge.fml.client.config;

import net.minecraft.item.DyeColor;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;
import net.minecraftforge.common.ForgeConfigSpec.ConfigValue;
import net.minecraftforge.common.ForgeConfigSpec.DoubleValue;
import net.minecraftforge.common.ForgeConfigSpec.EnumValue;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;
import net.minecraftforge.common.ForgeConfigSpec.LongValue;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.config.ModConfig;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static net.minecraftforge.fml.loading.LogMarkers.FORGEMOD;

public class TestConfig {

	public static final Common COMMON;
	public static final ForgeConfigSpec commonSpec;

	private static boolean aBoolean;
	private static int anInt;
	private static float aFloat;
	private static long aLong;
	private static double aDouble;
	private static DyeColor anEnum;
	private static List<String> aStringList;
	private static List<List<String>> aStringListList;
	private static boolean category0_aBoolean;
	private static int category0_anInt;
	private static boolean category0_category1_aBoolean;
	private static int category0_category1_anInt;

	static {
		final Pair<Common, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(Common::new);
		commonSpec = specPair.getRight();
		COMMON = specPair.getLeft();
	}

	@SubscribeEvent
	public static void onEvent(final ModConfig.ModConfigEvent configEvent) {
		Logger logger = LogManager.getLogger();
		logger.fatal(FORGEMOD, "Test config Event!");
		bakeConfig();
	}

	private static void bakeConfig() {
		Logger logger = LogManager.getLogger();
		aBoolean = COMMON.aBoolean.get();
		anInt = COMMON.anInt.get();
		aFloat = COMMON.aFloat.get();
		aLong = COMMON.aLong.get();
		aDouble = COMMON.aDouble.get();
		anEnum = COMMON.anEnum.get();
		aStringList = COMMON.aStringList.get();
		aStringListList = COMMON.aStringListList.get();

		category0_aBoolean = COMMON.category0_aBoolean.get();
		category0_anInt = COMMON.category0_anInt.get();

		category0_category1_aBoolean = COMMON.category0_category1_aBoolean.get();
		category0_category1_anInt = COMMON.category0_category1_anInt.get();
	}

	@SubscribeEvent
	public static void onLoad(final ModConfig.Loading configEvent) {
		LogManager.getLogger().fatal(FORGEMOD, "Loaded Test config file {}", configEvent.getConfig().getFileName());
	}

	@SubscribeEvent
	public static void onFileChange(final ModConfig.ConfigReloading configEvent) {
		LogManager.getLogger().fatal(FORGEMOD, "Test config just got changed on the file system!");
	}

	public static class Common {

		private final BooleanValue aBoolean;
		private final IntValue anInt;
		private final ForgeConfigSpec.FloatValue aFloat;
		private final LongValue aLong;
		private final DoubleValue aDouble;
		private final EnumValue<DyeColor> anEnum;
		private final ConfigValue<List<String>> aStringList;
		private final ConfigValue<List<List<String>>> aStringListList;

		private final BooleanValue category0_aBoolean;
		private final IntValue category0_anInt;

		private final BooleanValue category0_category1_aBoolean;
		private final IntValue category0_category1_anInt;

		Common(ForgeConfigSpec.Builder builder) {

			aBoolean = builder
					.comment("aBoolean")
					.translation("aBoolean")
					.worldRestart()
					.define("aBoolean", false);

			anInt = builder
					.comment("anInt")
					.translation("anInt")
					.worldRestart()
					.defineInRange("anInt", 10, 0, 100);

			aFloat = builder
					.comment("aFloat")
					.translation("aFloat")
					.worldRestart()
					.defineInRange("aFloat", 0.5F, 0.0F, 1.0F);

			aLong = builder
					.comment("aLong")
					.translation("aLong")
					.worldRestart()
					.defineInRange("aLong", Long.MAX_VALUE / 2L, 0L, Long.MAX_VALUE);

			aDouble = builder
					.comment("aDouble")
					.translation("aDouble")
					.worldRestart()
					.defineInRange("aDouble", 0.05D, 0.0D, 1.0D);

			anEnum = builder
					.comment("anEnum")
					.translation("anEnum")
					.defineEnum("anEnum", DyeColor.WHITE);

			aStringList = builder
					.comment("aStringList")
					.translation("aStringList")
					.define("aStringList", new ArrayList<>());

			aStringListList = builder
					.comment("aStringListList")
					.translation("aStringListList")
					.define("aStringListList", Arrays.asList(Arrays.asList("Hello"), Arrays.asList("World")));

			builder.push("category0");
			builder.comment("category0 configuration settings");
			{
				category0_aBoolean = builder
						.comment("category0_aBoolean")
						.translation("category0_aBoolean")
						.worldRestart()
						.define("category0_aBoolean", false);

				category0_anInt = builder
						.comment("category0_anInt")
						.translation("category0_anInt")
						.worldRestart()
						.defineInRange("category0_anInt", 10, 0, 100);

				builder.push("category1");
				builder.comment("category0 > category1 configuration settings");
				{
					category0_category1_aBoolean = builder
							.comment("category0_category1_aBoolean")
							.translation("category0_category1_aBoolean")
							.worldRestart()
							.define("category0_category1_aBoolean", false);

					category0_category1_anInt = builder
							.comment("category0_category1_anInt")
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
