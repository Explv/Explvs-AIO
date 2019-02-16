package util;

import java.text.DecimalFormat;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class RSUnits {

    public static final Pattern UNIT_PATTERN = Pattern.compile("^\\d+(?:(?:\\.\\d+[kmb])|[kmb])?$");

    private static final DecimalFormat decimalFormat = new DecimalFormat("#.###");

    private static final Map<Character, Integer> suffixMultipliers;

    static {
        // Use a LinkedHashMap to maintain insertion order (largest denomination first)
        Map<Character, Integer> suffixMultipliersTemp = new LinkedHashMap<>();

        suffixMultipliersTemp.put('b', 1_000_000_000);
        suffixMultipliersTemp.put('m', 1_000_000);
        suffixMultipliersTemp.put('k', 1_000);

        suffixMultipliers = Collections.unmodifiableMap(suffixMultipliersTemp);
    }

    public static long formattedToValue(String formattedValue) {
        formattedValue = formattedValue.trim();

        formattedValue = formattedValue.toLowerCase();

        char suffix = formattedValue.charAt(formattedValue.length() - 1);

        // If no suffix exists, assume it is already in GP
        if (!suffixMultipliers.containsKey(suffix)) {
            return Integer.parseInt(formattedValue);
        }

        String valueNoSuffix = formattedValue.substring(0, formattedValue.length() - 1);

        double valueDouble = Double.parseDouble(valueNoSuffix);

        valueDouble = valueDouble * suffixMultipliers.get(suffix);

        return (long) valueDouble;
    }

    public static String valueToFormatted(final long value) {
        for (Character suffix : suffixMultipliers.keySet()) {
            Integer multiplier = suffixMultipliers.get(suffix);

            if (value > multiplier) {
                double newValue = ((double) value / multiplier);

                return decimalFormat.format(newValue) + suffix;
            }
        }

        return Long.toString(value);
    }
}
