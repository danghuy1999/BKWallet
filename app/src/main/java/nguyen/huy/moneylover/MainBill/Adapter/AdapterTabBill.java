package nguyen.huy.moneylover.MainBill.Adapter;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import nguyen.huy.moneylover.MainBill.Fragment.FragmentApplying;
import nguyen.huy.moneylover.MainBill.Fragment.FragmentEnded;

public class AdapterTabBill extends FragmentPagerAdapter {

    public AdapterTabBill(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        Fragment fragment = null;
        switch (i)
        {
            case 0 : fragment = new FragmentApplying(); break;
            case 1 : fragment = new FragmentEnded(); break;
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
            case 0 : title = "Sắp thanh toán"; break;
            case 1 : title = "Đã thanh toán"; break;
        }
        return title;
    }
}
