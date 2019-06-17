package nguyen.huy.moneylover.MainBill.View;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

import nguyen.huy.moneylover.MainBill.Model.Bill;
import nguyen.huy.moneylover.R;
import nguyen.huy.moneylover.Tool.Convert;
import nguyen.huy.moneylover.Tool.GetImage;

public class DetailBillActivity extends AppCompatActivity {

    public static final int INTENT_ACTIVITY_EDITBILL = 2703;

    ImageView imgIconBillDetail,imgTypePayDetail;
    TextView txtNameBillDetail,txtAmountBillDetail,txtDateBillDetail,txtTypePayDetail,txtNoteBillDetail;
    Bill bill;

    FirebaseAuth auth=FirebaseAuth.getInstance();
    String UserID= Objects.requireNonNull(auth.getCurrentUser()).getUid();
    FirebaseDatabase database=FirebaseDatabase.getInstance();
    DatabaseReference myRef=database.getReference().child(UserID).child("Hóa đơn");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bill_activity_detailbill);

        addActionbar();
        addControls();
        addEvents();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detailkehoach_actionbar,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.edit:{
                toPrecessEdit();
                return true;
            }
            case R.id.delete:{
                toPrecessDelete();
                return true;
            }
            case android.R.id.home:{
                onBackPressed();
                return true;
            }
            default:break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==INTENT_ACTIVITY_EDITBILL){
            if(resultCode== RESULT_OK){
                assert data != null;
                Bill bill1= (Bill) data.getSerializableExtra("BILL_EDITED" );
                Bitmap bitmap= GetImage.getBitmapFromString(DetailBillActivity.this,bill1.getGroup());
                imgIconBillDetail.setImageBitmap(bitmap);
                txtAmountBillDetail.setText(Convert.Money(Long.parseLong(bill1.getAmount())));
                txtNameBillDetail.setText(bill1.getGroup());
                txtDateBillDetail.setText(bill1.getRepeat());
                txtTypePayDetail.setText(bill1.getForm());
                Bitmap bitmap1= GetImage.getBitmapFromString(DetailBillActivity.this,bill1.getForm());
                imgTypePayDetail.setImageBitmap(bitmap1);
            }
        }
    }

    private void toPrecessDelete() {
        final AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setMessage("Bạn có muốn xóa hóa đơn này ?");
        builder.setCancelable(false);
        builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                myRef.child("Đang áp dụng").child(bill.getIdbill()).removeValue();
                DetailBillActivity.this.finish();
            }
        });
        builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        AlertDialog alertDialog=builder.create();
        alertDialog.show();
    }

    private void toPrecessEdit() {
        Intent intent=new Intent(DetailBillActivity.this, EditBillActivity.class);
        Intent intent1=getIntent();

        bill= (Bill) intent1.getSerializableExtra("BILL");
        Bill editBill=bill;
        intent.putExtra("EDITBILL",editBill);
        startActivityForResult(intent,INTENT_ACTIVITY_EDITBILL);
    }

    private void addActionbar() {
        ActionBar actionBar=getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle("Chi tiết hóa đơn");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorPrimary)));
    }

    private void addEvents() {

    }

    @SuppressLint("SetTextI18n")
    private void addControls() {
        imgIconBillDetail=findViewById(R.id.imgIconBillDetail);
        imgTypePayDetail=findViewById(R.id.imgTypePayDetail);
        txtNameBillDetail=findViewById(R.id.txtNameBillDetail);
        txtAmountBillDetail=findViewById(R.id.txtAmountBillDetail);
        txtNoteBillDetail=findViewById(R.id.txtNoteBillDetail);
        txtDateBillDetail=findViewById(R.id.txtDateBillDetail);
        txtTypePayDetail=findViewById(R.id.txtTypePayDetail);

        Intent intent=getIntent();
        bill= (Bill) intent.getSerializableExtra("BILL");

        Bitmap bitmap= GetImage.getBitmapFromString(DetailBillActivity.this,bill.getGroup());
        imgIconBillDetail.setImageBitmap(bitmap);
        txtNameBillDetail.setText(bill.getGroup());
        long amount=Long.parseLong(bill.getAmount());
        txtAmountBillDetail.setText(Convert.Money(amount));
        txtNoteBillDetail.setText(bill.getNote());
        txtDateBillDetail.setText("Hóa đơn tiếp theo là "+bill.getRepeat());
        txtTypePayDetail.setText(bill.getForm());
        Bitmap bitmap1=GetImage.getBitmapFromString(DetailBillActivity.this,bill.getForm());
        imgTypePayDetail.setImageBitmap(bitmap1);
    }
}
