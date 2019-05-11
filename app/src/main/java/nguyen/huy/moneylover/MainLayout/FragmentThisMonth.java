package nguyen.huy.moneylover.MainLayout;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import nguyen.huy.moneylover.MinhLayout.AdapterParentListView;
import nguyen.huy.moneylover.MinhLayout.XuLyChuoiThuChi;
import nguyen.huy.moneylover.MinhLayout.XuLyThuChi;
import nguyen.huy.moneylover.Model.ThuChi;
import nguyen.huy.moneylover.R;

public class FragmentThisMonth extends Fragment {
    public FragmentThisMonth() {
    }
//    AdapterThuChi adapterThuChi;
    AdapterParentListView adapterParentListView;
    List<ThuChi> listThuChi;

    //TODO : this
    ArrayList<ArrayList<ThuChi>> arrayObjest = new ArrayList<>();

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
        adapterParentListView = new AdapterParentListView(getActivity(),R.layout.minh_custom_listview_parent,arrayObjest);
        listView.setAdapter(adapterParentListView);
        String ngaythangnam=xuLyThuChi.getSimpleDateFormat().format(xuLyThuChi.getCalendar().getTime());

        String[] result= xuLyChuoiThuChi.chuyenDinhDangNgay(ngaythangnam);

        readAllDayinThisMonth(result[0]);

        txtSoTienVao=view.findViewById(R.id.txtSoTienVaoListView);
        txtSoTienRa=view.findViewById(R.id.txtSoTienRaListView);
        txtSoDu=view.findViewById(R.id.txtSoTienDuListView);

        readTienVaoTienRa(result);

        addEvents();

        return view;

    }

    private void addEvents() {

    }

    private void readAllDayinThisMonth(String thang){
        databaseReference=FirebaseDatabase.getInstance().getReference().child(xuLyThuChi.getUser()).child("Thu chi").child(thang).child("Ngày");
        //TODO : cân nhắc xoá chỗ này
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

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
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!arrayObjest.isEmpty()) arrayObjest.clear();
                for(DataSnapshot snapshot :dataSnapshot.getChildren())
                {
                    String ngay = snapshot.getKey();
                    ArrayList<ThuChi> arrThuChi = new ArrayList<>();
                    for (DataSnapshot childSnapshot : snapshot.child("Giao dịch").getChildren())
                    {
                        ThuChi thuChi = childSnapshot.getValue(ThuChi.class);
                        arrThuChi.add(thuChi);
                    }
                    arrayObjest.add(arrThuChi);

                }
                adapterParentListView.notifyDataSetChanged();
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
