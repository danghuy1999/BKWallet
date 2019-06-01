package nguyen.huy.moneylover.MainBill.View;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import nguyen.huy.moneylover.MainBill.Adapter.AdapterPaymentForm;
import nguyen.huy.moneylover.R;

public class PaymentFormActivity extends AppCompatActivity {


    ListView lvPaymentForm;
    AdapterPaymentForm adapterPaymentForm;
    ArrayList<String> arrayListPhuongThucThanhToan=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bill_activity_payment_form);

        addActionbar();
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

    private void addActionbar() {
        ActionBar actionBar=getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle("Chọn hình thức thanh toán");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorPrimary)));
    }

    private void addEvents() {
        lvPaymentForm.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent();
                Intent intentCode=getIntent();
                int code = intentCode.getIntExtra("CODE",-1);
                if(code==1)
                    intent.setClass(PaymentFormActivity.this, AddBillActivity.class);
                else if(code==2)
                    intent.setClass(PaymentFormActivity.this, EditBillActivity.class);
                intent.putExtra("Form",arrayListPhuongThucThanhToan.get(position));
                Log.e("pos",arrayListPhuongThucThanhToan.get(position)+"");
                setResult(Activity.RESULT_OK,intent);
                finish();
            }
        });
    }

    private void addControls() {
        lvPaymentForm=findViewById(R.id.lvPaymentForm);

        arrayListPhuongThucThanhToan.add(getString(R.string.pttt_cash));
        arrayListPhuongThucThanhToan.add(getString(R.string.pttt_creditcard));
        arrayListPhuongThucThanhToan.add(getString(R.string.pttt_electronicwallet));

        adapterPaymentForm=new AdapterPaymentForm(PaymentFormActivity.this,R.layout.bill_item_select_group,arrayListPhuongThucThanhToan);
        lvPaymentForm.setAdapter(adapterPaymentForm);
    }

}
