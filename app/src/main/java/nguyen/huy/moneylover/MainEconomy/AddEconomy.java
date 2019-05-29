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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import nguyen.huy.moneylover.Model.TietKiem;
import nguyen.huy.moneylover.R;

public class AddEconomy extends AppCompatActivity {
    TietKiem tk;
    EditText edtMucDichTietKiem, edtMucTieuTietKiem, edtTienHienTai;
    TextView tvNgayThangNam;
    Toolbar toolbarSaveTietKiem;
    Calendar cal;
    SimpleDateFormat sdf1=new SimpleDateFormat("dd/MM/yyyy");
    public static FirebaseAuth mAuth;
    public static String user;
    FirebaseDatabase database;
    DatabaseReference myRef;
    ArrayList<TietKiem> arrTietKiem;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hoang_add_economy);

        addControls();
        addEvents();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.save_tietkiem,menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.saveEconomy2:{
                toProcessSaveTietKiem();
                return true;
            }

            case R.id.cancelEconomy:{
                toProcessCancelTietKiem();
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

    private void toProcessCancelTietKiem() {
        showAlbertCancelThemTietKiem();
    }

    private void showAlbertCancelThemTietKiem() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Hủy Thêm Tiết Kiệm Mới");
        builder.setMessage("Bạn có muốn hủy việc thêm tiết kiệm không?");
        builder.setCancelable(false);
        builder.setPositiveButton("Chắc chắn", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent=new Intent(AddEconomy.this, MainEconomy.class);
                startActivity(intent);
                finish();
            }
        });
        builder.setNegativeButton("Không, vẫn lưu", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void toProcessSaveTietKiem() {
        ShowAlbertDialogSaveTietKiem();
    }

    private void ShowAlbertDialogSaveTietKiem() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Lưu Tiết Kiệm Mới");
        builder.setMessage("Bạn có muốn lưu việc thêm tiết kiệm không?");
        builder.setCancelable(false);
        builder.setPositiveButton("Chắc chắn", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                toProcessSaveEconomy();
            }
        });
        builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void addEvents() {

        tvNgayThangNam.setOnClickListener(new View.OnClickListener() {
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
                tvNgayThangNam.setText(sdf1.format(cal.getTime()));

            }
        };
        DatePickerDialog datePickerDialog=new DatePickerDialog(AddEconomy.this,
                callback1,
                cal.get(cal.YEAR),
                cal.get(cal.MONTH),
                cal.get(cal.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    private void toProcessSaveEconomy() {
        if(TextUtils.isEmpty(edtMucDichTietKiem.getText().toString())) {
            edtMucDichTietKiem.setError("Nhập Lại Mục Đích Tiết Kiệm");
            return;
        }
        tk.setMucDichTietKiem(edtMucDichTietKiem.getText().toString());

        if(TextUtils.isEmpty(edtMucTieuTietKiem.getText().toString())) {
            edtMucTieuTietKiem.setError("Nhập Lại Mục Tiêu Tiết Kiệm");
            return;
        }
        tk.setMucTieuTietKiem(edtMucTieuTietKiem.getText().toString());

        if(TextUtils.isEmpty(edtTienHienTai.getText().toString())) {
            edtTienHienTai.setError("Nhập Lại Số Tiền Hiện Tại");
            return;
        }
        if(Long.parseLong(edtTienHienTai.getText().toString()) < Long.parseLong(edtMucTieuTietKiem.getText().toString())) {
            tk.setSoTienHienCo(edtTienHienTai.getText().toString());
            tk.setNgayThangNam(tvNgayThangNam.getText().toString());
            arrTietKiem.add(tk);

            if (checkTimeUp())
                myRef = myRef.child(user).child("Tiết kiệm").child("Đang áp dụng");
            else
                myRef = myRef.child(user).child("Tiết kiệm").child("Đã kết thúc");
            String key = myRef.push().getKey();
            tk.setTietKiemID(key);

            if (key != null) {
                myRef.child(key).setValue(tk);
            }
            Intent intent = new Intent(AddEconomy.this, MainEconomy.class);
            startActivity(intent);
            finish();
        }
        else{
            edtTienHienTai.setError("Nhập lại số tiền hiện có ");
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


    private void addControls() {

        edtMucDichTietKiem= this.<EditText>findViewById(R.id.edtMucDichTietKiem);
        edtMucTieuTietKiem= this.<EditText>findViewById(R.id.edtMucTieuTietKiem);
        edtTienHienTai= this.<EditText>findViewById(R.id.edtTienHienTai);
        tk =new TietKiem();
        cal=Calendar.getInstance();
        tvNgayThangNam= this.<TextView>findViewById(R.id.tvNgayThangNam);
        toolbarSaveTietKiem = this.<Toolbar>findViewById(R.id.toolbarSaveTietKiem);
        setSupportActionBar(toolbarSaveTietKiem);
        ActionBar actionBar=getSupportActionBar();
        actionBar.setTitle("Thêm Tiết Kiệm");
        actionBar.setDisplayHomeAsUpEnabled(true);

        mAuth=FirebaseAuth.getInstance();
        user=mAuth.getCurrentUser().getUid();
        database=FirebaseDatabase.getInstance();
        myRef=database.getReference();
        arrTietKiem= new ArrayList<TietKiem>();
    }
}
