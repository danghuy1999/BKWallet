package nguyen.huy.moneylover.MainBill.View;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import nguyen.huy.moneylover.MainBill.Adapter.AdapterTabBill;
import nguyen.huy.moneylover.R;

public class MainBillActivity extends AppCompatActivity {

    Toolbar toolbarBill;
    TabLayout tabLayout;
    ViewPager viewPager;
    FloatingActionButton fabAddBill;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bill_activity_main);

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

    private void addControls() {
        toolbarBill=findViewById(R.id.toolbarBill);
        setSupportActionBar(toolbarBill);
        ActionBar actionBar=getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle("Hóa đơn");
        actionBar.setDisplayHomeAsUpEnabled(true);

        tabLayout=findViewById(R.id.tabs);
        viewPager=findViewById(R.id.viewpager);
        AdapterTabBill adapterViewPager=new AdapterTabBill(getSupportFragmentManager());
        viewPager.setAdapter(adapterViewPager);
        viewPager.setCurrentItem(0);
        tabLayout.setupWithViewPager(viewPager);

        fabAddBill=findViewById(R.id.fabAddBill);

    }

    private void addEvents() {
        fabAddBill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainBillActivity.this, AddBillActivity.class);
                startActivity(intent);
            }
        });
    }
}
