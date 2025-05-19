package dev.tdnpgm.gregmek.lang;

import dev.tdnpgm.gregmek.Gregmek;
import mekanism.api.text.ILangEntry;
import net.minecraft.Util;

public enum GregmekLang implements ILangEntry {
    GREGMEK("general", "gregmek");

    private final String key;

    GregmekLang(String type, String path) {
        this(Util.makeDescriptionId(type, Gregmek.rl(path)));
    }

    GregmekLang(String key) {
        this.key = key;
    }

    public String getTranslationKey() {
        return this.key;
    }
}
