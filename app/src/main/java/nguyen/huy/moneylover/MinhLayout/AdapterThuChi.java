package nguyen.huy.moneylover.MinhLayout;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import nguyen.huy.moneylover.MainLayout.FragmentThisMonth;
import nguyen.huy.moneylover.Model.ThuChi;
import nguyen.huy.moneylover.R;

public class AdapterThuChi extends ArrayAdapter<ThuChi> {

    Context context;
    int resource;
    List<ThuChi> objects;
    public AdapterThuChi(@NonNull FragmentActivity context, int resource, @NonNull List<ThuChi> objects) {
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

        TextView txtSoTien=view.findViewById(R.id.txtSoTienListView);
        TextView txtNhom=view.findViewById(R.id.txtNhomListView);

        ThuChi thuChi=objects.get(position);
        txtSoTien.setText(thuChi.getNhapSoTien());
        txtNhom.setText(thuChi.getChonNhom());

        return view;
    }
}
