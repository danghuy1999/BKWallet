package nguyen.huy.moneylover.MainLayout;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import nguyen.huy.moneylover.MinhLayout.AdapterThuChi;
import nguyen.huy.moneylover.MinhLayout.ThuChiActivity;
import nguyen.huy.moneylover.Model.ThuChi;
import nguyen.huy.moneylover.R;

public class FragmentThisMonth extends Fragment {
    public FragmentThisMonth() {
    }
    AdapterThuChi adapterThuChi;
    List<ThuChi> listThuChi;
    ListView listView;
    DatabaseReference databaseReference;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_this_month, container,false);

        listView=view.findViewById(R.id.listGiaoDichThuChi);
        listThuChi=new ArrayList<>();
        adapterThuChi=new AdapterThuChi(getActivity(),R.layout.minh_custom_listview,listThuChi);
        listView.setAdapter(adapterThuChi);
        String ngaythangnam=ThuChiActivity.simpleDateFormat.format(ThuChiActivity.calendar.getTime());
        String[] result= xuLyChuoiThisMonth(ngaythangnam);
        databaseReference= FirebaseDatabase.getInstance().getReference().child("user 1").child("Thu chi").child(result[0]).child("Ngày").child(result[1]).child("Giao dịch");
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if(dataSnapshot.getValue()!=null) {
                    ThuChi thuChi = dataSnapshot.getValue(ThuChi.class);
                    listThuChi.add(thuChi);
                    adapterThuChi.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return view;

    }
    private String[] xuLyChuoiThisMonth(String string){
        String[] words=string.split("[/]");
        String[] result=new String[2];
        result[0]=words[1]+ "+" + words[2];
        result[1]= words[0]+ "+" +words[1]+ "+" + words[2];
        return result;
    }
}
