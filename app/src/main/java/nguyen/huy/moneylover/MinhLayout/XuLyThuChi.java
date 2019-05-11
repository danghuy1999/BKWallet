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
    int sogiaodich;
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

    //Lưu giao dịch mới vào Database
    public void xuLyLuuVaoDatabase(ThuChi giaodich, String[] result, int sogiaodich){
        databaseReference=FirebaseDatabase.getInstance().getReference();
        giaodich.setThuchiID(sogiaodich);
        giaodich.setThuchiKey(databaseReference.child("Thu chi").push().getKey());
        databaseReference.child(user).child("Thu chi").child(result[0]).child("Ngày").child(result[1]).child("Giao dịch").child("Giao dịch "+sogiaodich).setValue(giaodich);
    }

    //Lưu giao dịch mới vào database khi edit

    public void xuLyLuuVaoDatabaseKhiEdit(ThuChi giaodich,String[] result,int sogiaodich,String thuchiKey){
        databaseReference=FirebaseDatabase.getInstance().getReference();
        giaodich.setThuchiID(sogiaodich);
        giaodich.setThuchiKey(thuchiKey);
        databaseReference.child(user).child("Thu chi").child(result[0]).child("Ngày").child(result[1]).child("Giao dịch").child("Giao dịch "+sogiaodich).setValue(giaodich);
    }

    //Cập nhật số giao dịch khi giao dịch mới được thêm vào
    public void setSogiaodich(int sogiaodich, String[] result){
        databaseReference=FirebaseDatabase.getInstance().getReference();
        databaseReference.child(user).child("Thu chi").child(result[0]).child("Ngày").child(result[1]).child("số giao dịch").setValue(sogiaodich);
    }

    //Đọc số giao dịch và thêm giao dịch mới vào với "số" giao dịch phù hợp
    public void readDataseAndSetSoGiaoDich(final ThuChi thuChi, final String[]result){
        //Xu ly luu vao database
        databaseReference=FirebaseDatabase.getInstance().getReference().child(user).child("Thu chi").child(result[0]).child("Ngày").child(result[1]).child("số giao dịch");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue()==null){
                    sogiaodich=1;
                    setSogiaodich(sogiaodich,result);
                    xuLyLuuVaoDatabase(thuChi,result,sogiaodich);
                }
                else {
                    sogiaodich=dataSnapshot.getValue(Integer.class);
                    sogiaodich=sogiaodich+1;
                    setSogiaodich(sogiaodich,result);
                    xuLyLuuVaoDatabase(thuChi,result,sogiaodich);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    //Các hàm xử lý tiền vào tiền ra trong ngày

    //Xử lý tiền vào tiền ra trong ngày khi lưu
    public void xuLyTienVaoTienRaTrongNgayKhiLuu(final String[] result, final ThuChi thuChi){
        databaseReference=FirebaseDatabase.getInstance().getReference().child(user).child("Thu chi").child(result[0]).child("Ngày").child(result[1]);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child("Tiền vào").getValue()==null || dataSnapshot.child("Tiền ra").getValue()==null){
                    if (thuChi.getNhom().equals("Gửi tiền") || thuChi.getNhom().equals("Tiền lãi")) {
                        long tienvao = Long.parseLong(thuChi.getSotien());
                        CapNhatTienVaoTrongNgay(result,tienvao);
                        CapNhatTienRaTrongNgay(result,0);
                    } else if (thuChi.getNhom().equals("Rút tiền")) {
                        long tienra = Long.parseLong(thuChi.getSotien());
                        CapNhatTienRaTrongNgay(result,tienra);
                        CapNhatTienVaoTrongNgay(result,0);
                    }
                }
                else {
                    long tienvao = (long) dataSnapshot.child("Tiền vào").getValue();
                    long tienra = (long) dataSnapshot.child("Tiền ra").getValue();
                    if (thuChi.getNhom().equals("Gửi tiền") || thuChi.getNhom().equals("Tiền lãi")) {
                        tienvao = tienvao + Long.parseLong(thuChi.getSotien());
                        CapNhatTienVaoTrongNgay(result,tienvao);
                    } else if (thuChi.getNhom().equals("Rút tiền")) {
                        tienra = tienra + Long.parseLong(thuChi.getSotien());
                        CapNhatTienRaTrongNgay(result,tienra);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

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


    //Cập nhật lại tiền vào
    public void CapNhatTienVao(String[] result,long tienvao){
        databaseReference=FirebaseDatabase.getInstance().getReference().child(user).child("Thu chi").child(result[0]).child("Tiền vào");
        databaseReference.setValue(tienvao);
    }
    //Cập nhật lại tiền ra
    public void CapNhatTienRa(String[] result,long tienra){
        databaseReference=FirebaseDatabase.getInstance().getReference().child(user).child("Thu chi").child(result[0]).child("Tiền ra");
        databaseReference.setValue(tienra);
    }
    //Cập nhật tiền vào tiền ra khi lưu
    public void xuLyTienVaoRaKhiLuu(final String [] result, final EditText edtChonNhom,final EditText edtNhapSoTien){
        databaseReference=FirebaseDatabase.getInstance().getReference().child(user).child("Thu chi").child(result[0]);
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
                        tienra = tienra + Long.parseLong(edtNhapSoTien.getText().toString());
                        CapNhatTienRa(result, tienra);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    //Các hàm xử lý cho QRCode
    //Lưu khi đọc được giao dịch mới

    //Cập nhật tiền vào tiền ra khi xử lý Qrcode
    public void xuLyTienVaoRaQRCode(final String [] result, final long tienvaoQR,final  long tienraQR){
        databaseReference=FirebaseDatabase.getInstance().getReference().child(user).child("Thu chi").child(result[0]);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child("Tiền vào").getValue()==null || dataSnapshot.child("Tiền ra").getValue()==null){
                        CapNhatTienVao(result, tienvaoQR);

                        CapNhatTienRa(result, tienraQR);

                }
                else {
                    long tienvao = (long) dataSnapshot.child("Tiền vào").getValue();
                    long tienra = (long) dataSnapshot.child("Tiền ra").getValue();
                    tienvao = tienvao + tienvaoQR;
                    CapNhatTienVao(result, tienvao);
                    tienra = tienra + tienraQR;
                    CapNhatTienRa(result, tienra);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    //Cập nhật tiền vào tiền ra khi xóa
    public void xuTienVaoTienRaKhiXoa(final String result[], final ThuChi thuChi) {
        databaseReference=FirebaseDatabase.getInstance().getReference().child(user).child("Thu chi").child(result[0]);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                long tienvao = (long) dataSnapshot.child("Tiền vào").getValue();
                long tienra = (long) dataSnapshot.child("Tiền ra").getValue();
                if (thuChi.getNhom().equals("Gửi tiền") || thuChi.getNhom().equals("Tiền lãi")) {
                    tienvao = tienvao - Long.parseLong(thuChi.getSotien());
                    CapNhatTienVao(result, tienvao);
                } else if (thuChi.getNhom().equals("Rút tiền")) {
                    tienra = tienra - Long.parseLong(thuChi.getSotien());
                    CapNhatTienRa(result, tienra);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    //Cập nhật tiền vào tiển ra khi sửa
    public void xuLyTienVaoTienRaKhiSua(final String[] result,final ThuChi thuChi,final EditText edtChonNhom,final EditText edtNhapSoTien){
        databaseReference=FirebaseDatabase.getInstance().getReference().child(user).child("Thu chi").child(result[0]);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                long tienvao = (long) dataSnapshot.child("Tiền vào").getValue();
                long tienra = (long) dataSnapshot.child("Tiền ra").getValue();
                if(thuChi.getNhom().equals("Gửi tiền") || thuChi.getNhom().equals("Tiền lãi")){
                    tienvao=tienvao-Long.parseLong(thuChi.getSotien());
                }
                if(thuChi.getNhom().equals("Rút tiền")){
                    tienra=tienra-Long.parseLong(thuChi.getSotien());
                }
                if(edtChonNhom.getText().toString().equals("Gửi tiền") || edtChonNhom.getText().toString().equals("Tiền lãi")){
                    tienvao=tienvao+Long.parseLong(edtNhapSoTien.getText().toString());
                }
                if(edtChonNhom.getText().toString().equals("Rút tiền")){
                    tienra=tienra+Long.parseLong(edtNhapSoTien.getText().toString());
                }
                CapNhatTienVao(result,tienvao);
                CapNhatTienRa(result,tienra);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    //Xử lý tiền vào tiền ra khi hàm onChildChange() được kích hoạt
    public void xuLyTienVaoTienRaOnChildChange(final String [] result,final ThuChi thuChiOld,final ThuChi thuChiNew){
        databaseReference=FirebaseDatabase.getInstance().getReference().child(user).child("Thu chi").child(result[0]);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                long tienvao = (long) dataSnapshot.child("Tiền vào").getValue();
                long tienra = (long) dataSnapshot.child("Tiền ra").getValue();
                if(thuChiOld.getNhom().equals("Gửi tiền") || thuChiOld.getNhom().equals("Tiền lãi")){
                    tienvao=tienvao-Long.parseLong(thuChiOld.getSotien());
                }
                if(thuChiOld.getNhom().equals("Rút tiền")){
                    tienra=tienra-Long.parseLong(thuChiOld.getSotien());
                }
                if(thuChiNew.getNhom().equals("Gửi tiền") || thuChiNew.getNhom().equals("Tiền lãi")){
                    tienvao=tienvao+Long.parseLong(thuChiNew.getSotien());
                }
                if(thuChiNew.getNhom().equals("Rút tiền")){
                    tienra=tienra+Long.parseLong(thuChiNew.getSotien());
                }
                CapNhatTienVao(result,tienvao);
                CapNhatTienRa(result,tienra);
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
