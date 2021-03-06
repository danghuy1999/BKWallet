package nguyen.huy.moneylover.Report.Controller;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import nguyen.huy.moneylover.Report.View.FragmentReportIncome;
import nguyen.huy.moneylover.Report.View.FragmentReportOutcome;

public class ReportFragmentAdapter extends FragmentPagerAdapter {
    public ReportFragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        Fragment fragment = null;
        switch (i)
        {
            case 0 : fragment = new FragmentReportIncome();break;
            case 1 : fragment = new FragmentReportOutcome();break;
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        String title = "";
        switch (position)
        {
            case 0 : title = "Khoản thu";break;
            case 1 : title = "Khoản chi";break;
        }
        return title;
    }
}
