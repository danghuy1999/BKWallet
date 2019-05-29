package nguyen.huy.moneylover.Transaction.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import nguyen.huy.moneylover.Transaction.Model.Transaction;
import nguyen.huy.moneylover.Transaction.Controller.TransactionManager;
import nguyen.huy.moneylover.R;
import nguyen.huy.moneylover.Tool.Convert;
import nguyen.huy.moneylover.Tool.GetImage;

public class AdapterChildListView extends ArrayAdapter<Transaction> {
    Context context;
    int resource;
    List<Transaction> objects;
    public AdapterChildListView(@NonNull Context context, int resource, @NonNull List<Transaction> objects) {
        super(context, resource, objects);

        this.context=context;
        this.resource=resource;
        this.objects=objects;
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view=layoutInflater.inflate(resource,parent,false);

        TextView txtSoTien=view.findViewById(R.id.txtSoTienLVChild);
        TextView txtNhom=view.findViewById(R.id.txtNhomLVChild);
        TextView txtGhiChu=view.findViewById(R.id.txtGhiChuLVChild);
        //TextView txtSoTienTren=view.findViewById(R.id.txtSoTienLVChild);
        ImageView imageView=view.findViewById(R.id.imgViewLVChild);

        Transaction transaction =objects.get(position);
        Long sotien=Long.parseLong(transaction.getSotien());
        txtSoTien.setText(Convert.Money(sotien));
        txtNhom.setText(transaction.getNhom());

        if(transaction.getGhichu()!=""){
            txtGhiChu.setText(transaction.getGhichu());
        }
        Bitmap bitmap= GetImage.getBitmapFromString(getContext(), transaction.getNhom());
        imageView.setImageBitmap(bitmap);
        if(TransactionManager.checkMoneyIO(transaction))
            txtSoTien.setTextColor(Color.BLUE);
        else
            txtSoTien.setTextColor(Color.RED);

        return view;
    }


}
