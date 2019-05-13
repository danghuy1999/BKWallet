package nguyen.huy.moneylover.MinhLayout;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import nguyen.huy.moneylover.Model.ThuChi;

public class XuLyThuChi {
    private DatabaseReference databaseReference;
    private Calendar calendar;
    private SimpleDateFormat simpleDateFormat;
    private FirebaseAuth firebaseAuth;
    private String user;
    private long tienvao;
    private long tienra;

    public XuLyThuChi() {
        databaseReference= FirebaseDatabase.getInstance().getReference();
        calendar=Calendar.getInstance();
        simpleDateFormat=new SimpleDateFormat("dd/MM/yyyy");
        firebaseAuth=FirebaseAuth.getInstance();
        user=firebaseAuth.getCurrentUser().getUid();
        tienvao=0;
        tienra=0;
    }

    public DatabaseReference getDatabaseReference() {
        return databaseReference;
    }

    public void setDatabaseReference(DatabaseReference databaseReference) {
        this.databaseReference = databaseReference;
    }

    public Calendar getCalendar() {
        return calendar;
    }

    public void setCalendar(Calendar calendar) {
        this.calendar = calendar;
    }

    public SimpleDateFormat getSimpleDateFormat() {
        return simpleDateFormat;
    }

    public void setSimpleDateFormat(SimpleDateFormat simpleDateFormat) {
        this.simpleDateFormat = simpleDateFormat;
    }

    public FirebaseAuth getFirebaseAuth() {
        return firebaseAuth;
    }

    public void setFirebaseAuth(FirebaseAuth firebaseAuth) {
        this.firebaseAuth = firebaseAuth;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public long getTienvao() {
        return tienvao;
    }

    public void setTienvao(long tienvao) {
        this.tienvao = tienvao;
    }

    public long getTienra() {
        return tienra;
    }

    public void setTienra(long tienra) {
        this.tienra = tienra;
    }

    //Các hàm về xử lý Database

    //Hàm xét giao dịch thuộc tiền vào hay tiền ra
    //Tiền vào return true, tiền ra return false
    public static boolean checkMoneyIO(ThuChi giaodich){
        boolean check=false;
        switch (giaodich.getNhom()){
            case "Gửi tiền": check=true; break;
            case "Tiền lãi": check=true; break;
            case "Rút tiền": check=false; break;
        }
        return check;
    }
    //Lưu giao dịch mới vào Database
    public void xuLyLuuVaoDatabase(ThuChi giaodich, String[] result){
        databaseReference=FirebaseDatabase.getInstance().getReference();
        if(checkMoneyIO(giaodich)){
            giaodich.setThuchiKey(databaseReference.child(user).child("Thu chi").child(result[0]).child("Ngày").child(result[1]).child("Giao dịch vào").push().getKey());
            databaseReference.child(user).child("Thu chi").child(result[0]).child("Ngày").child(result[1]).child("Giao dịch vào").child(giaodich.getThuchiKey()).setValue(giaodich);
        }
        else {
            giaodich.setThuchiKey(databaseReference.child(user).child("Thu chi").child(result[0]).child("Ngày").child(result[1]).child("Giao dịch ra").push().getKey());
            databaseReference.child(user).child("Thu chi").child(result[0]).child("Ngày").child(result[1]).child("Giao dịch ra").child(giaodich.getThuchiKey()).setValue(giaodich);
        }
    }

    //Lưu giao dịch sau khi sửa vào database

    public void xuLyLuuVaoDatabaseKhiEdit(ThuChi giaodich,String[] result,String thuchiKey){
        databaseReference=FirebaseDatabase.getInstance().getReference();
        giaodich.setThuchiKey(thuchiKey);
        if(checkMoneyIO(giaodich)){
            databaseReference.child(user).child("Thu chi").child(result[0]).child("Ngày").child(result[1]).child("Giao dịch vào").child(giaodich.getThuchiKey()).setValue(giaodich);
        }
        else {
            databaseReference.child(user).child("Thu chi").child(result[0]).child("Ngày").child(result[1]).child("Giao dịch ra").child(giaodich.getThuchiKey()).setValue(giaodich);
        }
    }

    //Các hàm xử lý tiền vào tiền ra trong ngày

    //Cập nhật tiền vào trong ngày
    public void CapNhatTienVaoTrongNgay(String[] result,long tienvao){
        databaseReference=FirebaseDatabase.getInstance().getReference().child(user).child("Thu chi").child(result[0]).child("Ngày").child(result[1]).child("Tiền vào");
        databaseReference.setValue(tienvao);
    }
    //Cập nhật tiền ra trong ngày
    public void CapNhatTienRaTrongNgay(String[] result,long tienra){
        databaseReference=FirebaseDatabase.getInstance().getReference().child(user).child("Thu chi").child(result[0]).child("Ngày").child(result[1]).child("Tiền ra");
        databaseReference.setValue(tienra);
    }


    //Cập nhật lại tiền vào trong tháng
    public void CapNhatTienVao(String thang,long tienvao){
        databaseReference=FirebaseDatabase.getInstance().getReference().child(user).child("Thu chi").child(thang).child("Tiền vào");
        databaseReference.setValue(tienvao);
    }
    //Cập nhật lại tiền ra trong tháng
    public void CapNhatTienRa(String thang,long tienra){
        databaseReference=FirebaseDatabase.getInstance().getReference().child(user).child("Thu chi").child(thang).child("Tiền ra");
        databaseReference.setValue(tienra);
    }

    //Cập nhật giá trị hiện tại của ví
    public void setBalance(){
        databaseReference=FirebaseDatabase.getInstance().getReference().child(user).child("Thu chi");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                long tienvao=0;
                long tienra=0;
                for(DataSnapshot snapshot:dataSnapshot.getChildren()) {
                    tienvao=tienvao+Long.parseLong(snapshot.child("Tiền vào").getValue().toString());
                    tienra=tienra+Long.parseLong(snapshot.child("Tiền ra").getValue().toString());
                }
                long tiendu=tienvao-tienra;
                databaseReference=FirebaseDatabase.getInstance().getReference().child(user).child("Balance");
                databaseReference.setValue(tiendu);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    //Hiển thị datatime picker và chọn ngày
    public void xuLyHienThiNgayEditText(View view, final EditText edtChonNgay, Activity activity) {
        DatePickerDialog.OnDateSetListener callBack=new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR,year);
                calendar.set(Calendar.MONTH,month);
                calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);
                edtChonNgay.setText(simpleDateFormat.format(calendar.getTime()));
            }
        };
        DatePickerDialog dialog=new DatePickerDialog(activity,callBack,calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH));
        dialog.show();
    }
    public void xuLyHienThiNgayTextView(View view, final TextView txtChonNgay, Activity activity) {
        DatePickerDialog.OnDateSetListener callBack=new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR,year);
                calendar.set(Calendar.MONTH,month);
                calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);
                txtChonNgay.setText(simpleDateFormat.format(calendar.getTime()));
            }
        };
        DatePickerDialog dialog=new DatePickerDialog(activity,callBack,calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH));
        dialog.show();
    }

}
