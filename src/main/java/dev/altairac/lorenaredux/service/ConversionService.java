package dev.altairac.lorenaredux.service;

import dev.altairac.lorenaredux.enums.ConversionUnit;
import org.springframework.stereotype.Service;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.*;

@Service
public class ConversionService {

    public String convert(ConversionUnit fromConversionUnit, ConversionUnit toConversionUnit, double amount) {
        if (!fromConversionUnit.getUnit().isCompatible(toConversionUnit.getUnit())) {
            return "These units do not match";
        }

        double convertedValue = fromConversionUnit.getUnit().getConverterTo(toConversionUnit.getUnit()).convert(amount);

        return String.format(Locale.ROOT, "%s %s is %s %s",
                digits(amount, 3), fromConversionUnit.getPrintedName(),
                digits(convertedValue, 3), toConversionUnit.getPrintedName());
    }

    private String digits(double value, int digits) {
        DecimalFormat decimalFormat = new DecimalFormat("0." + "#".repeat(digits), DecimalFormatSymbols.getInstance(Locale.ROOT));
        decimalFormat.setRoundingMode(RoundingMode.HALF_UP);
        return decimalFormat.format(value);
    }


}
