package nguyen.huy.moneylover.MainSuKien;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import java.util.List;

import nguyen.huy.moneylover.R;

public class AdapterBieuTuong extends ArrayAdapter<Integer> {
    Activity context;
    int resource;
    List<Integer> objects;

    public AdapterBieuTuong(Activity context, int resource, List<Integer> objects) {
        super(context, resource, objects);
        this.context=context;
        this.resource=resource;
        this.objects=objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater=this.context.getLayoutInflater();
        View row=inflater.inflate(this.resource,null);
        ImageView img= row.<ImageView>findViewById(R.id.imgIconEvent);
        img.setImageResource(this.objects.get(position));
        return row;
    }
}
