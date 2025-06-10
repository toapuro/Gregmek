package dev.toapuro.gregmek.common.helper;

import mekanism.api.math.FloatingLong;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FloatingLongHelper {
    private static final Logger LOGGER = LoggerFactory.getLogger(FloatingLongHelper.class);

    public static FloatingLong getFloatingLong(double d, Unit unit) {
        return unit.value.multiply(d);
    }

    public static FloatingLong parse(String string) {
        for (Unit unit : Unit.values()) {
            if (string.endsWith(unit.symbol.toUpperCase()) || string.endsWith(unit.symbol.toLowerCase())) {
                String valueString = string.substring(0, string.length() - unit.symbol.length());
                try {
                    return getFloatingLong(Double.parseDouble(valueString), unit);
                } catch (NumberFormatException e) {
                    LOGGER.warn("Could not parse {},{} from {}", valueString, unit.name, string);
                    return FloatingLong.ZERO;
                }
            }
        }

        LOGGER.warn("Could not parse {}", string);
        return FloatingLong.ZERO;
    }

    public enum Unit {
        MILLI("Milli", "m", FloatingLong.createConst(0.001)),
        BASE("", "", FloatingLong.ONE),
        KILO("Kilo", "k", FloatingLong.createConst(1000L)),
        MEGA("Mega", "M", FloatingLong.createConst(1000000L)),
        GIGA("Giga", "G", FloatingLong.createConst(1000000000L)),
        TERA("Tera", "T", FloatingLong.createConst(1000000000000L)),
        PETA("Peta", "P", FloatingLong.createConst(1000000000000000L)),
        EXA("Exa", "E", FloatingLong.createConst(1000000000000000000L));

        private final String name;
        private final String symbol;
        private final FloatingLong value;

        Unit(String name, String symbol, FloatingLong value) {
            this.name = name;
            this.symbol = symbol;
            this.value = value;
        }

        public String getName(boolean getShort) {
            return getShort ? this.symbol : this.name;
        }

        public FloatingLong process(FloatingLong d) {
            return d.divide(this.value);
        }

        public boolean aboveEqual(FloatingLong d) {
            return d.greaterOrEqual(this.value);
        }

        public boolean below(FloatingLong d) {
            return d.smallerThan(this.value);
        }
    }
}
