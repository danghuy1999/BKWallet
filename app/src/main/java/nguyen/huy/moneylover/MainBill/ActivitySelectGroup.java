package nguyen.huy.moneylover.MainBill;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import nguyen.huy.moneylover.R;

public class ActivitySelectGroup extends AppCompatActivity {

    Toolbar toolbarSelectGroup;
    TabLayout tabLayout;
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bill_activity_select_group);

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
        toolbarSelectGroup=findViewById(R.id.toolbarSelectGroup);
        setSupportActionBar(toolbarSelectGroup);
        ActionBar actionBar=getSupportActionBar();
        actionBar.setTitle("Chọn nhóm");
        actionBar.setDisplayHomeAsUpEnabled(true);

        tabLayout=findViewById(R.id.tabs);
        viewPager=findViewById(R.id.viewpager);
        TabSelectGroup adapterViewPager=new TabSelectGroup(getSupportFragmentManager());
        viewPager.setAdapter(adapterViewPager);
        viewPager.setCurrentItem(1);
        tabLayout.setupWithViewPager(viewPager);


    }

    private void addEvents() {

    }
}
