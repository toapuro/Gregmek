package dev.toapuro.gregmek.content.tier;

import mekanism.api.math.MathUtils;
import net.minecraft.network.chat.TextColor;
import net.minecraft.world.level.material.MapColor;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;

public enum GMTier {
    PRIMITIVE("Primitive", new int[]{184, 165, 134}, MapColor.COLOR_BROWN),
    CRUDE("Crude", new int[]{117, 88, 19}, MapColor.COLOR_BROWN),
    INDUSTRIAL("Industrial", new int[]{95, 255, 184}, MapColor.COLOR_LIGHT_GREEN),
    IMPROVED("Improved", new int[]{95, 255, 184}, MapColor.COLOR_LIGHT_GREEN),
    REFINED("Refined", new int[]{74, 74, 690}, MapColor.COLOR_LIGHT_BLUE),
    INTEGRATED("Integrated", new int[]{255, 128, 106}, MapColor.TERRACOTTA_PINK),
    FABRICATED("Fabricated", new int[]{75, 248, 255}, MapColor.DIAMOND),
    QUANTUM("Quantum", new int[]{247, 135, 255}, MapColor.COLOR_MAGENTA),
    ENTANGLED("Entangled", new int[]{50, 50, 50}, MapColor.COLOR_BLACK);

    private static final GMTier[] TIERS = values();
    private final String name;
    private final MapColor mapColor;
    private TextColor textColor;
    private int[] rgbCode;

    GMTier(String name, int[] rgbCode, MapColor mapColor) {
        this.name = name;
        this.mapColor = mapColor;
        this.rgbCode = rgbCode;
        setColorFromAtlas(rgbCode);
    }

    public static GMTier byIndexStatic(int index) {
        return MathUtils.getByIndexMod(TIERS, index);
    }

    public boolean supports(GMTier other) {
        return this.ordinal() >= other.ordinal();
    }

    public String getSimpleName() {
        return this.name;
    }

    public String getLowerName() {
        return this.getSimpleName().toLowerCase(Locale.ROOT);
    }

    public MapColor getMapColor() {
        return this.mapColor;
    }

    public int[] getRgbCode() {
        return this.rgbCode;
    }

    public void setColorFromAtlas(int[] color) {
        this.rgbCode = color;
        this.textColor = TextColor.fromRgb(this.rgbCode[0] << 16 | this.rgbCode[1] << 8 | this.rgbCode[2]);
    }

    public TextColor getColor() {
        return this.textColor;
    }

    public @NotNull String getSerializedName() {
        return this.name().toLowerCase(Locale.ROOT);
    }
}
