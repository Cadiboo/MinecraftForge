package net.minecraftforge.fml.client.config.annotation;

import com.google.common.collect.Lists;
import net.minecraft.item.DyeColor;
import net.minecraftforge.fml.config.ModConfig;

import java.util.List;

/**
 * @author Cadiboo
 */
@Config(modid = "forge", type = ModConfig.Type.CLIENT)
public class TestAnnotationConfig {

	@Config.Comment("Sync Range")
	@Config.Range(min = 0, max = 128)
	@Config.Step(16)
	public static int syncRange = 64;

	public static DyeColor backgroundColor = DyeColor.GREEN;

	// Not included in config
	public static transient DyeColor transientBackgroundColor = DyeColor.CYAN;

	@Config.GameRestart
	public static int configValueGame = 100;

	@Config.WorldRestart
	public static int configValueWorld = 1000;

	@Config.Comment("Colors")
	public static List<DyeColor> colors = makeColors();

	private static List<DyeColor> makeColors() {
		return Lists.newArrayList(DyeColor.BROWN, DyeColor.GREEN, DyeColor.BLUE);
	}

	public static void main(String[] args) {
		System.out.println(syncRange);
		System.out.println(backgroundColor);
		System.out.println(transientBackgroundColor);
		System.out.println(configValueGame);
		System.out.println(configValueWorld);
		System.out.println(colors);
	}

}
