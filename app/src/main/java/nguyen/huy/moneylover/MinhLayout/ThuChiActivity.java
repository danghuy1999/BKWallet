package nguyen.huy.moneylover.MinhLayout;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.firebase.database.ChildEventListener;
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

public class ThuChiActivity extends AppCompatActivity{

    EditText edtNhapSoTien,edtThemGhiChu,edtChonVi,edtThemBan,edtDatNhacNho,edtChonSuKien;
    public static EditText edtChonNhom;
    public static ImageView imgchonNhom;
    public static EditText edtChonNgay;
    DatabaseReference databaseReference;
    //Tao data time picker
    public  static Calendar calendar=Calendar.getInstance();
    public static SimpleDateFormat simpleDateFormat=new SimpleDateFormat("dd/MM/yyyy");
    //Số giao dịch trong ngày
    int sogiaodich;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.minh_activity_thuchi);

        addControls();

        //Khởi tạo ngày mặc định cho mục chọn ngày
        edtChonNgay.setText(simpleDateFormat.format(calendar.getTime()));
    }
    //Xử lý đọc số giao dịch và thêm giao dịch
    private void readDatabase() {
        final String[] result=xuLyChuoi();
        //Xu ly luu vao database
        databaseReference=FirebaseDatabase.getInstance().getReference().child("user 1").child("Thu chi").child(result[0]).child("Ngày").child(result[1]).child("số giao dịch");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                xuLySnapshot(dataSnapshot,result);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void xuLySnapshot(DataSnapshot dataSnapshot,String[] result) {
        if(dataSnapshot.getValue()==null){
            sogiaodich=1;
            xuLyTruocKhiLuu(result);
        }
        else {
            sogiaodich=dataSnapshot.getValue(Integer.class);
            sogiaodich=sogiaodich+1;
            xuLyTruocKhiLuu(result);
        }
    }

    //Lưu giao dịch mới vào Database
    private void xuLyLuuVaoDatabase(ThuChi giaodich,String[] result,int sogiaodich){
        databaseReference=FirebaseDatabase.getInstance().getReference();
        databaseReference.child("user 1").child("Thu chi").child(result[0]).child("Ngày").child(result[1]).child("Giao dịch").child("Giao dịch "+sogiaodich).setValue(giaodich);
    }
    //Cập nhật số giao dịch
    private void setSogiaodich(int sogiaodich,String[] result){
        databaseReference=FirebaseDatabase.getInstance().getReference();
        databaseReference.child("user 1").child("Thu chi").child(result[0]).child("Ngày").child(result[1]).child("số giao dịch").setValue(sogiaodich);
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

    private  void xuLyTruocKhiLuu(String[] result){
        setSogiaodich(sogiaodich,result);
        xuLyLuuVaoDatabase(TaoGiaoDich(),result,sogiaodich);
    }
    public void xuLyLuu(View view) {
        readDatabase();

        String[] result=xuLyChuoi();
        xuLyTienVaoRa(result);
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

    //Xử lý tiền vào tiền ra

    private void xuLyTienVaoRa(final String [] result){
        databaseReference=FirebaseDatabase.getInstance().getReference().child("user 1").child("Thu chi").child(result[0]);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child("Tiền vào").getValue()==null || dataSnapshot.child("Tiền ra").getValue()==null){
                    if (edtChonNhom.getText().toString().equals("Gửi tiền") || edtChonNhom.getText().toString().equals("Tiền lãi")) {
                         long tienvao = Long.parseLong(edtNhapSoTien.getText().toString());
                        CapNhatTienVao(result, tienvao);
                    } else if (edtChonNhom.getText().toString().equals("Rút tiền")) {
                        long tienra = Long.parseLong(edtNhapSoTien.getText().toString());
                        CapNhatTienRa(result, tienra);
                    }
                }
                else {
                    long tienvao = (long) dataSnapshot.child("Tiền vào").getValue();
                    long tienra = (long) dataSnapshot.child("Tiền ra").getValue();
                    if (edtChonNhom.getText().toString().equals("Gửi tiền") || edtChonNhom.getText().toString().equals("Tiền lãi")) {
                        tienvao = tienvao + Long.parseLong(edtNhapSoTien.getText().toString());
                        CapNhatTienVao(result, tienvao);
                    } else if (edtChonNhom.getText().toString().equals("Rút tiền")) {
                        tienra = tienra - Long.parseLong(edtNhapSoTien.getText().toString());
                        CapNhatTienRa(result, tienra);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    //Cập nhật lại tiền vào
    private void CapNhatTienVao(String[] result,long tienvao){
        databaseReference=FirebaseDatabase.getInstance().getReference().child("user 1").child("Thu chi").child(result[0]).child("Tiền vào");
        databaseReference.setValue(tienvao);
    }
    //Cập nhật lại tiền re
    private void CapNhatTienRa(String[] result,long tienra){
        databaseReference=FirebaseDatabase.getInstance().getReference().child("user 1").child("Thu chi").child(result[0]).child("Tiền ra");
        databaseReference.setValue(tienra);
    }

}
