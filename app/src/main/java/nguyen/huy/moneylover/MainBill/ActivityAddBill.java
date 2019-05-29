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
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;

import nguyen.huy.moneylover.Model.Bill;
import nguyen.huy.moneylover.R;
import nguyen.huy.moneylover.Tool.GetImage;

public class ActivityAddBill extends AppCompatActivity {

    public static final int INTENT_ACTIVITY_SELECT_GROUP = 2703;
    public static final int INTENT_ACTIVITY_SELECT_REPEAT = 2704;
    public static final int INTENT_ACTIVITY_RESULT0 = 200;
    public static final int INTENT_ACTIVITY_RESULT1 = 201;
    public static final int INTENT_ACTIVITY_RESULT2 = 202;
    public static final int INTENT_ACTIVITY_RESULT3 = 203;

    @SuppressLint("SimpleDateFormat")
    SimpleDateFormat sdf1=new SimpleDateFormat("E, dd/MM/yyyy");
    Calendar cal;

    private String namegroup="";
    Toolbar toolbarAddBill;
    private EditText editAmout,editGroup,editNote,editRepeat,editLastDate;
    private TextView txtMoneyTotal,getTxtMoneyToDay,getTxtMoneyFuture;
    private ImageView imgIc;
    private ListView lvApplyingToDay,getLvApplyingFuture;

    private Bill bill=new Bill();
    ArrayList<Bill>arrBill= new ArrayList<>();

    FirebaseAuth auth = FirebaseAuth.getInstance();
    String UserID = Objects.requireNonNull(auth.getCurrentUser()).getUid();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bill_activity_add);

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
        if(requestCode==INTENT_ACTIVITY_SELECT_REPEAT){
            if(resultCode==INTENT_ACTIVITY_RESULT0){
                editRepeat.setText(data.getStringExtra("DAY"));
            }
        }
        if(requestCode==INTENT_ACTIVITY_SELECT_REPEAT){
            if(resultCode==INTENT_ACTIVITY_RESULT1){
                assert data != null;
                editRepeat.setText(data.getStringExtra("WEEK"));
            }
        }
        if(requestCode==INTENT_ACTIVITY_SELECT_REPEAT){
            if(resultCode==INTENT_ACTIVITY_RESULT2){
                assert data != null;
                editRepeat.setText(data.getStringExtra("MONTH"));
            }
        }
        if(requestCode==INTENT_ACTIVITY_SELECT_REPEAT){
            if(resultCode==INTENT_ACTIVITY_RESULT3){
                editRepeat.setText(data.getStringExtra("YEAR"));
            }
        }
        editGroup.setText(namegroup);
        Bitmap bitmap= GetImage.getBitmapFromString(ActivityAddBill.this,namegroup);
        imgIc.setImageBitmap(bitmap);


    }

    private void toProcessSave() {
        bill.setAmount(editAmout.getText().toString());
        bill.setGroup(namegroup);
        bill.setNote(editNote.getText().toString());
        bill.setRepeat(editRepeat.getText().toString());
        arrBill.add(bill);

        myRef = myRef.child(UserID).child("Hóa đơn").child("Đang áp dụng");
        String key = myRef.push().getKey();
        bill.setIdbill(key);

        if (key != null) {
            myRef.child(key).setValue(bill);
        }

        finish();
    }

    private void addEvents() {
        editGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toProcessSelectGroup();
            }
        });

//        editRepeat.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent=new Intent(ActivityAddBill.this,ActivityDialogRepeat.class);
//                startActivityForResult(intent,INTENT_ACTIVITY_SELECT_REPEAT);
//            }
//        });

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

        DatePickerDialog datePickerDialog=new DatePickerDialog(ActivityAddBill.this,
                callback1,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH));

        datePickerDialog.show();
    }

    private void toProcessSelectGroup() {
        Intent intent=new Intent(ActivityAddBill.this,ActivitySelectGroup.class);
        intent.putExtra("CODE", 1);
        startActivityForResult(intent,INTENT_ACTIVITY_SELECT_GROUP);
    }

    private void addControls() {
        toolbarAddBill=findViewById(R.id.toolbarAddBill);
        setSupportActionBar(toolbarAddBill);
        ActionBar actionBar=getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle("Thêm hóa đơn");
        actionBar.setDisplayHomeAsUpEnabled(true);

        editAmout=findViewById(R.id.editAmount);
        editGroup=findViewById(R.id.editGroup);
        editNote=findViewById(R.id.editNote);
        editRepeat=findViewById(R.id.editRepeat);
        imgIc=findViewById(R.id.imgIc);

        cal=Calendar.getInstance();
    }
}
