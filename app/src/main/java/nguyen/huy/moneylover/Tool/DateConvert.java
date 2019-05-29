package nguyen.huy.moneylover.Tool;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DateConvert {
    public static String[] getCurrentDay()
    {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String currentDay = simpleDateFormat.format(calendar.getTime());
        String[] words=currentDay.split("[/]");
        String[] resultString=new String[3];
        resultString[0]=words[1]+ "+" + words[2];
        resultString[1]= words[0]+ "+" +words[1]+ "+" + words[2];
        resultString[2]= currentDay;
        return resultString;
    }
    public static String firebasenode2StringDay (String s)
    {
        String[] words = s.split("[+]");
        return words[0]+"/"+words[1]+"/"+words[2];
    }
}
