package nguyen.huy.moneylover.MainTruong;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import nguyen.huy.moneylover.R;

public class ChonNhomActivity extends AppCompatActivity {

    String arrNhom[]={"Ăn uống","Mua sắm","Di chuyển","Giải trí","Hóa đơn & tiện ích"};
    ListView lvNhom;
    ArrayAdapter<String>adapter;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.truong_activity_chon_nhom);

        /*ActionBar actionBar=getSupportActionBar();
        actionBar.setTitle("Chọn nhóm");
        actionBar.setDisplayHomeAsUpEnabled(true);*/

        lvNhom=findViewById(R.id.lvNhom);
        adapter=new ArrayAdapter<>(ChonNhomActivity.this,android.R.layout.simple_list_item_1,arrNhom);
        lvNhom.setAdapter(adapter);

        intent=getIntent();

        lvNhom.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String txtchon=arrNhom[position];
                intent.putExtra("Nhom",txtchon);
                setResult(02,intent);
                finish();
            }
        });
    }
}
