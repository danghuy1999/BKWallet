package nguyen.huy.moneylover.MinhLayout;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import nguyen.huy.moneylover.Model.ThuChi;
import nguyen.huy.moneylover.R;

public class ThuChiActivity extends AppCompatActivity{

    EditText edtNhapSoTien,edtThemGhiChu,edtThemBan,edtDatNhacNho,edtChonSuKien;
    public static EditText edtChonNhom;
    public static ImageView imgchonNhom;
    public static EditText edtChonNgay;
    public static EditText edtChonPhuongThuc;
    public static ImageView imgChonPhuongThuc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.minh_activity_thuchi);

        addControls();

        //Khởi tạo ngày mặc định cho mục chọn ngày
        edtChonNgay.setText(XuLyThuChi.simpleDateFormat.format(XuLyThuChi.calendar.getTime()));
    }

    private void addControls() {
        edtNhapSoTien=findViewById(R.id.edtNhapSoTien);
        edtChonNhom=findViewById(R.id.edtChonNhom);
        edtThemGhiChu=findViewById(R.id.edtThemGhiChu);
        edtChonNgay=findViewById(R.id.edtChonNgay);
        edtChonPhuongThuc =findViewById(R.id.edtPhuongThucTT);
        edtThemBan=findViewById(R.id.edtThemBan);
        edtDatNhacNho=findViewById(R.id.edtDatNhacNho);
        edtChonSuKien=findViewById(R.id.edtChonSuKien);
        imgchonNhom=findViewById(R.id.imgChonNhom);
        imgChonPhuongThuc=findViewById(R.id.imgPhuongThucTT);
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
        if(TextUtils.isEmpty(edtChonPhuongThuc.getText().toString())){
            edtChonPhuongThuc.setError("Bạn chưa chọn phương thức thanh toán");
            return;
        }

        String result[]=XuLyChuoiThuChi.chuyenDinhDangNgay(edtChonNgay.getText().toString());
        XuLyThuChi.xuLyLuuVaoDatabase(TaoGiaoDich(),result);
        XuLyDatabaseSupport.SaveToDatabase(TaoGiaoDich());
        //Chuyển hình chọn nhóm về ban đầu
        /*Resources res=getResources();
        Drawable drawable=res.getDrawable(R.drawable.question2);
        imgchonNhom.setImageDrawable(drawable);*/

        finish();
    }

    private ThuChi TaoGiaoDich(){
        String SoTien=edtNhapSoTien.getText().toString();
        String Nhom=edtChonNhom.getText().toString();
        String GhiChu=edtThemGhiChu.getText().toString();
        String Ngay=edtChonNgay.getText().toString();
        String PhuongThuc= edtChonPhuongThuc.getText().toString();
        String Banbe=edtThemBan.getText().toString();
        String NhacNho=edtDatNhacNho.getText().toString();
        String SuKien=edtChonSuKien.getText().toString();
        //Khởi tạo giao dịch mới
        ThuChi giaodich=new ThuChi(SoTien,Nhom,GhiChu,Ngay,PhuongThuc,Banbe,NhacNho,SuKien);
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
        XuLyThuChi.xuLyHienThiNgayEditText(view,edtChonNgay,ThuChiActivity.this);
    }

    public void xuLyChonPhuongThuc(View view) {
        Intent intent=new Intent(ThuChiActivity.this,PhuongThucThanhToanActivity.class);
        startActivity(intent);
    }
}
