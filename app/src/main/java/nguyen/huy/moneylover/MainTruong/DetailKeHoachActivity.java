package nguyen.huy.moneylover.MainTruong;

import android.content.Intent;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.widget.Button;
import android.widget.TextView;

import nguyen.huy.moneylover.R;

public class DetailKeHoachActivity extends AppCompatActivity {

    TextView txtDKeHoach,txtDThoiGian,txtDXacThucThoiGian;
    Button btnChuaHoanTat,btnDSGiaoDich;

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
        Intent intent=getIntent();
        txtDKeHoach.setText(intent.getStringExtra("TENKEHOACH"));
        txtDThoiGian.setText(intent.getStringExtra("THOIGIAN1"));
        txtDXacThucThoiGian.setText(intent.getStringExtra("DETAILTIME"));
    }

    private void addControls() {
        txtDKeHoach= this.<TextView>findViewById(R.id.txtDKeHoach);
        txtDThoiGian= this.<TextView>findViewById(R.id.txtDThoiGian);
        txtDXacThucThoiGian= this.<TextView>findViewById(R.id.txtDXacThucThoiGian);

        btnChuaHoanTat= this.<Button>findViewById(R.id.btnChuaHoanTat);
        btnDSGiaoDich= this.<Button>findViewById(R.id.btnChuaHoanTat);
    }
}
