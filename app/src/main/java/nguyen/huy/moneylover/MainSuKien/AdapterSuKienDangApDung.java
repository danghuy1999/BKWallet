package nguyen.huy.moneylover.MainSuKien;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import nguyen.huy.moneylover.Model.SuKien;
import nguyen.huy.moneylover.R;

public class AdapterSuKienDangApDung extends ArrayAdapter<SuKien> {

    private Activity context;
    private int resource;
    private List<SuKien> objects;

    AdapterSuKienDangApDung(Activity context, int resource, List<SuKien> objects) {
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

        TextView txtkeHoach= convertView.findViewById(R.id.txtkeHoach);
        ImageView imgIcon=convertView.findViewById(R.id.imgIcon);

        SuKien suKien=this.objects.get(position);
        txtkeHoach.setText(suKien.getTen());
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), suKien.getIcon());
        imgIcon.setImageResource(R.drawable.icon_not_selected);
        if(suKien.getIcon()!=0)
            imgIcon.setImageBitmap(bitmap);

        return convertView;
    }

}
