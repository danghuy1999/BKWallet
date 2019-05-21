package nguyen.huy.moneylover.MinhLayout;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import nguyen.huy.moneylover.MainActivity;
import nguyen.huy.moneylover.Model.ThuChi;
import nguyen.huy.moneylover.R;
import nguyen.huy.moneylover.Tool.GetImage;

public class DocActivity extends AppCompatActivity {
    Intent intent;
    ThuChi thuChi;
    DatabaseReference reference;
    DatabaseReference delReference;
    String ngay;
    String result[];
    TextView txtEditNgay,txtEditVi;
    public static EditText edtEditNhom;
    EditText edtEditSoTien;
    public static ImageView imageViewEditNhom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doc);
        addControls();

        addEvents();

        int[] ngaythangnam=XuLyChuoiThuChi.tachNgayThangNam(thuChi.getNgay());
        XuLyThuChi.calendar.set(Calendar.DAY_OF_MONTH,ngaythangnam[0]);
        XuLyThuChi.calendar.set(Calendar.MONTH,ngaythangnam[1]-1);
        XuLyThuChi.calendar.set(Calendar.YEAR,ngaythangnam[2]);
    }

    private void addControls() {
        edtEditNhom=findViewById(R.id.edtEditNhom);
        txtEditNgay=findViewById(R.id.txtEditNgay);
        txtEditVi=findViewById(R.id.txtEditVi);
        edtEditSoTien=findViewById(R.id.edtEditSoTien);
        imageViewEditNhom=findViewById(R.id.imgViewEditNhom);

        intent = getIntent();
        thuChi = (ThuChi) intent.getSerializableExtra("Item");
        reference = FirebaseDatabase.getInstance().getReference();
        ngay = thuChi.getNgay();
        result=XuLyChuoiThuChi.chuyenDinhDangNgay(ngay);
    }

    private void addEvents() {
        ganThongTin();
    }

    private void ganThongTin() {
        txtEditVi.setText(thuChi.getVi());
        txtEditNgay.setText(thuChi.getNgay());
        edtEditNhom.setText(thuChi.getNhom());
        edtEditSoTien.setText(thuChi.getSotien());

        /*Resources resources=getResources();
        Drawable drawable=resources.getDrawable(R.drawable.question2);
        if(thuChi.getNhom().equals("Rút tiền"))
            drawable=resources.getDrawable(R.drawable.ruttien);
        else if(thuChi.getNhom().equals("Gửi tiền"))
            drawable=resources.getDrawable(R.drawable.guitien);
        else if(thuChi.getNhom().equals("Tiền lãi"))
            drawable=resources.getDrawable(R.drawable.tienlai);
        imageViewEditNhom.setImageDrawable(drawable);*/
        Bitmap bitmap= GetImage.getBitmapFromString(this,thuChi.getNhom());
        imageViewEditNhom.setImageBitmap(bitmap);
    }

    public void xuLyXoaThuChi() {
        reference.child(XuLyThuChi.user).child("Thu chi").child(result[0]).child("Ngày").child(result[1]).child("Giao dịch").child(thuChi.getThuchiKey()).removeValue();
        XuLyDatabaseSupport.DeleteFromDatabase(thuChi);
        finish();
    }

    public void xuLyThoat(View view) {
        finish();
    }

    public void hienThiAlertLogTruocKhiXoa(View view) {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("Xóa giao dịch");
        builder.setMessage("Bạn có chắc chắn muốn xóa?");
        builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                xuLyXoaThuChi();
            }
        });
        builder.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }

    public void xuLyHienThiNgay(View view) {
        XuLyThuChi.xuLyHienThiNgayTextView(view,txtEditNgay,DocActivity.this);
    }

    public void xuLySua(View view) {
        Intent intent=new Intent(DocActivity.this,EditThuChiActivity.class);
        intent.putExtra("Item1",thuChi);
        startActivity(intent);
        finish();
    }

    public void xuLyChonNhom(View view) {
        Intent intent=new Intent(DocActivity.this,ChonNhomActivity.class);
        startActivity(intent);
    }

    public void xuLyLuu(View view) {
        /*reference= FirebaseDatabase.getInstance().getReference().child(XuLyThuChi.user).child("Thu chi").child(result[0]).child("Ngày").child(result[1]).child("Giao dịch").child(thuChi.getThuchiKey());
        reference.child("nhom").setValue(edtEditNhom.getText().toString());
        reference.child("sotien").setValue(edtEditSoTien.getText().toString());
        reference.child("ngay").setValue(txtEditNgay.getText().toString());*/
        XuLyThuChi.xuLyLuuVaoDatabaseKhiEdit(thuChi,CreateTempTrans());
        //XuLyDatabaseSupport.EditToDatabase(thuChi,CreateTempTrans(),DocActivity.this);
        XuLyDatabaseSupport.supportEditToDatabase(thuChi,CreateTempTrans());
        finish();
    }

    private ThuChi CreateTempTrans() {
        String SoTien=edtEditSoTien.getText().toString();
        String Nhom=edtEditNhom.getText().toString();
        String GhiChu=thuChi.getGhichu();
        String Ngay=txtEditNgay.getText().toString();
        String Vi=thuChi.getVi();
        String Banbe=thuChi.getBanbe();
        String NhacNho=thuChi.getNhacnho();
        String SuKien=thuChi.getSukien();
        //Khởi tạo giao dịch mới
        ThuChi giaodich=new ThuChi(SoTien,Nhom,GhiChu,Ngay,Vi,Banbe,NhacNho,SuKien);
        return giaodich;
    }
}
