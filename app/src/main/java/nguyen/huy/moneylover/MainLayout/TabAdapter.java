package nguyen.huy.moneylover.MainLayout;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class TabAdapter extends FragmentPagerAdapter {
    public TabAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        Fragment fragment = null;
        switch (i)
        {
            case 0 : fragment = new FragmentLastMonth(); break;
            case 1 : fragment = new FragmentThisMonth(); break;
            case 2 : fragment = new FragmentFuture(); break;
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        String title = "";
        switch (position)
        {
            case 0 : title = "Last month"; break;
            case 1 : title = "This month"; break;
            case 2 : title = "Future"; break;
        }
        return title;
    }
}
