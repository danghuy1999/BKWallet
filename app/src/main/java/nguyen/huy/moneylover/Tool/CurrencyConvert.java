package nguyen.huy.moneylover.Tool;

import java.text.NumberFormat;
import java.util.Locale;

public class CurrencyConvert {
    public static String MoneyConvert(Long value)
    {
        NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.US);
        return numberFormat.format(value) + " đ";
    }
}
