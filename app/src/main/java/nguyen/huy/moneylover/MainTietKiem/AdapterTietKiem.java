package nguyen.huy.moneylover.MainTietKiem;


import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import nguyen.huy.moneylover.Model.tietKiem;
import nguyen.huy.moneylover.R;

public class AdapterTietKiem extends ArrayAdapter<tietKiem> {
    Activity context=null;
    int resource;
    List<tietKiem> objects=null;

    public AdapterTietKiem(Activity context, int resource, List<tietKiem> objects) {
        super(context, resource, objects);
        this.context= context;
        this.resource=resource;
        this.objects=objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater=this.context.getLayoutInflater();
        convertView=inflater.inflate(this.resource,null);

        TextView txtkeHoach= convertView.<TextView>findViewById(R.id.txtMucDichTietKiem);

        tietKiem tk=this.objects.get(position);
        txtkeHoach.setText(tk.getMucDichTietKiem());
        return convertView;
    }
}
