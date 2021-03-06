package nguyen.huy.moneylover.MainEvent.View;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import nguyen.huy.moneylover.MainEvent.Model.Event;
import nguyen.huy.moneylover.R;

public class EditEventActivity extends AppCompatActivity {

    public static final int INTENT_ACTIVITY_BIEUTUONG = 2703;
    private int pos=0;
    private String key="";
    private Event suKien;
    EditText editTen,editNgayKetThuc,editDonViTienTe,editNoteEventEdit;
    ImageView imgIcon;
    Toolbar toolbarEditEvent;

    Calendar cal;
    @SuppressLint("SimpleDateFormat")
    SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy");
    Long diff;

    FirebaseAuth auth = FirebaseAuth.getInstance();
    String UserID = Objects.requireNonNull(auth.getCurrentUser()).getUid();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.truong_activity_suasukien);


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
        switch (item.getItemId())
        {
            case R.id.save:{
                toProcessSave();
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

    private void toProcessSave() {
        toProcessTimePeriod(editNgayKetThuc.getText().toString());
        if(diff>=0)
            myRef=myRef.child(UserID).child("Sự kiện").child("Đang áp dụng").child(key);
        else
            myRef=myRef.child(UserID).child("Sự kiện").child("Đã kết thúc").child(key);
        myRef.child("ten").setValue(editTen.getText().toString());
        myRef.child("ngayketthuc").setValue(editNgayKetThuc.getText().toString());
        if(pos!=0)
            myRef.child("icon").setValue(pos);
        else
            myRef.child("icon").setValue(suKien.getIcon());

        Intent intent=new Intent();
        intent.setClass(EditEventActivity.this, DetailEventActivity.class);
        Event suKien1=new Event();
        Log.e("imgID",imgIcon.getId()+"");
        Log.e("pos",pos+"");
        if(pos!=0)
            suKien1.setIcon(pos);
        else suKien1.setIcon(suKien.getIcon());
        suKien1.setTen(editTen.getText().toString());
        suKien1.setNgayketthuc(editNgayKetThuc.getText().toString());
        intent.putExtra("EVENT_EDITED",suKien1);
        setResult(Activity.RESULT_OK,intent);
        finish();
    }

    private void addEvents() {
        imgIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(EditEventActivity.this, SymbolEventActivity.class);
                intent.putExtra("CODE",2);
                startActivityForResult(intent,INTENT_ACTIVITY_BIEUTUONG);
            }
        });

        editNgayKetThuc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toProcessDateFinish();
            }
        });
    }

    private void toProcessDateFinish() {
        DatePickerDialog.OnDateSetListener callback1=new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                cal.set(Calendar.YEAR,year);
                cal.set(Calendar.MONTH,month);
                cal.set(Calendar.DAY_OF_MONTH,dayOfMonth);
                editNgayKetThuc.setText(sdf1.format(cal.getTime()));

            }
        };

        DatePickerDialog datePickerDialog=new DatePickerDialog(EditEventActivity.this,
                callback1,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH));

        datePickerDialog.show();
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
                imgIcon.setImageBitmap(bitmap);
            }
        }
    }

    private void addControls() {
        toolbarEditEvent=findViewById(R.id.toolbarEditEvent);
        setSupportActionBar(toolbarEditEvent);
        ActionBar actionBar=getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle("Sửa sự kiện");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorPrimary)));

        cal=Calendar.getInstance();

        imgIcon=findViewById(R.id.imgIcon);
        editTen= findViewById(R.id.editTen);
        editNgayKetThuc=findViewById(R.id.editNgayKetThuc);
        editDonViTienTe=findViewById(R.id.editDonViTienTe);
        editNoteEventEdit=findViewById(R.id.editNoteEventEdit);

        Intent intent=getIntent();
        suKien= (Event) intent.getSerializableExtra("SUKIENEDIT");
        Bitmap bitmap;
        bitmap = BitmapFactory.decodeResource(getResources(),suKien.getIcon());
        imgIcon.setImageBitmap(bitmap);
        editTen.setText(suKien.getTen());
        editNgayKetThuc.setText(suKien.getNgayketthuc());
        editNoteEventEdit.setText(suKien.getNote());
        key=suKien.getId();
//        editDonViTienTe.setText(suKien.getDonvitiente());
//        editChonVi.setText(suKien.getThanhtoan());
    }

    private void toProcessTimePeriod(String enddate) {
        Calendar calendar=Calendar.getInstance();
        final SimpleDateFormat sdf= new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String timeNow=sdf.format(calendar.getTime());

        Date date = null;
        try {
            date=sdf.parse(timeNow);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Date timeKeHoach = null;
        try {
            timeKeHoach=sdf.parse(enddate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        assert timeKeHoach != null;
        assert date != null;
        diff=(timeKeHoach.getTime()- date.getTime());
    }
}
