package nguyen.huy.moneylover.MainTruong;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import nguyen.huy.moneylover.R;

public class ThemKeHoach extends AppCompatActivity {

    TextView txtChonthoigian, txtDatnhacnho, txtChonnhom;
    EditText edittenKeHoach,editnhapDiaDiem;

    Calendar cal;
    SimpleDateFormat sdf1=new SimpleDateFormat("dd/MM/yyyy");
    SimpleDateFormat sdf2=new SimpleDateFormat("HH:mm");

    Button btnLuu;

    FirebaseDatabase database=FirebaseDatabase.getInstance();
    DatabaseReference myRef=database.getReference();

    ArrayList<KeHoach> arrKeHoach= new ArrayList<KeHoach>();

    static int dem=-1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.truong_activity_them_ke_hoach);

        ActionBar actionBar=getSupportActionBar();
        actionBar.setTitle("Kế hoạch");
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

        txtChonthoigian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                xuLyChonThoiGian();
            }
        });

        txtChonnhom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                xyLyChonNhom();
            }
        });

        txtDatnhacnho.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                xuLyDatNhacNho();
            }
        });

        btnLuu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                xuLyLuuKeHoach();
            }
        });

    }

    private void xuLyLuuKeHoach() {

        KeHoach kh=new KeHoach();
        kh.setTenkehoach(edittenKeHoach.getText().toString());
        kh.setThoigian(txtChonthoigian.getText().toString());
        kh.setDiadiem(editnhapDiaDiem.getText().toString());
        kh.setNhom(txtChonnhom.getText().toString());
        kh.setNhacnho(txtDatnhacnho.getText().toString());
        arrKeHoach.add(kh);
        myRef = myRef.child("user 1").child("Sự kiện").child("Đang áp dụng");
        String key = myRef.push().getKey();

        myRef.child(key).setValue(kh);

        //Intent intent = getIntent();
        //intent.putExtra("KEHOACH",kh.getTenkehoach());
        //setResult(04,intent);
        finish();

    }

    private void xyLyChonNhom() {
        Intent intent=new Intent(ThemKeHoach.this,ChonNhomActivity.class);
        startActivityForResult(intent,01);
        //startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==01 && resultCode==02)
            txtChonnhom.setText(data.getStringExtra("Nhom"));
    }

    private void xuLyDatNhacNho() {
        TimePickerDialog.OnTimeSetListener callback2=new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                cal.set(cal.HOUR_OF_DAY,hourOfDay);
                cal.set(cal.MINUTE,minute);
                txtDatnhacnho.setText(sdf2.format(cal.getTime()));
            }
        };

        TimePickerDialog timePickerDialog=new TimePickerDialog(ThemKeHoach.this,
                callback2,
                cal.get(cal.HOUR_OF_DAY),
                cal.get(cal.MINUTE),
                true);

        timePickerDialog.show();
    }

    private void xuLyChonThoiGian() {
        DatePickerDialog.OnDateSetListener callback1=new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                cal.set(cal.YEAR,year);
                cal.set(cal.MONTH,month);
                cal.set(cal.DAY_OF_MONTH,dayOfMonth);
                txtChonthoigian.setText(sdf1.format(cal.getTime()));

            }
        };

        DatePickerDialog datePickerDialog=new DatePickerDialog(ThemKeHoach.this,
                callback1,
                cal.get(cal.YEAR),
                cal.get(cal.MONTH),
                cal.get(cal.DAY_OF_MONTH));

        datePickerDialog.show();
    }

    private void addControls() {

        edittenKeHoach= this.<EditText>findViewById(R.id.editTenkehoach);

        cal=Calendar.getInstance();
        txtChonthoigian= this.<TextView>findViewById(R.id.txtChonthoigian);

        txtChonnhom= this.<TextView>findViewById(R.id.txtChonnhom);

        txtDatnhacnho= this.<TextView>findViewById(R.id.txtDatnhacnho);

        editnhapDiaDiem= this.<EditText>findViewById(R.id.editNhapdiadiem);

        btnLuu= this.<Button>findViewById(R.id.btnLuu);

    }


}
