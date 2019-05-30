package nguyen.huy.moneylover;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import nguyen.huy.moneylover.Authentication.LogInActivity;
import nguyen.huy.moneylover.Authentication.UserInfoActivity;
import nguyen.huy.moneylover.MainEconomy.View.MainEconomy;
import nguyen.huy.moneylover.MainLayout.TabAdapter;
import nguyen.huy.moneylover.MainSuKien.ActivityMainSuKien;
import nguyen.huy.moneylover.Transaction.Controller.ReportDatabaseManager;
import nguyen.huy.moneylover.Transaction.View.TransactionActivity;
import nguyen.huy.moneylover.Transaction.Controller.TransactionManager;
import nguyen.huy.moneylover.Transaction.Controller.DayTimeManager;
import nguyen.huy.moneylover.Transaction.Model.Transaction;
import nguyen.huy.moneylover.QRCodeModule.QRCodeScannerActivity;

import nguyen.huy.moneylover.MainBill.MainBill;

public class MainActivity extends AppCompatActivity implements FirebaseAuth.AuthStateListener {
    private DrawerLayout drawerLayout;
    private FloatingActionButton fabAdd;
    private NavigationView navigationView;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private  TabAdapter adapter;
    private LinearLayout lyUserInfo;
    private CircleImageView imgUserAvatarMain;
    private TextView txtUserNameMain;

    public static long balance = 0;
    Bitmap bitmap;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;
    private StorageReference storageReference;
    private static final int QR_SCANNER = 1212;
    //Tạo firebase database
    //FirebaseDatabase firebaseDatabase;
    public static DatabaseReference databaseReference;

    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getPhoto();
        getUserName();
    }

    @Override
    protected void onStop() {
        super.onStop();
        firebaseAuth.removeAuthStateListener(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
//        {
//            return;
//        }
        databaseReference= FirebaseDatabase.getInstance().getReference();
        createNavigationDrawer();
        createTabLayout();
        addControls();
        addEvent();
        getBalance();
    }

    private void getBalance() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Long bl = dataSnapshot.getValue(Long.class);
                balance = bl;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        reference.child(user.getUid()).child("Balance").addValueEventListener(valueEventListener);
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
        View header = navigationView.getHeaderView(0);
        lyUserInfo = header.findViewById(R.id.lyUserInfo);
        txtUserNameMain = header.findViewById(R.id.txtUserNameMain);
        imgUserAvatarMain = header.findViewById(R.id.imgUserAvatarMain);
    }

    private void createTabLayout() {
        viewPager = findViewById(R.id.view_pager);
        tabLayout = findViewById(R.id.tablayout_header);
        adapter = new TabAdapter(getSupportFragmentManager());
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
        user = FirebaseAuth.getInstance().getCurrentUser();
        storageReference = FirebaseStorage.getInstance().getReference().child(user.getUid());
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
        lyUserInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.closeDrawers();
                Intent intent = new Intent(MainActivity.this, UserInfoActivity.class);
                startActivity(intent);
            }
        });
        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doQuanLyThuChi();
            }
        });
    }

    private void getUserName() {
        if (user!=null)
        {
            String name = user.getDisplayName();
            if (name != null) txtUserNameMain.setText(name);
        }
    }

    private void getPhoto() {
        final long TEN_MEGABYTE = 10 * 1024 * 1024;

        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        File directory = cw.getDir("profile", Context.MODE_PRIVATE);
        if (!directory.exists()) {
            //noinspection ResultOfMethodCallIgnored
            directory.mkdir();
        }
        File mypath = new File(directory, "avatar.jpg");
        try {
            FileInputStream fis = new FileInputStream(mypath);
            Bitmap retrievedBitmap = BitmapFactory.decodeStream(fis);
            imgUserAvatarMain.setImageBitmap(retrievedBitmap);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.images);
            imgUserAvatarMain.setImageDrawable(getDrawable(R.drawable.images));
        }

        storageReference.child("avatar").getBytes(TEN_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                imgUserAvatarMain.setImageBitmap(bitmap);
                saveBitmapToInternalStorage(bitmap);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }
    void saveBitmapToInternalStorage(Bitmap imageBitmap) {
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        File directory = cw.getDir("profile", Context.MODE_PRIVATE);
        if (!directory.exists()) {
            //noinspection ResultOfMethodCallIgnored
            directory.mkdir();
        }
        File mypath = new File(directory, "avatar.jpg");

        FileOutputStream fos;
        try {
            fos = new FileOutputStream(mypath);
            imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.close();
        } catch (Exception e) {
            Log.e("SAVE_IMAGE", e.getMessage(), e);
        }
    }

    private void handleItemClicked(int itemId) {
        switch (itemId){
            case R.id.nav_TietKiem : doTietKiem() ; break;
            case R.id.nav_SuKien : doSuKien() ; break;
            case R.id.nav_Bill: doBill(); break;
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
        }
        return super.onOptionsItemSelected(item);
    }

    private void doQuanLyThuChi() {
        //them thu chi (+)
        Intent intent=new Intent(MainActivity.this, TransactionActivity.class);
        startActivity(intent);
    }

    private void doTietKiem() {
        Intent intent=new Intent(MainActivity.this, MainEconomy.class);
        startActivity(intent);
    }

    private void doSuKien() {
        Intent intent=new Intent(MainActivity.this, ActivityMainSuKien.class);
        startActivity(intent);
    }

    private void doBill() {
        Intent intent= new Intent(MainActivity.this,MainBill.class);
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
                            case "Transaction": xuLyThemThuChi(initObject.getJSONArray("items"));break;
                            case "KeHoach" : xuLyThemKeHoach (initObject.getJSONArray("items"));break;
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private void xuLyThemThuChi(final JSONArray items) {
        final ArrayList<Transaction> list=new ArrayList<>();
        //Log.e("Length =",items.length()+"");
        for(int i=0;i<items.length();i++){
            try {
                final Transaction transaction =new Transaction();
                JSONObject jsonObject=items.getJSONObject(i);
                if(jsonObject.has("ghichu"))
                    transaction.setGhichu(jsonObject.getString("ghichu"));
                if(jsonObject.has("ngay"))
                    transaction.setNgay(jsonObject.getString("ngay"));
                if(jsonObject.has("nhacnho"))
                    transaction.setNhacnho(jsonObject.getString("nhacnho"));
                if(jsonObject.has("nhom"))
                    transaction.setNhom(jsonObject.getString("nhom"));
                if(jsonObject.has("sotien"))
                    transaction.setSotien(jsonObject.getString("sotien"));
                if(jsonObject.has("sukien"))
                    transaction.setSukien(jsonObject.getString("sukien"));
                if(jsonObject.has("thanhtoan"))
                    transaction.setThanhtoan(jsonObject.getString("thanhtoan"));
                list.add(transaction);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        final String[] result= DayTimeManager.ConvertFormatDay(list.get(0).getNgay());
        for (int i=0;i<list.size();i++)
        {
            Transaction tc = list.get(i);
            TransactionManager.SaveTransactionToDatabase(tc,result);
            //ReportDatabaseManager.SaveTransactionToDatabase(tc);
        }
        ReportDatabaseManager.SaveDataInQR(list);
        Toast.makeText(this,"Thu chi okay: success",Toast.LENGTH_SHORT).show();
    }

    private void xuLyThemKeHoach(JSONArray items) {
        //TODO
        Toast.makeText(this,"Ke hoach okay",Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
        user = firebaseAuth.getCurrentUser();
        if (user!=null)
        {
            TransactionManager.user = user.getUid();
        }

    }
}
