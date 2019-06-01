package nguyen.huy.moneylover.MainBill.Adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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

public class AdapterPaymentForm extends ArrayAdapter<String> {
    Activity context;
    int resource;
    List<String> objects;

    public AdapterPaymentForm(Activity context, int resource, List<String> objects) {
        super(context, resource, objects);
        this.context= context;
        this.resource=resource;
        this.objects=objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater=this.context.getLayoutInflater();

        View view = inflater.inflate(resource,parent,false);
        TextView txtForm=view.findViewById(R.id.txtdetail);
        ImageView imgIc=view.findViewById(R.id.imgic);
        String phuongthuc=this.objects.get(position);
        txtForm.setText(phuongthuc);
        Bitmap bitmap = GetImage.getBitmapFromString(getContext(),phuongthuc);
        imgIc.setImageBitmap(bitmap);

        return view;
    }
}

