package nguyen.huy.moneylover.MainTietKiem;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import nguyen.huy.moneylover.R;

public class detail_tietkiem extends AppCompatActivity {

    Button btnSuaTietKiem;
    Button btnXoaTietKiem;

    FirebaseDatabase database=FirebaseDatabase.getInstance();
    public static FirebaseAuth mAuth=FirebaseAuth.getInstance();
    public static String user=mAuth.getCurrentUser().getUid();
    DatabaseReference myRef=database.getReference().child(user).child("Tiết kiệm").child("Đang áp dụng");
    String key = myRef.push().getKey();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hoang_detail_tietkiem);

        addControl();
        addEvent();

        //myRef.addChildEventListener((ChildEventListener) detail_tietkiem.this);

    }

    private void addEvent() {
        btnXoaTietKiem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(detail_tietkiem.this, "Đã Xóa ", Toast.LENGTH_SHORT).show();
                //myRef.child(key).removeValue();
            }
        });

        btnSuaTietKiem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(detail_tietkiem.this, "Đã Xóa ", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addControl() {
        btnSuaTietKiem = this.<Button>findViewById(R.id.btnSuaTietKiem);
        btnXoaTietKiem = this.<Button>findViewById(R.id.btnXoaTietKiem);
    }
}
