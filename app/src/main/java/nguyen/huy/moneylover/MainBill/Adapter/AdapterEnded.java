package nguyen.huy.moneylover.MainBill.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
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
import java.util.Objects;

import nguyen.huy.moneylover.MainBill.Model.Bill;
import nguyen.huy.moneylover.R;
import nguyen.huy.moneylover.Tool.Convert;
import nguyen.huy.moneylover.Tool.GetImage;

public class AdapterEnded extends ArrayAdapter<Bill> {

    private Activity context=null;
    private int resource;
    private List<Bill> objects=null;

    private FirebaseAuth auth=FirebaseAuth.getInstance();
    private String UserID= Objects.requireNonNull(auth.getCurrentUser()).getUid();
    private FirebaseDatabase database=FirebaseDatabase.getInstance();
    private DatabaseReference myRef=database.getReference().child(UserID).child("Hóa đơn");

    public AdapterEnded(Activity context, int resource, List<Bill> objects) {
        super(context, resource, objects);
        this.context= context;
        this.resource=resource;
        this.objects=objects;
    }

    @SuppressLint("ViewHolder")
    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater=this.context.getLayoutInflater();
        convertView=inflater.inflate(this.resource,null);

        ImageView imgIconEnded=convertView.findViewById(R.id.imgIcEnded);
        TextView txtNameBill=convertView.findViewById(R.id.txtNameBill);
        TextView txtNote=convertView.findViewById(R.id.txtNote);
        TextView txtAmount=convertView.findViewById(R.id.txtAmount);
        ImageView imgDelete=convertView.findViewById(R.id.imgDelete);

        final Bill bill=this.objects.get(position);

        Bitmap bitmap= GetImage.getBitmapFromString(getContext(),bill.getGroup());
        imgIconEnded.setImageBitmap(bitmap);
        txtNameBill.setText(bill.getGroup());
        txtNote.setText(bill.getNote());
        long amount=Long.parseLong(bill.getAmount());
        txtAmount.setText(Convert.Money(amount));
        imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
                builder.setMessage("Bạn có muốn xóa hóa đơn này không?");
                builder.setCancelable(false);
                builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Bill bill1=bill;
                        myRef.child("Đã thanh toán").child(bill1.getIdbill()).removeValue();
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
        });

        return convertView;
    }
}
