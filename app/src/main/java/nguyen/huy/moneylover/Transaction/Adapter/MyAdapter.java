package nguyen.huy.moneylover.Transaction.Adapter;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import nguyen.huy.moneylover.Transaction.View.OutComeFragment;
import nguyen.huy.moneylover.Transaction.View.InComeFragment;

public class MyAdapter extends FragmentStatePagerAdapter {
    private String listTab[]={"KHOẢN THU","KHOẢN CHI"};
    private InComeFragment inComeFragment;
    private OutComeFragment outComeFragment;

    public MyAdapter(FragmentManager fm) {
        super(fm);
        inComeFragment =new InComeFragment();
        outComeFragment =new OutComeFragment();
    }

    @Override
    public Fragment getItem(int i) {
        if(i==0){
            return inComeFragment;
        }
        else if(i==1){
            return outComeFragment;
        }
        return null;
    }

    @Override
    public int getCount() {
        return listTab.length;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return listTab[position];
    }
}
