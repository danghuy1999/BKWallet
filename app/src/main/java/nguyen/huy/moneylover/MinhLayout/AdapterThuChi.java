package nguyen.huy.moneylover.MinhLayout;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import nguyen.huy.moneylover.Model.ThuChi;
import nguyen.huy.moneylover.R;
import nguyen.huy.moneylover.Tool.GetImage;


public class AdapterThuChi extends ArrayAdapter<String> {

    Context context;
    int resource;
    List<String> objects;
    public AdapterThuChi(@NonNull FragmentActivity context, int resource, @NonNull List<String> objects) {
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
        String nhom=objects.get(position);
        txtChonNhomListView.setText(nhom);
        Bitmap bitmap = GetImage.getBitmapFromString(context,nhom);
        imgChonNhomListView.setImageBitmap(bitmap);
        //String nhom=objects.get(position);

        //txtNgay.setText(thuChi.getNgay());


        return view;
    }

}
