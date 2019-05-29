package nguyen.huy.moneylover.MainSuKien;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import java.util.ArrayList;

import nguyen.huy.moneylover.R;

public class ActivityBieuTuongSuKien extends AppCompatActivity {

    GridView gvHinh;
    public ArrayList<Integer>dsHinh;
    AdapterBieuTuong adapterBieuTuong;
    Toolbar toolbarSymbol;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.truong_activity_bieutuongsukien);

        addControls();
        addEvents();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;

            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }


    private void addEvents() {
        gvHinh.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent();
                Intent intent1=getIntent();
                int code=intent1.getIntExtra("CODE",DEFAULT_KEYS_DISABLE);
                if(code==1)
                    intent.setClass(ActivityBieuTuongSuKien.this,ActivityThemSuKien.class);
                else if (code==2)
                    intent.setClass(ActivityBieuTuongSuKien.this,ActivitySuaSuKien.class);
                intent.putExtra("SYMBOL",dsHinh.get(position));
                Log.e("poslist",dsHinh.get(position)+"");
                setResult(Activity.RESULT_OK,intent);

                finish();
            }
        });

    }

    private void addControls() {

        toolbarSymbol=findViewById(R.id.toolbarSymbol);
        setSupportActionBar(toolbarSymbol);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle("Chọn biểu tượng");
        actionBar.setDisplayHomeAsUpEnabled(true);

        gvHinh=findViewById(R.id.gvHinh);
        dsHinh=new ArrayList<>();

        dsHinh.add(R.drawable.ic_category_award);dsHinh.add(R.drawable.ic_category_debt);
        dsHinh.add(R.drawable.ic_category_doctor);dsHinh.add(R.drawable.ic_category_donations);
        dsHinh.add(R.drawable.ic_category_education);dsHinh.add(R.drawable.ic_category_entertainment);
        dsHinh.add(R.drawable.ic_category_family);dsHinh.add(R.drawable.ic_category_foodndrink);
        dsHinh.add(R.drawable.ic_category_friendnlover);dsHinh.add(R.drawable.ic_category_give);
        dsHinh.add(R.drawable.ic_category_interestmoney);dsHinh.add(R.drawable.ic_category_invest);
        dsHinh.add(R.drawable.ic_category_loan);dsHinh.add(R.drawable.ic_category_medical);
        dsHinh.add(R.drawable.ic_category_pharmacy);dsHinh.add(R.drawable.ic_category_travel);
        dsHinh.add(R.drawable.ic_category_salary);dsHinh.add(R.drawable.ic_category_selling);
        dsHinh.add(R.drawable.ic_category_shopping);dsHinh.add(R.drawable.ic_category_transport);

        adapterBieuTuong=new AdapterBieuTuong(ActivityBieuTuongSuKien.this,R.layout.truong_item_bieutuong,dsHinh);
        gvHinh.setAdapter(adapterBieuTuong);
    }

}
