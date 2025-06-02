package dev.toapuro.gregmek.content.lang;

import dev.toapuro.gregmek.Gregmek;
import mekanism.api.text.ILangEntry;
import net.minecraft.Util;
import org.jetbrains.annotations.NotNull;

public enum GregmekLang implements ILangEntry {
    GREGMEK("general", "gregmek"),
    GREGMEK_DESCRIPTION("general", "description"),
    CIRCUIT_CONFIG("configuration", "circuit");

    private final String key;

    GregmekLang(String type, String path) {
        this(Util.makeDescriptionId(type, Gregmek.rl(path)));
    }

    GregmekLang(String key) {
        this.key = key;
    }

    public @NotNull String getTranslationKey() {
        return this.key;
    }
}
