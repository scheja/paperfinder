package edu.kit.ksri.paperfinder.util;

/**
 * Created by janscheurenbrand on 02.12.14.
 */
public class NumberUtils {
    public static int parseInt( String string, int defaultValue ) {
        try {
            return Integer.parseInt(string);
        }
        catch (NumberFormatException e ) {
            return defaultValue;
        }
    }
}
