package nguyen.huy.moneylover.MainSuKien;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.graphics.drawable.IconCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import nguyen.huy.moneylover.Model.SuKien;
import nguyen.huy.moneylover.R;

import static android.support.v4.graphics.drawable.IconCompat.*;

public class AdapterSuKienDangApDung extends ArrayAdapter<SuKien> {

    Activity context=null;
    int resource;
    List<SuKien> objects=null;

    public AdapterSuKienDangApDung(Activity context, int resource, List<SuKien> objects) {
        super(context, resource, objects);
        this.context= context;
        this.resource=resource;
        this.objects=objects;
    }

    @Override
    public View getView(int position,View convertView,ViewGroup parent) {
        LayoutInflater inflater=this.context.getLayoutInflater();
        convertView=inflater.inflate(this.resource,null);

        TextView txtkeHoach= convertView.<TextView>findViewById(R.id.txtkeHoach);
        ImageView imgIcon=convertView.findViewById(R.id.imgIcon);

        SuKien suKien=this.objects.get(position);
        txtkeHoach.setText(suKien.getTen());
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), suKien.getIcon());
        imgIcon.setImageResource(R.drawable.icon_not_selected);
        if(suKien.getIcon()!=0)
            imgIcon.setImageBitmap(bitmap);

        return convertView;
    }

    public SuKien getItemByID(String id){
        for(int i=0;i<objects.size();i++)
        {
            if(objects.get(i).getId().equals(id));
            return objects.get(i);
        }
        return null;
    }
}
