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
import net.minecraftforge.common.ForgeConfigSpec.ShortValue;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.client.config.entry.widget.IDisplayColorableEnum;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

import java.awt.Color;
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

/**
 * Mod to test that all config stuff works correctly.
 * Used for testing & debugging the config spec and config gui.
 *
 * @author Cadiboo
 */
@Mod(ConfigTest.MOD_ID)
public class ConfigTest {

	public static final Marker CONFIG_TEST = MarkerManager.getMarker("CONFIG_TEST");
	public static final String MOD_ID = "config_test";

	public ConfigTest() {
		ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, TestConfig.Common.SPEC);
		ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, TestConfig.Client.SPEC);
//		ModLoadingContext.get().registerConfig(ModConfig.Type.PLAYER, TestConfig.Player.SPEC);
		ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, TestConfig.Server.SPEC);
	}

	/**
	 * For testing colored enums in the GUI
	 */
	enum HSBColor implements IDisplayColorableEnum {
		H_0("0", 0xff0000),
		H_15("15", 0xff4000),
		H_30("30", 0xff8000),
		H_45("45", 0xffbf00),
		H_60("60", 0xffff00),
		H_75("75", 0xbfff00),
		H_90("90", 0x80ff00),
		H_105("105", 0x40ff00),
		H_120("120", 0x00ff00),
		H_135("135", 0x00ff40),
		H_150("150", 0x00ff80),
		H_165("165", 0x00ffbf),
		H_180("180", 0x00ffff),
		H_195("195", 0x00bfff),
		H_210("210", 0x0080ff),
		H_225("225", 0x0040ff),
		H_240("240", 0x0000ff),
		H_255("255", 0x4000ff),
		H_270("270", 0x8000ff),
		H_285("285", 0xbf00ff),
		H_300("300", 0xff00ff),
		H_315("315", 0xff00bf),
		H_330("330", 0xff0080),
		H_345("345", 0xff0040),

		;

		private final String name;
		private final int color;

		HSBColor(final String hue, final int color) {
			this.name = hue;
			this.color = color;
		}

		@Override
		public String toString() {
			return name;
		}

		@Override
		public int getDisplayColor() {
			return color;
		}
	}

	/**
	 * For testing colored enums in the GUI
	 */
	enum HtmlColorAlphabetical implements IDisplayColorableEnum {
		ALICE_BLUE("AliceBlue", "Alice Blue", 0xF0F8FF),
		ANTIQUE_WHITE("AntiqueWhite", "Antique White", 0xFAEBD7),
		AQUA("Aqua", "Aqua", 0x00FFFF),
		AQUAMARINE("Aquamarine", "Aquamarine", 0x7FFFD4),
		AZURE("Azure", "Azure", 0xF0FFFF),
		BEIGE("Beige", "Beige", 0xF5F5DC),
		BISQUE("Bisque", "Bisque", 0xFFE4C4),
		BLACK("Black", "Black", 0x000000),
		BLANCHED_ALMOND("BlanchedAlmond", "Blanched Almond", 0xFFEBCD),
		BLUE("Blue", "Blue", 0x0000FF),
		BLUE_VIOLET("BlueViolet", "Blue Violet", 0x8A2BE2),
		BROWN("Brown", "Brown", 0xA52A2A),
		BURLY_WOOD("BurlyWood", "Burly Wood", 0xDEB887),
		CADET_BLUE("CadetBlue", "Cadet Blue", 0x5F9EA0),
		CHARTREUSE("Chartreuse", "Chartreuse", 0x7FFF00),
		CHOCOLATE("Chocolate", "Chocolate", 0xD2691E),
		CORAL("Coral", "Coral", 0xFF7F50),
		CORNFLOWER_BLUE("CornflowerBlue", "Cornflower Blue", 0x6495ED),
		CORNSILK("Cornsilk", "Cornsilk", 0xFFF8DC),
		CRIMSON("Crimson", "Crimson", 0xDC143C),
		CYAN("Cyan", "Cyan", 0x00FFFF),
		DARK_BLUE("DarkBlue", "Dark Blue", 0x00008B),
		DARK_CYAN("DarkCyan", "Dark Cyan", 0x008B8B),
		DARK_GOLDEN_ROD("DarkGoldenRod", "Dark Golden Rod", 0xB8860B),
		DARK_GRAY("DarkGray", "Dark Gray", 0xA9A9A9),
		DARK_GREY("DarkGrey", "Dark Grey", 0xA9A9A9),
		DARK_GREEN("DarkGreen", "Dark Green", 0x006400),
		DARK_KHAKI("DarkKhaki", "Dark Khaki", 0xBDB76B),
		DARK_MAGENTA("DarkMagenta", "Dark Magenta", 0x8B008B),
		DARK_OLIVE_GREEN("DarkOliveGreen", "Dark Olive Green", 0x556B2F),
		DARK_ORANGE("DarkOrange", "Dark Orange", 0xFF8C00),
		DARK_ORCHID("DarkOrchid", "Dark Orchid", 0x9932CC),
		DARK_RED("DarkRed", "Dark Red", 0x8B0000),
		DARK_SALMON("DarkSalmon", "Dark Salmon", 0xE9967A),
		DARK_SEA_GREEN("DarkSeaGreen", "Dark Sea Green", 0x8FBC8F),
		DARK_SLATE_BLUE("DarkSlateBlue", "Dark Slate Blue", 0x483D8B),
		DARK_SLATE_GRAY("DarkSlateGray", "Dark Slate Gray", 0x2F4F4F),
		DARK_SLATE_GREY("DarkSlateGrey", "Dark Slate Grey", 0x2F4F4F),
		DARK_TURQUOISE("DarkTurquoise", "Dark Turquoise", 0x00CED1),
		DARK_VIOLET("DarkViolet", "Dark Violet", 0x9400D3),
		DEEP_PINK("DeepPink", "Deep Pink", 0xFF1493),
		DEEP_SKY_BLUE("DeepSkyBlue", "Deep Sky Blue", 0x00BFFF),
		DIM_GRAY("DimGray", "Dim Gray", 0x696969),
		DIM_GREY("DimGrey", "Dim Grey", 0x696969),
		DODGER_BLUE("DodgerBlue", "Dodger Blue", 0x1E90FF),
		FIRE_BRICK("FireBrick", "Fire Brick", 0xB22222),
		FLORAL_WHITE("FloralWhite", "Floral White", 0xFFFAF0),
		FOREST_GREEN("ForestGreen", "Forest Green", 0x228B22),
		FUCHSIA("Fuchsia", "Fuchsia", 0xFF00FF),
		GAINSBORO("Gainsboro", "Gainsboro", 0xDCDCDC),
		GHOST_WHITE("GhostWhite", "Ghost White", 0xF8F8FF),
		GOLD("Gold", "Gold", 0xFFD700),
		GOLDEN_ROD("GoldenRod", "Golden Rod", 0xDAA520),
		GRAY("Gray", "Gray", 0x808080),
		GREY("Grey", "Grey", 0x808080),
		GREEN("Green", "Green", 0x008000),
		GREEN_YELLOW("GreenYellow", "Green Yellow", 0xADFF2F),
		HONEY_DEW("HoneyDew", "Honey Dew", 0xF0FFF0),
		HOT_PINK("HotPink", "Hot Pink", 0xFF69B4),
		INDIAN_RED("IndianRed", "Indian Red", 0xCD5C5C),
		INDIGO("Indigo", "Indigo", 0x4B0082),
		IVORY("Ivory", "Ivory", 0xFFFFF0),
		KHAKI("Khaki", "Khaki", 0xF0E68C),
		LAVENDER("Lavender", "Lavender", 0xE6E6FA),
		LAVENDER_BLUSH("LavenderBlush", "Lavender Blush", 0xFFF0F5),
		LAWN_GREEN("LawnGreen", "Lawn Green", 0x7CFC00),
		LEMON_CHIFFON("LemonChiffon", "Lemon Chiffon", 0xFFFACD),
		LIGHT_BLUE("LightBlue", "Light Blue", 0xADD8E6),
		LIGHT_CORAL("LightCoral", "Light Coral", 0xF08080),
		LIGHT_CYAN("LightCyan", "Light Cyan", 0xE0FFFF),
		LIGHT_GOLDEN_ROD_YELLOW("LightGoldenRodYellow", "Light Golden Rod Yellow", 0xFAFAD2),
		LIGHT_GRAY("LightGray", "Light Gray", 0xD3D3D3),
		LIGHT_GREY("LightGrey", "Light Grey", 0xD3D3D3),
		LIGHT_GREEN("LightGreen", "Light Green", 0x90EE90),
		LIGHT_PINK("LightPink", "Light Pink", 0xFFB6C1),
		LIGHT_SALMON("LightSalmon", "Light Salmon", 0xFFA07A),
		LIGHT_SEA_GREEN("LightSeaGreen", "Light Sea Green", 0x20B2AA),
		LIGHT_SKY_BLUE("LightSkyBlue", "Light Sky Blue", 0x87CEFA),
		LIGHT_SLATE_GRAY("LightSlateGray", "Light Slate Gray", 0x778899),
		LIGHT_SLATE_GREY("LightSlateGrey", "Light Slate Grey", 0x778899),
		LIGHT_STEEL_BLUE("LightSteelBlue", "Light Steel Blue", 0xB0C4DE),
		LIGHT_YELLOW("LightYellow", "Light Yellow", 0xFFFFE0),
		LIME("Lime", "Lime", 0x00FF00),
		LIME_GREEN("LimeGreen", "Lime Green", 0x32CD32),
		LINEN("Linen", "Linen", 0xFAF0E6),
		MAGENTA("Magenta", "Magenta", 0xFF00FF),
		MAROON("Maroon", "Maroon", 0x800000),
		MEDIUM_AQUA_MARINE("MediumAquaMarine", "Medium Aqua Marine", 0x66CDAA),
		MEDIUM_BLUE("MediumBlue", "Medium Blue", 0x0000CD),
		MEDIUM_ORCHID("MediumOrchid", "Medium Orchid", 0xBA55D3),
		MEDIUM_PURPLE("MediumPurple", "Medium Purple", 0x9370DB),
		MEDIUM_SEA_GREEN("MediumSeaGreen", "Medium Sea Green", 0x3CB371),
		MEDIUM_SLATE_BLUE("MediumSlateBlue", "Medium Slate Blue", 0x7B68EE),
		MEDIUM_SPRING_GREEN("MediumSpringGreen", "Medium Spring Green", 0x00FA9A),
		MEDIUM_TURQUOISE("MediumTurquoise", "Medium Turquoise", 0x48D1CC),
		MEDIUM_VIOLET_RED("MediumVioletRed", "Medium Violet Red", 0xC71585),
		MIDNIGHT_BLUE("MidnightBlue", "Midnight Blue", 0x191970),
		MINT_CREAM("MintCream", "Mint Cream", 0xF5FFFA),
		MISTY_ROSE("MistyRose", "Misty Rose", 0xFFE4E1),
		MOCCASIN("Moccasin", "Moccasin", 0xFFE4B5),
		NAVAJO_WHITE("NavajoWhite", "Navajo White", 0xFFDEAD),
		NAVY("Navy", "Navy", 0x000080),
		OLD_LACE("OldLace", "Old Lace", 0xFDF5E6),
		OLIVE("Olive", "Olive", 0x808000),
		OLIVE_DRAB("OliveDrab", "Olive Drab", 0x6B8E23),
		ORANGE("Orange", "Orange", 0xFFA500),
		ORANGE_RED("OrangeRed", "Orange Red", 0xFF4500),
		ORCHID("Orchid", "Orchid", 0xDA70D6),
		PALE_GOLDEN_ROD("PaleGoldenRod", "Pale Golden Rod", 0xEEE8AA),
		PALE_GREEN("PaleGreen", "Pale Green", 0x98FB98),
		PALE_TURQUOISE("PaleTurquoise", "Pale Turquoise", 0xAFEEEE),
		PALE_VIOLET_RED("PaleVioletRed", "Pale Violet Red", 0xDB7093),
		PAPAYA_WHIP("PapayaWhip", "Papaya Whip", 0xFFEFD5),
		PEACH_PUFF("PeachPuff", "Peach Puff", 0xFFDAB9),
		PERU("Peru", "Peru", 0xCD853F),
		PINK("Pink", "Pink", 0xFFC0CB),
		PLUM("Plum", "Plum", 0xDDA0DD),
		POWDER_BLUE("PowderBlue", "Powder Blue", 0xB0E0E6),
		PURPLE("Purple", "Purple", 0x800080),
		REBECCA_PURPLE("RebeccaPurple", "Rebecca Purple", 0x663399),
		RED("Red", "Red", 0xFF0000),
		ROSY_BROWN("RosyBrown", "Rosy Brown", 0xBC8F8F),
		ROYAL_BLUE("RoyalBlue", "Royal Blue", 0x4169E1),
		SADDLE_BROWN("SaddleBrown", "Saddle Brown", 0x8B4513),
		SALMON("Salmon", "Salmon", 0xFA8072),
		SANDY_BROWN("SandyBrown", "Sandy Brown", 0xF4A460),
		SEA_GREEN("SeaGreen", "Sea Green", 0x2E8B57),
		SEA_SHELL("SeaShell", "Sea Shell", 0xFFF5EE),
		SIENNA("Sienna", "Sienna", 0xA0522D),
		SILVER("Silver", "Silver", 0xC0C0C0),
		SKY_BLUE("SkyBlue", "Sky Blue", 0x87CEEB),
		SLATE_BLUE("SlateBlue", "Slate Blue", 0x6A5ACD),
		SLATE_GRAY("SlateGray", "Slate Gray", 0x708090),
		SLATE_GREY("SlateGrey", "Slate Grey", 0x708090),
		SNOW("Snow", "Snow", 0xFFFAFA),
		SPRING_GREEN("SpringGreen", "Spring Green", 0x00FF7F),
		STEEL_BLUE("SteelBlue", "Steel Blue", 0x4682B4),
		TAN("Tan", "Tan", 0xD2B48C),
		TEAL("Teal", "Teal", 0x008080),
		THISTLE("Thistle", "Thistle", 0xD8BFD8),
		TOMATO("Tomato", "Tomato", 0xFF6347),
		TURQUOISE("Turquoise", "Turquoise", 0x40E0D0),
		VIOLET("Violet", "Violet", 0xEE82EE),
		WHEAT("Wheat", "Wheat", 0xF5DEB3),
		WHITE("White", "White", 0xFFFFFF),
		WHITE_SMOKE("WhiteSmoke", "White Smoke", 0xF5F5F5),
		YELLOW("Yellow", "Yellow", 0xFFFF00),
		YELLOW_GREEN("YellowGreen", "Yellow Green", 0x9ACD32),

		;

		private final String squashedName;

		private final String spacedName;
		private final int color;

		HtmlColorAlphabetical(final String squashedName, final String spacedName, final int color) {
			this.squashedName = Objects.requireNonNull(squashedName, "squashedName");
			this.spacedName = Objects.requireNonNull(spacedName, "spacedName");
			this.color = color;
		}

		@Override
		public String toString() {
			return spacedName;
		}

		@Override
		public int getDisplayColor() {
			return color;
		}
	}

	/**
	 * For testing colored enums in the GUI
	 */
	enum HtmlColorLightestFirst implements IDisplayColorableEnum {
		WHITE("White", "White", 0xFFFFFF),
		IVORY("Ivory", "Ivory", 0xFFFFF0),
		LIGHT_YELLOW("LightYellow", "Light Yellow", 0xFFFFE0),
		YELLOW("Yellow", "Yellow", 0xFFFF00),
		SNOW("Snow", "Snow", 0xFFFAFA),
		FLORAL_WHITE("FloralWhite", "Floral White", 0xFFFAF0),
		LEMON_CHIFFON("LemonChiffon", "Lemon Chiffon", 0xFFFACD),
		CORNSILK("Cornsilk", "Cornsilk", 0xFFF8DC),
		SEA_SHELL("SeaShell", "Sea Shell", 0xFFF5EE),
		LAVENDER_BLUSH("LavenderBlush", "Lavender Blush", 0xFFF0F5),
		PAPAYA_WHIP("PapayaWhip", "Papaya Whip", 0xFFEFD5),
		BLANCHED_ALMOND("BlanchedAlmond", "Blanched Almond", 0xFFEBCD),
		MISTY_ROSE("MistyRose", "Misty Rose", 0xFFE4E1),
		BISQUE("Bisque", "Bisque", 0xFFE4C4),
		MOCCASIN("Moccasin", "Moccasin", 0xFFE4B5),
		NAVAJO_WHITE("NavajoWhite", "Navajo White", 0xFFDEAD),
		PEACH_PUFF("PeachPuff", "Peach Puff", 0xFFDAB9),
		GOLD("Gold", "Gold", 0xFFD700),
		PINK("Pink", "Pink", 0xFFC0CB),
		LIGHT_PINK("LightPink", "Light Pink", 0xFFB6C1),
		ORANGE("Orange", "Orange", 0xFFA500),
		LIGHT_SALMON("LightSalmon", "Light Salmon", 0xFFA07A),
		DARK_ORANGE("DarkOrange", "Dark Orange", 0xFF8C00),
		CORAL("Coral", "Coral", 0xFF7F50),
		HOT_PINK("HotPink", "Hot Pink", 0xFF69B4),
		TOMATO("Tomato", "Tomato", 0xFF6347),
		ORANGE_RED("OrangeRed", "Orange Red", 0xFF4500),
		DEEP_PINK("DeepPink", "Deep Pink", 0xFF1493),
		MAGENTA("Magenta", "Magenta", 0xFF00FF),
		FUCHSIA("Fuchsia", "Fuchsia", 0xFF00FF),
		RED("Red", "Red", 0xFF0000),
		OLD_LACE("OldLace", "Old Lace", 0xFDF5E6),
		LIGHT_GOLDEN_ROD_YELLOW("LightGoldenRodYellow", "Light Golden Rod Yellow", 0xFAFAD2),
		LINEN("Linen", "Linen", 0xFAF0E6),
		ANTIQUE_WHITE("AntiqueWhite", "Antique White", 0xFAEBD7),
		SALMON("Salmon", "Salmon", 0xFA8072),
		GHOST_WHITE("GhostWhite", "Ghost White", 0xF8F8FF),
		MINT_CREAM("MintCream", "Mint Cream", 0xF5FFFA),
		WHITE_SMOKE("WhiteSmoke", "White Smoke", 0xF5F5F5),
		BEIGE("Beige", "Beige", 0xF5F5DC),
		WHEAT("Wheat", "Wheat", 0xF5DEB3),
		SANDY_BROWN("SandyBrown", "Sandy Brown", 0xF4A460),
		AZURE("Azure", "Azure", 0xF0FFFF),
		HONEY_DEW("HoneyDew", "Honey Dew", 0xF0FFF0),
		ALICE_BLUE("AliceBlue", "Alice Blue", 0xF0F8FF),
		KHAKI("Khaki", "Khaki", 0xF0E68C),
		LIGHT_CORAL("LightCoral", "Light Coral", 0xF08080),
		PALE_GOLDEN_ROD("PaleGoldenRod", "Pale Golden Rod", 0xEEE8AA),
		VIOLET("Violet", "Violet", 0xEE82EE),
		DARK_SALMON("DarkSalmon", "Dark Salmon", 0xE9967A),
		LAVENDER("Lavender", "Lavender", 0xE6E6FA),
		LIGHT_CYAN("LightCyan", "Light Cyan", 0xE0FFFF),
		BURLY_WOOD("BurlyWood", "Burly Wood", 0xDEB887),
		PLUM("Plum", "Plum", 0xDDA0DD),
		GAINSBORO("Gainsboro", "Gainsboro", 0xDCDCDC),
		CRIMSON("Crimson", "Crimson", 0xDC143C),
		PALE_VIOLET_RED("PaleVioletRed", "Pale Violet Red", 0xDB7093),
		GOLDEN_ROD("GoldenRod", "Golden Rod", 0xDAA520),
		ORCHID("Orchid", "Orchid", 0xDA70D6),
		THISTLE("Thistle", "Thistle", 0xD8BFD8),
		LIGHT_GREY("LightGrey", "Light Grey", 0xD3D3D3),
		LIGHT_GRAY("LightGray", "Light Gray", 0xD3D3D3),
		TAN("Tan", "Tan", 0xD2B48C),
		CHOCOLATE("Chocolate", "Chocolate", 0xD2691E),
		PERU("Peru", "Peru", 0xCD853F),
		INDIAN_RED("IndianRed", "Indian Red", 0xCD5C5C),
		MEDIUM_VIOLET_RED("MediumVioletRed", "Medium Violet Red", 0xC71585),
		SILVER("Silver", "Silver", 0xC0C0C0),
		DARK_KHAKI("DarkKhaki", "Dark Khaki", 0xBDB76B),
		ROSY_BROWN("RosyBrown", "Rosy Brown", 0xBC8F8F),
		MEDIUM_ORCHID("MediumOrchid", "Medium Orchid", 0xBA55D3),
		DARK_GOLDEN_ROD("DarkGoldenRod", "Dark Golden Rod", 0xB8860B),
		FIRE_BRICK("FireBrick", "Fire Brick", 0xB22222),
		POWDER_BLUE("PowderBlue", "Powder Blue", 0xB0E0E6),
		LIGHT_STEEL_BLUE("LightSteelBlue", "Light Steel Blue", 0xB0C4DE),
		PALE_TURQUOISE("PaleTurquoise", "Pale Turquoise", 0xAFEEEE),
		GREEN_YELLOW("GreenYellow", "Green Yellow", 0xADFF2F),
		LIGHT_BLUE("LightBlue", "Light Blue", 0xADD8E6),
		DARK_GREY("DarkGrey", "Dark Grey", 0xA9A9A9),
		DARK_GRAY("DarkGray", "Dark Gray", 0xA9A9A9),
		BROWN("Brown", "Brown", 0xA52A2A),
		SIENNA("Sienna", "Sienna", 0xA0522D),
		YELLOW_GREEN("YellowGreen", "Yellow Green", 0x9ACD32),
		DARK_ORCHID("DarkOrchid", "Dark Orchid", 0x9932CC),
		PALE_GREEN("PaleGreen", "Pale Green", 0x98FB98),
		DARK_VIOLET("DarkViolet", "Dark Violet", 0x9400D3),
		MEDIUM_PURPLE("MediumPurple", "Medium Purple", 0x9370DB),
		LIGHT_GREEN("LightGreen", "Light Green", 0x90EE90),
		DARK_SEA_GREEN("DarkSeaGreen", "Dark Sea Green", 0x8FBC8F),
		SADDLE_BROWN("SaddleBrown", "Saddle Brown", 0x8B4513),
		DARK_MAGENTA("DarkMagenta", "Dark Magenta", 0x8B008B),
		DARK_RED("DarkRed", "Dark Red", 0x8B0000),
		BLUE_VIOLET("BlueViolet", "Blue Violet", 0x8A2BE2),
		LIGHT_SKY_BLUE("LightSkyBlue", "Light Sky Blue", 0x87CEFA),
		SKY_BLUE("SkyBlue", "Sky Blue", 0x87CEEB),
		GREY("Grey", "Grey", 0x808080),
		GRAY("Gray", "Gray", 0x808080),
		OLIVE("Olive", "Olive", 0x808000),
		PURPLE("Purple", "Purple", 0x800080),
		MAROON("Maroon", "Maroon", 0x800000),
		AQUAMARINE("Aquamarine", "Aquamarine", 0x7FFFD4),
		CHARTREUSE("Chartreuse", "Chartreuse", 0x7FFF00),
		LAWN_GREEN("LawnGreen", "Lawn Green", 0x7CFC00),
		MEDIUM_SLATE_BLUE("MediumSlateBlue", "Medium Slate Blue", 0x7B68EE),
		LIGHT_SLATE_GREY("LightSlateGrey", "Light Slate Grey", 0x778899),
		LIGHT_SLATE_GRAY("LightSlateGray", "Light Slate Gray", 0x778899),
		SLATE_GREY("SlateGrey", "Slate Grey", 0x708090),
		SLATE_GRAY("SlateGray", "Slate Gray", 0x708090),
		OLIVE_DRAB("OliveDrab", "Olive Drab", 0x6B8E23),
		SLATE_BLUE("SlateBlue", "Slate Blue", 0x6A5ACD),
		DIM_GREY("DimGrey", "Dim Grey", 0x696969),
		DIM_GRAY("DimGray", "Dim Gray", 0x696969),
		MEDIUM_AQUA_MARINE("MediumAquaMarine", "Medium Aqua Marine", 0x66CDAA),
		REBECCA_PURPLE("RebeccaPurple", "Rebecca Purple", 0x663399),
		CORNFLOWER_BLUE("CornflowerBlue", "Cornflower Blue", 0x6495ED),
		CADET_BLUE("CadetBlue", "Cadet Blue", 0x5F9EA0),
		DARK_OLIVE_GREEN("DarkOliveGreen", "Dark Olive Green", 0x556B2F),
		INDIGO("Indigo", "Indigo", 0x4B0082),
		MEDIUM_TURQUOISE("MediumTurquoise", "Medium Turquoise", 0x48D1CC),
		DARK_SLATE_BLUE("DarkSlateBlue", "Dark Slate Blue", 0x483D8B),
		STEEL_BLUE("SteelBlue", "Steel Blue", 0x4682B4),
		ROYAL_BLUE("RoyalBlue", "Royal Blue", 0x4169E1),
		TURQUOISE("Turquoise", "Turquoise", 0x40E0D0),
		MEDIUM_SEA_GREEN("MediumSeaGreen", "Medium Sea Green", 0x3CB371),
		LIME_GREEN("LimeGreen", "Lime Green", 0x32CD32),
		DARK_SLATE_GREY("DarkSlateGrey", "Dark Slate Grey", 0x2F4F4F),
		DARK_SLATE_GRAY("DarkSlateGray", "Dark Slate Gray", 0x2F4F4F),
		SEA_GREEN("SeaGreen", "Sea Green", 0x2E8B57),
		FOREST_GREEN("ForestGreen", "Forest Green", 0x228B22),
		LIGHT_SEA_GREEN("LightSeaGreen", "Light Sea Green", 0x20B2AA),
		DODGER_BLUE("DodgerBlue", "Dodger Blue", 0x1E90FF),
		MIDNIGHT_BLUE("MidnightBlue", "Midnight Blue", 0x191970),
		CYAN("Cyan", "Cyan", 0x00FFFF),
		AQUA("Aqua", "Aqua", 0x00FFFF),
		SPRING_GREEN("SpringGreen", "Spring Green", 0x00FF7F),
		LIME("Lime", "Lime", 0x00FF00),
		MEDIUM_SPRING_GREEN("MediumSpringGreen", "Medium Spring Green", 0x00FA9A),
		DARK_TURQUOISE("DarkTurquoise", "Dark Turquoise", 0x00CED1),
		DEEP_SKY_BLUE("DeepSkyBlue", "Deep Sky Blue", 0x00BFFF),
		DARK_CYAN("DarkCyan", "Dark Cyan", 0x008B8B),
		TEAL("Teal", "Teal", 0x008080),
		GREEN("Green", "Green", 0x008000),
		DARK_GREEN("DarkGreen", "Dark Green", 0x006400),
		BLUE("Blue", "Blue", 0x0000FF),
		MEDIUM_BLUE("MediumBlue", "Medium Blue", 0x0000CD),
		DARK_BLUE("DarkBlue", "Dark Blue", 0x00008B),
		NAVY("Navy", "Navy", 0x000080),
		BLACK("Black", "Black", 0x000000),
		;

		private final String squashedName;

		private final String spacedName;
		private final int color;

		HtmlColorLightestFirst(final String squashedName, final String spacedName, final int color) {
			this.squashedName = Objects.requireNonNull(squashedName, "squashedName");
			this.spacedName = Objects.requireNonNull(spacedName, "spacedName");
			this.color = color;
		}

		@Override
		public String toString() {
			return spacedName;
		}

		@Override
		public int getDisplayColor() {
			return color;
		}
	}

	/**
	 * For testing colored enums in the GUI
	 */
	enum HtmlColorDarkestFirst implements IDisplayColorableEnum {
		BLACK("Black", "Black", 0x000000),
		NAVY("Navy", "Navy", 0x000080),
		DARK_BLUE("DarkBlue", "Dark Blue", 0x00008B),
		MEDIUM_BLUE("MediumBlue", "Medium Blue", 0x0000CD),
		BLUE("Blue", "Blue", 0x0000FF),
		DARK_GREEN("DarkGreen", "Dark Green", 0x006400),
		GREEN("Green", "Green", 0x008000),
		TEAL("Teal", "Teal", 0x008080),
		DARK_CYAN("DarkCyan", "Dark Cyan", 0x008B8B),
		DEEP_SKY_BLUE("DeepSkyBlue", "Deep Sky Blue", 0x00BFFF),
		DARK_TURQUOISE("DarkTurquoise", "Dark Turquoise", 0x00CED1),
		MEDIUM_SPRING_GREEN("MediumSpringGreen", "Medium Spring Green", 0x00FA9A),
		LIME("Lime", "Lime", 0x00FF00),
		SPRING_GREEN("SpringGreen", "Spring Green", 0x00FF7F),
		AQUA("Aqua", "Aqua", 0x00FFFF),
		CYAN("Cyan", "Cyan", 0x00FFFF),
		MIDNIGHT_BLUE("MidnightBlue", "Midnight Blue", 0x191970),
		DODGER_BLUE("DodgerBlue", "Dodger Blue", 0x1E90FF),
		LIGHT_SEA_GREEN("LightSeaGreen", "Light Sea Green", 0x20B2AA),
		FOREST_GREEN("ForestGreen", "Forest Green", 0x228B22),
		SEA_GREEN("SeaGreen", "Sea Green", 0x2E8B57),
		DARK_SLATE_GRAY("DarkSlateGray", "Dark Slate Gray", 0x2F4F4F),
		DARK_SLATE_GREY("DarkSlateGrey", "Dark Slate Grey", 0x2F4F4F),
		LIME_GREEN("LimeGreen", "Lime Green", 0x32CD32),
		MEDIUM_SEA_GREEN("MediumSeaGreen", "Medium Sea Green", 0x3CB371),
		TURQUOISE("Turquoise", "Turquoise", 0x40E0D0),
		ROYAL_BLUE("RoyalBlue", "Royal Blue", 0x4169E1),
		STEEL_BLUE("SteelBlue", "Steel Blue", 0x4682B4),
		DARK_SLATE_BLUE("DarkSlateBlue", "Dark Slate Blue", 0x483D8B),
		MEDIUM_TURQUOISE("MediumTurquoise", "Medium Turquoise", 0x48D1CC),
		INDIGO("Indigo", "Indigo", 0x4B0082),
		DARK_OLIVE_GREEN("DarkOliveGreen", "Dark Olive Green", 0x556B2F),
		CADET_BLUE("CadetBlue", "Cadet Blue", 0x5F9EA0),
		CORNFLOWER_BLUE("CornflowerBlue", "Cornflower Blue", 0x6495ED),
		REBECCA_PURPLE("RebeccaPurple", "Rebecca Purple", 0x663399),
		MEDIUM_AQUA_MARINE("MediumAquaMarine", "Medium Aqua Marine", 0x66CDAA),
		DIM_GRAY("DimGray", "Dim Gray", 0x696969),
		DIM_GREY("DimGrey", "Dim Grey", 0x696969),
		SLATE_BLUE("SlateBlue", "Slate Blue", 0x6A5ACD),
		OLIVE_DRAB("OliveDrab", "Olive Drab", 0x6B8E23),
		SLATE_GRAY("SlateGray", "Slate Gray", 0x708090),
		SLATE_GREY("SlateGrey", "Slate Grey", 0x708090),
		LIGHT_SLATE_GRAY("LightSlateGray", "Light Slate Gray", 0x778899),
		LIGHT_SLATE_GREY("LightSlateGrey", "Light Slate Grey", 0x778899),
		MEDIUM_SLATE_BLUE("MediumSlateBlue", "Medium Slate Blue", 0x7B68EE),
		LAWN_GREEN("LawnGreen", "Lawn Green", 0x7CFC00),
		CHARTREUSE("Chartreuse", "Chartreuse", 0x7FFF00),
		AQUAMARINE("Aquamarine", "Aquamarine", 0x7FFFD4),
		MAROON("Maroon", "Maroon", 0x800000),
		PURPLE("Purple", "Purple", 0x800080),
		OLIVE("Olive", "Olive", 0x808000),
		GRAY("Gray", "Gray", 0x808080),
		GREY("Grey", "Grey", 0x808080),
		SKY_BLUE("SkyBlue", "Sky Blue", 0x87CEEB),
		LIGHT_SKY_BLUE("LightSkyBlue", "Light Sky Blue", 0x87CEFA),
		BLUE_VIOLET("BlueViolet", "Blue Violet", 0x8A2BE2),
		DARK_RED("DarkRed", "Dark Red", 0x8B0000),
		DARK_MAGENTA("DarkMagenta", "Dark Magenta", 0x8B008B),
		SADDLE_BROWN("SaddleBrown", "Saddle Brown", 0x8B4513),
		DARK_SEA_GREEN("DarkSeaGreen", "Dark Sea Green", 0x8FBC8F),
		LIGHT_GREEN("LightGreen", "Light Green", 0x90EE90),
		MEDIUM_PURPLE("MediumPurple", "Medium Purple", 0x9370DB),
		DARK_VIOLET("DarkViolet", "Dark Violet", 0x9400D3),
		PALE_GREEN("PaleGreen", "Pale Green", 0x98FB98),
		DARK_ORCHID("DarkOrchid", "Dark Orchid", 0x9932CC),
		YELLOW_GREEN("YellowGreen", "Yellow Green", 0x9ACD32),
		SIENNA("Sienna", "Sienna", 0xA0522D),
		BROWN("Brown", "Brown", 0xA52A2A),
		DARK_GRAY("DarkGray", "Dark Gray", 0xA9A9A9),
		DARK_GREY("DarkGrey", "Dark Grey", 0xA9A9A9),
		LIGHT_BLUE("LightBlue", "Light Blue", 0xADD8E6),
		GREEN_YELLOW("GreenYellow", "Green Yellow", 0xADFF2F),
		PALE_TURQUOISE("PaleTurquoise", "Pale Turquoise", 0xAFEEEE),
		LIGHT_STEEL_BLUE("LightSteelBlue", "Light Steel Blue", 0xB0C4DE),
		POWDER_BLUE("PowderBlue", "Powder Blue", 0xB0E0E6),
		FIRE_BRICK("FireBrick", "Fire Brick", 0xB22222),
		DARK_GOLDEN_ROD("DarkGoldenRod", "Dark Golden Rod", 0xB8860B),
		MEDIUM_ORCHID("MediumOrchid", "Medium Orchid", 0xBA55D3),
		ROSY_BROWN("RosyBrown", "Rosy Brown", 0xBC8F8F),
		DARK_KHAKI("DarkKhaki", "Dark Khaki", 0xBDB76B),
		SILVER("Silver", "Silver", 0xC0C0C0),
		MEDIUM_VIOLET_RED("MediumVioletRed", "Medium Violet Red", 0xC71585),
		INDIAN_RED("IndianRed", "Indian Red", 0xCD5C5C),
		PERU("Peru", "Peru", 0xCD853F),
		CHOCOLATE("Chocolate", "Chocolate", 0xD2691E),
		TAN("Tan", "Tan", 0xD2B48C),
		LIGHT_GRAY("LightGray", "Light Gray", 0xD3D3D3),
		LIGHT_GREY("LightGrey", "Light Grey", 0xD3D3D3),
		THISTLE("Thistle", "Thistle", 0xD8BFD8),
		ORCHID("Orchid", "Orchid", 0xDA70D6),
		GOLDEN_ROD("GoldenRod", "Golden Rod", 0xDAA520),
		PALE_VIOLET_RED("PaleVioletRed", "Pale Violet Red", 0xDB7093),
		CRIMSON("Crimson", "Crimson", 0xDC143C),
		GAINSBORO("Gainsboro", "Gainsboro", 0xDCDCDC),
		PLUM("Plum", "Plum", 0xDDA0DD),
		BURLY_WOOD("BurlyWood", "Burly Wood", 0xDEB887),
		LIGHT_CYAN("LightCyan", "Light Cyan", 0xE0FFFF),
		LAVENDER("Lavender", "Lavender", 0xE6E6FA),
		DARK_SALMON("DarkSalmon", "Dark Salmon", 0xE9967A),
		VIOLET("Violet", "Violet", 0xEE82EE),
		PALE_GOLDEN_ROD("PaleGoldenRod", "Pale Golden Rod", 0xEEE8AA),
		LIGHT_CORAL("LightCoral", "Light Coral", 0xF08080),
		KHAKI("Khaki", "Khaki", 0xF0E68C),
		ALICE_BLUE("AliceBlue", "Alice Blue", 0xF0F8FF),
		HONEY_DEW("HoneyDew", "Honey Dew", 0xF0FFF0),
		AZURE("Azure", "Azure", 0xF0FFFF),
		SANDY_BROWN("SandyBrown", "Sandy Brown", 0xF4A460),
		WHEAT("Wheat", "Wheat", 0xF5DEB3),
		BEIGE("Beige", "Beige", 0xF5F5DC),
		WHITE_SMOKE("WhiteSmoke", "White Smoke", 0xF5F5F5),
		MINT_CREAM("MintCream", "Mint Cream", 0xF5FFFA),
		GHOST_WHITE("GhostWhite", "Ghost White", 0xF8F8FF),
		SALMON("Salmon", "Salmon", 0xFA8072),
		ANTIQUE_WHITE("AntiqueWhite", "Antique White", 0xFAEBD7),
		LINEN("Linen", "Linen", 0xFAF0E6),
		LIGHT_GOLDEN_ROD_YELLOW("LightGoldenRodYellow", "Light Golden Rod Yellow", 0xFAFAD2),
		OLD_LACE("OldLace", "Old Lace", 0xFDF5E6),
		RED("Red", "Red", 0xFF0000),
		FUCHSIA("Fuchsia", "Fuchsia", 0xFF00FF),
		MAGENTA("Magenta", "Magenta", 0xFF00FF),
		DEEP_PINK("DeepPink", "Deep Pink", 0xFF1493),
		ORANGE_RED("OrangeRed", "Orange Red", 0xFF4500),
		TOMATO("Tomato", "Tomato", 0xFF6347),
		HOT_PINK("HotPink", "Hot Pink", 0xFF69B4),
		CORAL("Coral", "Coral", 0xFF7F50),
		DARK_ORANGE("DarkOrange", "Dark Orange", 0xFF8C00),
		LIGHT_SALMON("LightSalmon", "Light Salmon", 0xFFA07A),
		ORANGE("Orange", "Orange", 0xFFA500),
		LIGHT_PINK("LightPink", "Light Pink", 0xFFB6C1),
		PINK("Pink", "Pink", 0xFFC0CB),
		GOLD("Gold", "Gold", 0xFFD700),
		PEACH_PUFF("PeachPuff", "Peach Puff", 0xFFDAB9),
		NAVAJO_WHITE("NavajoWhite", "Navajo White", 0xFFDEAD),
		MOCCASIN("Moccasin", "Moccasin", 0xFFE4B5),
		BISQUE("Bisque", "Bisque", 0xFFE4C4),
		MISTY_ROSE("MistyRose", "Misty Rose", 0xFFE4E1),
		BLANCHED_ALMOND("BlanchedAlmond", "Blanched Almond", 0xFFEBCD),
		PAPAYA_WHIP("PapayaWhip", "Papaya Whip", 0xFFEFD5),
		LAVENDER_BLUSH("LavenderBlush", "Lavender Blush", 0xFFF0F5),
		SEA_SHELL("SeaShell", "Sea Shell", 0xFFF5EE),
		CORNSILK("Cornsilk", "Cornsilk", 0xFFF8DC),
		LEMON_CHIFFON("LemonChiffon", "Lemon Chiffon", 0xFFFACD),
		FLORAL_WHITE("FloralWhite", "Floral White", 0xFFFAF0),
		SNOW("Snow", "Snow", 0xFFFAFA),
		YELLOW("Yellow", "Yellow", 0xFFFF00),
		LIGHT_YELLOW("LightYellow", "Light Yellow", 0xFFFFE0),
		IVORY("Ivory", "Ivory", 0xFFFFF0),
		WHITE("White", "White", 0xFFFFFF),
		;

		private final String squashedName;

		private final String spacedName;
		private final int color;

		HtmlColorDarkestFirst(final String squashedName, final String spacedName, final int color) {
			this.squashedName = Objects.requireNonNull(squashedName, "squashedName");
			this.spacedName = Objects.requireNonNull(spacedName, "spacedName");
			this.color = color;
		}

		@Override
		public String toString() {
			return spacedName;
		}

		@Override
		public int getDisplayColor() {
			return color;
		}
	}

	/**
	 * For testing colored enums in the GUI
	 */
	enum RandomColor implements IDisplayColorableEnum {
		FIRST,
		SECOND,
		THIRD,
		FOURTH,
		FIFTH,
		SIXTH,
		SEVENTH,
		EIGHTH,
		NINTH,
		TENTH,
		;

		@Override
		public String toString() {
			return name() + " " + Math.random();
		}

		@Override
		public int getDisplayColor() {
			return Color.HSBtoRGB((float) Math.random(), 1.0F, 1.0F);
		}
	}

	@Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
	public static class TestConfig {

		@SubscribeEvent
		public static void onEvent(final ModConfig.ModConfigEvent configEvent) {
			final ForgeConfigSpec spec = configEvent.getConfig().getSpec();
			final String changed;
			if (spec == Common.SPEC) {
				Common.bakeAndDebugConfig();
				changed = "Common";
			} else if (spec == Client.SPEC) {
				Client.bakeAndDebugConfig();
				changed = "Client";
			} else if (spec == Player.SPEC) {
				Player.bakeAndDebugConfig();
				changed = "Player";
			} else if (spec == Server.SPEC) {
				Server.bakeAndDebugConfig();
				changed = "Server";
			} else
				return;
			LogManager.getLogger().debug(CONFIG_TEST, "ModConfigEvent for TestConfig @ " + changed + "! " + configEvent.getClass().getSimpleName());
		}

		private static <T> void bakeAndDebug(final Supplier<T> getter, final ConfigValue<T> configValue, final Consumer<T> setter, final Logger logger) {
			T oldValue = getter.get();
			T newValue = configValue.get();
			String path = Strings.join(configValue.getPath(), "_");
//	    	LOGGER.warn(CONFIG_TEST, path + ": " + oldValue + ", " + newValue + ", " + Objects.equals(oldValue, newValue));
			logger.warn(CONFIG_TEST, path + ": " + Objects.equals(oldValue, newValue));
			setter.accept(newValue);
		}

		@SafeVarargs
		private static <E> List<E> newList(E... elements) {
			return Lists.newArrayList(elements);
		}

		@SafeVarargs
		private static <E> CommentedConfig newConfig(final E... elements) {
			final CommentedConfig config = TomlFormat.newConfig();
			for (int i = 0; i < elements.length; i++)
				config.add("element" + i, elements[i]);
			return config;
		}

		public static class Common {

			public static final ConfigImpl CONFIG;
			public static final ForgeConfigSpec SPEC;
			private static final Logger LOGGER = LogManager.getLogger();

			private static boolean aBoolean;
			private static byte aByte;
			private static short aShort;
			private static int anInt;
			private static float aFloat;
			private static long aLong;
			private static double aDouble;
			private static DyeColor anEnum;
			private static String aString;
			private static LocalTime aLocalTime;
			private static LocalDate aLocalDate;
			private static LocalDateTime aLocalDateTime;
			private static OffsetDateTime anOffsetDateTime;
			private static Config aConfig;

			private static List<Boolean> aBooleanList;
			private static List<Byte> aByteList;
			private static List<Short> aShortList;
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
			private static List<List<Short>> aShortListList;
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
			// TODO: Bug in NightConfig - The writer writes this in a way that can't be parsed. See "https://github.com/TheElectronWill/night-config/pull/75"
//			private static List<List<Config>> aConfigListList;
			private static List<List<String>> aStringWeirdListWeirdList;
			private static List<? extends List<String>> aStringListOldList;

			private static Config aBooleanConfig;
			private static Config aByteConfig;
			private static Config aShortConfig;
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
			private static short aShortInList;
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
			private static HSBColor aHSBColor;
			private static HtmlColorAlphabetical aHtmlColorAlphabetical;
			private static HtmlColorLightestFirst aHtmlColorLightestFirst;
			private static HtmlColorDarkestFirst aHtmlColorDarkestFirst;
			private static RandomColor aRandomColor;

			private static boolean category0_aBoolean;
			private static int category0_anInt;

			private static boolean category0_category1_aBoolean;
			private static int category0_category1_anInt;

			static {
				final Pair<ConfigImpl, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(ConfigImpl::new);
				SPEC = specPair.getRight();
				CONFIG = specPair.getLeft();
			}

			private static void bakeAndDebugConfig() {
				bakeAndDebug(() -> aBoolean, CONFIG.aBoolean, $ -> aBoolean = $, LOGGER);
				bakeAndDebug(() -> aByte, CONFIG.aByte, $ -> aByte = $, LOGGER);
				bakeAndDebug(() -> aShort, CONFIG.aShort, $ -> aShort = $, LOGGER);
				bakeAndDebug(() -> anInt, CONFIG.anInteger, $ -> anInt = $, LOGGER);
				bakeAndDebug(() -> aFloat, CONFIG.aFloat, $ -> aFloat = $, LOGGER);
				bakeAndDebug(() -> aLong, CONFIG.aLong, $ -> aLong = $, LOGGER);
				bakeAndDebug(() -> aDouble, CONFIG.aDouble, $ -> aDouble = $, LOGGER);
				bakeAndDebug(() -> anEnum, CONFIG.anEnum, $ -> anEnum = $, LOGGER);
				bakeAndDebug(() -> aString, CONFIG.aString, $ -> aString = $, LOGGER);
				bakeAndDebug(() -> aLocalTime, CONFIG.aLocalTime, $ -> aLocalTime = $, LOGGER);
				bakeAndDebug(() -> aLocalDate, CONFIG.aLocalDate, $ -> aLocalDate = $, LOGGER);
				bakeAndDebug(() -> aLocalDateTime, CONFIG.aLocalDateTime, $ -> aLocalDateTime = $, LOGGER);
				bakeAndDebug(() -> anOffsetDateTime, CONFIG.anOffsetDateTime, $ -> anOffsetDateTime = $, LOGGER);
				bakeAndDebug(() -> aConfig, CONFIG.aConfig, $ -> aConfig = $, LOGGER);

				bakeAndDebug(() -> aBooleanList, CONFIG.aBooleanList, $ -> aBooleanList = $, LOGGER);
				bakeAndDebug(() -> aByteList, CONFIG.aByteList, $ -> aByteList = $, LOGGER);
				bakeAndDebug(() -> aShortList, CONFIG.aShortList, $ -> aShortList = $, LOGGER);
				bakeAndDebug(() -> anIntegerList, CONFIG.anIntegerList, $ -> anIntegerList = $, LOGGER);
				bakeAndDebug(() -> aFloatList, CONFIG.aFloatList, $ -> aFloatList = $, LOGGER);
				bakeAndDebug(() -> aLongList, CONFIG.aLongList, $ -> aLongList = $, LOGGER);
				bakeAndDebug(() -> aDoubleList, CONFIG.aDoubleList, $ -> aDoubleList = $, LOGGER);
				bakeAndDebug(() -> anEnumList, CONFIG.anEnumList, $ -> anEnumList = $, LOGGER);
				bakeAndDebug(() -> aStringList, CONFIG.aStringList, $ -> aStringList = $, LOGGER);
				bakeAndDebug(() -> aLocalTimeList, CONFIG.aLocalTimeList, $ -> aLocalTimeList = $, LOGGER);
				bakeAndDebug(() -> aLocalDateList, CONFIG.aLocalDateList, $ -> aLocalDateList = $, LOGGER);
				bakeAndDebug(() -> aLocalDateTimeList, CONFIG.aLocalDateTimeList, $ -> aLocalDateTimeList = $, LOGGER);
				bakeAndDebug(() -> anOffsetDateTimeList, CONFIG.anOffsetDateTimeList, $ -> anOffsetDateTimeList = $, LOGGER);
				bakeAndDebug(() -> aConfigList, CONFIG.aConfigList, $ -> aConfigList = $, LOGGER);
				bakeAndDebug(() -> aStringWeirdList, CONFIG.aStringWeirdList, $ -> aStringWeirdList = $, LOGGER);
				bakeAndDebug(() -> aStringListOld, CONFIG.aStringListOld, $ -> aStringListOld = $, LOGGER);

				bakeAndDebug(() -> aBooleanListList, CONFIG.aBooleanListList, $ -> aBooleanListList = $, LOGGER);
				bakeAndDebug(() -> aByteListList, CONFIG.aByteListList, $ -> aByteListList = $, LOGGER);
				bakeAndDebug(() -> aShortListList, CONFIG.aShortListList, $ -> aShortListList = $, LOGGER);
				bakeAndDebug(() -> anIntegerListList, CONFIG.anIntegerListList, $ -> anIntegerListList = $, LOGGER);
				bakeAndDebug(() -> aFloatListList, CONFIG.aFloatListList, $ -> aFloatListList = $, LOGGER);
				bakeAndDebug(() -> aLongListList, CONFIG.aLongListList, $ -> aLongListList = $, LOGGER);
				bakeAndDebug(() -> aDoubleListList, CONFIG.aDoubleListList, $ -> aDoubleListList = $, LOGGER);
				bakeAndDebug(() -> anEnumListList, CONFIG.anEnumListList, $ -> anEnumListList = $, LOGGER);
				bakeAndDebug(() -> aStringListList, CONFIG.aStringListList, $ -> aStringListList = $, LOGGER);
				bakeAndDebug(() -> aLocalTimeListList, CONFIG.aLocalTimeListList, $ -> aLocalTimeListList = $, LOGGER);
				bakeAndDebug(() -> aLocalDateListList, CONFIG.aLocalDateListList, $ -> aLocalDateListList = $, LOGGER);
				bakeAndDebug(() -> aLocalDateTimeListList, CONFIG.aLocalDateTimeListList, $ -> aLocalDateTimeListList = $, LOGGER);
				bakeAndDebug(() -> anOffsetDateTimeListList, CONFIG.anOffsetDateTimeListList, $ -> anOffsetDateTimeListList = $, LOGGER);
				// TODO: Bug in NightConfig - The writer writes this in a way that can't be parsed. See "https://github.com/TheElectronWill/night-config/pull/75"
//				bakeAndDebug(() -> aConfigListList, CONFIG.aConfigListList, $ -> aConfigListList = $, LOGGER);
				bakeAndDebug(() -> aStringWeirdListWeirdList, CONFIG.aStringWeirdListWeirdList, $ -> aStringWeirdListWeirdList = $, LOGGER);
				bakeAndDebug(() -> aStringListOldList, CONFIG.aStringListOldList, $ -> aStringListOldList = $, LOGGER);

				bakeAndDebug(() -> aBooleanInList, CONFIG.aBooleanInList, $ -> aBooleanInList = $, LOGGER);
				bakeAndDebug(() -> aByteInList, CONFIG.aByteInList, $ -> aByteInList = $, LOGGER);
				bakeAndDebug(() -> aShortInList, CONFIG.aShortInList, $ -> aShortInList = $, LOGGER);
				bakeAndDebug(() -> anIntInList, CONFIG.anIntegerInList, $ -> anIntInList = $, LOGGER);
				bakeAndDebug(() -> aFloatInList, CONFIG.aFloatInList, $ -> aFloatInList = $, LOGGER);
				bakeAndDebug(() -> aLongInList, CONFIG.aLongInList, $ -> aLongInList = $, LOGGER);
				bakeAndDebug(() -> aDoubleInList, CONFIG.aDoubleInList, $ -> aDoubleInList = $, LOGGER);
				bakeAndDebug(() -> anEnumInList, CONFIG.anEnumInList, $ -> anEnumInList = $, LOGGER);
				bakeAndDebug(() -> aStringInList, CONFIG.aStringInList, $ -> aStringInList = $, LOGGER);
				bakeAndDebug(() -> aLocalTimeInList, CONFIG.aLocalTimeInList, $ -> aLocalTimeInList = $, LOGGER);
				bakeAndDebug(() -> aLocalDateInList, CONFIG.aLocalDateInList, $ -> aLocalDateInList = $, LOGGER);
				bakeAndDebug(() -> aLocalDateTimeInList, CONFIG.aLocalDateTimeInList, $ -> aLocalDateTimeInList = $, LOGGER);
				bakeAndDebug(() -> anOffsetDateTimeInList, CONFIG.anOffsetDateTimeInList, $ -> anOffsetDateTimeInList = $, LOGGER);
				bakeAndDebug(() -> aConfigInList, CONFIG.aConfigInList, $ -> aConfigInList = $, LOGGER);
				bakeAndDebug(() -> aStringListInList, CONFIG.aStringListInList, $ -> aStringListInList = $, LOGGER);

				bakeAndDebug(() -> aBooleanConfig, CONFIG.aBooleanConfig, $ -> aBooleanConfig = $, LOGGER);
				bakeAndDebug(() -> aByteConfig, CONFIG.aByteConfig, $ -> aByteConfig = $, LOGGER);
				bakeAndDebug(() -> aShortConfig, CONFIG.aShortConfig, $ -> aShortConfig = $, LOGGER);
				bakeAndDebug(() -> anIntegerConfig, CONFIG.anIntegerConfig, $ -> anIntegerConfig = $, LOGGER);
				bakeAndDebug(() -> aFloatConfig, CONFIG.aFloatConfig, $ -> aFloatConfig = $, LOGGER);
				bakeAndDebug(() -> aLongConfig, CONFIG.aLongConfig, $ -> aLongConfig = $, LOGGER);
				bakeAndDebug(() -> aDoubleConfig, CONFIG.aDoubleConfig, $ -> aDoubleConfig = $, LOGGER);
				bakeAndDebug(() -> anEnumConfig, CONFIG.anEnumConfig, $ -> anEnumConfig = $, LOGGER);
				bakeAndDebug(() -> aStringConfig, CONFIG.aStringConfig, $ -> aStringConfig = $, LOGGER);
				bakeAndDebug(() -> aLocalTimeConfig, CONFIG.aLocalTimeConfig, $ -> aLocalTimeConfig = $, LOGGER);
				bakeAndDebug(() -> aLocalDateConfig, CONFIG.aLocalDateConfig, $ -> aLocalDateConfig = $, LOGGER);
				bakeAndDebug(() -> aLocalDateTimeConfig, CONFIG.aLocalDateTimeConfig, $ -> aLocalDateTimeConfig = $, LOGGER);
				bakeAndDebug(() -> anOffsetDateTimeConfig, CONFIG.anOffsetDateTimeConfig, $ -> anOffsetDateTimeConfig = $, LOGGER);
				bakeAndDebug(() -> aConfigConfig, CONFIG.aConfigConfig, $ -> aConfigConfig = $, LOGGER);

				bakeAndDebug(() -> aVeryNestedStringList, CONFIG.aVeryNestedStringList, $ -> aVeryNestedStringList = $, LOGGER);
				bakeAndDebug(() -> aVeryNestedStringConfig, CONFIG.aVeryNestedStringConfig, $ -> aVeryNestedStringConfig = $, LOGGER);
				bakeAndDebug(() -> aPathDefinedString, CONFIG.aPathDefinedString, $ -> aPathDefinedString = $, LOGGER);
				bakeAndDebug(() -> aHSBColor, CONFIG.aHSBColor, $ -> aHSBColor = $, LOGGER);
				bakeAndDebug(() -> aHtmlColorAlphabetical, CONFIG.aHtmlColorAlphabetical, $ -> aHtmlColorAlphabetical = $, LOGGER);
				bakeAndDebug(() -> aHtmlColorLightestFirst, CONFIG.aHtmlColorLightestFirst, $ -> aHtmlColorLightestFirst = $, LOGGER);
				bakeAndDebug(() -> aHtmlColorDarkestFirst, CONFIG.aHtmlColorDarkestFirst, $ -> aHtmlColorDarkestFirst = $, LOGGER);
				bakeAndDebug(() -> aRandomColor, CONFIG.aRandomColor, $ -> aRandomColor = $, LOGGER);

				bakeAndDebug(() -> category0_aBoolean, CONFIG.category0_aBoolean, $ -> category0_aBoolean = $, LOGGER);
				bakeAndDebug(() -> category0_anInt, CONFIG.category0_anInt, $ -> category0_anInt = $, LOGGER);

				bakeAndDebug(() -> category0_category1_aBoolean, CONFIG.category0_category1_aBoolean, $ -> category0_category1_aBoolean = $, LOGGER);
				bakeAndDebug(() -> category0_category1_anInt, CONFIG.category0_category1_anInt, $ -> category0_category1_anInt = $, LOGGER);
			}

			private static class ConfigImpl {

				private final BooleanValue aBoolean;
				private final ByteValue aByte;
				private final ShortValue aShort;
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
				private final ConfigValue<List<Short>> aShortList;
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
				private final ConfigValue<List<List<Short>>> aShortListList;
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
				// TODO: Bug in NightConfig - The writer writes this in a way that can't be parsed. See "https://github.com/TheElectronWill/night-config/pull/75"
//				private final ConfigValue<List<List<Config>>> aConfigListList;
				private final ConfigValue<List<List<String>>> aStringWeirdListWeirdList;
				private final ConfigValue<List<? extends List<String>>> aStringListOldList;

				private final ConfigValue<Boolean> aBooleanInList;
				private final ConfigValue<Byte> aByteInList;
				private final ConfigValue<Short> aShortInList;
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
				private final ConfigValue<Config> aShortConfig;
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
				private final EnumValue<HSBColor> aHSBColor;
				private final EnumValue<HtmlColorAlphabetical> aHtmlColorAlphabetical;
				private final EnumValue<HtmlColorLightestFirst> aHtmlColorLightestFirst;
				private final EnumValue<HtmlColorDarkestFirst> aHtmlColorDarkestFirst;
				private final EnumValue<RandomColor> aRandomColor;

				private final BooleanValue category0_aBoolean;
				private final IntValue category0_anInt;

				private final BooleanValue category0_category1_aBoolean;
				private final IntValue category0_category1_anInt;

				ConfigImpl(ForgeConfigSpec.Builder builder) {

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

					aShort = builder
							.comment("a Short")
							.translation("aShort")
							.worldRestart()
							.defineInRange("aShort", (short) 10, Short.MIN_VALUE, Short.MAX_VALUE);

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
							.define("anOffsetDateTime", OffsetDateTime.of(2019, 12, 26, 21, 58, 10, 368, ZoneOffset.of("+10:00")));

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
								.define("aBooleanList", newList(true, false));

						aByteList = builder
								.comment("a ByteList")
								.translation("aByteList")
								.define("aByteList", newList((byte) 0, Byte.MIN_VALUE, Byte.MAX_VALUE, (byte) 256));

						aShortList = builder
								.comment("a ShortList")
								.translation("aShortList")
								.define("aShortList", newList((short) 0, Short.MIN_VALUE, Short.MAX_VALUE, (short) 256));

						anIntegerList = builder
								.comment("an IntegerList")
								.translation("anIntegerList")
								.define("anIntegerList", newList(0, Integer.MIN_VALUE, Integer.MAX_VALUE, 256));

						aFloatList = builder
								.comment("a FloatList")
								.translation("aFloatList")
								.define("aFloatList", newList(0F, Float.MIN_VALUE, Float.MAX_VALUE, 256F));

						aLongList = builder
								.comment("a LongList")
								.translation("aLongList")
								.define("aLongList", newList(0L, Long.MIN_VALUE, Long.MAX_VALUE, 256L));

						aDoubleList = builder
								.comment("a DoubleList")
								.translation("aDoubleList")
								.define("aDoubleList", newList(0d, Double.MIN_VALUE, Double.MAX_VALUE, 256d));

						anEnumList = builder
								.comment("an EnumList")
								.translation("anEnumList")
								.define("anEnumList", newList(DyeColor.BLACK, DyeColor.GREEN, DyeColor.CYAN, DyeColor.RED));

						aStringList = builder
								.comment("a StringList")
								.translation("aStringList")
								.define("aStringList", newList("aStringList_value0", "aStringList_value1"));

						aLocalTimeList = builder
								.comment("a LocalTimeList")
								.translation("aLocalTimeList")
								.define("aLocalTimeList", newList(LocalTime.of(0, 0, 0), LocalTime.of(23, 59, 59)));

						aLocalDateList = builder
								.comment("a LocalDateList")
								.translation("aLocalDateList")
								.define("aLocalDateList", newList(LocalDate.of(1999, 1, 1), LocalDate.of(2000, 1, 1)));

						aLocalDateTimeList = builder
								.comment("a LocalDateTimeList")
								.translation("aLocalDateTimeList")
								.define("aLocalDateTimeList", newList(LocalDateTime.of(1999, 1, 1, 10, 0), LocalDateTime.of(2000, 1, 1, 23, 59)));

						anOffsetDateTimeList = builder
								.comment("an OffsetDateTimeList")
								.translation("anOffsetDateTimeList")
								.define("anOffsetDateTimeList", newList(OffsetDateTime.of(2019, 12, 26, 21, 58, 10, 368, ZoneOffset.of("+11:00")), OffsetDateTime.of(2019, 12, 26, 21, 58, 10, 368, ZoneOffset.UTC)));

						aConfigList = builder
								.comment("a ConfigList")
								.translation("aConfigList")
								.define("aConfigList", newList(newConfig("foo"), newConfig("bar"), newConfig("baz"), newConfig("fuz")));

						aStringWeirdList = builder
								.comment("a StringWeirdList")
								.translation("aStringWeirdList")
								.define("aStringWeirdList", Arrays.asList("aStringWeirdList_value0", "aStringWeirdList_value1"));

						aStringListOld = builder
								.comment("a StringListOld")
								.translation("aStringListOld")
								.defineList("aStringListOld", newList("aStringListOld_value0", "aStringListOld_value1"), o -> o instanceof String);
					}
					builder.pop();

					builder.comment("Nested List tests")
							.push("nestedlists");
					{
						aBooleanListList = builder
								.comment("a BooleanListList")
								.translation("aBooleanListList")
								.define("aBooleanListList", newList(newList(true, false), newList(true, false), newList(true, false)));

						aByteListList = builder
								.comment("a ByteListList")
								.translation("aByteListList")
								.define("aByteListList", newList(newList((byte) 0, Byte.MIN_VALUE, Byte.MAX_VALUE, (byte) 256), newList((byte) 0, Byte.MIN_VALUE, Byte.MAX_VALUE, (byte) 256), newList((byte) 0, Byte.MIN_VALUE, Byte.MAX_VALUE, (byte) 256)));

						aShortListList = builder
								.comment("a ShortListList")
								.translation("aShortListList")
								.define("aShortListList", newList(newList((short) 0, Short.MIN_VALUE, Short.MAX_VALUE, (short) 256), newList((short) 0, Short.MIN_VALUE, Short.MAX_VALUE, (short) 256), newList((short) 0, Short.MIN_VALUE, Short.MAX_VALUE, (short) 256)));

						anIntegerListList = builder
								.comment("an IntegerListList")
								.translation("anIntegerListList")
								.define("anIntegerListList", newList(newList(0, Integer.MIN_VALUE, Integer.MAX_VALUE, 256), newList(0, Integer.MIN_VALUE, Integer.MAX_VALUE, 256), newList(0, Integer.MIN_VALUE, Integer.MAX_VALUE, 256)));

						aFloatListList = builder
								.comment("a FloatListList")
								.translation("aFloatListList")
								.define("aFloatListList", newList(newList(0F, Float.MIN_VALUE, Float.MAX_VALUE, 256F), newList(0F, Float.MIN_VALUE, Float.MAX_VALUE, 256F), newList(0F, Float.MIN_VALUE, Float.MAX_VALUE, 256F)));

						aLongListList = builder
								.comment("a LongListList")
								.translation("aLongListList")
								.define("aLongListList", newList(newList(0L, Long.MIN_VALUE, Long.MAX_VALUE, 256L), newList(0L, Long.MIN_VALUE, Long.MAX_VALUE, 256L), newList(0L, Long.MIN_VALUE, Long.MAX_VALUE, 256L)));

						aDoubleListList = builder
								.comment("a DoubleListList")
								.translation("aDoubleListList")
								.define("aDoubleListList", newList(newList(0d, Double.MIN_VALUE, Double.MAX_VALUE, 256d), newList(0d, Double.MIN_VALUE, Double.MAX_VALUE, 256d), newList(0d, Double.MIN_VALUE, Double.MAX_VALUE, 256d)));

						anEnumListList = builder
								.comment("an EnumListList")
								.translation("anEnumListList")
								.define("anEnumListList", newList(newList(DyeColor.BLACK, DyeColor.GREEN, DyeColor.CYAN, DyeColor.RED), newList(DyeColor.BLACK, DyeColor.GREEN, DyeColor.CYAN, DyeColor.RED), newList(DyeColor.BLACK, DyeColor.GREEN, DyeColor.CYAN, DyeColor.RED)));

						aStringListList = builder
								.comment("a StringListListList")
								.translation("aStringListListList")
								.define("aStringListListList", newList(newList("Hello", "World!"), newList("World", "Hello")));

						aLocalTimeListList = builder
								.comment("a LocalTimeListList")
								.translation("aLocalTimeListList")
								.define("aLocalTimeListList", newList(newList(LocalTime.of(0, 0, 0), LocalTime.of(23, 59, 59)), newList(LocalTime.of(0, 0, 0), LocalTime.of(23, 59, 59)), newList(LocalTime.of(0, 0, 0), LocalTime.of(23, 59, 59))));

						aLocalDateListList = builder
								.comment("a LocalDateListList")
								.translation("aLocalDateListList")
								.define("aLocalDateListList", newList(newList(LocalDate.of(1999, 1, 1), LocalDate.of(2000, 1, 1)), newList(LocalDate.of(1999, 1, 1), LocalDate.of(2000, 1, 1)), newList(LocalDate.of(1999, 1, 1), LocalDate.of(2000, 1, 1))));

						aLocalDateTimeListList = builder
								.comment("a LocalDateTimeListList")
								.translation("aLocalDateTimeListList")
								.define("aLocalDateTimeListList", newList(newList(LocalDateTime.of(1999, 1, 1, 10, 0), LocalDateTime.of(2000, 1, 1, 23, 59)), newList(LocalDateTime.of(1999, 1, 1, 10, 0), LocalDateTime.of(2000, 1, 1, 23, 59)), newList(LocalDateTime.of(1999, 1, 1, 10, 0), LocalDateTime.of(2000, 1, 1, 23, 59))));

						anOffsetDateTimeListList = builder
								.comment("an OffsetDateTimeListList")
								.translation("anOffsetDateTimeListList")
								.define("anOffsetDateTimeListList", newList(newList(OffsetDateTime.of(2019, 12, 26, 21, 58, 10, 368, ZoneOffset.of("+11:00")), OffsetDateTime.of(2019, 12, 26, 21, 58, 10, 368, ZoneOffset.UTC)), newList(OffsetDateTime.of(2019, 12, 26, 21, 58, 10, 368, ZoneOffset.of("+15:00")), OffsetDateTime.of(2019, 12, 26, 21, 58, 10, 1368, ZoneOffset.UTC)), newList(OffsetDateTime.of(2019, 12, 26, 21, 58, 10, 368123, ZoneOffset.of("+01:00")), OffsetDateTime.of(2019, 12, 26, 21, 58, 10, 368, ZoneOffset.UTC))));

						// TODO: Bug in NightConfig - The writer writes this in a way that can't be parsed. See "https://github.com/TheElectronWill/night-config/pull/75"
//						aConfigListList = builder
//								.comment("a ConfigListList")
//								.translation("aConfigListList")
//								.define("aConfigListList", newList(newList(newConfig("foo"), newConfig("bar"), newConfig("baz"), newConfig("lumen")), newList(newConfig("F"), newConfig("O"), newConfig("O"), newConfig("B")), newList(newConfig("A"), newConfig("R"), newConfig("Baz"), newConfig("Im not very inventive in my testing stuff naming sorry"))));

						aStringWeirdListWeirdList = builder
								.comment("a StringWeirdListWeirdList")
								.translation("aStringWeirdListWeirdList")
								.define("aStringWeirdListWeirdList", Arrays.asList(Arrays.asList("aStringWeirdListWeirdList_value0", "aStringWeirdListWeirdList_value1"), Arrays.asList("aStringWeirdListWeirdList_value0", "aStringWeirdListWeirdList_value1"), Arrays.asList("aStringWeirdListWeirdList_value0", "aStringWeirdListWeirdList_value1")));

						aStringListOldList = builder
								.comment("a StringListOldList")
								.translation("aStringListOldList")
								.defineList("aStringListOldList", newList(newList("aStringListOld_value0", "aStringListOld_value1"), newList("aStringListOld_value0", "aStringListOld_value1"), newList("aStringListOld_value0", "aStringListOld_value1")), o -> o instanceof List);
					}
					builder.pop();

					builder.comment("In List tests")
							.push("inLists");
					{
						aBooleanInList = builder
								.comment("a BooleanInList")
								.translation("aBooleanInList")
								.defineInList("aBooleanInList", true, newList(true, false));

						aByteInList = builder
								.comment("a ByteInList")
								.translation("aByteInList")
								.defineInList("aByteInList", (byte) 256, newList((byte) 0, Byte.MIN_VALUE, Byte.MAX_VALUE, (byte) 256));

						aShortInList = builder
								.comment("a ShortInList")
								.translation("aShortInList")
								.defineInList("aShortInList", (short) 256, newList((short) 0, Short.MIN_VALUE, Short.MAX_VALUE, (short) 256));

						anIntegerInList = builder
								.comment("an IntegerInList")
								.translation("anIntegerInList")
								.defineInList("anIntegerInList", 256, newList(0, Integer.MIN_VALUE, Integer.MAX_VALUE, 256));

						aFloatInList = builder
								.comment("a FloatInList")
								.translation("aFloatInList")
								.defineInList("aFloatInList", 256F, newList(0F, Float.MIN_VALUE, Float.MAX_VALUE, 256F));

						aLongInList = builder
								.comment("a LongInList")
								.translation("aLongInList")
								.defineInList("aLongInList", 256L, newList(0L, Long.MIN_VALUE, Long.MAX_VALUE, 256L));

						aDoubleInList = builder
								.comment("a DoubleInList")
								.translation("aDoubleInList")
								.defineInList("aDoubleInList", 256d, newList(0d, Double.MIN_VALUE, Double.MAX_VALUE, 256d));

						anEnumInList = builder
								.comment("an EnumInList")
								.translation("anEnumInList")
								.defineInList("anEnumInList", DyeColor.ORANGE, newList(DyeColor.ORANGE, DyeColor.BLACK, DyeColor.GREEN, DyeColor.CYAN, DyeColor.RED));

						aStringInList = builder
								.comment("a StringInList")
								.translation("aStringInList")
								.defineInList("aStringInList", "aStringListInList_value0", newList("aStringListInList_value0", "aStringListInList_value1"));

						aLocalTimeInList = builder
								.comment("a LocalTimeInList")
								.translation("aLocalTimeInList")
								.defineInList("aLocalTimeInList", LocalTime.of(0, 0, 0), newList(LocalTime.of(0, 0, 0), LocalTime.of(23, 59, 59)));

						aLocalDateInList = builder
								.comment("a LocalDateInList")
								.translation("aLocalDateInList")
								.defineInList("aLocalDateInList", LocalDate.of(1999, 1, 1), newList(LocalDate.of(1999, 1, 1), LocalDate.of(2000, 1, 1)));

						aLocalDateTimeInList = builder
								.comment("a LocalDateTimeInList")
								.translation("aLocalDateTimeInList")
								.defineInList("aLocalDateTimeInList", LocalDateTime.of(1999, 1, 1, 10, 0), newList(LocalDateTime.of(1999, 1, 1, 10, 0), LocalDateTime.of(2000, 1, 1, 23, 59)));

						anOffsetDateTimeInList = builder
								.comment("an OffsetDateTimeInList")
								.translation("anOffsetDateTimeInList")
								.defineInList("anOffsetDateTimeInList", OffsetDateTime.of(2019, 12, 26, 21, 58, 10, 368, ZoneOffset.of("+11:00")), newList(OffsetDateTime.of(2019, 12, 26, 21, 58, 10, 368, ZoneOffset.of("+11:00")), OffsetDateTime.of(2019, 12, 26, 21, 58, 10, 368, ZoneOffset.of("+15:00")), OffsetDateTime.of(2019, 12, 26, 21, 58, 10, 368, ZoneOffset.UTC)));

						final CommentedConfig config = newConfig("defaultValue yay :D");
						aConfigInList = builder
								.comment("a ConfigInList")
								.translation("aConfigInList")
								.defineInList("aConfigInList", config, newList(config, newConfig(1111, 2222, 3333, 4444), newConfig("How is this going to work???"), newConfig("erm.....")));

						aStringListInList = builder
								.comment("a StringListInList")
								.translation("aStringListInList")
								.defineInList("aStringListInList", newList("Hello"), newList(newList("Hello"), newList("World")));
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

						aShortConfig = builder
								.comment("a ShortConfig")
								.translation("aShortConfig")
								.define("aShortConfig", newConfig((short) 0, Short.MIN_VALUE, Short.MAX_VALUE, (short) 256));

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
								.define("anOffsetDateTimeConfig", newConfig(OffsetDateTime.of(2019, 12, 26, 21, 58, 10, 368, ZoneOffset.of("+11:00")), OffsetDateTime.of(2019, 12, 26, 21, 58, 10, 368, ZoneOffset.UTC)));

						aConfigConfig = builder
								.comment("a ConfigConfig")
								.translation("aConfigConfig")
								.define("aConfigConfig", newConfig(newConfig(true, false), newConfig("hello", "world"), newConfig(newList("Hello!!!", "World___"), newList("Hello______!!!", "World__!!_!!")), newConfig("foo")));
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

					aHSBColor = builder
							.comment("a HSBColor")
							.translation("aHSBColor")
							.defineEnum("aHSBColor", HSBColor.H_0);

					aHtmlColorAlphabetical = builder
							.comment("a HtmlColorAlphabetical")
							.translation("aHtmlColorAlphabetical")
							.defineEnum("aHtmlColorAlphabetical", HtmlColorAlphabetical.ALICE_BLUE);

					aHtmlColorLightestFirst = builder
							.comment("a HtmlColorLightestFirst")
							.translation("aHtmlColorLightestFirst")
							.defineEnum("aHtmlColorLightestFirst", HtmlColorLightestFirst.WHITE);

					aHtmlColorDarkestFirst = builder
							.comment("a HtmlColorDarkestFirst")
							.translation("aHtmlColorDarkestFirst")
							.defineEnum("aHtmlColorDarkestFirst", HtmlColorDarkestFirst.BLACK);

					aRandomColor = builder
							.comment("a RandomColor")
							.translation("aRandomColor")
							.defineEnum("aRandomColor", RandomColor.FIRST);

					// Category tests

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

			}

		}

		public static class Client {

			public static final ConfigImpl CONFIG;
			public static final ForgeConfigSpec SPEC;
			private static final Logger LOGGER = LogManager.getLogger();

			// TODO: Bug in NightConfig - The writer writes these in a way that can't be parsed. See "https://github.com/TheElectronWill/night-config/pull/75"
//			private static List<Config> aConfigList;
//			private static List<List<Config>> aConfigListList;
//			private static List<List<List<Config>>> aConfigListListList;

			static {
				final Pair<ConfigImpl, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(ConfigImpl::new);
				SPEC = specPair.getRight();
				CONFIG = specPair.getLeft();
			}

			private static void bakeAndDebugConfig() {
				// TODO: Bug in NightConfig - The writer writes these in a way that can't be parsed. See "https://github.com/TheElectronWill/night-config/pull/75"
//				bakeAndDebug(() -> aConfigList, CONFIG.aConfigList, $ -> aConfigList = $, LOGGER);
//				bakeAndDebug(() -> aConfigListList, CONFIG.aConfigListList, $ -> aConfigListList = $, LOGGER);
//				bakeAndDebug(() -> aConfigListListList, CONFIG.aConfigListListList, $ -> aConfigListListList = $, LOGGER);
			}

			private static class ConfigImpl {

				// TODO: Bug in NightConfig - The writer writes these in a way that can't be parsed. See "https://github.com/TheElectronWill/night-config/pull/75"
//				private final ConfigValue<List<Config>> aConfigList;
//				private final ConfigValue<List<List<Config>>> aConfigListList;
//				private final ConfigValue<List<List<List<Config>>>> aConfigListListList;

				ConfigImpl(ForgeConfigSpec.Builder builder) {
					// TODO: Bug in NightConfig - The writer writes these in a way that can't be parsed. See "https://github.com/TheElectronWill/night-config/pull/75"
//					aConfigList = builder
//							.comment("a List<Config>. 2 branches each.")
//							.translation("aConfigList")
//							.define("aConfigList", newList(
//									newConfig("list->config0->0", "list->config0->1"),
//									newConfig("list->config1->0", "list->config1->1")
//							));
//
//					aConfigListList = builder
//							.comment("a List<List<Config>>. 2 branches each.")
//							.translation("aConfigListList")
//							.define("aConfigListList", newList(
//									newList(
//											newConfig("list->list0->config0->0", "list->list0->config0->1"),
//											newConfig("list->list0->config1->0", "list->list0->config1->1")
//									),
//									newList(
//											newConfig("list->list1->config0->0", "list->list1->config0->1"),
//											newConfig("list->list1->config1->0", "list->list1->config1->1")
//									)
//							));
//
//					aConfigListListList = builder
//							.comment("a List<List<List<Config>>>. 2 branches each.")
//							.translation("aConfigListListList")
//							.define("aConfigListListList", newList(
//									newList(
//											newList(
//													newConfig("list->list0->list0->config0->0", "list->list0->config0->1"),
//													newConfig("list->list0->list0->config1->0", "list->list0->config1->1")
//											),
//											newList(
//													newConfig("list->list0->list1->config0->0", "list->list1->config0->1"),
//													newConfig("list->list0->list1->config1->0", "list->list1->config1->1")
//											)
//									),
//									newList(
//											newList(
//													newConfig("list->list1->list0->config0->0", "list->list0->config0->1"),
//													newConfig("list->list1->list0->config1->0", "list->list0->config1->1")
//											),
//											newList(
//													newConfig("list->list1->list1->config0->0", "list->list1->config0->1"),
//													newConfig("list->list1->list1->config1->0", "list->list1->config1->1")
//											)
//									)
//							));
				}

			}

		}

		public static class Player {

			public static final ConfigImpl CONFIG;
			public static final ForgeConfigSpec SPEC;
			private static final Logger LOGGER = LogManager.getLogger();

			static {
				final Pair<ConfigImpl, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(ConfigImpl::new);
				SPEC = specPair.getRight();
				CONFIG = specPair.getLeft();
			}

			private static void bakeAndDebugConfig() {
				LOGGER.debug(CONFIG_TEST, "Player config baked! What?");
			}

			private static class ConfigImpl {

				ConfigImpl(ForgeConfigSpec.Builder builder) {
					builder.comment("Player configuration settings. Doesn't exist (yet)!")
							.push("Player");

					builder.pop();
				}

			}

		}

		public static class Server {

			public static final ConfigImpl CONFIG;
			public static final ForgeConfigSpec SPEC;
			private static final Logger LOGGER = LogManager.getLogger();

//			private static List<Config> aConfigList;
//			private static List<List<Config>> aConfigListList;
//			private static List<List<List<Config>>> aConfigListListList;

			static {
				final Pair<ConfigImpl, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(ConfigImpl::new);
				SPEC = specPair.getRight();
				CONFIG = specPair.getLeft();
			}

			private static void bakeAndDebugConfig() {
				// TODO: Bug in NightConfig - The writer writes these in a way that can't be parsed. See "https://github.com/TheElectronWill/night-config/pull/75"
//				bakeAndDebug(() -> aConfigList, CONFIG.aConfigList, $ -> aConfigList = $, LOGGER);
//				bakeAndDebug(() -> aConfigListList, CONFIG.aConfigListList, $ -> aConfigListList = $, LOGGER);
//				bakeAndDebug(() -> aConfigListListList, CONFIG.aConfigListListList, $ -> aConfigListListList = $, LOGGER);
			}

			private static class ConfigImpl {

				// TODO: Bug in NightConfig - The writer writes these in a way that can't be parsed. See "https://github.com/TheElectronWill/night-config/pull/75"
//				private final ConfigValue<List<Config>> aConfigList;
//				private final ConfigValue<List<List<Config>>> aConfigListList;
//				private final ConfigValue<List<List<List<Config>>>> aConfigListListList;

				ConfigImpl(ForgeConfigSpec.Builder builder) {
					// TODO: Bug in NightConfig - The writer writes these in a way that can't be parsed. See "https://github.com/TheElectronWill/night-config/pull/75"
//					aConfigList = builder
//							.comment("a List<Config>. 3 branches each.")
//							.translation("aConfigList")
//							.define("aConfigList", newList(
//									newConfig("list->config0->0", "list->config0->1", "list->config0->2"),
//									newConfig("list->config1->0", "list->config1->1", "list->config1->2"),
//									newConfig("list->config2->0", "list->config2->1", "list->config2->2")
//							));
//
//					aConfigListList = builder
//							.comment("a List<List<Config>>. 3 branches each.")
//							.translation("aConfigListList")
//							.define("aConfigListList", newList(
//									newList(
//											newConfig("list->list0->config0->0", "list->list0->config0->1", "list->list0->config0->2"),
//											newConfig("list->list0->config1->0", "list->list0->config1->1", "list->list0->config1->2"),
//											newConfig("list->list0->config2->0", "list->list0->config2->1", "list->list0->config2->2")
//									),
//									newList(
//											newConfig("list->list1->config0->0", "list->list1->config0->1", "list->list1->config0->2"),
//											newConfig("list->list1->config1->0", "list->list1->config1->1", "list->list1->config1->2"),
//											newConfig("list->list1->config2->0", "list->list1->config2->1", "list->list1->config2->2")
//									),
//									newList(
//											newConfig("list->list2->config0->0", "list->list2->config0->1", "list->list2->config0->2"),
//											newConfig("list->list2->config1->0", "list->list2->config1->1", "list->list2->config1->2"),
//											newConfig("list->list2->config2->0", "list->list2->config2->1", "list->list2->config2->2")
//									)
//							));
//
//					aConfigListListList = builder
//							.comment("a List<List<List<Config>>>. 3 branches each.")
//							.translation("aConfigListListList")
//							.define("aConfigListListList", newList(
//									newList(
//											newList(
//													newConfig("list->list0->list0->config0->0", "list->list0->config0->1", "list->list0->config0->2"),
//													newConfig("list->list0->list0->config1->0", "list->list0->config1->1", "list->list0->config1->2"),
//													newConfig("list->list0->list0->config2->0", "list->list0->config2->1", "list->list0->config2->2")
//											),
//											newList(
//													newConfig("list->list0->list1->config0->0", "list->list1->config0->1", "list->list1->config0->2"),
//													newConfig("list->list0->list1->config1->0", "list->list1->config1->1", "list->list1->config1->2"),
//													newConfig("list->list0->list1->config2->0", "list->list1->config2->1", "list->list1->config2->2")
//											),
//											newList(
//													newConfig("list->list0->list2->config0->0", "list->list2->config0->1", "list->list2->config0->2"),
//													newConfig("list->list0->list2->config1->0", "list->list2->config1->1", "list->list2->config1->2"),
//													newConfig("list->list0->list2->config2->0", "list->list2->config2->1", "list->list2->config2->2")
//											)
//									),
//									newList(
//											newList(
//													newConfig("list->list1->list0->config0->0", "list->list0->config0->1", "list->list0->config0->2"),
//													newConfig("list->list1->list0->config1->0", "list->list0->config1->1", "list->list0->config1->2"),
//													newConfig("list->list1->list0->config2->0", "list->list0->config2->1", "list->list0->config2->2")
//											),
//											newList(
//													newConfig("list->list1->list1->config0->0", "list->list1->config0->1", "list->list1->config0->2"),
//													newConfig("list->list1->list1->config1->0", "list->list1->config1->1", "list->list1->config1->2"),
//													newConfig("list->list1->list1->config2->0", "list->list1->config2->1", "list->list1->config2->2")
//											),
//											newList(
//													newConfig("list->list1->list2->config0->0", "list->list2->config0->1", "list->list2->config0->2"),
//													newConfig("list->list1->list2->config1->0", "list->list2->config1->1", "list->list2->config1->2"),
//													newConfig("list->list1->list2->config2->0", "list->list2->config2->1", "list->list2->config2->2")
//											)
//									),
//									newList(
//											newList(
//													newConfig("list->list2->list0->config0->0", "list->list0->config0->1", "list->list0->config0->2"),
//													newConfig("list->list2->list0->config1->0", "list->list0->config1->1", "list->list0->config1->2"),
//													newConfig("list->list2->list0->config2->0", "list->list0->config2->1", "list->list0->config2->2")
//											),
//											newList(
//													newConfig("list->list2->list1->config0->0", "list->list1->config0->1", "list->list1->config0->2"),
//													newConfig("list->list2->list1->config1->0", "list->list1->config1->1", "list->list1->config1->2"),
//													newConfig("list->list2->list1->config2->0", "list->list1->config2->1", "list->list1->config2->2")
//											),
//											newList(
//													newConfig("list->list2->list2->config0->0", "list->list2->config0->1", "list->list2->config0->2"),
//													newConfig("list->list2->list2->config1->0", "list->list2->config1->1", "list->list2->config1->2"),
//													newConfig("list->list2->list2->config2->0", "list->list2->config2->1", "list->list2->config2->2")
//											)
//									)
//							));
				}

			}

		}

	}

}
