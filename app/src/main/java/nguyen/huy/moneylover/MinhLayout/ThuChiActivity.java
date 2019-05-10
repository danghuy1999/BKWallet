package nguyen.huy.moneylover.MinhLayout;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import nguyen.huy.moneylover.Model.ThuChi;
import nguyen.huy.moneylover.R;

public class ThuChiActivity extends AppCompatActivity{

    EditText edtNhapSoTien,edtThemGhiChu,edtChonVi,edtThemBan,edtDatNhacNho,edtChonSuKien;
    public static EditText edtChonNhom;
    public static ImageView imgchonNhom;
    public static EditText edtChonNgay;

    XuLyThuChi xuLyThuChi=new XuLyThuChi();
    XuLyChuoiThuChi xuLyChuoiThuChi=new XuLyChuoiThuChi();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.minh_activity_thuchi);

        addControls();

        //Khởi tạo ngày mặc định cho mục chọn ngày
        edtChonNgay.setText(xuLyThuChi.getSimpleDateFormat().format(xuLyThuChi.getCalendar().getTime()));
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
        if(TextUtils.isEmpty(edtNhapSoTien.getText().toString())){
            edtNhapSoTien.setError("Bạn chưa nhập số tiền");
            return;
        }
        if(TextUtils.isEmpty(edtChonNhom.getText().toString())){
            Toast.makeText(getApplicationContext(),"Bạn chưa chọn nhóm",Toast.LENGTH_LONG).show();
            return;
        }
        if(TextUtils.isEmpty(edtChonVi.getText().toString())){
            edtChonVi.setError("Bạn chưa chọn ví");
            return;
        }

        String result[]=xuLyChuoiThuChi.chuyenDinhDangNgay(edtChonNgay.getText().toString());
        xuLyThuChi.readDataseAndSetSoGiaoDich(TaoGiaoDich(),result);
        xuLyThuChi.xuLyTienVaoRaKhiLuu(result,edtChonNhom,edtNhapSoTien);
        //Chuyển hình chọn nhóm về ban đầu
        Resources res=getResources();
        Drawable drawable=res.getDrawable(R.drawable.question2);
        imgchonNhom.setImageDrawable(drawable);

        finish();
    }

    private ThuChi TaoGiaoDich(){
        String SoTien=edtNhapSoTien.getText().toString();
        String Nhom=edtChonNhom.getText().toString();
        String GhiChu=edtThemGhiChu.getText().toString();
        String Ngay=edtChonNgay.getText().toString();
        String Vi=edtChonVi.getText().toString();
        String Banbe=edtThemBan.getText().toString();
        String NhacNho=edtDatNhacNho.getText().toString();
        String SuKien=edtChonSuKien.getText().toString();
        //Khởi tạo giao dịch mới
        ThuChi giaodich=new ThuChi(SoTien,Nhom,GhiChu,Ngay,Vi,Banbe,NhacNho,SuKien);
        return giaodich;
    }


    public void xuLyChonNhom(View view) {
        Intent intent=new Intent(ThuChiActivity.this,ChonNhomActivity.class);
        startActivity(intent);
    }

    public void xuLyThoat(View view) {
        finish();
    }

    public void xuLyHienThiNgay(View view) {
        xuLyThuChi.xuLyHienThiNgayEditText(view,edtChonNgay,ThuChiActivity.this);
    }

}
