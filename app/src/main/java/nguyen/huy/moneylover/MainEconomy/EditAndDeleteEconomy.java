package nguyen.huy.moneylover.MainEconomy;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import nguyen.huy.moneylover.Model.TietKiem;
import nguyen.huy.moneylover.R;

public class EditAndDeleteEconomy extends AppCompatActivity {
    ArrayList<TietKiem> arrSuaTietKiem;
    AdapterEconomyApplying adapterSuaTietKiem;
    Toolbar toolbarSuaVaXoaTietKiem;

    EditText edtSuaMucDichTietKiem, edtSuaMucTieuTietKiem, edtSuaTienHienTai;
    TextView tvSuaNgayThangNam;
    Intent intent;
    String TietKiemID;
    TietKiem tk;


    Calendar cal;
    SimpleDateFormat sdf1=new SimpleDateFormat("dd/MM/yyyy");

    FirebaseDatabase database;
    public static FirebaseAuth mAuth;
    public static String user;
    DatabaseReference myRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hoang_edit_and_delete_economy);

        addControl();
        addEvent();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_tietkiem,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.saveTietKiem:{
                toProcessEditTietKiem();
                return true;
            }

            case R.id.deleteTietKiem:{
                toProcessDeleteTietKiem();
                return true;
            }

            case android.R.id.home: {
                onBackPressed();
                return true;
            }

            default:break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void toProcessDeleteTietKiem() {
        showAlbertDialogDelete();
    }

    private void showAlbertDialogDelete() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Xóa Tiết Kiệm");
        builder.setMessage("Bạn có chắc chắn muốn xóa tiết kiệm không?");
        builder.setCancelable(false);
        builder.setPositiveButton("Chắc chắn :( ", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                myRef.child(user).child("Tiết kiệm").child("Đang áp dụng").child(TietKiemID).removeValue();
                Intent intent=new Intent(EditAndDeleteEconomy.this, MainEconomy.class);
                startActivity(intent);
                finish();
            }
        });
        builder.setNegativeButton("Suy nghĩ lại :)", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void toProcessEditTietKiem() {
        setDuLieuCu();
        if(TextUtils.isEmpty(edtSuaMucDichTietKiem.getText().toString())) {
            edtSuaMucDichTietKiem.setError("Nhập lại mục đích tiết kiệm");
            return;
        }
        tk.setMucDichTietKiem(edtSuaMucDichTietKiem.getText().toString());
        if(TextUtils.isEmpty(edtSuaMucTieuTietKiem.getText().toString())) {
            edtSuaMucTieuTietKiem.setError("Nhập lại mục tiêu tiết kiệm");
            return;
        }
        tk.setMucTieuTietKiem(edtSuaMucTieuTietKiem.getText().toString());
        if(TextUtils.isEmpty(edtSuaTienHienTai.getText().toString())) {
            edtSuaTienHienTai.setError("Nhập lại số tiền hiện có");
            return;
        }
        if(Long.parseLong(edtSuaTienHienTai.getText().toString()) < Long.parseLong(edtSuaMucTieuTietKiem.getText().toString()))
        {
            tk.setSoTienHienCo(edtSuaTienHienTai.getText().toString());
            tk.setNgayThangNam(tvSuaNgayThangNam.getText().toString());
            tk.setTietKiemID(TietKiemID);
            myRef.child(TietKiemID).setValue(tk);
            if (checkTimeUp()) {
                myRef = myRef.child(user).child("Tiết kiệm").child("Đang áp dụng");
                tk.setTietKiemID(TietKiemID);
                myRef.child(TietKiemID).setValue(tk);
            }
            else {
                myRef.child(user).child("Tiết kiệm").child("Đang áp dụng").child(TietKiemID).removeValue();
                myRef = myRef.child(user).child("Tiết kiệm").child("Đã kết thúc");
                String key = myRef.push().getKey();
                tk.setTietKiemID(key);
                myRef.child(key).setValue(tk);
            }

            Intent intent=new Intent(EditAndDeleteEconomy.this, MainEconomy.class);
            startActivity(intent);
            finish();
        }
        else{
            edtSuaTienHienTai.setError("Nhập lại số tiền hiện có ");
        }
    }

    private boolean checkTimeUp() {
        Date date, dateTietKiem;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        Calendar calendar = Calendar.getInstance();
        String timeNow = simpleDateFormat.format(calendar.getTime());
        date = null;
        try {
            date = simpleDateFormat.parse(timeNow);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        try {
            dateTietKiem = simpleDateFormat.parse(tk.getNgayThangNam());
            if((dateTietKiem.getTime() - date.getTime()) >= 0) return true;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }


    private void addControl() {
        intent = getIntent();
        TietKiemID = intent.getStringExtra("TietKiemID");
        tk = new TietKiem();
        edtSuaMucDichTietKiem = EditAndDeleteEconomy.this.<EditText>findViewById(R.id.edtSuaMucDichTietKiem);
        edtSuaMucTieuTietKiem = EditAndDeleteEconomy.this.<EditText>findViewById(R.id.edtSuaMucTieuTietKiem);
        edtSuaTienHienTai = EditAndDeleteEconomy.this.<EditText>findViewById(R.id.edtSuaTienHienTai);
        cal=Calendar.getInstance();
        tvSuaNgayThangNam = EditAndDeleteEconomy.this.<TextView>findViewById(R.id.tvSuaNgayThangNam);
        database=FirebaseDatabase.getInstance();
        mAuth=FirebaseAuth.getInstance();
        user=mAuth.getCurrentUser().getUid();
        myRef=database.getReference();
        toolbarSuaVaXoaTietKiem = findViewById(R.id.toolbarSuaVaXoaTietKiem);
        setSupportActionBar(toolbarSuaVaXoaTietKiem);

        ActionBar actionBarEditTietKiem=getSupportActionBar();
        actionBarEditTietKiem.setTitle("Sửa Và Xóa Tiết Kiệm");
        actionBarEditTietKiem.setDisplayHomeAsUpEnabled(true);
    }

    private void addEvent() {
        setDuLieuCu();
        tvSuaNgayThangNam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                xuLyNgayThangNam();
            }
        });
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
        DatePickerDialog datePickerDialog=new DatePickerDialog(EditAndDeleteEconomy.this,
                callback1,
                cal.get(cal.YEAR),
                cal.get(cal.MONTH),
                cal.get(cal.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    private void setDuLieuCu() {
        myRef.child(user).child("Tiết kiệm").child("Đang áp dụng").child(TietKiemID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                TietKiem value = dataSnapshot.getValue(TietKiem.class);
                edtSuaMucDichTietKiem.setText(value.getMucDichTietKiem());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        myRef.child(user).child("Tiết kiệm").child("Đang áp dụng").child(TietKiemID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                TietKiem value = dataSnapshot.getValue(TietKiem.class);
                edtSuaMucTieuTietKiem.setText(value.getMucTieuTietKiem());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        myRef.child(user).child("Tiết kiệm").child("Đang áp dụng").child(TietKiemID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                TietKiem value = dataSnapshot.getValue(TietKiem.class);
                edtSuaTienHienTai.setText(value.getSoTienHienCo());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        myRef.child(user).child("Tiết kiệm").child("Đang áp dụng").child(TietKiemID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                TietKiem value = dataSnapshot.getValue(TietKiem.class);
                tvSuaNgayThangNam.setText(value.getNgayThangNam());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
}
