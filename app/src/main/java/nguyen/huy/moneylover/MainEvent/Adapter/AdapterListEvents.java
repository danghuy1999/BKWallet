package nguyen.huy.moneylover.MainEvent.Adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import nguyen.huy.moneylover.MainEvent.Model.Event;
import nguyen.huy.moneylover.R;


public class AdapterListEvents extends ArrayAdapter<Event> {

    private Activity context;
    private int resource;
    private List<Event> objects;

    public AdapterListEvents(Activity context, int resource, List<Event> objects) {
        super(context, resource, objects);
        this.context= context;
        this.resource=resource;
        this.objects=objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater=this.context.getLayoutInflater();

        View view = inflater.inflate(resource,parent,false);
        TextView txtNameEvent=view.findViewById(R.id.txtdetail);
        ImageView imgic=view.findViewById(R.id.imgic);

        Event event=this.objects.get(position);
        txtNameEvent.setText(event.getTen());
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), event.getIcon());
        imgic.setImageBitmap(bitmap);


        return view;
    }
}
