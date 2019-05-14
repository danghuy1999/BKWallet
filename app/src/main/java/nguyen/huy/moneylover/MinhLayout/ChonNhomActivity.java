package nguyen.huy.moneylover.MinhLayout;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;

import nguyen.huy.moneylover.Model.ThuChi;
import nguyen.huy.moneylover.R;
import nguyen.huy.moneylover.Tool.GetImage;

public class ChonNhomActivity extends AppCompatActivity {

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
