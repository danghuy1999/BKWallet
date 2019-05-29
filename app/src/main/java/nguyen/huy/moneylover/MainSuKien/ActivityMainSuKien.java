package nguyen.huy.moneylover.MainSuKien;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import nguyen.huy.moneylover.R;


public class ActivityMainSuKien extends AppCompatActivity {

    Toolbar toolbar;
    TabLayout tabLayout;
    ViewPager viewPager;
    FloatingActionButton fabThemKeHoach;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.truong_activity_mainsukien);

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

        tabLayout.addOnTabSelectedListener(new TabLayout.BaseOnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        fabThemKeHoach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ActivityMainSuKien.this, ActivityThemSuKien.class);
                startActivity(intent);
            }
        });

    }


    private void addControls() {
        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar=getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle("Sự kiện");

        tabLayout=findViewById(R.id.tabs);
        viewPager=findViewById(R.id.viewpager);

        ViewPagerApdater apdater = new ViewPagerApdater(getSupportFragmentManager());
        viewPager.setAdapter(apdater);
        viewPager.setCurrentItem(0);
        tabLayout.setupWithViewPager(viewPager);

        fabThemKeHoach=findViewById(R.id.fabThemKeHoach);

        actionBar.setDisplayHomeAsUpEnabled(true);
    }
}
