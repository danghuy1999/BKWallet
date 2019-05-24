package nguyen.huy.moneylover.MainBill;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import nguyen.huy.moneylover.Model.Bill;
import nguyen.huy.moneylover.R;
import nguyen.huy.moneylover.Tool.Convert;
import nguyen.huy.moneylover.Tool.GetImage;

public class AdapterApplying extends ArrayAdapter<Bill> {

    private Activity context=null;
    private int resource;
    private List<Bill> objects=null;

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

        ImageView imgIcApplying=convertView.findViewById(R.id.imgIcApplying);
        TextView txtNameBill=convertView.findViewById(R.id.txtNameBill);
        TextView txtNote=convertView.findViewById(R.id.txtNote);
        TextView txtText1=convertView.findViewById(R.id.txtText1);
        Button btnPay=convertView.findViewById(R.id.btnPay);

        final Bill bill=this.objects.get(position);

        Bitmap bitmap= GetImage.getBitmapFromString(getContext(),bill.getGroup());
        imgIcApplying.setImageBitmap(bitmap);
        txtNameBill.setText(bill.getGroup());
        txtNote.setText(bill.getNote());
        txtText1.setText("Hóa đơn tiếp theo là "+bill.getRepeat());
        long amount=Long.parseLong(bill.getAmount());
        btnPay.setText("TRẢ "+ Convert.Money(amount));

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
