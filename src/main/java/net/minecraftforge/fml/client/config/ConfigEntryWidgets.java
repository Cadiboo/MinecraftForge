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

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.DyeColor;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.config.ModConfig;
import org.apache.commons.lang3.tuple.ImmutableTriple;
import org.apache.commons.lang3.tuple.Triple;
import org.apache.logging.log4j.LogManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * @author Cadiboo
 */
public class ConfigEntryWidgets {

	private static final Map<Class<?>, List<IWidgetMaker>> REGISTRY = new HashMap<>();
	static {
		init();
	}

	private static void init() {
		REGISTRY.clear();
		register(Boolean.class, ConfigEntryWidgets::makeBoolean);
		register(Integer.class, ConfigEntryWidgets::makeInteger);
		register(Float.class, ConfigEntryWidgets::makeFloat);
		register(Long.class, ConfigEntryWidgets::makeLong);
		register(Double.class, ConfigEntryWidgets::makeDouble);
		register(Enum.class, ConfigEntryWidgets::makeEnum);
		register(String.class, ConfigEntryWidgets::makeString);
		register(Object.class, ConfigEntryWidgets::makeList);
	}

	public static void register(final Class<?> clazz, final IWidgetMaker widgetMaker) {
		REGISTRY.computeIfAbsent(clazz, $ -> new ArrayList<>()).add(widgetMaker);
	}

	public static Triple<Widget, ConfigEntry<?>, Consumer<?>> makeWidgetForValue(final ModConfig modConfig, final ForgeConfigSpec.ValueSpec valueSpec, final String path) {
		final Class<?> clazz = valueSpec.getClazz();
		Class<?> testClass = clazz;
		while (testClass != null) {
			List<IWidgetMaker> widgetMakers = REGISTRY.get(testClass);
			if (widgetMakers != null) {
				if (testClass != clazz)
					REGISTRY.put(clazz, widgetMakers);
				for (final IWidgetMaker widgetMaker : widgetMakers) {
					final Triple<Widget, ConfigEntry<?>, Consumer<?>> ret = widgetMaker.make(new IWidgetMaker.Context(modConfig, valueSpec, path));
					if (ret != null)
						return ret;
				}
			}
			testClass = testClass.getSuperclass();
		}
		return null;
	}

	private static Triple<Widget, ConfigEntry<?>, Consumer<?>> makeBoolean(final IWidgetMaker.Context context) {
		final ConfigEntry<Boolean> configEntry = ConfigEntry.of(Boolean.class, context.getModConfig(), context.getPath(), context.getValueSpec());
		final Function<Boolean, String> textGetter = bool -> Boolean.toString(bool);

		final BiConsumer<Boolean, Widget> textSetter = (bool, widget) -> {
			widget.setMessage(textGetter.apply(bool));
			widget.setFGColor(getColor(bool));
		};
		final GuiButtonExt widget = new GuiButtonExt(0, 0, 100, 20, textGetter.apply(configEntry.getCurrentValue()), b -> {
			configEntry.setCurrentValue(!configEntry.getCurrentValue());
			configEntry.saveAndLoad();
			textSetter.accept(configEntry.getCurrentValue(), b);
		});
		widget.setFGColor(getColor(configEntry.getCurrentValue()));
		final Consumer<Boolean> outTextSetter = bool -> textSetter.accept(bool, widget);
		return new ImmutableTriple<>(widget, configEntry, outTextSetter);
	}

	private static Triple<Widget, ConfigEntry<?>, Consumer<?>> makeInteger(final IWidgetMaker.Context context) {
		final ForgeConfigSpec.ValueSpec valueSpec = context.getValueSpec();
		final ConfigEntry<Integer> configEntry = ConfigEntry.of(Integer.class, context.getModConfig(), context.getPath(), valueSpec);
		final ConfigEntryList.ConfigTextFieldWidget<Integer> widget = new ConfigEntryList.ConfigTextFieldWidget<>(valueSpec, configEntry, Integer::valueOf, s -> Integer.toString(s));
		final Consumer<Integer> textSetter = widget::convertAndSet;
		return new ImmutableTriple<>(widget, configEntry, textSetter);
	}

	private static Triple<Widget, ConfigEntry<?>, Consumer<?>> makeEnum(final IWidgetMaker.Context context) {
		// Enums are stored internally as strings... yay.
		final ConfigEntry<Enum> configEntry = ConfigEntry.ofEnum(context.getModConfig(), context.getPath(), context.getValueSpec());
		final Class<? extends Enum> enumClass = (Class<? extends Enum>) context.getValueSpec().getClazz();
		final Enum[] enumConstants = enumClass.getEnumConstants();
		final Function<Enum, String> textGetter = e -> {
			if (e instanceof IStringSerializable)
				return I18n.format(((IStringSerializable) e).getName());
			else
				return e.name();
		};
		final BiConsumer<Widget, Enum> setter = (w, val) -> {
			w.setMessage(textGetter.apply(val));
			w.setFGColor(getColor(val));
		};
		final GuiButtonExt widget = new GuiButtonExt(0, 0, 100, 20, textGetter.apply(configEntry.getCurrentValue()), b -> {
			final Enum nextEnum = enumConstants[(configEntry.getCurrentValue().ordinal() + 1) % enumConstants.length];
			configEntry.setCurrentValue(nextEnum);
			configEntry.saveAndLoad();
			setter.accept(b, configEntry.getCurrentValue());
		});
		widget.setFGColor(getColor(configEntry.getCurrentValue()));
		final Consumer<Enum> textSetter = s -> setter.accept(widget, s);
		return new ImmutableTriple<>(widget, configEntry, textSetter);
	}

	// TODO extend and allow modders to use
	private static int getColor(final Object val) {
		if (val instanceof Boolean)
			return (Boolean) val ? 0x55FF55 : 0xFF5555; // green or red
		else if (val instanceof DyeColor)
			return ((DyeColor) val).getColorValue();
		else if (val instanceof TextFormatting) {
			final Integer color = ((TextFormatting) val).getColor();
			if (color != null)
				return color;
		}
		return 0xFFFFFF; // white
	}

	private static Triple<Widget, ConfigEntry<?>, Consumer<?>> makeString(final IWidgetMaker.Context context) {
		final ForgeConfigSpec.ValueSpec valueSpec = context.getValueSpec();
		final ConfigEntry<String> configEntry = ConfigEntry.of(String.class, context.getModConfig(), context.getPath(), valueSpec);
		final ConfigEntryList.ConfigTextFieldWidget<String> widget = new ConfigEntryList.ConfigTextFieldWidget<>(valueSpec, configEntry, s -> s, s -> s);
		final Consumer<String> textSetter = widget::convertAndSet;
		return new ImmutableTriple<>(widget, configEntry, textSetter);
	}

	private static Triple<Widget, ConfigEntry<?>, Consumer<?>> makeDouble(final IWidgetMaker.Context context) {
		final ForgeConfigSpec.ValueSpec valueSpec = context.getValueSpec();
		final ConfigEntry<Double> configEntry = ConfigEntry.of(Double.class, context.getModConfig(), context.getPath(), valueSpec);
		final ConfigEntryList.ConfigTextFieldWidget<Double> widget = new ConfigEntryList.ConfigTextFieldWidget<>(valueSpec, configEntry, Double::valueOf, s -> Double.toString(s));
		final Consumer<Double> textSetter = widget::convertAndSet;
		return new ImmutableTriple<>(widget, configEntry, textSetter);
	}

	private static Triple<Widget, ConfigEntry<?>, Consumer<?>> makeLong(final IWidgetMaker.Context context) {
		final ForgeConfigSpec.ValueSpec valueSpec = context.getValueSpec();
		final ConfigEntry<Long> configEntry = ConfigEntry.of(Long.class, context.getModConfig(), context.getPath(), valueSpec);
		final ConfigEntryList.ConfigTextFieldWidget<Long> widget = new ConfigEntryList.ConfigTextFieldWidget<>(valueSpec, configEntry, Long::valueOf, s -> Long.toString(s));
		final Consumer<Long> textSetter = widget::convertAndSet;
		return new ImmutableTriple<>(widget, configEntry, textSetter);
	}

	private static Triple<Widget, ConfigEntry<?>, Consumer<?>> makeFloat(final IWidgetMaker.Context context) {
		final ForgeConfigSpec.ValueSpec valueSpec = context.getValueSpec();
		final ConfigEntry<Float> configEntry = ConfigEntry.of(Float.class, context.getModConfig(), context.getPath(), valueSpec);
		final ConfigEntryList.ConfigTextFieldWidget<Float> widget = new ConfigEntryList.ConfigTextFieldWidget<>(valueSpec, configEntry, Float::valueOf, s -> Float.toString(s));
		final Consumer<Float> textSetter = widget::convertAndSet;
		return new ImmutableTriple<>(widget, configEntry, textSetter);
	}

	private static Triple<Widget, ConfigEntry<?>, Consumer<?>> makeList(final IWidgetMaker.Context context) {
		final ForgeConfigSpec.ValueSpec valueSpec = context.getValueSpec();
		final ConfigEntry<List> configEntry = ConfigEntry.of(List.class, context.getModConfig(), context.getPath(), valueSpec);
		if (!(configEntry.getDefaultValue() instanceof List))
			return null;
		final String title = context.getModConfig().getFileName() + " > " + configEntry.getPath().replace(".", " > ");
		final GuiButtonExt widget = new GuiButtonExt(0, 0, 100, 20, configEntry.getInitialValue().toString(), b -> {
			final Screen currentScreen = Minecraft.getInstance().currentScreen;
			if (!(currentScreen instanceof ConfigScreen)) {
				LogManager.getLogger().warn("Unable to make List screen. Current screen is not a config screen. {}", currentScreen);
				return;
			}
			final ConfigScreen parentScreen = ((ConfigScreen) currentScreen);
			final Screen screen = new Screen(new StringTextComponent(title)) {
				@Override
				public void render(final int mouseX, final int mouseY, final float partialTicks) {
					this.renderDirtBackground(0);

					final int halfWidth = this.width / 2;

					this.drawCenteredString(font, this.title.getFormattedText(), halfWidth, 8, 0xFFFFFF);
					this.drawCenteredString(font, "Lists are not Implemented Yet", halfWidth, this.height / 2, 0xFFFFFF);

					super.render(mouseX, mouseY, partialTicks);
				}

				@Override
				public void onClose() {
					minecraft.displayGuiScreen(parentScreen);
				}

				@Override
				protected void init() {
					super.init();
					int buttonWidthHalf = 100 / 2;

					int xPos = this.width / 2 - buttonWidthHalf;
					int yPos = this.height - 29;
					this.addButton(new GuiButtonExt(xPos, yPos, 100, 20, I18n.format("gui.done"), b -> this.onClose()));
				}
			};
			Minecraft.getInstance().displayGuiScreen(screen);
		});
		final Consumer<List> textSetter = loadValue -> widget.setMessage(loadValue.toString());
		return new ImmutableTriple<>(widget, configEntry, textSetter);
	}

}
