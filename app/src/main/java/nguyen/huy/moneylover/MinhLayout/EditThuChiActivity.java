package nguyen.huy.moneylover.MinhLayout;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import nguyen.huy.moneylover.Model.ThuChi;
import nguyen.huy.moneylover.R;
import nguyen.huy.moneylover.Tool.GetImage;

public class EditThuChiActivity extends AppCompatActivity {

    EditText edtNhapSoTien,edtThemGhiChu ,edtThemBan,edtDatNhacNho,edtChonSuKien;
    public static EditText edtChonNhom;
    public static ImageView imgchonNhom;
    public static EditText edtChonNgay;

    public static EditText edtPhuongThucTT;
    public static ImageView imgPhuongThucTT;
    ThuChi thuChi;
    Intent intent;
    XuLyThuChi xuLyThuChi=new XuLyThuChi();
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
        result=XuLyChuoiThuChi.chuyenDinhDangNgay(thuChi.getNgay());
        khoiTaoHinhChonNhom();
    }

    private void khoiTaoHinhChonNhom() {
        /*Resources resources=getResources();
        Drawable drawable=resources.getDrawable(R.drawable.question2);
        if(edtChonNhom.getText().toString().equals("Rút tiền"))
            drawable=resources.getDrawable(R.drawable.ruttien);
        else if(edtChonNhom.getText().toString().equals("Gửi tiền"))
            drawable=resources.getDrawable(R.drawable.guitien);
        else if(edtChonNhom.getText().toString().equals("Tiền lãi"))
            drawable=resources.getDrawable(R.drawable.tienlai);*/

        Bitmap bitmap= GetImage.getBitmapFromString(this,edtChonNhom.getText().toString());
        imgchonNhom.setImageBitmap(bitmap);
        Bitmap bitmap1=GetImage.getBitmapFromString(this,thuChi.getThanhtoan());
        imgPhuongThucTT.setImageBitmap(bitmap1);
    }

    private void setThuChiToEdit() {
        edtNhapSoTien.setText(thuChi.getSotien());
        edtChonNhom.setText(thuChi.getNhom());
        edtThemGhiChu.setText(thuChi.getGhichu());
        edtChonNgay.setText(thuChi.getNgay());
        edtPhuongThucTT.setText(thuChi.getThanhtoan());
        edtThemBan.setText(thuChi.getBanbe());
        edtDatNhacNho.setText(thuChi.getNhacnho());
        edtChonSuKien.setText(thuChi.getSukien());
    }

    private void addControlls() {
        edtNhapSoTien=findViewById(R.id.edtNhapSoTien);
        edtChonNhom=findViewById(R.id.edtChonNhom);
        edtThemGhiChu=findViewById(R.id.edtThemGhiChu);
        edtChonNgay=findViewById(R.id.edtChonNgay);
        edtPhuongThucTT =findViewById(R.id.edtPhuongThucTTEdit);
        edtThemBan=findViewById(R.id.edtThemBan);
        edtDatNhacNho=findViewById(R.id.edtDatNhacNho);
        edtChonSuKien=findViewById(R.id.edtChonSuKien);
        imgchonNhom=findViewById(R.id.imgChonNhom);
        imgPhuongThucTT=findViewById(R.id.imgPhuongThucTTEdit);
        intent = getIntent();
        thuChi = (ThuChi) intent.getSerializableExtra("Item1");
    }

    private ThuChi TaoGiaoDich(){
        String SoTien=edtNhapSoTien.getText().toString();
        String Nhom=edtChonNhom.getText().toString();
        String GhiChu=edtThemGhiChu.getText().toString();
        String Ngay=edtChonNgay.getText().toString();
        String Vi= edtPhuongThucTT.getText().toString();
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
        xuLyThuChi.xuLyLuuVaoDatabaseKhiEdit(thuChi,TaoGiaoDich());
        //XuLyDatabaseSupport.EditToDatabase(thuChi,TaoGiaoDich(),EditThuChiActivity.this);
        XuLyDatabaseSupport.supportEditToDatabase(thuChi,TaoGiaoDich());
        /*AlertDialog.Builder dialog;
        XuLyDatabaseSupport.DeleteFromDatabase(thuChi);
        dialog=new AlertDialog.Builder(EditThuChiActivity.this);
        dialog.setTitle("Sửa");
        dialog.setMessage("Sửa thành công");
        dialog.setCancelable(false);
        dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alertDialog=dialog.create();
        alertDialog.show();

        XuLyDatabaseSupport.SaveToDatabase(TaoGiaoDich());*/
        finish();
    }

    public void xuLyChonNhom(View view) {
        Intent intent=new Intent(EditThuChiActivity.this,ChonNhomActivity.class);
        startActivity(intent);
    }

    public void xuLyHienThiNgay(View view) {
        xuLyThuChi.xuLyHienThiNgayEditText(view,edtChonNgay,EditThuChiActivity.this);
    }

    public void xuLyChonPhuongThucEdit(View view) {
        Intent intent=new Intent(EditThuChiActivity.this,PhuongThucThanhToanActivity.class);
        startActivity(intent);
    }
}
