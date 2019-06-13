package nguyen.huy.moneylover.MainEvent.View;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import nguyen.huy.moneylover.MainEvent.Model.Event;
import nguyen.huy.moneylover.R;

public class AddEventActivity extends AppCompatActivity {
    public static final int INTENT_ACTIVITY_BIEUTUONG = 2703;
    public int pos = 0;
    Toolbar toolbar2;
    ActionBar actionBar;

    public EditText editTen, editNgayKetThuc, editDonViTienTe, editNote;
    public ImageView imgSymbol;

    Calendar cal;
    @SuppressLint("SimpleDateFormat")
    SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy");


    FirebaseAuth auth = FirebaseAuth.getInstance();
    String UserID = Objects.requireNonNull(auth.getCurrentUser()).getUid();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();
    Event sk = new Event();
    ArrayList<Event> arrSuKien = new ArrayList<>();

    Long diff;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.truong_activity_themsukien);

        toolbar2=findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar2);
        actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle("Thêm sự kiện");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorPrimary)));

        addControls();
        addEvents();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.themkehoach_actionbar,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save:{
                toProcessSave();
            }

            case android.R.id.home:{
                onBackPressed();
                return true;
            }

            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==INTENT_ACTIVITY_BIEUTUONG)
        {
            if(resultCode== Activity.RESULT_OK)
            {
                assert data != null;
                pos = data.getIntExtra("SYMBOL",R.drawable.icon_not_selected);
                Bitmap bitmap;
                bitmap = BitmapFactory.decodeResource(getResources(),pos);
                imgSymbol.setImageBitmap(bitmap);
            }
        }

    }



    private void addEvents() {
        editNgayKetThuc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                xuLyChonThoiGian();
            }
        });

        imgSymbol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AddEventActivity.this, SymbolEventActivity.class);
                intent.putExtra("CODE",1);
                startActivityForResult(intent,INTENT_ACTIVITY_BIEUTUONG);
            }
        });
    }



    private void toProcessSave() {

        sk.setIcon(pos);
        sk.setTen(editTen.getText().toString());
        sk.setNgayketthuc(editNgayKetThuc.getText().toString());
        sk.setDonvitiente(editDonViTienTe.getText().toString());
        sk.setNote(editNote.getText().toString());
        arrSuKien.add(sk);

        if (xuLyKhoangThoiGian() >= 0)
            myRef = myRef.child(UserID).child("Sự kiện").child("Đang áp dụng");
        else
            myRef = myRef.child(UserID).child("Sự kiện").child("Đã kết thúc");
        String key = myRef.push().getKey();
        sk.setId(key);

        if (key != null) {
            myRef.child(key).setValue(sk);
        }
        Intent intent = new Intent(AddEventActivity.this, MainEventActivity.class);
        startActivity(intent);
    }

    private Long xuLyKhoangThoiGian() {
        Date date, datesukien;
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
            datesukien = simpleDateFormat.parse(sk.getNgayketthuc());
            assert date != null;
            diff = datesukien.getTime() - date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return diff;
    }

    private void xuLyChonThoiGian() {
        DatePickerDialog.OnDateSetListener callback1=new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                cal.set(Calendar.YEAR,year);
                cal.set(Calendar.MONTH,month);
                cal.set(Calendar.DAY_OF_MONTH,dayOfMonth);
                editNgayKetThuc.setText(sdf1.format(cal.getTime()));

            }
        };

        DatePickerDialog datePickerDialog=new DatePickerDialog(AddEventActivity.this,
                callback1,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH));

        datePickerDialog.show();
    }

    private void addControls() {

        editTen=findViewById(R.id.editTen);
        editNgayKetThuc=findViewById(R.id.editNgayKetThuc);
        editDonViTienTe=findViewById(R.id.editDonViTienTe);
        editNote=findViewById(R.id.editNote);

        imgSymbol=findViewById(R.id.imgSymbol);

        cal=Calendar.getInstance();

    }

}
