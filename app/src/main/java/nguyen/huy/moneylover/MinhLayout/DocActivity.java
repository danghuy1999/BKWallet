package nguyen.huy.moneylover.MinhLayout;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import nguyen.huy.moneylover.MainActivity;
import nguyen.huy.moneylover.Model.ThuChi;
import nguyen.huy.moneylover.R;

public class DocActivity extends AppCompatActivity {
    Intent intent;
    ThuChi thuChi;
    DatabaseReference reference;
    DatabaseReference delReference;
    String ngay;
    String result[];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doc);
        addControls();
        addEvents();
    }

    private void addControls() {
        intent = getIntent();
        thuChi = (ThuChi) intent.getSerializableExtra("Item");
        reference = FirebaseDatabase.getInstance().getReference();
        ngay = thuChi.getNgay();
        result=xuLyChuoi(ngay);
    }

    private void addEvents() {

    }

    public void xuLyXoaThuChi(View view) {
        reference.child(ThuChiActivity.user).child("Thu chi").child(result[0]).child("Ngày").child(result[1]).child("Giao dịch").child("Giao dịch "+thuChi.getThuchiID()).removeValue();
        xuLyXoaTrenFirebase();
        Intent intent1=new Intent(this, MainActivity.class);
        startActivity(intent1);
        finish();
    }

    private void xuLyXoaTrenFirebase() {
        reference=FirebaseDatabase.getInstance().getReference().child(ThuChiActivity.user).child("Thu chi").child(result[0]);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                long tienvao = (long) dataSnapshot.child("Tiền vào").getValue();
                long tienra = (long) dataSnapshot.child("Tiền ra").getValue();
                if (thuChi.getNhom().equals("Gửi tiền") || thuChi.getNhom().equals("Tiền lãi")) {
                    tienvao = tienvao - Long.parseLong(thuChi.getSotien());
                    CapNhatTienVao(result, tienvao);
                } else if (thuChi.getNhom().equals("Rút tiền")) {
                    tienra = tienra - Long.parseLong(thuChi.getSotien());
                    CapNhatTienRa(result, tienra);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    //Cập nhật lại tiền vào
    private void CapNhatTienVao(String[] result,long tienvao){
        reference=FirebaseDatabase.getInstance().getReference().child(ThuChiActivity.user).child("Thu chi").child(result[0]).child("Tiền vào");
        reference.setValue(tienvao);
    }
    //Cập nhật lại tiền ra
    private void CapNhatTienRa(String[] result,long tienra){
        reference=FirebaseDatabase.getInstance().getReference().child(ThuChiActivity.user).child("Thu chi").child(result[0]).child("Tiền ra");
        reference.setValue(tienra);
    }

    private String[]  xuLyChuoi(String ngay){
        String[] words=ngay.split("[/]");
        String[] result=new String[2];
        result[0]=words[1]+ "+" + words[2];
        result[1]= words[0]+ "+" +words[1]+ "+" + words[2];
        return result;
    }
}
