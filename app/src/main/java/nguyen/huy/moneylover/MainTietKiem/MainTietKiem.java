package nguyen.huy.moneylover.MainTietKiem;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TabHost;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import nguyen.huy.moneylover.Model.tietKiem;
import nguyen.huy.moneylover.R;

public class MainTietKiem extends AppCompatActivity implements ChildEventListener {

    ImageButton btnThemTietKiem;
    TabHost tab;
    ListView lvTietKiem;
    ArrayList<tietKiem>arrTietKiem;
    AdapterTietKiem adapterTietKiem;

    FirebaseDatabase database=FirebaseDatabase.getInstance();
    public static FirebaseAuth mAuth=FirebaseAuth.getInstance();
    public static String user=mAuth.getCurrentUser().getUid();
    DatabaseReference myRef=database.getReference().child(user).child("Tiết kiệm").child("Đang áp dụng");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hoang_activity_main_tietkiem);

        ActionBar actionBar=getSupportActionBar();
        actionBar.setTitle("Tiết Kiệm");

        addControls();
        addEvents();

        myRef.addChildEventListener(MainTietKiem.this);

    }


    private void addEvents() {
        loadTab();
        btnThemTietKiem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                xuLyThemTietKiem();
            }
        });

        lvTietKiem.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                xuLyClickTietKiem();
            }
        });
    }

    private void xuLyClickTietKiem() {
        Intent intent=new Intent(MainTietKiem.this,detail_tietkiem.class);
        startActivity(intent);
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

    private void xuLyThemTietKiem() {
        Intent intent=new Intent(MainTietKiem.this,ThemTietKiem.class);
        startActivityForResult(intent,03);
    }

    private void addControls() {

        btnThemTietKiem = this.<ImageButton>findViewById(R.id.btnThemTietKiem);
        tab = this.<TabHost>findViewById(R.id.tabhost);

        lvTietKiem= this.<ListView>findViewById(R.id.lvTietKiem);
        arrTietKiem=new ArrayList<tietKiem>();
        adapterTietKiem=new AdapterTietKiem(this,R.layout.hoang_item_tietkiem,arrTietKiem);
        lvTietKiem.setAdapter(adapterTietKiem);
    }

    @Override
    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
        tietKiem tk=dataSnapshot.getValue(tietKiem.class);
        arrTietKiem.add(tk);
        if(arrTietKiem.size() > 0)
            adapterTietKiem.notifyDataSetChanged();
    }

    @Override
    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

    }

    @Override
    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

    }

    @Override
    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {

    }
}
