package nguyen.huy.moneylover.MainTruong;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TabHost;

import java.util.ArrayList;

import nguyen.huy.moneylover.R;

public class MainKeHoach extends AppCompatActivity {

    ImageButton btnThemkehoach;
    TabHost tab;
    ListView lvkeHoach;
    ArrayList<KeHoach>arrKeHoach;
    AdapterKeHoach adapterKeHoach;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.truong_activity_mainkehoach);

        ActionBar actionBar=getSupportActionBar();
        actionBar.setTitle("");


        addControls();
        addEvents();

    }


    private void addEvents() {
        loadTab();
        btnThemkehoach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                xuLyThemKeHoach();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==03 && resultCode==04){
            KeHoach kh=new KeHoach();
            kh.setTenkehoach(data.getStringExtra("KEHOACH"));
            arrKeHoach.add(kh);
            adapterKeHoach.notifyDataSetChanged();
        }
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

        btnThemkehoach= this.<ImageButton>findViewById(R.id.btnThemkehoach);
        tab = this.<TabHost>findViewById(R.id.tabhost);

        lvkeHoach= this.<ListView>findViewById(R.id.lvkeHoach);
        arrKeHoach=new ArrayList<KeHoach>();
        adapterKeHoach=new AdapterKeHoach(this,R.layout.truong_item_kehoach,arrKeHoach);
        lvkeHoach.setAdapter(adapterKeHoach);
    }

}
