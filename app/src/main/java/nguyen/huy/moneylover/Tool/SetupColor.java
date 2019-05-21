package nguyen.huy.moneylover.Tool;

import android.graphics.Color;

import java.util.ArrayList;
import java.util.Collections;

public class SetupColor {

    public static ArrayList<Integer> randomListOf16()
    {
        ArrayList<Integer> list = new ArrayList<>();
        for (int i = 0;i<17;i++)
        {
            list.add(i);
        }
        Collections.shuffle(list);
        return list;
    }

    public static int getBestColor(int GroupPosition)
    {
        int color = 0;
        switch (GroupPosition)
        {
            case 0 : color = Color.parseColor("#36688D");break;
            case 1 : color = Color.parseColor("#F18904");break;
            case 2 : color = Color.parseColor("#A7414A");break;
            case 3 : color = Color.parseColor("#6A8A82");break;
            case 4 : color = Color.parseColor("#0444BF");break;
            case 5 : color = Color.parseColor("#0584F2");break;
            case 6 : color = Color.parseColor("#6465A5");break;
            case 7 : color = Color.parseColor("#F05837");break;
            case 8 : color = Color.parseColor("#595775");break;
            case 9 : color = Color.parseColor("#BF988F");break;
            case 10 : color = Color.parseColor("#192E5B");break;
            case 11 : color = Color.parseColor("#00743F");break;
            case 12 : color = Color.parseColor("#132226");break;
            case 13 : color = Color.parseColor("#DE8CF0");break;
            case 14 : color = Color.parseColor("#0ABDA0");break;
            case 15 : color = Color.parseColor("#C0334D");break;
            case 16 : color = Color.parseColor("#522E75");break;
        }
        return color;
    }
}
