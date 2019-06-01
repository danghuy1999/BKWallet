package nguyen.huy.moneylover.MainBill.Adapter;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import nguyen.huy.moneylover.Transaction.Controller.ReportDatabaseManager;
import nguyen.huy.moneylover.Transaction.Controller.TransactionManager;
import nguyen.huy.moneylover.MainBill.Model.Bill;
import nguyen.huy.moneylover.Transaction.Model.Transaction;
import nguyen.huy.moneylover.R;
import nguyen.huy.moneylover.Tool.Convert;
import nguyen.huy.moneylover.Tool.DateConvert;
import nguyen.huy.moneylover.Tool.GetImage;

public class AdapterApplying extends ArrayAdapter<Bill> {

    private Activity context=null;
    private int resource;
    private List<Bill> objects=null;

    ImageView imgIcApplying;
    TextView txtNameBill;
    TextView txtNote;
    TextView txtText1;
    TextView btnPay;

    private FirebaseAuth auth=FirebaseAuth.getInstance();
    private String UserID=auth.getCurrentUser().getUid();
    private FirebaseDatabase database=FirebaseDatabase.getInstance();
    private DatabaseReference myRef=database.getReference().child(UserID).child("Hóa đơn");

    public AdapterApplying(Activity context, int resource, List<Bill> objects) {
        super(context, resource, objects);
        this.context= context;
        this.resource=resource;
        this.objects=objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater=this.context.getLayoutInflater();
        convertView=inflater.inflate(this.resource,null);

        imgIcApplying=convertView.findViewById(R.id.imgIcApplying);
        txtNameBill=convertView.findViewById(R.id.txtNameBill);
        txtText1=convertView.findViewById(R.id.txtText1);
        btnPay=convertView.findViewById(R.id.btnPay);

        final Bill bill=this.objects.get(position);

        Bitmap bitmap= GetImage.getBitmapFromString(getContext(),bill.getGroup());
        imgIcApplying.setImageBitmap(bitmap);
        txtNameBill.setText(bill.getGroup());
        txtText1.setText("Hóa đơn tiếp theo là "+bill.getRepeat());
        long amount=Long.parseLong(bill.getAmount());
        btnPay.setText(" TRẢ "+ Convert.Money(amount)+" ");

        btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
                builder.setMessage("Bạn có muốn trả hóa đơn này không?");
                builder.setCancelable(false);
                builder.setPositiveButton("Trả", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Bill bill1=bill;
                        myRef.child("Đã thanh toán").child(bill1.getIdbill()).setValue(bill1);
                        myRef.child("Đang áp dụng").child(bill1.getIdbill()).removeValue();
                        Transaction transaction = new Transaction();
                        transaction.setNhom(context.getResources().getString(R.string.ts_bill));
                        transaction.setGhichu(bill1.getNote());
                        transaction.setSotien(bill1.getAmount());
                        String[] result= DateConvert.getCurrentDay();
                        transaction.setNgay(result[2]);
                        TransactionManager.SaveTransactionToDatabase(transaction,result);
                        ReportDatabaseManager.SaveToDatabase(transaction);
                    }
                });
                builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                AlertDialog alertDialog=builder.create();
                alertDialog.show();

            }
        });

        return convertView;
    }

}
