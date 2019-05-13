package nguyen.huy.moneylover.MainLayout;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
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
    //TODO : this
    ArrayList<ArrayList<ThuChi>> arrayObjest = new ArrayList<>();
    ListView listView;
    DatabaseReference databaseReference;
    TextView txtSoTienVao,txtSoTienRa,txtSoDu;
    XuLyChuoiThuChi xuLyChuoiThuChi=new XuLyChuoiThuChi();
    XuLyThuChi xuLyThuChi=new XuLyThuChi();
    String[] result;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_this_month, container,false);

        listView=view.findViewById(R.id.listGiaoDichThuChi);
        adapterParentListView = new AdapterParentListView(getActivity(),R.layout.minh_custom_listview_parent,arrayObjest);
        listView.setAdapter(adapterParentListView);
        String ngaythangnam=xuLyThuChi.getSimpleDateFormat().format(xuLyThuChi.getCalendar().getTime());

        result= xuLyChuoiThuChi.chuyenDinhDangNgay(ngaythangnam);

        readAllDayinThisMonth(result[0]);

        txtSoTienVao=view.findViewById(R.id.txtSoTienVaoListView);
        txtSoTienRa=view.findViewById(R.id.txtSoTienRaListView);
        txtSoDu=view.findViewById(R.id.txtSoTienDuListView);

        readTienVaoTienRa(result);

        addEvents();

        xuLyThuChi.setBalance();

        return view;

    }

    private void addEvents() {

    }

    private void readAllDayinThisMonth(final String thang){
        databaseReference=FirebaseDatabase.getInstance().getReference().child(xuLyThuChi.getUser()).child("Thu chi").child(thang).child("Ngày");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                long tienvaothang=0;
                long tienrathang=0;
                if (!arrayObjest.isEmpty()) arrayObjest.clear();
                for(DataSnapshot snapshot :dataSnapshot.getChildren())
                {
                    long tienvaongay=0;
                    long tienrangay=0;
                    String ngay = snapshot.getKey();
                    ArrayList<ThuChi> arrThuChi = new ArrayList<>();
                    for (DataSnapshot childSnapshot : snapshot.child("Giao dịch vào").getChildren())
                    {
                        ThuChi thuChi = childSnapshot.getValue(ThuChi.class);
                        if(XuLyThuChi.checkMoneyIO(thuChi)){
                            tienvaongay=tienvaongay+Long.parseLong(thuChi.getSotien());
                        }
                        else{
                            tienrangay=tienrangay+Long.parseLong(thuChi.getSotien());
                        }
                        arrThuChi.add(thuChi);
                    }
                    for(DataSnapshot childSnapshot:snapshot.child("Giao dịch ra").getChildren()){
                        ThuChi thuChi = childSnapshot.getValue(ThuChi.class);
                        if(XuLyThuChi.checkMoneyIO(thuChi)){
                            tienvaongay=tienvaongay+Long.parseLong(thuChi.getSotien());
                        }
                        else{
                            tienrangay=tienrangay+Long.parseLong(thuChi.getSotien());
                        }
                        arrThuChi.add(thuChi);
                    }
                    tienvaothang=tienvaothang+tienvaongay;
                    tienrathang=tienrathang+tienrangay;
                    if(tienvaongay==0 && tienrangay==0) {
                        DatabaseReference dt = FirebaseDatabase.getInstance().getReference().child(xuLyThuChi.getUser()).child("Thu chi").child(thang).child("Ngày").child(ngay);
                        dt.child("Tiền vào").setValue(null);
                        dt.child("Tiền ra").setValue(null);
                        break;
                    }

                    arrayObjest.add(arrThuChi);

                    xuLyThuChi.CapNhatTienVaoTrongNgay(xuLyChuoiThuChi.chuyenDinhDangNgayLayThang(ngay),tienvaongay);
                    xuLyThuChi.CapNhatTienRaTrongNgay(xuLyChuoiThuChi.chuyenDinhDangNgayLayThang(ngay),tienrangay);


                }
                xuLyThuChi.CapNhatTienVao(thang,tienvaothang);
                xuLyThuChi.CapNhatTienRa(thang,tienrathang);
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
                else{
                    txtSoTienRa.setText("0 đ");
                    txtSoTienVao.setText("0 đ");
                    txtSoDu.setText("0 đ");
                    txtSoTienVao.setTextColor(Color.BLUE);
                    txtSoTienRa.setTextColor(Color.RED);
                    txtSoDu.setTextColor(Color.BLACK);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
