package nguyen.huy.moneylover.MinhLayout;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.firebase.database.FirebaseDatabase;

import nguyen.huy.moneylover.MainActivity;
import nguyen.huy.moneylover.Model.ThuChi;
import nguyen.huy.moneylover.R;

public class EditThuChiActivity extends AppCompatActivity {

    EditText edtNhapSoTien,edtThemGhiChu,edtChonVi,edtThemBan,edtDatNhacNho,edtChonSuKien;
    public static EditText edtChonNhom;
    public static ImageView imgchonNhom;
    public static EditText edtChonNgay;
    ThuChi thuChi;
    Intent intent;
    XuLyThuChi xuLyThuChi=new XuLyThuChi();
    XuLyChuoiThuChi xuLyChuoiThuChi=new XuLyChuoiThuChi();
    String[] result;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_thu_chi);
        addControlls();
        addEvents();
    }

    private void addEvents() {
        setThuChiToEdit();
        result=xuLyChuoiThuChi.chuyenDinhDangNgay(thuChi.getNgay());
        khoiTaoHinhChonNhom();
    }

    private void khoiTaoHinhChonNhom() {
        Resources resources=getResources();
        Drawable drawable=resources.getDrawable(R.drawable.question2);
        if(edtChonNhom.getText().toString().equals("Rút tiền"))
            drawable=resources.getDrawable(R.drawable.ruttien);
        else if(edtChonNhom.getText().toString().equals("Gửi tiền"))
            drawable=resources.getDrawable(R.drawable.guitien);
        else if(edtChonNhom.getText().toString().equals("Tiền lãi"))
            drawable=resources.getDrawable(R.drawable.tienlai);
        imgchonNhom.setImageDrawable(drawable);
    }

    private void setThuChiToEdit() {
        edtNhapSoTien.setText(thuChi.getSotien());
        edtChonNhom.setText(thuChi.getNhom());
        edtThemGhiChu.setText(thuChi.getGhichu());
        edtChonNgay.setText(thuChi.getNgay());
        edtChonVi.setText(thuChi.getVi());
        edtThemBan.setText(thuChi.getBanbe());
        edtDatNhacNho.setText(thuChi.getNhacnho());
        edtChonSuKien.setText(thuChi.getSukien());
    }

    private void addControlls() {
        edtNhapSoTien=findViewById(R.id.edtNhapSoTien);
        edtChonNhom=findViewById(R.id.edtChonNhom);
        edtThemGhiChu=findViewById(R.id.edtThemGhiChu);
        edtChonNgay=findViewById(R.id.edtChonNgay);
        edtChonVi=findViewById(R.id.edtChonVi);
        edtThemBan=findViewById(R.id.edtThemBan);
        edtDatNhacNho=findViewById(R.id.edtDatNhacNho);
        edtChonSuKien=findViewById(R.id.edtChonSuKien);
        imgchonNhom=findViewById(R.id.imgChonNhom);
        intent = getIntent();
        thuChi = (ThuChi) intent.getSerializableExtra("Item1");
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

    public void xuLyThoat(View view) {
        finish();
    }

    public void xuLyLuu(View view) {
        xuLyThuChi.xuLyLuuVaoDatabaseKhiEdit(TaoGiaoDich(),result,thuChi.getThuchiID(),thuChi.getThuchiKey());
        //xuLyThuChi.getDatabaseReference().child(xuLyThuChi.getUser()).child("Thu chi").child(result[0]).child("Ngày").child(result[1]).child("Giao dịch").child("Giao dịch "+thuChi.getThuchiID()).setValue(TaoGiaoDich());
        //xuLyThuChi.xuTienVaoTienRaKhiXoa(result,thuChi);
        //xuLyThuChi.readDataseAndSetSoGiaoDich(TaoGiaoDich(),result);
        //xuLyThuChi.xuLyTienVaoRaKhiLuu(result,edtChonNhom,edtNhapSoTien);
        //xuLyThuChi.xuLyTienVaoTienRaKhiSua(result,thuChi,edtChonNhom,edtNhapSoTien);
        //Intent intent1=new Intent(EditThuChiActivity.this, MainActivity.class);
        //startActivity(intent1);
        finish();
    }

    public void xuLyChonNhom(View view) {
        Intent intent=new Intent(EditThuChiActivity.this,ChonNhomActivity.class);
        startActivity(intent);
    }

    public void xuLyHienThiNgay(View view) {
        xuLyThuChi.xuLyHienThiNgayEditText(view,edtChonNgay,EditThuChiActivity.this);
    }
}
