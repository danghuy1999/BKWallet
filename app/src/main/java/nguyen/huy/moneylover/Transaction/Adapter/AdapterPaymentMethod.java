package nguyen.huy.moneylover.Transaction.Adapter;


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

import java.util.List;

import nguyen.huy.moneylover.R;
import nguyen.huy.moneylover.Tool.GetImage;

public class AdapterPaymentMethod extends ArrayAdapter<String> {

    Context context;
    int resource;
    List<String> objects;
    public AdapterPaymentMethod(@NonNull Context context, int resource, @NonNull List<String> objects) {
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
        TextView txtChonNhomListView=view.findViewById(R.id.txtChonNhomListView);
        ImageView imgChonNhomListView=view.findViewById(R.id.imgChonNhomListView);
        String phuongthuc=objects.get(position);
        txtChonNhomListView.setText(phuongthuc);
        Bitmap bitmap = GetImage.getBitmapFromString(context,phuongthuc);
        imgChonNhomListView.setImageBitmap(bitmap);

        return view;
    }
}
