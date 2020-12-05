package za.co.botcoin.utils;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

public class MathUtils
{
    public static Double precision(Double value)
    {
        Double toReturn = null;

        if(value != null)
        {
            DecimalFormatSymbols dfs = new DecimalFormatSymbols();
            dfs.setDecimalSeparator('.');

            DecimalFormat decimalFormat = new DecimalFormat("#.##");
            decimalFormat.setDecimalFormatSymbols(dfs);

            toReturn = Double.valueOf(decimalFormat.format(value));
        }

        return toReturn;
    }

    public static Double percentage(Double value, Integer percentage)
    {
        Double toReturn = null;

        if(value != null && percentage != null)
        {
            toReturn = value * (percentage / 100.0f);
        }

        return toReturn;
    }
}
