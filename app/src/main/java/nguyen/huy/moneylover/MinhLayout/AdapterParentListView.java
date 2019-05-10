package nguyen.huy.moneylover.MinhLayout;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.support.annotation.NonNull;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import nguyen.huy.moneylover.Model.ThuChi;
import nguyen.huy.moneylover.R;

public class AdapterParentListView extends ArrayAdapter<ThuChi> {
    Context context;
    int resource;
    List<ThuChi> objects;
    public AdapterParentListView(@NonNull Context context, int resource, @NonNull List objects) {
        super(context, resource, objects);
        this.context=context;
        this.resource=resource;
        this.objects=objects;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(resource,parent,false);
        ListView lvInListview = view.findViewById(R.id.lvInListview);
        TextView txtNgayListview = view.findViewById(R.id.txtNgayLV);
        TextView txtThuListView = view.findViewById(R.id.txtThuLV);
        TextView txtThangNamLV = view.findViewById(R.id.txtThangNamLV);

//        AdapterThuChi adapterThuChi = new AdapterThuChi(context,R.layout.minh_custom_listview,objects);
        return view;
    }
}
