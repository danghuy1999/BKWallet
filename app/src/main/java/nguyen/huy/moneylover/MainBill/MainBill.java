package nguyen.huy.moneylover.MainBill;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import nguyen.huy.moneylover.R;

public class MainBill extends AppCompatActivity {

    Toolbar toolbarBill;
    TabLayout tabLayout;
    ViewPager viewPager;
    FloatingActionButton fabAddBill;
    DrawerLayout drawerLayout;

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
        actionBar.setTitle("Hóa đơn");
        actionBar.setDisplayHomeAsUpEnabled(true);

        tabLayout=findViewById(R.id.tabs);
        viewPager=findViewById(R.id.viewpager);
        TabBill adapterViewPager=new TabBill(getSupportFragmentManager());
        viewPager.setAdapter(adapterViewPager);
        viewPager.setCurrentItem(0);
        tabLayout.setupWithViewPager(viewPager);

        fabAddBill=findViewById(R.id.fabAddBill);
        drawerLayout=findViewById(R.id.drawer_layout);

    }

    private void addEvents() {
        fabAddBill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainBill.this,ActivityAddBill.class);
                startActivity(intent);
            }
        });
    }
}
