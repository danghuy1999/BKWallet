package nguyen.huy.moneylover.MinhLayout;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
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
import java.util.List;

import nguyen.huy.moneylover.Model.ThuChi;
import nguyen.huy.moneylover.R;
import nguyen.huy.moneylover.Tool.GetImage;

public class AdapterChildListView extends ArrayAdapter<ThuChi> {
    Context context;
    int resource;
    List<ThuChi> objects;
    public AdapterChildListView(@NonNull Context context, int resource, @NonNull List<ThuChi> objects) {
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

        TextView txtSoTien=view.findViewById(R.id.txtSoTienLVChild);
        TextView txtNhom=view.findViewById(R.id.txtNhomLVChild);
        TextView txtGhiChu=view.findViewById(R.id.txtGhiChuLVChild);
        //TextView txtSoTienTren=view.findViewById(R.id.txtSoTienLVChild);
        ImageView imageView=view.findViewById(R.id.imgViewLVChild);

        ThuChi thuChi=objects.get(position);
        txtSoTien.setText(thuChi.getSotien()+" đ");
        txtNhom.setText(thuChi.getNhom());

        if(thuChi.getGhichu()!=""){
            txtGhiChu.setText(thuChi.getGhichu());
        }
        Bitmap bitmap= GetImage.getBitmapFromString(getContext(),thuChi.getNhom());
        imageView.setImageBitmap(bitmap);
        if(XuLyThuChi.checkMoneyIO(thuChi))
            txtSoTien.setTextColor(Color.BLUE);
        else
            txtSoTien.setTextColor(Color.RED);

        return view;
    }


}
