package net.minecraftforge.fml.client.config.annotation;

import net.minecraftforge.fml.config.ModConfig;

/**
 * @author Cadiboo
 */
public @interface Config {

	String modid();

	ModConfig.Type type();

	@interface Comment {

		String value();

	}

	@interface Range {

		double min();

		double max();

	}

	@interface Step {

		double value();

	}

	@interface GameRestart {

	}

	@interface WorldRestart {

	}

}
