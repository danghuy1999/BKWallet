package nguyen.huy.moneylover.MainTietKiem;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import nguyen.huy.moneylover.Model.tietKiem;
import nguyen.huy.moneylover.R;

public class ThemTietKiem extends AppCompatActivity {
    EditText edtMucDichTietKiem, edtMucTieuTietKiem, edtTienHienTai;
    TextView tvNgayThangNam;

    Calendar cal;
    SimpleDateFormat sdf1=new SimpleDateFormat("dd/MM/yyyy");

    Button btnLuuTietKiem;

    public static FirebaseAuth mAuth=FirebaseAuth.getInstance();
    public static String user=mAuth.getCurrentUser().getUid();


    FirebaseDatabase database=FirebaseDatabase.getInstance();
    DatabaseReference myRef=database.getReference();

    ArrayList<tietKiem> arrTietKiem= new ArrayList<tietKiem>();

    static int dem=-1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hoang_them_tietkiem);

        ActionBar actionBar=getSupportActionBar();
        actionBar.setTitle("Tiết Kiệm");
        actionBar.setDisplayHomeAsUpEnabled(true);

        addControls();
        addEvents();
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case android.R.id.home:
                onBackPressed();
                return true;

            default:break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void addEvents() {

        tvNgayThangNam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                xuLyNgayThangNam();
            }
        });

        btnLuuTietKiem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                xuLyLuuTietKiem();
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
                tvNgayThangNam.setText(sdf1.format(cal.getTime()));

            }
        };

        DatePickerDialog datePickerDialog=new DatePickerDialog(ThemTietKiem.this,
                callback1,
                cal.get(cal.YEAR),
                cal.get(cal.MONTH),
                cal.get(cal.DAY_OF_MONTH));

        datePickerDialog.show();
    }

    private void xuLyLuuTietKiem() {

        tietKiem tk =new tietKiem();
        if(TextUtils.isEmpty(edtMucDichTietKiem.getText().toString())) {
            Toast.makeText(this, "Vui Lòng Nhập Mục Đích Tiết Kiệm Lại ", Toast.LENGTH_SHORT).show();
            return;
        }
        tk.setMucDichTietKiem(edtMucDichTietKiem.getText().toString());
        if(TextUtils.isEmpty(edtMucTieuTietKiem.getText().toString())) {
            Toast.makeText(this, "Vui Lòng Nhập Mục Tiêu Tiết Kiệm Lại ", Toast.LENGTH_SHORT).show();
            return;
        }
        tk.setMucTieuTietKiem(edtMucTieuTietKiem.getText().toString());
        if(TextUtils.isEmpty(edtTienHienTai.getText().toString())) {
            Toast.makeText(this, "Vui Lòng Nhập Số Tiền Hiện Tại Lại", Toast.LENGTH_SHORT).show();
            return;
        }
        tk.setSoTienHienCo(edtTienHienTai.getText().toString());
        tk.setNgayThangNam(tvNgayThangNam.getText().toString());
        arrTietKiem.add(tk);
        myRef = myRef.child(user).child("Tiết kiệm").child("Đang áp dụng");
        String key = myRef.push().getKey();
        tk.setTietKiemID(key);
        myRef.child(key).setValue(tk);

        finish();

    }



    private void addControls() {

        edtMucDichTietKiem= this.<EditText>findViewById(R.id.edtMucDichTietKiem);
        edtMucTieuTietKiem= this.<EditText>findViewById(R.id.edtMucTieuTietKiem);
        edtTienHienTai= this.<EditText>findViewById(R.id.edtTienHienTai);
        cal=Calendar.getInstance();
        tvNgayThangNam= this.<TextView>findViewById(R.id.tvNgayThangNam);

        cal=Calendar.getInstance();

        btnLuuTietKiem= this.<Button>findViewById(R.id.btnLuuTietKiem);

    }
}
