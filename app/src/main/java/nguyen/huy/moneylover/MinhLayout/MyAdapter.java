package nguyen.huy.moneylover.MinhLayout;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class MyAdapter extends FragmentStatePagerAdapter {
    private String listTab[]={"KHOẢN THU","KHOẢN CHI"};
    private KhoanThuFragment khoanThuFragment;
    private KhoanChiFragment khoanChiFragment;

    public MyAdapter(FragmentManager fm) {
        super(fm);
        khoanThuFragment=new KhoanThuFragment();
        khoanChiFragment=new KhoanChiFragment();
    }

    @Override
    public Fragment getItem(int i) {
        if(i==0){
            return khoanThuFragment;
        }
        else if(i==1){
            return khoanChiFragment;
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
