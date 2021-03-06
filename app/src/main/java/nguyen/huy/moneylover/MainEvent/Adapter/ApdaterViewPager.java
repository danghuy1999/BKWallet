package nguyen.huy.moneylover.MainEvent.Adapter;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import nguyen.huy.moneylover.MainEvent.Fragment.FragmentDaKetThuc;
import nguyen.huy.moneylover.MainEvent.Fragment.FragmentDangApDung;

public class ApdaterViewPager extends FragmentPagerAdapter {

    public ApdaterViewPager(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        Fragment fragment = null;
        switch (i)
        {
            case 0 : fragment = new FragmentDangApDung(); break;
            case 1 : fragment = new FragmentDaKetThuc(); break;
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
            case 0 : title = "Đang áp dụng"; break;
            case 1 : title = "Đã kết thúc"; break;
        }
        return title;
    }
}
