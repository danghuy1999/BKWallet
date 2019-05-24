package nguyen.huy.moneylover.MainBill;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
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

    private String namegroup="";
    private Toolbar toolbarAddBill;
    private EditText editAmout,editGroup,editNote,editRepeat,editLastDate;
    private TextView txtMoneyTotal,getTxtMoneyToDay,getTxtMoneyFuture;
    private ImageView imgIc;
    private ListView lvApplyingToDay,getLvApplyingFuture;

    private Bill bill=new Bill();
    private ArrayList<Bill>arrBill=new ArrayList<Bill>();

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
                namegroup=data.getStringExtra("PAY");
            }
            else if (resultCode==1){
                namegroup=data.getStringExtra("LOAN");
            }
        }
        if(requestCode==INTENT_ACTIVITY_SELECT_REPEAT){
            if(resultCode==INTENT_ACTIVITY_RESULT0){
                Log.e("Day",data.getStringExtra("DAY"));
                editRepeat.setText(data.getStringExtra("DAY"));
            }
        }
        if(requestCode==INTENT_ACTIVITY_SELECT_REPEAT){
            if(resultCode==INTENT_ACTIVITY_RESULT1){
                editRepeat.setText(data.getStringExtra("WEEK"));
            }
        }
        if(requestCode==INTENT_ACTIVITY_SELECT_REPEAT){
            if(resultCode==INTENT_ACTIVITY_RESULT2){
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
        Intent intent = new Intent(ActivityAddBill.this, MainBill.class);
        startActivity(intent);

    }

    private void addEvents() {
        editGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toProcessSelectGroup();
            }
        });

        editRepeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ActivityAddBill.this,ActivityDialogRepeat.class);
                startActivityForResult(intent,INTENT_ACTIVITY_SELECT_REPEAT);
            }
        });

    }

    private void toProcessSelectGroup() {
        Intent intent=new Intent(ActivityAddBill.this,ActivitySelectGroup.class);
        startActivityForResult(intent,INTENT_ACTIVITY_SELECT_GROUP);
    }

    private void addControls() {
        toolbarAddBill=findViewById(R.id.toolbarAddBill);
        setSupportActionBar(toolbarAddBill);
        ActionBar actionBar=getSupportActionBar();
        actionBar.setTitle("Thêm hóa đơn");
        actionBar.setDisplayHomeAsUpEnabled(true);

        editAmout=findViewById(R.id.editAmount);
        editGroup=findViewById(R.id.editGroup);
        editNote=findViewById(R.id.editNote);
        editRepeat=findViewById(R.id.editRepeat);
        imgIc=findViewById(R.id.imgIc);

    }
}
