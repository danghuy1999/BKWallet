package nguyen.huy.moneylover.MinhLayout;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import nguyen.huy.moneylover.R;

public class ChonNhomActivity extends AppCompatActivity {

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

    public void xuLyLuuKhoanChi(View view) {
        ThuChiActivity.edtChonNhom.setText("Rút tiền");
        Resources res=getResources();
        Drawable drawable=res.getDrawable(R.drawable.ruttien);
        ThuChiActivity.imgchonNhom.setImageDrawable(drawable);
        finish();
    }

    public void xuLyKhoanThuGuiTien(View view) {
        ThuChiActivity.edtChonNhom.setText("Gửi tiền");
        Resources res=getResources();
        Drawable drawable=res.getDrawable(R.drawable.guitien);
        ThuChiActivity.imgchonNhom.setImageDrawable(drawable);
        finish();
    }

    public void xuLyKhoanThuTienLai(View view) {
        ThuChiActivity.edtChonNhom.setText("Tiền lãi");
        Resources res=getResources();
        Drawable drawable=res.getDrawable(R.drawable.tienlai);
        ThuChiActivity.imgchonNhom.setImageDrawable(drawable);
        finish();
    }
}
