package ch.epfl.alpano.gui;

import java.math.BigDecimal;
import java.math.RoundingMode;

import javafx.util.StringConverter;

/**
 * Represents a converter from a String to an Integer and vice versa
 * 
 * @author Mathieu Chevalley (274698)
 * @author Louis Amaudruz (271808)
 *
 * @see StringConverter<Integer>
 */
public final class FixedPointStringConverter extends StringConverter<Integer> {

    private final int decimal;

    /**
     * Create a converter given the number of decimal to represent
     * 
     * @param decimal
     */
    public FixedPointStringConverter(int decimal) {
        this.decimal = decimal;
    }

    @Override
    public Integer fromString(String s) {
        if (s == null) {
            return -1;
        }
        BigDecimal d = new BigDecimal(s);
        return d.movePointRight(decimal).setScale(0, RoundingMode.HALF_UP)
                .intValueExact();
    }

    @Override
    public String toString(Integer i) {
        if (i == null) {
            return "";
        }
        BigDecimal d = new BigDecimal(i);
        return d.movePointLeft(decimal).stripTrailingZeros().toPlainString();
    }

}
