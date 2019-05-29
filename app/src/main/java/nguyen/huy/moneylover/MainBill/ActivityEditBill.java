package nguyen.huy.moneylover.MainBill;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Objects;

import nguyen.huy.moneylover.Model.Bill;
import nguyen.huy.moneylover.R;
import nguyen.huy.moneylover.Tool.Convert;
import nguyen.huy.moneylover.Tool.GetImage;

public class ActivityEditBill extends AppCompatActivity {

    public static final int INTENT_ACTIVITY_SELECT_GROUP = 2703;
    String namegroup;

    EditText editAmout;
    EditText editGroup;
    EditText editNote;
    EditText editRepeat;
    ImageView imgIc;
    Calendar cal;
    @SuppressLint("SimpleDateFormat")
    SimpleDateFormat sdf1=new SimpleDateFormat("E, dd/MM/yyyy");

    FirebaseAuth auth = FirebaseAuth.getInstance();
    String UserID = Objects.requireNonNull(auth.getCurrentUser()).getUid();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference().child(UserID).child("Hóa đơn");

    private String key="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bill_activity_edit);

        addActionbar();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==INTENT_ACTIVITY_SELECT_GROUP){
            if(resultCode== Activity.RESULT_OK){
                assert data != null;
                namegroup=data.getStringExtra("PAY");
            }
            else if (resultCode==1){
                assert data != null;
                namegroup=data.getStringExtra("LOAN");
            }
        }
        editGroup.setText(namegroup);
        Bitmap bitmap= GetImage.getBitmapFromString(ActivityEditBill.this,namegroup);
        imgIc.setImageBitmap(bitmap);
    }

    private void toProcessSave() {
        myRef=myRef.child("Đang áp dụng").child(key);
        myRef.child("amount").setValue(editAmout.getText().toString());
        myRef.child("group").setValue(editGroup.getText().toString());
        myRef.child("note").setValue(editNote.getText().toString());
        myRef.child("repeat").setValue(editRepeat.getText().toString());

        Intent intent=new Intent();
        intent.setClass(ActivityEditBill.this,ActivityEditBill.class);
        Bill bill1=new Bill();
        bill1.setAmount(editAmout.getText().toString());
        bill1.setGroup(editGroup.getText().toString());
        Bitmap bitmap= GetImage.getBitmapFromString(ActivityEditBill.this, bill1.getGroup());
        imgIc.setImageBitmap(bitmap);
        bill1.setRepeat(editRepeat.getText().toString());
        bill1.setNote(editNote.getText().toString());
        intent.putExtra("BILL_EDITED",bill1);
        setResult(Activity.RESULT_OK,intent);
        finish();
    }

    private void addActionbar() {
        ActionBar actionBar=getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle("Thêm hóa đơn");
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    private void addControls() {
        editAmout = findViewById(R.id.editAmount);
        editGroup = findViewById(R.id.editGroup);
        editNote = findViewById(R.id.editNote);
        editRepeat = findViewById(R.id.editRepeat);
        imgIc = findViewById(R.id.imgIc);

        Intent intent=getIntent();
        Bill bill = (Bill) intent.getSerializableExtra("EDITBILL");
        Bitmap bitmap= GetImage.getBitmapFromString(ActivityEditBill.this, bill.getGroup());
        imgIc.setImageBitmap(bitmap);
        long amount=Long.parseLong(bill.getAmount());
        editAmout.setText(Convert.Money(amount));
        editGroup.setText(bill.getGroup());
        editNote.setText(bill.getNote());
        editRepeat.setText(bill.getRepeat());

        cal=Calendar.getInstance();
        key=bill.getIdbill();
    }

    private void addEvents() {
        editGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ActivityEditBill.this,ActivitySelectGroup.class);
                startActivityForResult(intent,INTENT_ACTIVITY_SELECT_GROUP);
            }
        });

        editRepeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toProcessOpenDateTime();
            }
        });
    }

    private void toProcessOpenDateTime() {
        DatePickerDialog.OnDateSetListener callback1=new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                cal.set(Calendar.YEAR,year);
                cal.set(Calendar.MONTH,month);
                cal.set(Calendar.DAY_OF_MONTH,dayOfMonth);
                editRepeat.setText(sdf1.format(cal.getTime()));
            }
        };

        DatePickerDialog datePickerDialog=new DatePickerDialog(ActivityEditBill.this,
                callback1,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH));

        datePickerDialog.show();
    }
}
