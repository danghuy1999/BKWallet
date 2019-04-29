package nguyen.huy.moneylover.MinhLayout;

import android.content.Context;
import android.content.res.Resources;
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
import java.util.Date;
import java.util.List;
import java.util.Locale;

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
        TextView txtNgay=view.findViewById(R.id.txtNgayListView);
        TextView txtThu=view.findViewById(R.id.txtThuListView);
        TextView txtThangNam=view.findViewById(R.id.txtThangNamListView);
        TextView txtGhiChu=view.findViewById(R.id.txtGhiChuListView);
        TextView txtSoTienTren=view.findViewById(R.id.txtSoTienTrenListView);
        ImageView imageView=view.findViewById(R.id.imgViewListView);

        ThuChi thuChi=objects.get(position);
        txtSoTien.setText(thuChi.getSotien()+" đ");
        txtNhom.setText(thuChi.getNhom());
        //txtNgay.setText(thuChi.getNgay());

        xuLyDinhDangNgay(txtNgay,txtThu,txtThangNam,thuChi);

        if(thuChi.getGhichu()!=""){
            txtGhiChu.setText(thuChi.getGhichu());
        }
        Resources res= getContext().getResources();
        if(thuChi.getNhom().equals("Tiền lãi")) {
            Drawable drawable = res.getDrawable(R.drawable.tienlai);
            imageView.setImageDrawable(drawable);
            txtSoTien.setTextColor(Color.BLUE);
            txtSoTienTren.setText(thuChi.getSotien() + " đ");
        }
        else if(thuChi.getNhom().equals("Gửi tiền")){
            Drawable drawable=res.getDrawable((R.drawable.guitien));
            imageView.setImageDrawable(drawable);
            txtSoTien.setTextColor(Color.BLUE);
            txtSoTienTren.setText(thuChi.getSotien() + " đ");
        }
        else if(thuChi.getNhom().equals("Rút tiền")){
            Drawable drawable=res.getDrawable(R.drawable.ruttien);
            imageView.setImageDrawable(drawable);
            txtSoTien.setTextColor(Color.RED);
            txtSoTienTren.setText("-" + thuChi.getSotien() +" đ");
        }

        return view;
    }

    private void xuLyDinhDangNgay(TextView txtNgay,TextView txtThu,TextView txtThangNam,ThuChi thuChi){
        String[] words=thuChi.getNgay().split("[/]");
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("EEEE");
        int ngay=Integer.parseInt(words[0]);
        int thang=Integer.parseInt(words[1]);
        int nam=Integer.parseInt(words[2]);

        txtNgay.setText(words[0]);
        txtThangNam.setText("tháng "+words[1]+" "+words[2]);
        Calendar calendar=Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH,ngay);
        calendar.set(Calendar.MONTH,thang-1);
        calendar.set(Calendar.YEAR,nam);
        String dayname=simpleDateFormat.format(calendar.getTime());
        txtThu.setText(dayname);
    }

}
