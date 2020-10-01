package net.minecraftforge.client.gui.config;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.item.DyeColor;
import net.minecraft.util.ColorHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.gui.config.ControlCreator.ConfigElementTextField;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ExtensionPoint;
import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLConstructModEvent;
import net.minecraftforge.forgespi.language.IModInfo;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nullable;
import java.awt.Color;
import java.util.List;
import java.util.Random;
import java.util.function.Consumer;

import static net.minecraftforge.client.gui.config.ControlCreator.RED;

/**
 * Just for testing the extensibility of the system.
 * Will either be removed or converted to a test mod.
 *
 * @author Cadiboo
 */
@EventBusSubscriber(bus = Bus.MOD, modid = "forge", value = Dist.CLIENT)
public class ConfigScreenTesting extends ModConfigScreen {

    public static final Common COMMON;
    static final ForgeConfigSpec commonSpec;

    static {
        final Pair<Common, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(Common::new);
        commonSpec = specPair.getRight();
        COMMON = specPair.getLeft();
    }

    private final ControlCreator controlCreator;

    public ConfigScreenTesting(Screen screen, IModInfo mod) {
        super(screen, mod);
        controlCreator = new ControlCreator() {

            @Override
            public TextFieldWidget createTextField(Interactor<?> interactor) {
                if (interactor instanceof ConfigValueInteractor)
                    if (((ConfigValueInteractor<?>) interactor).getConfigValue() == COMMON.hexColorString)
                        return new TestColorTextField(interactor.title);
                return super.createTextField(interactor);
            }

            @Override
            public Button createButton(Interactor<?> interactor, Button.IPressable iPressable) {
                if (interactor instanceof ConfigValueInteractor)
                    if (((ConfigValueInteractor<?>) interactor).getConfigValue() == COMMON.colouredBoolean)
                        return new ConfigElementButton(interactor.title, iPressable) {
                            final Random random = new Random();
                            @Override
                            public void func_230431_b_(MatrixStack mStack, int mouseX, int mouseY, float partial) {
                                final float hue = random.nextFloat();
                                final float saturation = 0.9F;
                                final float luminance = 1.0F;
                                Color color = Color.getHSBColor(hue, saturation, luminance);
                                int packed = ColorHelper.PackedColor.func_233006_a_(color.getAlpha(), color.getRed(), color.getBlue(), color.getGreen());
                                setFGColor(packed);
                                super.func_230431_b_(mStack, mouseX, mouseY, partial);
                            }
                        };
                return super.createButton(interactor, iPressable);
            }
        };
    }

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        ModContainer forge = ModList.get().getModContainerById("forge").get();
        forge.registerExtensionPoint(ExtensionPoint.CONFIGGUIFACTORY, () -> (mc, screen) -> new ConfigScreenTesting(screen, forge.getModInfo()));
    }

    @SubscribeEvent
    public static void onCommonSetup(FMLConstructModEvent event) {
        ModContainer forge = ModList.get().getModContainerById("forge").get();
        forge.addConfig(new ModConfig(ModConfig.Type.COMMON, commonSpec, forge));
    }

    @Override
    public ControlCreator getControlCreator() {
        return controlCreator;
    }

    /**
     * Just testing to check that something like a custom color wheel would be able to work
     */
    public static class TestColorTextField extends ConfigElementTextField {
        public TestColorTextField(ITextComponent title) {
            super(title);
        }

        @Override
        public void setResponder(@Nullable Consumer<String> responderIn) {
            super.setResponder(newText -> {
                if (responderIn != null)
                    responderIn.accept(newText);
                setColor(newText);
            });
        }

        private void setColor(String newText) {
            if (newText == null)
                return;
            if (newText.matches("#[0-9A-f][0-9A-f][0-9A-f]")) {
                String r = newText.substring(1, 2);
                String g = newText.substring(2, 3);
                String b = newText.substring(3, 4);
                newText = "#" + r + r + g + g + b + b;
            }
            if (newText.matches("#[0-9A-f][0-9A-f][0-9A-f][0-9A-f][0-9A-f][0-9A-f]")) {
                String r = newText.substring(1, 3);
                String g = newText.substring(3, 5);
                String b = newText.substring(5, 7);
                int ir;
                int ig;
                int ib;
                try {
                    ir = Integer.parseInt(r, 16);
                    ig = Integer.parseInt(g, 16);
                    ib = Integer.parseInt(b, 16);
                } catch (NumberFormatException e) {
                    setTextColor(RED);
                    return;
                }
                setTextColor(ir << 16 | ig << 8 | ib);
            }
        }
    }

    public static class Common {

        private final ForgeConfigSpec.ConfigValue<String> hexColorString;
        private final ForgeConfigSpec.BooleanValue colouredBoolean;

        Common(ForgeConfigSpec.Builder builder) {
            builder.comment("a Boolean comment")
                    .translation("forge.configgui.a.boolean")
                    .worldRestart()
                    .define("aBoolean", false);

            builder.comment("Category comment")
                    .push("numbers");
            {
                builder.comment("an Int comment")
                        .translation("forge.configgui.an.int")
                        .defineInRange("anInt", 5, -10, 1000);
                builder.comment("a Long comment")
                        .translation("forge.configgui.a.long")
                        .defineInRange("aLong", Long.MIN_VALUE, Long.MIN_VALUE, 5);
                builder.comment("a Double comment")
                        .translation("forge.configgui.a.double")
                        .defineInRange("aDouble", 5d, 4d, 6d);

                builder.comment("Sub-category comment")
                        .push("empty");
                builder.pop();

                builder.comment("Sub-category comment")
                        .push("not-empty");
                {
                    builder.comment("sub - an Int comment")
                            .translation("forge.configgui.sub.an.int")
                            .defineInRange("subAnInt", 5, -10, 1000);
                }
                builder.pop();
            }
            builder.pop();

            builder.comment("an Enum comment")
                    .translation("forge.configgui.an.enum")
                    .defineEnum("anEnum", DyeColor.GREEN, dc -> dc instanceof DyeColor && ((DyeColor) dc).getId() >= 10);
            builder.comment("a String comment")
                    .translation("forge.configgui.a.string")
                    .define("aString", "foo");
            hexColorString = builder.comment("a Hex Color String comment")
                    .translation("forge.configgui.a.hexColorString")
                    .define("aHexColorString", "#0FF");
            colouredBoolean = builder.comment("a Coloured Boolean comment")
                    .translation("forge.configgui.a.colouredBoolean")
                    .define("aColouredBoolean", false);
            builder.comment("an Enum List comment")
                    .translation("forge.configgui.an.enum.list")
                    .defineList("anEnumList", Lists.newArrayList(), o -> o instanceof String);
            builder.comment("a String In List comment")
                    .translation("forge.configgui.a.string.in.list")
                    .defineInList("aStringInList", "bar", Lists.newArrayList("foo", "bar", "baz"));

            builder.comment("I'm in Spain but the S is silent")
                    .push("Lists!");
            {
                builder.comment("a List<Boolean> comment")
                        .translation("forge.configgui.a.list.boolean")
                        .defineList("aListBoolean", Lists.newArrayList(true, false, true, false, true, true, false, false), o -> o instanceof Boolean);
                builder.comment("a List<Int> comment")
                        .translation("forge.configgui.a.list.int")
                        .defineList("aListInt", Lists.newArrayList(1, 2, 5, 66, 3, 7), o -> o instanceof Integer);
                builder.comment("a List<Long> comment")
                        .translation("forge.configgui.a.list.long")
                        .defineList("aListLong", Lists.newArrayList(5L, 12L, 623L, Long.MIN_VALUE), o -> o instanceof Long);
                builder.comment("a List<Double> comment")
                        .translation("forge.configgui.a.list.double")
                        .defineList("aListDouble", Lists.newArrayList(123.213, 21343.21, 456.32, 9765.2), o -> o instanceof Double);
                builder.comment("a List<Enum> comment")
                        .translation("forge.configgui.a.list.enum")
                        .defineList("aListEnum", Lists.newArrayList(DyeColor.BLACK, DyeColor.GREEN, DyeColor.RED, DyeColor.BLACK), o -> o instanceof DyeColor);
                builder.comment("a List<String> comment")
                        .translation("forge.configgui.a.list.string")
                        .defineList("aListString", Lists.newArrayList("foo", "bar", "baz", "qez"), o -> o instanceof String);
                builder.comment("a List<List<String>> comment")
                        .translation("forge.configgui.a.list.list.string")
                        .defineList("aListListString", Lists.newArrayList(Lists.newArrayList("foo", "bar", "baz", "qez"), Lists.newArrayList("foobar")), o -> o instanceof List);
            }
            builder.pop();
        }
    }
}