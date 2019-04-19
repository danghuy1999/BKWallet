package nguyen.huy.moneylover.MinhLayout;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import nguyen.huy.moneylover.R;

public class ThuChiActivity extends AppCompatActivity {

    EditText edtNhapSoTien,edtThemGhiChu,edtChonNgay,edtChonVi,edtThemBan,edtDatNhacNho,edtChonSuKien;
    public static EditText edtChonNhom;
    public static ImageView imgchonNhom;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.minh_activity_thuchi);
        addControls();
    }

    private void addControls() {
        edtNhapSoTien=findViewById(R.id.edtNhapSoTien);
        edtChonNhom=findViewById(R.id.edtChonNhom);
        edtThemGhiChu=findViewById(R.id.edtThemGhiChu);
        edtChonNgay=findViewById(R.id.edtChonNgay);
        edtChonVi=findViewById(R.id.edtChonVi);
        edtThemBan=findViewById(R.id.edtThemBan);
        edtDatNhacNho=findViewById(R.id.edtDatNhacNho);
        edtChonSuKien=findViewById(R.id.edtChonSuKien);
        imgchonNhom=findViewById(R.id.imgChonNhom);
    }

    public void xuLyLuu(View view) {
        String SoTien=edtNhapSoTien.getText().toString();
        String Nhom=edtChonNhom.getText().toString();
        String GhiChu=edtThemGhiChu.getText().toString();
        String Ngay=edtChonNgay.getText().toString();
        String Vi=edtChonVi.getText().toString();
        String Banbe=edtThemBan.getText().toString();
        String NhacNho=edtDatNhacNho.getText().toString();
        String SuKien=edtChonSuKien.getText().toString();


        /*Xoa trang de nguoi dung nhap tiep*/
        edtNhapSoTien.setText("");
        edtChonNhom.setText("");
        edtThemGhiChu.setText("");
        edtChonNgay.setText("");
        edtChonVi.setText("");
        edtThemBan.setText("");
        edtDatNhacNho.setText("");
        edtChonSuKien.setText("");
        edtNhapSoTien.requestFocus();

        //Chuyển hình chọn nhóm về ban đầu
        Resources res=getResources();
        Drawable drawable=res.getDrawable(R.drawable.question2);
        imgchonNhom.setImageDrawable(drawable);
    }


    public void xuLyChonNhom(View view) {
        Intent intent=new Intent(ThuChiActivity.this,ChonNhomActivity.class);
        startActivity(intent);
    }

    public void xuLyThoat(View view) {
        finish();
    }
}
