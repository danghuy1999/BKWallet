package nguyen.huy.moneylover.MainEconomy.View;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import nguyen.huy.moneylover.MainEconomy.Controller.EconomyViewPagerApdater;
import nguyen.huy.moneylover.R;

public class MainEconomy extends AppCompatActivity {
    Toolbar toolbarMainEconomy;
    TabLayout tabLayout;
    ViewPager viewPager;
    FloatingActionButton fabAddEconomy;
    private Dialog dialog;
    Button btnMakeEconomy;
    TextView tvCloseEconomy;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hoang_main_economy);

        addControls();
        addEvents();


    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case android.R.id.home: {
                onBackPressed();
                return true;
            }

            default:break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void addEvents() {
        showDialogAddEconomy();
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

        fabAddEconomy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainEconomy.this,AddEconomy.class);
                startActivity(intent);
            }
        });
    }

    private void showDialogAddEconomy() {
        btnMakeEconomy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainEconomy.this,AddEconomy.class);
                startActivity(intent);
            }
        });
        tvCloseEconomy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void addControls() {
        dialog = new Dialog(MainEconomy.this);
        dialog.setContentView(R.layout.hoang_make_economy);
        tvCloseEconomy = dialog.findViewById(R.id.tvCloseEconomy);
        btnMakeEconomy = dialog.findViewById(R.id.btnMakeEconomy);
        toolbarMainEconomy=findViewById(R.id.toolbarMainEconomy);
        setSupportActionBar(toolbarMainEconomy);
        ActionBar actionBar=getSupportActionBar();
        actionBar.setTitle("Tiết Kiệm");
        actionBar.setDisplayHomeAsUpEnabled(true);

        tabLayout=findViewById(R.id.tablayout_economy);
        viewPager=findViewById(R.id.view_pager_economy);
        EconomyViewPagerApdater adapterViewPagerEconmy = new EconomyViewPagerApdater(getSupportFragmentManager());
        viewPager.setAdapter(adapterViewPagerEconmy);
        viewPager.setCurrentItem(0);
        tabLayout.setupWithViewPager(viewPager);

        fabAddEconomy=findViewById(R.id.fabAddEconomy);
    }
}
