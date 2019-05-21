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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

import nguyen.huy.moneylover.MinhLayout.AdapterParentListView;
import nguyen.huy.moneylover.MinhLayout.XuLyChuoiThuChi;
import nguyen.huy.moneylover.MinhLayout.XuLyThuChi;
import nguyen.huy.moneylover.Model.ThuChi;
import nguyen.huy.moneylover.R;
import nguyen.huy.moneylover.Report.ReportActivity;
import nguyen.huy.moneylover.Tool.Convert;

public class FragmentThisMonth extends Fragment {
    public FragmentThisMonth() {
    }
//    AdapterThuChi adapterThuChi;
    AdapterParentListView adapterParentListView;
    //TODO : this
    ArrayList<ArrayList<ThuChi>> arrayObject = new ArrayList<>();
    ListView listView;
    DatabaseReference databaseReference;
    TextView txtSoTienVao,txtSoTienRa,txtSoDu;
    String[] result;
    LinearLayout lyReport;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_this_month, container,false);

        listView=view.findViewById(R.id.listGiaoDichThuChi);
        adapterParentListView = new AdapterParentListView(getActivity(),R.layout.minh_custom_listview_parent, arrayObject);
        listView.setAdapter(adapterParentListView);
        String ngaythangnam=XuLyThuChi.simpleDateFormat.format(XuLyThuChi.calendar.getTime());
        lyReport = view.findViewById(R.id.lyReport);
        result= XuLyChuoiThuChi.chuyenDinhDangNgay(ngaythangnam);

        readAllDayinThisMonth(result[0]);

        txtSoTienVao=view.findViewById(R.id.txtSoTienVaoListView);
        txtSoTienRa=view.findViewById(R.id.txtSoTienRaListView);
        txtSoDu=view.findViewById(R.id.txtSoTienDuListView);

        readTienVaoTienRa(result);

        addEvents();

        XuLyThuChi.setBalance();

        return view;

    }

    private void addEvents() {
        lyReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ReportActivity.class);
                intent.putExtra("ValueThisMonth", arrayObject);
                startActivity(intent);
            }
        });

    }

    private void readAllDayinThisMonth(final String thang){
        databaseReference=FirebaseDatabase.getInstance().getReference().child(XuLyThuChi.user).child("Thu chi").child(thang).child("Ngày");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                long tienvaothang=0;
                long tienrathang=0;
                if (!arrayObject.isEmpty()) arrayObject.clear();
                for(DataSnapshot snapshot :dataSnapshot.getChildren())
                {
                    long tienvaongay=0;
                    long tienrangay=0;
                    String ngay = snapshot.getKey();
                    ArrayList<ThuChi> arrThuChi = new ArrayList<>();
                    for (DataSnapshot childSnapshot : snapshot.child("Giao dịch").getChildren())
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

                    tienvaothang=tienvaothang+tienvaongay;
                    tienrathang=tienrathang+tienrangay;
                    if(tienvaongay==0 && tienrangay==0) {
                        DatabaseReference dt = FirebaseDatabase.getInstance().getReference().child(XuLyThuChi.user).child("Thu chi").child(thang).child("Ngày").child(ngay);
                        dt.child("Tiền vào").setValue(null);
                        dt.child("Tiền ra").setValue(null);
                        break;
                    }

                    arrayObject.add(arrThuChi);

                    XuLyThuChi.CapNhatTienVaoTrongNgay(XuLyChuoiThuChi.chuyenDinhDangNgayLayThang(ngay),tienvaongay);
                    XuLyThuChi.CapNhatTienRaTrongNgay(XuLyChuoiThuChi.chuyenDinhDangNgayLayThang(ngay),tienrangay);


                }
                XuLyThuChi.CapNhatTienVao(thang,tienvaothang);
                XuLyThuChi.CapNhatTienRa(thang,tienrathang);
                Collections.reverse(arrayObject);
                adapterParentListView.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    //Đọc lây tiền vào tiền ra
    private void readTienVaoTienRa(String[] result){
        databaseReference=FirebaseDatabase.getInstance().getReference().child(XuLyThuChi.user).child("Thu chi").child(result[0]);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child("Tiền vào").getValue()!=null && dataSnapshot.child("Tiền ra").getValue()!=null) {
                    long tienvao=Long.parseLong(dataSnapshot.child("Tiền vào").getValue().toString());
                    long tienra=Long.parseLong(dataSnapshot.child("Tiền ra").getValue().toString());
                    txtSoTienVao.setText(Convert.Money(tienvao));
                    txtSoTienRa.setText(Convert.Money(tienra));
                    long sodu = tienvao-tienra;
                    txtSoDu.setText(Convert.Money(sodu));
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
