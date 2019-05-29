package nguyen.huy.moneylover.Transaction.View;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import nguyen.huy.moneylover.Transaction.Adapter.MyAdapter;
import nguyen.huy.moneylover.R;

public class SelectGroupActivity extends AppCompatActivity {

    Resources res;
    Drawable drawable;
    private ViewPager ViewPChonNhom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.minh_activity_chon_nhom);
        addControls();
    }


    private void addControls() {
        ViewPChonNhom=findViewById(R.id.ViewPChonNhom);
        ViewPChonNhom.setAdapter(new MyAdapter(getSupportFragmentManager()));
        TabLayout tabChonNhom=findViewById(R.id.tabChonNhom);
        tabChonNhom.setupWithViewPager(ViewPChonNhom);
    }

    public void xuLyThoat(View view) {
        finish();
    }

}
