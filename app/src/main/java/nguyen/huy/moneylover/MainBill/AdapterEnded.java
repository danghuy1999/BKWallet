package nguyen.huy.moneylover.MainBill;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;
import java.util.Objects;

import nguyen.huy.moneylover.Model.Bill;
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
    private DatabaseReference myRef=database.getReference().child(UserID).child("Hóa đơn").child("Đã thanh toán");

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

        final Bill bill=this.objects.get(position);

        Bitmap bitmap= GetImage.getBitmapFromString(getContext(),bill.getGroup());
        imgIconEnded.setImageBitmap(bitmap);
        txtNameBill.setText(bill.getGroup());
        txtNote.setText(bill.getNote());
        long amount=Long.parseLong(bill.getAmount());
        txtAmount.setText(Convert.Money(amount));

        return convertView;
    }
}
