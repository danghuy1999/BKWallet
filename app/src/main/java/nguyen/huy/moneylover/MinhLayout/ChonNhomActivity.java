package nguyen.huy.moneylover.MinhLayout;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import nguyen.huy.moneylover.Model.ThuChi;
import nguyen.huy.moneylover.R;

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

    public void xuLyLuuKhoanChi(View view) {
        if(ThuChiActivity.edtChonNhom!=null && ThuChiActivity.imgchonNhom!=null) {
            ThuChiActivity.edtChonNhom.setText("Rút tiền");
            res=getResources();
            drawable = res.getDrawable(R.drawable.ruttien);
            ThuChiActivity.imgchonNhom.setImageDrawable(drawable);
        }
        if(EditThuChiActivity.edtChonNhom!=null && EditThuChiActivity.imgchonNhom!=null){
            EditThuChiActivity.edtChonNhom.setText("Rút tiền");
            res=getResources();
            drawable=res.getDrawable(R.drawable.ruttien);
            EditThuChiActivity.imgchonNhom.setImageDrawable(drawable);
        }
        if(DocActivity.edtEditNhom!=null && DocActivity.imageViewEditNhom!=null){
            DocActivity.edtEditNhom.setText("Rút tiền");
            res=getResources();
            drawable=res.getDrawable(R.drawable.ruttien);
            DocActivity.imageViewEditNhom.setImageDrawable(drawable);
        }
        finish();
    }

    public void xuLyKhoanThuGuiTien(View view) {
        if(ThuChiActivity.edtChonNhom!=null && ThuChiActivity.imgchonNhom!=null){
            ThuChiActivity.edtChonNhom.setText("Gửi tiền");
            res=getResources();
            drawable = res.getDrawable(R.drawable.guitien);
            ThuChiActivity.imgchonNhom.setImageDrawable(drawable);
        }
        if(EditThuChiActivity.edtChonNhom!=null && EditThuChiActivity.imgchonNhom!=null){
            EditThuChiActivity.edtChonNhom.setText("Gửi tiền");
            res=getResources();
            drawable=res.getDrawable(R.drawable.guitien);
            EditThuChiActivity.imgchonNhom.setImageDrawable(drawable);
        }
        if(DocActivity.edtEditNhom!=null && DocActivity.imageViewEditNhom!=null){
            DocActivity.edtEditNhom.setText("Gửi tiền");
            res=getResources();
            drawable=res.getDrawable(R.drawable.guitien);
            DocActivity.imageViewEditNhom.setImageDrawable(drawable);
        }
        finish();
    }

    public void xuLyKhoanThuTienLai(View view) {
        if(ThuChiActivity.edtChonNhom!=null && ThuChiActivity.imgchonNhom !=null) {
            ThuChiActivity.edtChonNhom.setText("Tiền lãi");
            res=getResources();
            drawable = res.getDrawable(R.drawable.tienlai);
            ThuChiActivity.imgchonNhom.setImageDrawable(drawable);
        }
        if(EditThuChiActivity.edtChonNhom!=null && EditThuChiActivity.imgchonNhom !=null){
            EditThuChiActivity.edtChonNhom.setText("Tiền lãi");
            res=getResources();
            drawable=res.getDrawable(R.drawable.tienlai);
            EditThuChiActivity.imgchonNhom.setImageDrawable(drawable);
        }
        if(DocActivity.edtEditNhom!=null && DocActivity.imageViewEditNhom!=null){
            DocActivity.edtEditNhom.setText("Tiền lãi");
            res=getResources();
            drawable=res.getDrawable(R.drawable.tienlai);
            DocActivity.imageViewEditNhom.setImageDrawable(drawable);
        }
        finish();
    }
}
