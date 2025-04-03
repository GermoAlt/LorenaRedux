package dev.altairac.lorenaredux.enums;

import org.springframework.data.util.Pair;
import tech.units.indriya.unit.ProductUnit;
import tech.units.indriya.unit.Units;

import javax.measure.Unit;
import javax.measure.UnitConverter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.regex.Pattern;


public enum ConversionUnit {
    // Base units
    KELVIN(Units.KELVIN, "K", "kelvin", "k"),
    KILOGRAM(Units.KILOGRAM, "kg", "kg", "kilogram", "kgs", "kilograms"),
    METRE(Units.METRE, "m", "metre", "meter", "meters", "metres", "m"),
    SECOND(Units.SECOND, "sec", "second", "seconds", "s"),

    // Derived units
    GRAM(Units.GRAM, "g", "gram", "grams", "g"),
    CELSIUS(Units.CELSIUS, "°C", "celsius", "°c", "°", "c"),
    SQUARE_METRE(Units.SQUARE_METRE, "m²", "m²", "m2", "square meter", "square meters"),
    KILOMETRE(Units.METRE.multiply(1000), "km", "km", "kilometre", "kilometres", "kilometer", "kilometers"),
    CENTIMETRE(Units.METRE.divide(100), "cm", "cm", "centimetre", "centimetres", "centimeter", "centimeters"),
    MILLIMETRE(Units.METRE.divide(1000), "mm", "mm", "millimetre", "millimeters", "millimeter", "millimeters"),

    // Non SI
    FOOT(Units.METRE.multiply(0.3048), "ft", "foot", "feet"),
    YARD(Units.METRE.multiply(0.9144), "yd", "yard", "yards"),
    INCH(Units.METRE.multiply(0.0254), "in", "inch", "inches"),
    MILE(Units.METRE.multiply(1609.34), "mi", "mile", "miles"),
    SQUARE_FOOT(new ProductUnit<>(Units.METRE.multiply(0.3048).multiply(Units.METRE.multiply(0.3048))),
            "ft²", "square foot", "square feet", "ft2"),



    MINUTE(Units.SECOND.multiply(60), "min", "minute", "minutes"),
    HOUR(Units.SECOND.multiply(3600), "h", "hour", "hours"),
    DAY(Units.SECOND.multiply(86400), "d", "day", "days"),
    WEEK(Units.SECOND.multiply(604800), "w", "week", "weeks"),
    YEAR(Units.SECOND.multiply(31536000), "yr", "year", "years"),


    POUND(Units.KILOGRAM.multiply(0.453592), "lb", "pound", "pounds"),
    OUNCE(Units.KILOGRAM.multiply(0.0283495), "oz", "ounce", "ounces"),
    TON(Units.KILOGRAM.multiply(1000), "t", "ton", "tonne", "tons", "tonnes"),
    US_TON(Units.KILOGRAM.multiply(907.18474), "us t", "us ton", "us tons", "imperial ton", "imperial tons"),

    // Temperature
    FAHRENHEIT(null, "°F", "fahrenheit", "f"), // Special handling will be needed for non-linear conversions

    KMH(Units.METRE.divide(Units.SECOND).multiply(3.6), "km/h", "kilometers per hour", "kmh", "kph"),
    MPH(Units.METRE.divide(Units.SECOND).multiply(2.23694), "mph", "miles per hour"),
    METERS_PER_SECOND(Units.METRE.divide(Units.SECOND), "m/s", "meters per second"),

    LITRE(Units.LITRE, "l", "litre", "liter", "litres", "liters"),
    MILLILITRE(Units.LITRE.divide(1000), "ml", "millilitre", "milliliter", "millilitres", "milliliters"),
    CENTILITRE(Units.LITRE.divide(100), "cl", "centilitre", "centiliter", "centilitres", "centiliters"),
    DECILITRE(Units.LITRE.divide(10), "dl", "decilitre", "deciliter", "decilitres", "deciliters"),
    US_GALLON(Units.LITRE.multiply(3.78541), "gal", "gallon", "gallons"),
    CUP(Units.LITRE.multiply(0.236588), "cup", "cups"),
    LIQUID_OUNCE(Units.LITRE.multiply(0.0295735), "fl oz", "fluid ounce", "liquid ounce", "liquid ounces"),

    // Fun Units
    JOHNE(INCH.unit.multiply(26), "JohnEs", "johne", "johnes", "john", "johns"),
    BANANA(CENTIMETRE.unit.multiply(13), "bananas", "banana", "bananas"),
    SCARAMUCCI(DAY.unit.multiply(10), "Mooches", "mooch", "mooches", "scaramucci", "scaramuccis"),
    SMOOT(CENTIMETRE.unit.multiply(170), "Smoots", "smoot", "smoots"),
    ROUND_TOWERS(METRE.unit.multiply(34.8), "round towers", "round tower", "round towers"),
    MARATHONS(METRE.unit.multiply(42195), "marathons", "marathons", "marathon"),
    LOADED_JUMBO_JETS(KILOGRAM.unit.multiply(442000), "loaded jumbo jets", "loaded jumbo jets", "loaded jumbo jet"),
    USAIN_BOLT(KMH.unit.multiply(37.58), "Usain Bolt's average speed", "usain bolts", "usain bolts average speed"),
    REALTOR_STONE_THROW(METRE.unit.multiply(943), "Realtor's stone throws", "stone throw", "stone throws");

    // Fields
    private final Unit<?> unit;
    private final String printedName;
    private final String[] ciNames;

    ConversionUnit(Unit<?> unit, String printedName, String... ciNames) {
        this.unit = unit;
        this.printedName = printedName;
        this.ciNames = ciNames;
    }

    public static final Set<ConversionUnit> AUTO_CONVERSION = new HashSet<>(List.of(KILOGRAM, METRE, KILOMETRE,
            CENTIMETRE, MILLIMETRE, CELSIUS,
            SQUARE_METRE, FOOT, SQUARE_FOOT, YARD, INCH, MILE, POUND,
            OUNCE, TON, FAHRENHEIT, KMH, MPH, LITRE, MILLILITRE, CENTILITRE,
            DECILITRE, LIQUID_OUNCE, US_GALLON, CUP))
    ;
    public static final Set<String> AUTO_CONVERSION_NAMES = AUTO_CONVERSION.stream()
            .flatMap(item -> Arrays.stream(item.getCiNames()))
            .collect(Collectors.toSet());

    private static final Map<ConversionUnit, ConversionUnit> AUTO_CONVERTERS = new HashMap<>();

    static {
        AUTO_CONVERTERS.put(KILOGRAM, POUND);
        AUTO_CONVERTERS.put(METRE, FOOT);
        AUTO_CONVERTERS.put(KILOMETRE, MILE);
        AUTO_CONVERTERS.put(CENTIMETRE, INCH);
        AUTO_CONVERTERS.put(MILLIMETRE, INCH);
        AUTO_CONVERTERS.put(CELSIUS, FAHRENHEIT);
        AUTO_CONVERTERS.put(SQUARE_METRE, SQUARE_FOOT);
        AUTO_CONVERTERS.put(FOOT, METRE);
        AUTO_CONVERTERS.put(SQUARE_FOOT, SQUARE_METRE);
        AUTO_CONVERTERS.put(YARD, METRE);
        AUTO_CONVERTERS.put(INCH, CENTIMETRE);
        AUTO_CONVERTERS.put(MILE, KILOMETRE);
        AUTO_CONVERTERS.put(POUND, KILOGRAM);
        AUTO_CONVERTERS.put(OUNCE, KILOGRAM);
        AUTO_CONVERTERS.put(FAHRENHEIT, CELSIUS);
        AUTO_CONVERTERS.put(KMH, MPH);
        AUTO_CONVERTERS.put(LITRE, US_GALLON);
        AUTO_CONVERTERS.put(MILLILITRE, LIQUID_OUNCE);
        AUTO_CONVERTERS.put(CENTILITRE, LIQUID_OUNCE);
        AUTO_CONVERTERS.put(DECILITRE, LIQUID_OUNCE);
        AUTO_CONVERTERS.put(LIQUID_OUNCE, MILLILITRE);
        AUTO_CONVERTERS.put(US_GALLON, LITRE);
        AUTO_CONVERTERS.put(CUP, MILLILITRE);
    }

    public Unit<?> getUnit() {
        return unit;
    }

    public String getPrintedName() {
        return printedName;
    }

    public String[] getCiNames() {
        return ciNames;
    }

    public static ConversionUnit match(String name) {
        String lower = name.toLowerCase(Locale.ROOT);
        return Arrays.stream(values())
                .filter(conversionUnit -> Arrays.asList(conversionUnit.ciNames).contains(lower))
                .findFirst()
                .orElse(null);
    }

    /**
     * Equivalent of fun Units.corresponding(): Units? = AUTO_CONVERTERS[this]
     */
    public static ConversionUnit corresponding(ConversionUnit conversionUnit) {
        return AUTO_CONVERTERS.get(conversionUnit); // Returns the mapped value if it exists, otherwise null
    }

//    /**
//     * Equivalent of fun Pair<Units, Units>.autoConverter(): UnitConverter? = ...
//     */
//    public static UnitConverter autoConverter(Pair<ConversionUnit, ConversionUnit> unitPair) {
//        ConversionUnit correspondingConversionUnit = AUTO_CONVERTERS.get(unitPair.getFirst());
//        if (correspondingConversionUnit != null) {
//            return unitPair.getFirst().getUnit().getConverterTo(correspondingConversionUnit.getUnit());
//        }
//        return null;
//    }

    /**
     * Equivalent of fun splitTokenAuto(token: String): List<String>
     */
    public static List<String> splitTokenAuto(String token) {
        return splitToken(token, AUTO_CONVERSION_NAMES);
    }

    /**
     * Equivalent of fun matchAuto(first: String, second: String? = null): Units?
     */
    public static ConversionUnit matchAuto(String first, String second) {
        return match(first.trim(), second != null ? second.trim() : null, AUTO_CONVERSION);
    }

    public static ConversionUnit matchAuto(String first) {
        return matchAuto(first, null); // Overload when `second` is not provided
    }

    /**
     * Equivalent of fun matchAll(first: String, second: String? = null): Units?
     */
    public static ConversionUnit matchAll(String first, String second) {
        return match(first.trim(), second != null ? second.trim() : null, Set.of(ConversionUnit.values()));
    }

    public static ConversionUnit matchAll(String first) {
        return matchAll(first, null); // Overload when `second` is not provided
    }

    /**
     * Equivalent of the private fun splitToken(token: String, collection: Set<String>): List<String>
     */
    private static List<String> splitToken(String token, Set<String> collection) {
        for (String suffix : collection) {
            if (Pattern.compile("\\d" + suffix + "\\b").matcher(token.toLowerCase(Locale.ROOT)).find()) {
                String firstPart = token.substring(0, token.length() - suffix.length());
                try {
                    Double.parseDouble(firstPart); // Check if the first part is numeric
                    return List.of(firstPart, suffix);
                } catch (NumberFormatException e) {
                    // Ignored, fall back to returning the original token
                }
            }
        }
        return List.of(token);
    }

    /**
     * Equivalent of private fun match(first: String, second: String?, collection: Array<Units>): Units?
     */
    private static ConversionUnit match(String first, String second, Set<ConversionUnit> collection) {
        // Convert `first` to lowercase for case-insensitive matching
        String firstLower = first.toLowerCase(Locale.ROOT);

        // First pass: Check if `first` matches directly in `ciNames`
        for (ConversionUnit conversionUnit : collection) {
            if (Arrays.asList(conversionUnit.getCiNames()).contains(firstLower)) { // Use contains on ciNames collection
                return conversionUnit; // A match is found
            }
        }

        // Second pass: If `second` is not null, combine `first` and `second` and check matches
        if (second != null) {
            String combined = (first + " " + second).toLowerCase(Locale.ROOT);
            for (ConversionUnit conversionUnit : collection) {
                if (Arrays.asList(conversionUnit.getCiNames()).contains(combined)) { // Check combined string
                    return conversionUnit; // A match is found
                }
            }
        }

        // No match found
        return null;
    }

    /**
     * Special handling for Fahrenheit conversion since it requires custom formula due to non-linear scaling.
     */
    public static String convertTemperature(double value, boolean toFahrenheit) {
        if (toFahrenheit) {
            // Celsius to Fahrenheit
            return String.format("%.2f °F", (value * 9 / 5) + 32);
        } else {
            // Fahrenheit to Celsius
            return String.format("%.2f °C", (value - 32) * 5 / 9);
        }
    }


}