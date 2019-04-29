package nguyen.huy.moneylover.MainLayout;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
    TextView txtSoTienVao,txtSoTienRa,txtSoDu;
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
        readAllDayinThisMonth(result[0]);

        txtSoTienVao=view.findViewById(R.id.txtSoTienVaoListView);
        txtSoTienRa=view.findViewById(R.id.txtSoTienRaListView);
        txtSoDu=view.findViewById(R.id.txtSoTienDuListView);

        readTienVaoTienRa(result);

        return view;

    }
    //Chuyen tu dd/MM/yyyy -> dd+MM+yyyy
    private String[] xuLyChuoiThisMonth(String string){
        String[] words=string.split("[/]");
        String[] result=new String[2];
        result[0]=words[1]+ "+" + words[2];
        result[1]= words[0]+ "+" +words[1]+ "+" + words[2];
        return result;
    }
    //Tách chuối từ dd+MM+yyyy đê lấy MM+yyyy
    private String[] xuLyChuoiThisMonth2(String string){
        String[] words=string.split("[+]");
        String[] result=new String[2];
        result[0]=words[1]+ "+" + words[2];
        result[1]= words[0]+ "+" +words[1]+ "+" + words[2];
        return result;
    }

    private void readAllDayinThisMonth(String thang){
        databaseReference=FirebaseDatabase.getInstance().getReference().child("user 1").child("Thu chi").child(thang).child("Ngày");
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
        String[] result= xuLyChuoiThisMonth2(ngay);
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
    }
    //Đọc lây tiền vào tiền ra
    private void readTienVaoTienRa(String[] result){
        databaseReference=FirebaseDatabase.getInstance().getReference().child("user 1").child("Thu chi").child(result[0]);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child("Tiền vào").getValue()!=null) {
                    txtSoTienVao.setText(dataSnapshot.child("Tiền vào").getValue().toString()+" đ");
                    txtSoTienRa.setText(dataSnapshot.child("Tiền ra").getValue().toString()+" đ");
                    long sodu = Long.parseLong(dataSnapshot.child("Tiền vào").getValue().toString()) - Long.parseLong(dataSnapshot.child("Tiền ra").getValue().toString());
                    txtSoDu.setText(sodu + " đ");
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
