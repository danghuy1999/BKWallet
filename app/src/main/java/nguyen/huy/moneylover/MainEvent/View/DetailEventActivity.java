package nguyen.huy.moneylover.MainEvent.View;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

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

public class DetailEventActivity extends AppCompatActivity {

    public static final int INTENT_ACTIVITY_EDITEVENT = 2703;

    TextView txtDKeHoach,txtDThoiGian,txtDXacThucThoiGian,txtNoteEventDetail;
    ImageView imgIcon;
    FirebaseAuth auth=FirebaseAuth.getInstance();
    String UserID= Objects.requireNonNull(auth.getCurrentUser()).getUid();
    FirebaseDatabase database=FirebaseDatabase.getInstance();
    DatabaseReference myRef=database.getReference().child(UserID).child("Sự kiện");
    Event suKien;
    Long diff;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.truong_activity_chitietsukien);

        ActionBar actionBar=getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle("Chi tiết sự kiện");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorPrimary)));
        addControls();
        addEvents();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detailkehoach_actionbar,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.edit:{
                xuLyChinhSua();
                return true;
            }
            case R.id.delete:{
                xyLyXoa();
                return true;
            }
            case android.R.id.home:{
                onBackPressed();
                return true;
            }
            default:break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void xuLyChinhSua() {
        Intent intent=new Intent(DetailEventActivity.this, EditEventActivity.class);
        Intent intent1=getIntent();

        suKien= (Event) intent1.getSerializableExtra("SUKIEN");
        Event keHoachEdit=suKien;
        intent.putExtra("SUKIENEDIT",keHoachEdit);
        startActivityForResult(intent,INTENT_ACTIVITY_EDITEVENT);

    }

    private void xyLyXoa() {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setMessage("Bạn có muốn xóa sự kiện này?");
        builder.setCancelable(false);
        builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(diff>=0)
                    myRef.child("Đang áp dụng").child(suKien.getId()).removeValue();
                else
                    myRef.child("Đã kết thúc").child(suKien.getId()).removeValue();
                DetailEventActivity.this.finish();
            }
        });
        builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        AlertDialog alertDialog=builder.create();
        alertDialog.show();
    }

    private void addEvents() {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==INTENT_ACTIVITY_EDITEVENT){
            if(resultCode==RESULT_OK){
                assert data != null;
                Event suKien1= (Event) data.getSerializableExtra("EVENT_EDITED");
                Bitmap bitmap;
                bitmap = BitmapFactory.decodeResource(getResources(),suKien1.getIcon());
                if(suKien1.getIcon()!=0)
                    imgIcon.setImageBitmap(bitmap);
                txtDKeHoach.setText(suKien1.getTen());
                txtDThoiGian.setText(suKien1.getNgayketthuc());
                toProcessTimePeriod(suKien1);
            }
        }
    }

    @SuppressLint("CutPasteId")
    private void addControls() {
        imgIcon=findViewById(R.id.imgIcon);
        txtDKeHoach= findViewById(R.id.txtDKeHoach);
        txtDThoiGian= findViewById(R.id.txtDThoiGian);
        txtDXacThucThoiGian= findViewById(R.id.txtDXacThucThoiGian);
        txtNoteEventDetail=findViewById(R.id.txtNoteEventDetail);

        Intent intent=getIntent();

        suKien= (Event) intent.getSerializableExtra("SUKIEN");
        txtDKeHoach.setText(suKien.getTen());
        txtDThoiGian.setText(suKien.getNgayketthuc());
        txtNoteEventDetail.setText(suKien.getNote());
        Bitmap bitmap;
        bitmap = BitmapFactory.decodeResource(getResources(),suKien.getIcon());
        imgIcon.setImageBitmap(bitmap);

        //Xử lý hiệu thời gian - quá hạn hay còn hạn
        toProcessTimePeriod(suKien);


    }

    @SuppressLint("SetTextI18n")
    private void toProcessTimePeriod(Event sk) {
        Calendar cal=Calendar.getInstance();
        final SimpleDateFormat sdf= new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String timeNow=sdf.format(cal.getTime());


        Date date = null;
        try {
            date=sdf.parse(timeNow);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Date timeKeHoach = null;
        try {
            timeKeHoach=sdf.parse(sk.getNgayketthuc());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        assert timeKeHoach != null;
        assert date != null;
        diff=(timeKeHoach.getTime()- date.getTime())/(24*60*60*1000);
        Log.e("diff",diff+"");
        String output="Còn lại "+diff.toString()+" ngày ";
        if(diff>=0)
            txtDXacThucThoiGian.setText(output);
        else {
            txtDXacThucThoiGian.setText("Quá hạn");
        }
    }
}
