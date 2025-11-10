package utils;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

public class CurrencyUtil {

    private static final DecimalFormat CURRENCY_FORMAT =
            (DecimalFormat) NumberFormat.getNumberInstance(new Locale("pt", "MZ"));

    static {
        CURRENCY_FORMAT.applyPattern("#,##0.00");
    }

    public static String format(double value) {
        return "MZN " + CURRENCY_FORMAT.format(value);
    }

    public static double parse(String value) throws NumberFormatException {
        if (value == null || value.trim().isEmpty()) {
            throw new NumberFormatException("Valor vazio");
        }

        // Remove "MZN" e espaços, troca vírgula por ponto
        String cleaned = value.replace("MZN", "").trim().replace(",", ".");

        return Double.parseDouble(cleaned);
    }

    public static boolean isValidAmount(String value) {
        try {
            double amount = parse(value);
            return amount > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}