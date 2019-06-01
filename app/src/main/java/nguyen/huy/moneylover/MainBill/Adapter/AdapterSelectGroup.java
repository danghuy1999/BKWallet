package nguyen.huy.moneylover.MainBill.Adapter;

import android.graphics.Bitmap;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import nguyen.huy.moneylover.R;
import nguyen.huy.moneylover.Tool.GetImage;

public class AdapterSelectGroup extends ArrayAdapter<String> {

    FragmentActivity context;
    int resource;
    List<String> objects;

    public AdapterSelectGroup(FragmentActivity context, int resource, List<String> objects) {
        super(context, resource, objects);
        this.context= context;
        this.resource=resource;
        this.objects=objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater=this.context.getLayoutInflater();

        View view = inflater.inflate(resource,parent,false);
        TextView txtGroup=view.findViewById(R.id.txtdetail);
        ImageView imgic=view.findViewById(R.id.imgic);

        String namegroup=this.objects.get(position);
        txtGroup.setText(namegroup);
        Bitmap bitmap= GetImage.getBitmapFromString(getContext(),namegroup);
        imgic.setImageBitmap(bitmap);

        return view;
    }
}
