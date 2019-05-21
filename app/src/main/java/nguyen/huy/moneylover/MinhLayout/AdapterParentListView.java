package nguyen.huy.moneylover.MinhLayout;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.support.annotation.NonNull;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import nguyen.huy.moneylover.MainLayout.MyListview;
import nguyen.huy.moneylover.Model.ThuChi;
import nguyen.huy.moneylover.R;
import nguyen.huy.moneylover.Tool.Convert;

public class AdapterParentListView extends ArrayAdapter<ArrayList<ThuChi>> {
    Context context;
    int resource;
    ArrayList<ArrayList<ThuChi>> objests;
    public AdapterParentListView(@NonNull Context context, int resource, @NonNull ArrayList<ArrayList<ThuChi>> objects) {
        super(context, resource, objects);
        this.context=context;
        this.resource=resource;
        this.objests=objects;

    }

    XuLyThuChi xuLyThuChi=new XuLyThuChi();
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(resource,parent,false);
        MyListview lvInListview = view.findViewById(R.id.lvInListview);
        TextView txtNgayListview = view.findViewById(R.id.txtNgayLV);
        TextView txtThuListView = view.findViewById(R.id.txtThuLV);
        TextView txtThangNamLV = view.findViewById(R.id.txtThangNamLV);
        TextView txtSoTienLV = view.findViewById(R.id.txtSoTienLV);
        final ArrayList<ThuChi> item = objests.get(position);
        if(!item.isEmpty())
            xuLyDinhDangNgay(txtNgayListview,txtThuListView,txtThangNamLV,item.get(0));
        AdapterChildListView adapterChildListView = new AdapterChildListView( context,R.layout.minh_custom_listview_child,item);
        lvInListview.setAdapter(adapterChildListView);
        lvInListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent;
                intent=new Intent(context.getApplicationContext(), DocActivity.class);

                intent.putExtra("Item",item.get(position));
                context.startActivity(intent);
            }
        });
        if(!item.isEmpty())
            getMoneyRefunDay(txtSoTienLV,item.get(0));
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

    //Đọc số tiền dư trong ngày

    private void getMoneyRefunDay(final TextView txtSoTienLV, ThuChi thuChi){
        final String[] result=XuLyChuoiThuChi.chuyenDinhDangNgay(thuChi.getNgay());
        final DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference().child(XuLyThuChi.user).child("Thu chi").child(result[0]).child("Ngày").child(result[1]);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child("Tiền vào").getValue()!=null && dataSnapshot.child("Tiền ra").getValue()!=null) {
                    long tienvao = Long.parseLong(dataSnapshot.child("Tiền vào").getValue().toString());
                    long tienra = Long.parseLong(dataSnapshot.child("Tiền ra").getValue().toString());
                    long tiendu = tienvao - tienra;
                    txtSoTienLV.setText(Convert.Money(tiendu));
                    txtSoTienLV.setTextColor(Color.BLACK);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
