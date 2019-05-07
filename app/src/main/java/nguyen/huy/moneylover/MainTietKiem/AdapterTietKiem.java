package nguyen.huy.moneylover.MainTietKiem;


import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import nguyen.huy.moneylover.Model.TietKiem;
import nguyen.huy.moneylover.R;

public class AdapterTietKiem extends ArrayAdapter<TietKiem> {
    Activity context=null;
    int resource;
    List<TietKiem> objects=null;

    public AdapterTietKiem(Activity context, int resource, List<TietKiem> objects) {
        super(context, resource, objects);
        this.context= context;
        this.resource=resource;
        this.objects=objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater=this.context.getLayoutInflater();
        convertView=inflater.inflate(this.resource,null);

        TextView txtTietKiem= convertView.<TextView>findViewById(R.id.txtMucDichTietKiem);
        TextView txtSoTienConThieu = convertView.<TextView>findViewById(R.id.txtSoTienConThieu);

        TietKiem tk=this.objects.get(position);
        txtTietKiem.setText(tk.getMucDichTietKiem());
        int soThieu = Integer.parseInt(tk.getMucTieuTietKiem().toString()) - Integer.parseInt(tk.getSoTienHienCo().toString());
        txtSoTienConThieu.setText(Integer.toString(soThieu));
        return convertView;
    }
}
