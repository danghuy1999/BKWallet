package nguyen.huy.moneylover.MainBill;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class TabSelectGroup extends FragmentPagerAdapter {

    public TabSelectGroup(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        Fragment fragment = null;
        switch (i)
        {
            case 0 : fragment = new FragmentLoan(); break;
            case 1 : fragment = new FragmentPay(); break;
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int i){
        String title = "";
        switch (i)
        {
            case 0 : title = "Cho vay"; break;
            case 1 : title = "Khoản chi"; break;
        }
        return title;
    }
}
