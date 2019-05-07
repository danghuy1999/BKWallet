package nguyen.huy.moneylover.MainTietKiem;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import nguyen.huy.moneylover.R;

public class detail_tietkiem extends AppCompatActivity {


    FirebaseDatabase database=FirebaseDatabase.getInstance();
    public static FirebaseAuth mAuth=FirebaseAuth.getInstance();
    public static String user=mAuth.getCurrentUser().getUid();
    DatabaseReference myRef=database.getReference().child(user).child("Tiết kiệm").child("Đang áp dụng");
    String key = myRef.push().getKey();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hoang_detail_tietkiem);


    }


}
