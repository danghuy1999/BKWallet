package nguyen.huy.moneylover;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import nguyen.huy.moneylover.Authentication.LogInActivity;
import nguyen.huy.moneylover.Authentication.UserInfoActivity;
import nguyen.huy.moneylover.MainLayout.TabAdapter;
import nguyen.huy.moneylover.MainTruong.MainKeHoach;
import nguyen.huy.moneylover.MinhLayout.ThuChiActivity;
import nguyen.huy.moneylover.MainTietKiem.MainTietKiem;
import nguyen.huy.moneylover.QRCodeModule.QRCodeScannerActivity;

public class MainActivity extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private FloatingActionButton fabAdd;
    private NavigationView navigationView;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private FirebaseAuth firebaseAuth;
    private static final int QR_SCANNER = 1212;
    //Tạo firebase database
    //FirebaseDatabase firebaseDatabase;
    public static DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        databaseReference= FirebaseDatabase.getInstance().getReference();
        createNavigationDrawer();
        createTabLayout();
        addControls();
        addEvent();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_actionbar,menu);
        return true;
    }


    private void createNavigationDrawer() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
    }

    private void createTabLayout() {
        viewPager = findViewById(R.id.view_pager);
        tabLayout = findViewById(R.id.tablayout_header);
        TabAdapter adapter = new TabAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(1);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
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
    }

    private void addControls() {
        fabAdd = findViewById(R.id.fabAdd);
        firebaseAuth = FirebaseAuth.getInstance();
    }

    private void addEvent() {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        // set item as selected to persist highlight
                        menuItem.setChecked(true);
                        // close drawer when item is tapped
                        handleItemClicked(menuItem.getItemId());
                        drawerLayout.closeDrawers();

                        // Add code here to update the UI based on the item selected
                        // For example, swap UI fragments here
                        return true;
                    }
                });

        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doQuanLyThuChi();
            }
        });
    }


    private void handleItemClicked(int itemId) {
        switch (itemId){
            case R.id.nav_TietKiem : doTietKiem() ; break;
            case R.id.nav_SuKien : doSuKien() ; break;
            case R.id.nav_logout :
            {
                firebaseAuth.signOut();
                startActivity(new Intent(MainActivity.this, LogInActivity.class));
                finish();
            } break;
        }
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
            case R.id.mnuScanner:
            {
                Intent intent = new Intent(MainActivity.this, QRCodeScannerActivity.class);
                startActivityForResult(intent,QR_SCANNER);
            } return true;
            case R.id.mnuUserInfo:
            {
                Intent intent = new Intent(MainActivity.this, UserInfoActivity.class);
                startActivity(intent);
            } return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void doQuanLyThuChi() {
        //them thu chi (+)
        Intent intent=new Intent(MainActivity.this, ThuChiActivity.class);
        startActivity(intent);
    }

    private void doTietKiem() {
        Intent intent=new Intent(MainActivity.this, MainTietKiem.class);
        startActivity(intent);
    }

    private void doSuKien() {
        Intent intent=new Intent(MainActivity.this, MainKeHoach.class);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == QR_SCANNER)
        {
            if (resultCode == Activity.RESULT_OK)
            {
                boolean success = data.getBooleanExtra("Success",false);
                if (success == true)
                {
                    String initString = data.getStringExtra("Value");
                    try {
                        JSONObject initObject = new JSONObject(initString);
                        String type = initObject.getString("Type");
                        switch (type)
                        {
                            case "ThuChi": xuLyThemThuChi(initObject.getJSONArray("items"));break;
                            case "KeHoach" : xuLyThemKeHoach (initObject.getJSONArray("items"));break;
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private void xuLyThemThuChi(JSONArray items) {
        //TODO
        Toast.makeText(this,"Thu chi okay",Toast.LENGTH_SHORT).show();
    }

    private void xuLyThemKeHoach(JSONArray items) {
        //TODO
        Toast.makeText(this,"Ke hoach okay",Toast.LENGTH_SHORT).show();
    }




}
