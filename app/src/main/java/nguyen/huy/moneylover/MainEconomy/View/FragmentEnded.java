package nguyen.huy.moneylover.MainEconomy.View;

import android.app.Dialog;
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
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import nguyen.huy.moneylover.MainActivity;
import nguyen.huy.moneylover.MainEconomy.Adapter.AdapterEconomyApplying;
import nguyen.huy.moneylover.MainEconomy.Model.Economy;
import nguyen.huy.moneylover.R;
import nguyen.huy.moneylover.Tool.AddSaveMoney;

public class FragmentEnded extends Fragment implements ChildEventListener {

    public FragmentEnded(){}

    View view;
    ListView lvEconomyEnded;
    Button btnMakeThuChi;
    Button btnCloseDialog;
    private Dialog dialog;
    ArrayList<Economy> arrEconomyEnded;
    AdapterEconomyApplying adapterEconomyEnded;
    List<String> keyList=new ArrayList<String>();
    FirebaseAuth auth=FirebaseAuth.getInstance();
    String UserID=auth.getCurrentUser().getUid();
    FirebaseDatabase database=FirebaseDatabase.getInstance();
    DatabaseReference myRef=database.getReference().child(UserID).child("Tiết kiệm").child("Đã kết thúc");

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.hoang_fragment_ended,container, false);

        addControls();
        addEvents();
        return view;
    }

    private void addEvents() {
        myRef.addChildEventListener(FragmentEnded.this);

        lvEconomyEnded.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                final Economy economy = adapterEconomyEnded.getItem(position);
                btnMakeThuChi.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(MainActivity.balance > (Long.parseLong(economy.getMucTieuTietKiem()) - Long.parseLong(economy.getSoTienHienCo()))) {
                            AddSaveMoney.SaveMoney(economy.getNgayThangNam(), economy.getMucDichTietKiem(), Long.parseLong(economy.getMucTieuTietKiem()) - Long.parseLong(economy.getSoTienHienCo()));
                            myRef.child(economy.getTietKiemID()).removeValue();
                            dialog.dismiss();
                        }
                        else{
                            Toast.makeText(getContext(), "Không đủ số dư để thanh toán khoản tiết kiệm này :(", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                btnCloseDialog.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });
    }

    private void addControls() {
        dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.hoang_make_thuchi);
        btnCloseDialog = dialog.findViewById(R.id.btnXemLaiTabKetThucTietKiem);
        btnMakeThuChi = dialog.findViewById(R.id.btnTaoThuChiTuTietKiem);
        lvEconomyEnded=view.findViewById(R.id.lvEconomyEnded);
        arrEconomyEnded=new ArrayList<>();
        adapterEconomyEnded= new AdapterEconomyApplying(
                getActivity(),R.layout.hoang_item_economy,arrEconomyEnded);
        lvEconomyEnded.setAdapter(adapterEconomyEnded);
    }


    @Override
    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
        Economy economy =dataSnapshot.getValue(Economy.class);
        arrEconomyEnded.add(economy);
        keyList.add(dataSnapshot.getKey());
        if(arrEconomyEnded.size()>=0)
            adapterEconomyEnded.notifyDataSetChanged();
    }

    @Override
    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
        Economy economy =dataSnapshot.getValue(Economy.class);
        String key=dataSnapshot.getKey();
        int index=keyList.indexOf(key);
        arrEconomyEnded.set(index, economy);
        adapterEconomyEnded.notifyDataSetChanged();
    }

    @Override
    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
        int index=keyList.indexOf(dataSnapshot.getKey());
        arrEconomyEnded.remove(index);
        keyList.remove(index);
        if(arrEconomyEnded.size()>=0)
            adapterEconomyEnded.notifyDataSetChanged();
    }

    @Override
    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {

    }
}
