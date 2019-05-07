package nguyen.huy.moneylover.MainTruong;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TabHost;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import nguyen.huy.moneylover.Model.KeHoach;
import nguyen.huy.moneylover.R;

public class MainKeHoach extends AppCompatActivity implements ChildEventListener {
    Date timeNow1;
    SimpleDateFormat sdf1;
    FloatingActionButton btnThemkehoach;
    TabHost tab;
    ListView lvkeHoach,lvKeHoachDaKetThuc;
    ArrayList<KeHoach>arrKeHoach,arrKeHoachDaKetThuc;
    AdapterKeHoach adapterKeHoach;
    AdapterKeHoachDaKetThuc adapterKeHoachDaKetThuc;

    FirebaseDatabase database=FirebaseDatabase.getInstance();
    DatabaseReference myRef=database.getReference().child("user 1").child("Sự kiện").child("Đang áp dụng");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.truong_activity_mainkehoach);

        ActionBar actionBar=getSupportActionBar();
        actionBar.setTitle("Kế hoạch");

        addControls();
        addEvents();

        myRef.addChildEventListener(MainKeHoach.this);

    }


    private void addEvents(){
        loadTab();
        btnThemkehoach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                xuLyThemKeHoach();
            }
        });

        Calendar cal=Calendar.getInstance();
        final SimpleDateFormat sdf= new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String timeNow=sdf.format(cal.getTime());

        Date timeNow3 = null;
        try {
            timeNow3=sdf.parse(timeNow);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        final Date finalTimeNow = timeNow3;
        lvkeHoach.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                String tenKH=arrKeHoach.get(position).getTenkehoach();
//                String ngayThangNam=arrKeHoach.get(position).getThoigian();
                Intent intent2=new Intent(MainKeHoach.this,DetailKeHoachActivity.class);
//                intent2.putExtra("TENKEHOACH",tenKH);
//                intent2.putExtra("THOIGIAN1",ngayThangNam);
                intent2.putExtra("KeHoach",arrKeHoach.get(position));
                Date time3 = null;
                try {
                    time3=sdf.parse(arrKeHoach.get(position).getThoigian());
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                Long diff=(time3.getTime()- finalTimeNow.getTime())/(24*60*60*1000);
                String output="Còn lại "+diff.toString()+" ngày ";
                if(diff>0)
                    intent2.putExtra("DETAILTIME",output);
                else {
                    output = "Quá hạn";
                    intent2.putExtra("DETAILTIME", output);
                }

                startActivity(intent2);
            }
        });

        xyLyHienThiKeHoachDaKetThuc();
    }

    private void xyLyHienThiKeHoachDaKetThuc() {
        Calendar cal=Calendar.getInstance();
        Log.e("runable","runale");
        sdf1= new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String timeNow=sdf1.format(cal.getTime());

        timeNow1 = null;
        try {
            timeNow1=sdf1.parse(timeNow);
        } catch (ParseException e) {
            e.printStackTrace();
        }




    }


    /*@Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==03 && resultCode==04){
            KeHoach kh=new KeHoach();
            //kh.setTenkehoach(data.getStringExtra("KEHOACH"));
            arrKeHoach.add(kh);
            //adapterKeHoach.notifyDataSetChanged();
        }
    }*/

    int soNgay(int year, int month, int day){
        if (month<3){
            year--;
            month+=12;
        }
        return 365*year+year/4-year/100+year/400+(153+month-475)/5+day-306;
    }

    private void loadTab() {
        tab.setup();
        TabHost.TabSpec spec;
        spec=tab.newTabSpec("t1");
        spec.setContent(R.id.tab1);
        spec.setIndicator("Đang áp dụng");
        tab.addTab(spec);

        spec=tab.newTabSpec("t2");
        spec.setContent(R.id.tab2);
        spec.setIndicator("Đã kết thúc");
        tab.addTab(spec);

        tab.setCurrentTab(0);
    }

    private void xuLyThemKeHoach() {
        Intent intent=new Intent(MainKeHoach.this,ThemKeHoach.class);
        startActivityForResult(intent,03);
    }

    private void addControls() {

        btnThemkehoach= this.<FloatingActionButton>findViewById(R.id.btnThemkehoach);
        tab = this.<TabHost>findViewById(R.id.tabhost);

        lvkeHoach= this.<ListView>findViewById(R.id.lvkeHoach);
        arrKeHoach=new ArrayList<KeHoach>();
        adapterKeHoach=new AdapterKeHoach(this,R.layout.truong_item_kehoach,arrKeHoach);
        lvkeHoach.setAdapter(adapterKeHoach);

        lvKeHoachDaKetThuc= this.<ListView>findViewById(R.id.lvKeHoachDaKetThuc);
        arrKeHoachDaKetThuc=new ArrayList<KeHoach>();
        adapterKeHoachDaKetThuc =new AdapterKeHoachDaKetThuc(this,R.layout.truong_item_kehoach,arrKeHoachDaKetThuc);
        lvKeHoachDaKetThuc.setAdapter(adapterKeHoachDaKetThuc);
    }

    @Override
    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
        KeHoach keHoach=dataSnapshot.getValue(KeHoach.class);
        arrKeHoach.add(keHoach);

        Date timeKeHoach = null;
        if(arrKeHoach.size() > 0)
            adapterKeHoach.notifyDataSetChanged();
        try {
            Log.e("runable","oke");
            if (!adapterKeHoachDaKetThuc.isEmpty())adapterKeHoachDaKetThuc.clear();
            for (KeHoach kh:arrKeHoach) {
                timeKeHoach=sdf1.parse(kh.getThoigian());
                Long diff1=timeKeHoach.getTime()- timeNow1.getTime();
                Log.e("diff",diff1+"");
                if(diff1<0) arrKeHoachDaKetThuc.add(kh);
            }
            adapterKeHoachDaKetThuc.notifyDataSetChanged();
        }
        catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

    }

    @Override
    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
        KeHoach kh=dataSnapshot.getValue(KeHoach.class);
        adapterKeHoach.remove(adapterKeHoach.getItemByID(kh.getKeHoachID()));
        adapterKeHoach.notifyDataSetChanged();
    }

    @Override
    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {

    }
}
