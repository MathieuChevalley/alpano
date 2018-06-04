package ch.epfl.alpano.gui;

import java.util.Arrays;
import java.util.List;

import javafx.util.StringConverter;

/**
 * Represents a converter from a list of String to Integer and the other way
 * around
 * 
 * @author Mathieu Chevalley (274698)
 * @author Louis Amaudruz (271808)
 *
 * @see StringConverter<Integer>
 */
public final class LabeledListStringConverter extends StringConverter<Integer> {

    private final List<String> strings;

    /**
     * Construct the converter with a list of string
     * 
     * @param strings
     *            the list of string
     */
    public LabeledListStringConverter(String... strings) {
        this.strings = Arrays.asList(strings);
    }

    @Override
    public Integer fromString(String s) {
        if (s == null) {
            return -1;
        }
        return strings.indexOf(s);
    }

    @Override
    public String toString(Integer i) {
        if (i == null) {
            return "";
        }
        return strings.get(i);
    }

}
