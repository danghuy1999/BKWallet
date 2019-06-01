package nguyen.huy.moneylover.MainBill.View;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
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

import nguyen.huy.moneylover.MainBill.Model.Bill;
import nguyen.huy.moneylover.R;
import nguyen.huy.moneylover.Tool.GetImage;

public class EditBillActivity extends AppCompatActivity {

    public static final int INTENT_ACTIVITY_SELECT_GROUP = 2703;
    public static final int INTENT_ACTIVITY_SELECT_FORM = 2704;

    String namegroup;

    EditText editAmout;
    EditText editGroup;
    EditText editNote;
    EditText editRepeat,editFormDetail;
    ImageView imgIc,imgFormDetail;
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

        if(requestCode==INTENT_ACTIVITY_SELECT_FORM){
            if(resultCode==Activity.RESULT_OK){
                assert data != null;
                String form=data.getStringExtra("Form");
                editFormDetail.setText(form);
                Bitmap bitmap1= GetImage.getBitmapFromString(EditBillActivity.this,form);
                imgFormDetail.setImageBitmap(bitmap1);
            }
        }
        editGroup.setText(namegroup);
        Bitmap bitmap= GetImage.getBitmapFromString(EditBillActivity.this,namegroup);
        imgIc.setImageBitmap(bitmap);
    }

    private void toProcessSave() {
        myRef=myRef.child("Đang áp dụng").child(key);
        myRef.child("amount").setValue(editAmout.getText().toString());
        myRef.child("group").setValue(editGroup.getText().toString());
        myRef.child("note").setValue(editNote.getText().toString());
        myRef.child("repeat").setValue(editRepeat.getText().toString());
        myRef.child("form").setValue(editFormDetail.getText().toString());

        Intent intent=new Intent();
        intent.setClass(EditBillActivity.this, DetailBillActivity.class);
        Bill bill1=new Bill();
        bill1.setAmount(editAmout.getText().toString());
        bill1.setGroup(editGroup.getText().toString());
        bill1.setRepeat(editRepeat.getText().toString());
        bill1.setNote(editNote.getText().toString());
        bill1.setForm(editFormDetail.getText().toString());

        intent.putExtra("BILL_EDITED",bill1);
        setResult(Activity.RESULT_OK,intent);
        finish();
    }

    private void addActionbar() {
        ActionBar actionBar=getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle("Chỉnh sửa hóa đơn");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorPrimary)));
    }

    private void addControls() {
        editAmout = findViewById(R.id.editAmount);
        editGroup = findViewById(R.id.editGroup);
        editNote = findViewById(R.id.editNote);
        editRepeat = findViewById(R.id.editRepeat);
        imgIc = findViewById(R.id.imgIc);
        imgFormDetail=findViewById(R.id.imgFormDetail);
        editFormDetail=findViewById(R.id.editFormDetail);

        Intent intent=getIntent();
        Bill bill = (Bill) intent.getSerializableExtra("EDITBILL");
        Bitmap bitmap= GetImage.getBitmapFromString(EditBillActivity.this, bill.getGroup());
        imgIc.setImageBitmap(bitmap);
        editAmout.setText(bill.getAmount());
        editGroup.setText(bill.getGroup());
        editNote.setText(bill.getNote());
        editRepeat.setText(bill.getRepeat());
        Bitmap bitmap1= GetImage.getBitmapFromString(EditBillActivity.this, bill.getForm());
        imgFormDetail.setImageBitmap(bitmap1);
        editFormDetail.setText(bill.getForm());


        cal=Calendar.getInstance();
        key=bill.getIdbill();
    }

    private void addEvents() {
        editGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(EditBillActivity.this, SelectGroupActivity.class);
                startActivityForResult(intent,INTENT_ACTIVITY_SELECT_GROUP);
            }
        });

        editRepeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toProcessOpenDateTime();
            }
        });

        editFormDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(EditBillActivity.this, PaymentFormActivity.class);
                intent.putExtra("CODE",2);
                startActivityForResult(intent,INTENT_ACTIVITY_SELECT_FORM);
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

        DatePickerDialog datePickerDialog=new DatePickerDialog(EditBillActivity.this,
                callback1,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH));

        datePickerDialog.show();
    }
}
