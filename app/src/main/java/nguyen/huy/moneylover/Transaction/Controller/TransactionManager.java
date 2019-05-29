package nguyen.huy.moneylover.Transaction.Controller;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
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

import nguyen.huy.moneylover.Transaction.Model.Transaction;

public class TransactionManager {
    public static DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference();
    public static Calendar calendar=Calendar.getInstance();
    public static SimpleDateFormat simpleDateFormat=new SimpleDateFormat("dd/MM/yyyy");
    public static String user=FirebaseAuth.getInstance().getCurrentUser().getUid();
    private long tienvao;
    private long tienra;

    public TransactionManager() {
    }

    //Các hàm về xử lý Database

    //Hàm xét giao dịch thuộc tiền vào hay tiền ra
    //Tiền vào return true, tiền ra return false
    public static boolean checkMoneyIO(Transaction giaodich){
        boolean check=false;
        switch (giaodich.getNhom()){
            case "Gửi tiền": check=true; break;
            case "Tiền lãi": check=true; break;
            case "Được tặng": check=true; break;
            case "Thưởng": check=true; break;
            case "Lương": check=true; break;
            case "Bán đồ": check=true; break;
            case "Khoản thu khác": check=true; break;
        }
        return check;
    }
    public static boolean checkMoneyIOString(String nhom){
        boolean check=false;
        switch (nhom){
            case "Gửi tiền": check=true; break;
            case "Tiền lãi": check=true; break;
            case "Được tặng": check=true; break;
            case "Thưởng": check=true; break;
            case "Lương": check=true; break;
            case "Bán đồ": check=true; break;
            case "Khoản thu khác": check=true; break;
        }
        return check;
    }


    //hỗ trợ kiểm tra null
    public static void supportCheckNull(EditText editText, ImageView imageView, Bitmap bitmap,String string){
        if(editText!=null && imageView!=null){
            editText.setText(string);
            imageView.setImageBitmap(bitmap);
        }
    }

    //Lưu giao dịch mới vào Database
    public static void SaveTransactionToDatabase(Transaction giaodich, String[] result){
        databaseReference=FirebaseDatabase.getInstance().getReference();
        giaodich.setThuchiKey(databaseReference.child(user).child("Thu chi").child(result[0]).child("Ngày").child(result[1]).child("Giao dịch").push().getKey());
        databaseReference.child(user).child("Thu chi").child(result[0]).child("Ngày").child(result[1]).child("Giao dịch").child(giaodich.getThuchiKey()).setValue(giaodich);
    }

    //Lưu giao dịch sau khi sửa vào database

    public static void SaveTransactionToDatabaseEdit(Transaction giaodichOld, Transaction giaodichNew){
        databaseReference=FirebaseDatabase.getInstance().getReference();
        String[] resultOld= DayTimeManager.ConvertFormatDay(giaodichOld.getNgay());
        String[] resultNew= DayTimeManager.ConvertFormatDay(giaodichNew.getNgay());
        databaseReference.child(user).child("Thu chi").child(resultOld[0]).child("Ngày").child(resultOld[1]).child("Giao dịch").child(giaodichOld.getThuchiKey()).removeValue();

        SaveTransactionToDatabase(giaodichNew,resultNew);
    }

    //Các hàm xử lý tiền vào tiền ra trong ngày

    //Cập nhật tiền vào trong ngày
    public static void UpdateMoneyOutcomeInDay(String[] result, long tienvao){
        databaseReference=FirebaseDatabase.getInstance().getReference().child(user).child("Thu chi").child(result[0]).child("Ngày").child(result[1]).child("Tiền vào");
        databaseReference.setValue(tienvao);
    }
    //Cập nhật tiền ra trong ngày
    public static void UpdateMoneyIncomeDay(String[] result, long tienra){
        databaseReference=FirebaseDatabase.getInstance().getReference().child(user).child("Thu chi").child(result[0]).child("Ngày").child(result[1]).child("Tiền ra");
        databaseReference.setValue(tienra);
    }


    //Cập nhật lại tiền vào trong tháng
    public static void UpdateMoneyIncomeMonth(String thang, long tienvao){
        databaseReference=FirebaseDatabase.getInstance().getReference().child(user).child("Thu chi").child(thang).child("Tiền vào");
        databaseReference.setValue(tienvao);
    }
    //Cập nhật lại tiền ra trong tháng
    public static void UpdateMoneyOutcomeMonth(String thang, long tienra){
        databaseReference=FirebaseDatabase.getInstance().getReference().child(user).child("Thu chi").child(thang).child("Tiền ra");
        databaseReference.setValue(tienra);
    }

    //Cập nhật giá trị hiện tại của ví
    public static void setBalance(){
        databaseReference=FirebaseDatabase.getInstance().getReference().child(user).child("Thu chi");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                long tienvao=0;
                long tienra=0;
                for(DataSnapshot snapshot:dataSnapshot.getChildren()) {
                    if(snapshot.child("Tiền vào").getValue()!=null && snapshot.child("Tiền ra").getValue()!=null) {
                        tienvao = tienvao + Long.parseLong(snapshot.child("Tiền vào").getValue().toString());
                        tienra = tienra + Long.parseLong(snapshot.child("Tiền ra").getValue().toString());
                    }
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
    public static void displayDayEditText(View view, final EditText edtChonNgay, Activity activity) {
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
    public static void displayDayTextView(View view, final TextView txtChonNgay, Activity activity) {
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
