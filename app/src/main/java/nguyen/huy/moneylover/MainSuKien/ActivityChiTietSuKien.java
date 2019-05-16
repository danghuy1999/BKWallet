package nguyen.huy.moneylover.MainSuKien;

import android.content.DialogInterface;
import android.content.Intent;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
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

import nguyen.huy.moneylover.Model.SuKien;
import nguyen.huy.moneylover.R;

public class ActivityChiTietSuKien extends AppCompatActivity {

    public static final int INTENT_ACTIVITY_EDITEVENT = 2703;

    TextView txtDKeHoach,txtDThoiGian,txtDXacThucThoiGian;
    ImageView imgIcon;
    Button btnChuaHoanTat,btnDSGiaoDich;
    FirebaseAuth auth=FirebaseAuth.getInstance();
    String UserID=auth.getCurrentUser().getUid();
    FirebaseDatabase database=FirebaseDatabase.getInstance();
    DatabaseReference myRef=database.getReference().child(UserID).child("Sự kiện");
    SuKien suKien;
    Long diff;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.truong_activity_chitietsukien);

        ActionBar actionBar=getSupportActionBar();
        actionBar.setTitle("Chi tiết sự kiện");
        actionBar.setDisplayHomeAsUpEnabled(true);
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
        Intent intent=new Intent(ActivityChiTietSuKien.this, ActivitySuaSuKien.class);
        Intent intent1=getIntent();

        suKien= (SuKien) intent1.getSerializableExtra("SUKIEN");
        SuKien keHoachEdit=suKien;
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
                ActivityChiTietSuKien.this.finish();
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
                SuKien suKien1= (SuKien) data.getSerializableExtra("EVENT_EDITED");
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

    private void addControls() {
        imgIcon=findViewById(R.id.imgIcon);
        txtDKeHoach= this.<TextView>findViewById(R.id.txtDKeHoach);
        txtDThoiGian= this.<TextView>findViewById(R.id.txtDThoiGian);
        txtDXacThucThoiGian= this.<TextView>findViewById(R.id.txtDXacThucThoiGian);

        btnChuaHoanTat= this.<Button>findViewById(R.id.btnChuaHoanTat);
        btnDSGiaoDich= this.<Button>findViewById(R.id.btnChuaHoanTat);

        Intent intent=getIntent();

        suKien= (SuKien) intent.getSerializableExtra("SUKIEN");
        txtDKeHoach.setText(suKien.getTen());
        txtDThoiGian.setText(suKien.getNgayketthuc());
        Bitmap bitmap;
        bitmap = BitmapFactory.decodeResource(getResources(),suKien.getIcon());
        imgIcon.setImageBitmap(bitmap);

        //Xử lý hiệu thời gian - quá hạn hay còn hạn
        toProcessTimePeriod(suKien);


    }

    private void toProcessTimePeriod(SuKien sk) {
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
