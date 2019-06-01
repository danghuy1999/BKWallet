package nguyen.huy.moneylover.MainEvent.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import java.util.List;

import nguyen.huy.moneylover.R;

public class AdapterSymbol extends ArrayAdapter<Integer> {
    private Activity context;
    private int resource;
    private List<Integer> objects;

    public AdapterSymbol(Activity context, int resource, List<Integer> objects) {
        super(context, resource, objects);
        this.context=context;
        this.resource=resource;
        this.objects=objects;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater=this.context.getLayoutInflater();
        @SuppressLint("ViewHolder") View row=inflater.inflate(this.resource,null);
        ImageView img= row.findViewById(R.id.imgIconEvent);
        img.setImageResource(this.objects.get(position));
        return row;
    }
}
