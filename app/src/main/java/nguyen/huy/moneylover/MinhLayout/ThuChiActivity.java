package nguyen.huy.moneylover.MinhLayout;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import nguyen.huy.moneylover.MainActivity;
import nguyen.huy.moneylover.R;

public class ThuChiActivity extends AppCompatActivity {

    EditText edtNhapSoTien,edtThemGhiChu,edtChonNgay,edtChonVi,edtThemBan,edtDatNhacNho,edtChonSuKien;
    public static EditText edtChonNhom;
    public static ImageView imgchonNhom;
    //Tao data time picker
    Calendar calendar=Calendar.getInstance();
    SimpleDateFormat simpleDateFormat=new SimpleDateFormat("dd/MM/yyyy");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.minh_activity_thuchi);

        addControls();

        //Khởi tạo ngày mặc định cho mục chọn ngày
        edtChonNgay.setText(simpleDateFormat.format(calendar.getTime()));
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
        //Khoi tao ngay moi
        classThuChi ngay1=new classThuChi(SoTien,Nhom,GhiChu,Vi,Banbe,NhacNho,SuKien);
        //Lấy ra tháng từ trong chuỗi
        String[] result=xuLyChuoi();
        //Xu ly luu vao database
        MainActivity.databaseReference.child("user 1").child("Thu chi").child(result[0]).child(result[1]).child("Mục 1").setValue(ngay1);

        //Chuyển hình chọn nhóm về ban đầu
        Resources res=getResources();
        Drawable drawable=res.getDrawable(R.drawable.question2);
        imgchonNhom.setImageDrawable(drawable);

        finish();
    }

    private String[]  xuLyChuoi(){
        String chuoi=edtChonNgay.getText().toString();
        String[] words=chuoi.split("[/]");
        String[] result=new String[2];
        result[0]=words[1]+ "+" + words[2];
        result[1]= words[0]+ "+" +words[1]+ "+" + words[2];
        return result;
    }

    public void xuLyChonNhom(View view) {
        Intent intent=new Intent(ThuChiActivity.this,ChonNhomActivity.class);
        startActivity(intent);
    }

    public void xuLyThoat(View view) {
        finish();
    }

    public void xuLyHienThiNgay(View view) {
        DatePickerDialog.OnDateSetListener callBack=new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR,year);
                calendar.set(Calendar.MONTH,month);
                calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);
                edtChonNgay.setText(simpleDateFormat.format(calendar.getTime()));
            }
        };
        DatePickerDialog dialog=new DatePickerDialog(ThuChiActivity.this,callBack,calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH));
        dialog.show();
    }
}
