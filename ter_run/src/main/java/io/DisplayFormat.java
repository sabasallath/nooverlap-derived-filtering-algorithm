package io;

/**
 * Strings constants use to display data in a consistent way.
 */
public class DisplayFormat {

    /**
     * Format integer value in a consistent way.
     * @param i Unformated integer.
     * @return Formated integer.
     */
    public static String format(int i) {
        return String.format("%6d", i);
    }

    /**
     * Format double value in a consistent way.
     * @param d Unformated double.
     * @return Formated double.
     */
    public static String format(double d) {
        return String.format("%6.3f", d);
    }

    /**
     * Format char value in a consistent way.
     * @param c Unformated char.
     * @return Formated char.
     */
    public static String format(char c) {
        return String.format("%6c", c);
    }

    /**
     * Format string in a consistent way.
     * @param str Unformated string.
     * @return Formated string.
     */
    public static String format(String str) {
        return String.format("%" + (Math.min(30, str.length())) + "s", str);
    }
}
