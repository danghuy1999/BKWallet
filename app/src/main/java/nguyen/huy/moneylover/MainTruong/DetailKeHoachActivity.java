package nguyen.huy.moneylover.MainTruong;

import android.content.Intent;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import nguyen.huy.moneylover.Model.KeHoach;
import nguyen.huy.moneylover.R;

public class DetailKeHoachActivity extends AppCompatActivity {

    TextView txtDKeHoach,txtDThoiGian,txtDXacThucThoiGian;
    Button btnChuaHoanTat,btnDSGiaoDich,btnXoa;
    DatabaseReference reference;
    KeHoach kh;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.truong_activity_detail_ke_hoach);

        //ActionBar actionBar=getSupportActionBar();
        //actionBar.setDisplayHomeAsUpEnabled(true);
        addControls();
        addEvents();
    }


    private void addEvents() {
        btnXoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reference.child(kh.getKeHoachID()).removeValue();
                finish();
            }
        });
    }

    private void addControls() {
        txtDKeHoach= this.<TextView>findViewById(R.id.txtDKeHoach);
        txtDThoiGian= this.<TextView>findViewById(R.id.txtDThoiGian);
        txtDXacThucThoiGian= this.<TextView>findViewById(R.id.txtDXacThucThoiGian);

        btnChuaHoanTat= this.<Button>findViewById(R.id.btnChuaHoanTat);
        btnDSGiaoDich= this.<Button>findViewById(R.id.btnChuaHoanTat);
        btnXoa= this.<Button>findViewById(R.id.btnXoaKeHoach);

        Intent intent=getIntent();

        kh= (KeHoach) intent.getSerializableExtra("KeHoach");
        txtDKeHoach.setText(kh.getTenkehoach());
        txtDThoiGian.setText(kh.getThoigian());
        txtDXacThucThoiGian.setText(intent.getStringExtra("DETAILTIME"));

        reference = FirebaseDatabase.getInstance().getReference().child("user 1").child("Sự kiện").child("Đang áp dụng");

    }
}
