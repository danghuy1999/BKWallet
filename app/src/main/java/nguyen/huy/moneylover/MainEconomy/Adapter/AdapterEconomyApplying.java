package nguyen.huy.moneylover.MainEconomy.Adapter;

import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import nguyen.huy.moneylover.MainActivity;
import nguyen.huy.moneylover.MainEconomy.Model.Economy;
import nguyen.huy.moneylover.R;

import static nguyen.huy.moneylover.Tool.Convert.Money;

public class AdapterEconomyApplying extends ArrayAdapter<Economy> {
    Activity context;
    int resource;
    List<Economy> objects;
    private long balance= MainActivity.balance;


    public AdapterEconomyApplying(Activity context, int resource, List<Economy> objects) {
        super(context, resource, objects);
        this.context= context;
        this.resource=resource;
        this.objects=objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater=this.context.getLayoutInflater();
        convertView=inflater.inflate(this.resource,null);

        TextView tvTrangThai = convertView.<TextView>findViewById(R.id.tvTrangThai);
        TextView txtTietKiem= convertView.<TextView>findViewById(R.id.txtMucDichTietKiem);
        TextView txtSoTienConThieu = convertView.<TextView>findViewById(R.id.txtSoTienConThieu);

        Economy tk=this.objects.get(position);
        txtTietKiem.setText(tk.getMucDichTietKiem());
        Long soThieu = Long.parseLong(tk.getMucTieuTietKiem()) - Long.parseLong(tk.getSoTienHienCo());
        txtSoTienConThieu.setText(Money(soThieu));
        if(balance < soThieu) {
            tvTrangThai.setText("Chưa hoàn thành");
            tvTrangThai.setTextColor(Color.RED);
        }
        else{
            tvTrangThai.setText("Đã hoàn thành");
            tvTrangThai.setTextColor(Color.BLUE);
        }
        return convertView;
    }

    public Economy getTietKiemByID(String ID)
    {
        for (Economy economy : objects)
        {
            if (economy.getTietKiemID().equals(ID))
                return economy;
        }
        return null;
    }
}
