package nguyen.huy.moneylover.MainLayout;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
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
import nguyen.huy.moneylover.MinhLayout.DocActivity;
import nguyen.huy.moneylover.MinhLayout.ThuChiActivity;
import nguyen.huy.moneylover.MinhLayout.XuLyChuoiThuChi;
import nguyen.huy.moneylover.MinhLayout.XuLyThuChi;
import nguyen.huy.moneylover.Model.ThuChi;
import nguyen.huy.moneylover.R;

public class FragmentThisMonth extends Fragment {
    public FragmentThisMonth() {
    }
    AdapterThuChi adapterThuChi;
    List<ThuChi> listThuChi;
    ListView listView;
    DatabaseReference databaseReference;
    TextView txtSoTienVao,txtSoTienRa,txtSoDu;
    XuLyChuoiThuChi xuLyChuoiThuChi=new XuLyChuoiThuChi();
    XuLyThuChi xuLyThuChi=new XuLyThuChi();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_this_month, container,false);

        listView=view.findViewById(R.id.listGiaoDichThuChi);
        listThuChi=new ArrayList<>();
        adapterThuChi=new AdapterThuChi(getActivity(),R.layout.minh_custom_listview,listThuChi);
        listView.setAdapter(adapterThuChi);
        String ngaythangnam=xuLyThuChi.getSimpleDateFormat().format(xuLyThuChi.getCalendar().getTime());

        String[] result= xuLyChuoiThuChi.chuyenDinhDangNgay(ngaythangnam);

        readAllDayinThisMonth(result[0]);

        txtSoTienVao=view.findViewById(R.id.txtSoTienVaoListView);
        txtSoTienRa=view.findViewById(R.id.txtSoTienRaListView);
        txtSoDu=view.findViewById(R.id.txtSoTienDuListView);

        readTienVaoTienRa(result);

        //readAllDayinThisMonth(result[0]);

        addEvents();

        return view;

    }

    private void addEvents() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(getActivity(), DocActivity.class);
                intent.putExtra("Item",adapterThuChi.getItem(position));
                startActivity(intent);
            }
        });
    }

    private void readAllDayinThisMonth(String thang){
        databaseReference=FirebaseDatabase.getInstance().getReference().child(xuLyThuChi.getUser()).child("Thu chi").child(thang).child("Ngày");
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if(dataSnapshot.getKey()!=null) {
                    String ngay = dataSnapshot.getKey();
                    hienThiTungNgayLenListView(ngay);
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                /*if(dataSnapshot.getKey()!=null) {
                    String ngay = dataSnapshot.getKey();
                    hienThiTungNgayLenListView(ngay);
                }*/
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void hienThiTungNgayLenListView(String ngay){
        String[] result= xuLyChuoiThuChi.chuyenDinhDangNgayLayThang(ngay);
        databaseReference= FirebaseDatabase.getInstance().getReference().child(xuLyThuChi.getUser()).child("Thu chi").child(result[0]).child("Ngày").child(result[1]).child("Giao dịch");
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
                /*ThuChi thuChi=dataSnapshot.getValue(ThuChi.class);
                listThuChi.remove(thuChi);
                adapterThuChi.notifyDataSetChanged();
                listView.invalidateViews();*/
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    //Đọc lây tiền vào tiền ra
    private void readTienVaoTienRa(String[] result){
        databaseReference=FirebaseDatabase.getInstance().getReference().child(xuLyThuChi.getUser()).child("Thu chi").child(result[0]);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child("Tiền vào").getValue()!=null && dataSnapshot.child("Tiền ra").getValue()!=null) {
                    txtSoTienVao.setText(dataSnapshot.child("Tiền vào").getValue().toString()+" đ");
                    txtSoTienRa.setText(dataSnapshot.child("Tiền ra").getValue().toString()+" đ");
                    long sodu = Long.parseLong(dataSnapshot.child("Tiền vào").getValue().toString()) - Long.parseLong(dataSnapshot.child("Tiền ra").getValue().toString());
                    txtSoDu.setText(sodu + " đ");
                    txtSoTienVao.setTextColor(Color.BLUE);
                    txtSoTienRa.setTextColor(Color.RED);
                    txtSoDu.setTextColor(Color.BLACK);
                }
                /*else if(dataSnapshot.child("Tiền vào").getValue()!=null && dataSnapshot.child("Tiền ra").getValue()==null){
                    txtSoTienVao.setText(dataSnapshot.child("Tiê"));
                }*/
                else{
                    txtSoTienRa.setText("0 đ");
                    txtSoTienVao.setText("0 đ");
                    txtSoDu.setText("0 đ");
                    txtSoTienVao.setTextColor(Color.BLUE);
                    txtSoTienRa.setTextColor(Color.RED);
                    txtSoDu.setTextColor(Color.BLACK);
                    databaseReference.child("Tiền vào").setValue(0);
                    databaseReference.child("Tiền ra").setValue(0);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
