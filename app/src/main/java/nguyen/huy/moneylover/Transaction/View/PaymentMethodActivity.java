package nguyen.huy.moneylover.Transaction.View;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import nguyen.huy.moneylover.Transaction.Adapter.AdapterPaymentMethod;
import nguyen.huy.moneylover.Transaction.Controller.TransactionManager;
import nguyen.huy.moneylover.R;
import nguyen.huy.moneylover.Tool.GetImage;

public class PaymentMethodActivity extends AppCompatActivity {

    ListView lvPhuongThucThanhToan;
    AdapterPaymentMethod adapterPhuongThucThanhToan;
    ArrayList<String> arrayListPhuongThucThanhToan=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phuong_thuc_thanh_toan);
        addListView();
        lvPhuongThucThanhToan=findViewById(R.id.lvPhuongThucThanhToan);
        adapterPhuongThucThanhToan=new AdapterPaymentMethod(this,R.layout.minh_custom_listview,arrayListPhuongThucThanhToan);
        lvPhuongThucThanhToan.setAdapter(adapterPhuongThucThanhToan);
        getEventItems();
    }

    private void getEventItems() {
        lvPhuongThucThanhToan.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bitmap bitmap= GetImage.getBitmapFromString(getApplicationContext(),arrayListPhuongThucThanhToan.get(position));
                TransactionManager.supportCheckNull(TransactionActivity.edtChonPhuongThuc, TransactionActivity.imgChonPhuongThuc,bitmap,arrayListPhuongThucThanhToan.get(position));
                TransactionManager.supportCheckNull(DetailTransactionActivity.edtPhuongThuc, DetailTransactionActivity.imgPhuongThuc,bitmap,arrayListPhuongThucThanhToan.get(position));
                TransactionManager.supportCheckNull(EditTransactionActivity.edtPhuongThucTT, EditTransactionActivity.imgPhuongThucTT,bitmap,arrayListPhuongThucThanhToan.get(position));
                finish();
            }
        });
    }

    private void addListView() {
        arrayListPhuongThucThanhToan.add(getString(R.string.pttt_cash));
        arrayListPhuongThucThanhToan.add(getString(R.string.pttt_creditcard));
        arrayListPhuongThucThanhToan.add(getString(R.string.pttt_electronicwallet));
    }


    public void xuLyThoatPhuongThucThanhToan(View view) {
        finish();
    }
}
