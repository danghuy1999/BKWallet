package nguyen.huy.moneylover.MainTietKiem;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import nguyen.huy.moneylover.Model.TietKiem;
import nguyen.huy.moneylover.R;

public class SuaVaXoaTietKiem extends AppCompatActivity implements ChildEventListener {

    Button btnSuaTietKiem;
    Button btnXoaTietKiem;
    ArrayList<TietKiem>arrSuaTietKiem;
    AdapterTietKiem adapterSuaTietKiem;
    ListView lvTietKiem;

    EditText edtSuaMucDichTietKiem, edtSuaMucTieuTietKiem, edtSuaTienHienTai;
    TextView tvSuaNgayThangNam;
    Intent intent;
    String TietKiemID;


    Calendar cal;
    SimpleDateFormat sdf1=new SimpleDateFormat("dd/MM/yyyy");

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    public static FirebaseAuth mAuth = FirebaseAuth.getInstance();
    public static String user = mAuth.getCurrentUser().getUid();
    DatabaseReference myRef = database.getReference().child(user).child("Tiết kiệm").child("Đang áp dụng");
    //ArrayList<TietKiem> arrSuaTietKiem= new ArrayList<TietKiem>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hoang_sua_va_xoa_tietkiem);
        addControl();
        addEvent();
        //myRef.addChildEventListener(SuaVaXoaTietKiem.this);
    }

    private void addControl() {
        intent = getIntent();
        TietKiemID = intent.getStringExtra("TietKiemID");
        //okay, h ong child toi cai TietKiemID nhe
        btnSuaTietKiem = findViewById(R.id.btnSuaTietKiem);
        btnXoaTietKiem = findViewById(R.id.btnXoaTietKiem);
        edtSuaMucDichTietKiem = SuaVaXoaTietKiem.this.<EditText>findViewById(R.id.edtSuaMucDichTietKiem);
        edtSuaMucTieuTietKiem = SuaVaXoaTietKiem.this.<EditText>findViewById(R.id.edtSuaMucTieuTietKiem);
        edtSuaTienHienTai = SuaVaXoaTietKiem.this.<EditText>findViewById(R.id.edtSuaTienHienTai);
        cal=Calendar.getInstance();
        tvSuaNgayThangNam = SuaVaXoaTietKiem.this.<TextView>findViewById(R.id.tvSuaNgayThangNam);

    }

    private void addEvent() {
        setDuLieuCu();
        btnXoaTietKiem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(SuaVaXoaTietKiem.this, "Done", Toast.LENGTH_LONG).show();
                myRef.child(TietKiemID).removeValue();
            }
        });

        tvSuaNgayThangNam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                xuLyNgayThangNam();
            }

            private void xuLyNgayThangNam() {
                DatePickerDialog.OnDateSetListener callback1=new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        cal.set(cal.YEAR,year);
                        cal.set(cal.MONTH,month);
                        cal.set(cal.DAY_OF_MONTH,dayOfMonth);
                        tvSuaNgayThangNam.setText(sdf1.format(cal.getTime()));

                    }
                };
                DatePickerDialog datePickerDialog=new DatePickerDialog(SuaVaXoaTietKiem.this,
                        callback1,
                        cal.get(cal.YEAR),
                        cal.get(cal.MONTH),
                        cal.get(cal.DAY_OF_MONTH));
                datePickerDialog.show();
            }
        });

        btnSuaTietKiem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDuLieuCu();
                TietKiem tk =new TietKiem();
                if(TextUtils.isEmpty(edtSuaMucDichTietKiem.getText().toString())) {
                    return;
                }
                tk.setMucDichTietKiem(edtSuaMucDichTietKiem.getText().toString());
                if(TextUtils.isEmpty(edtSuaMucTieuTietKiem.getText().toString())) {
                    return;
                }
                tk.setMucTieuTietKiem(edtSuaMucTieuTietKiem.getText().toString());
                if(TextUtils.isEmpty(edtSuaTienHienTai.getText().toString())) {
                    return;
                }
                tk.setSoTienHienCo(edtSuaTienHienTai.getText().toString());
                tk.setNgayThangNam(tvSuaNgayThangNam.getText().toString());
                arrSuaTietKiem.add(tk);
                /*String key = myRef.push().getKey();
                tk.setTietKiemID(key);*/
                myRef.child(TietKiemID).setValue(tk);

                finish();
            }
        });

    }

    private void setDuLieuCu() {
        myRef.child(TietKiemID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                TietKiem value = dataSnapshot.getValue(TietKiem.class);
                edtSuaMucDichTietKiem.setText(value.getMucDichTietKiem());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        myRef.child(TietKiemID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                TietKiem value = dataSnapshot.getValue(TietKiem.class);
                edtSuaMucTieuTietKiem.setText(value.getMucTieuTietKiem());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        myRef.child(TietKiemID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                TietKiem value = dataSnapshot.getValue(TietKiem.class);
                edtSuaTienHienTai.setText(value.getSoTienHienCo());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        myRef.child(TietKiemID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                TietKiem value = dataSnapshot.getValue(TietKiem.class);
                tvSuaNgayThangNam.setText(value.getNgayThangNam());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        edtSuaMucDichTietKiem.setText(myRef.child(TietKiemID).child("mucDichTietKiem").getRef().toString());
        edtSuaMucTieuTietKiem.setText(myRef.child(TietKiemID).child("mucTieuTietKiem").toString());
        edtSuaTienHienTai.setText(myRef.child(TietKiemID).child("soTienHienTai").toString());
        tvSuaNgayThangNam.setText(myRef.child(TietKiemID).child("ngayThangNam").toString());
    }

    @Override
    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
        TietKiem tk=dataSnapshot.getValue(TietKiem.class);
        arrSuaTietKiem.add(tk);
        if(arrSuaTietKiem.size() > 0)
            adapterSuaTietKiem.notifyDataSetChanged();
    }

    @Override
    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

    }

    @Override
    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

    }

    @Override
    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {

    }
}
