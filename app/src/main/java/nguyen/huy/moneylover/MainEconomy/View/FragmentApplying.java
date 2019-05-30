package nguyen.huy.moneylover.MainEconomy.View;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import nguyen.huy.moneylover.MainEconomy.Adapter.AdapterEconomyApplying;
import nguyen.huy.moneylover.MainEconomy.Model.Economy;
import nguyen.huy.moneylover.R;

public class FragmentApplying extends Fragment implements ChildEventListener {

    public FragmentApplying(){}

    View view;
    ListView lvEconomyApplying;
    ArrayList<Economy> arrEconomyAppling;
    AdapterEconomyApplying adapterEconomyApplying;
    Economy economy;
    List<String> keyList=new ArrayList<String>();
    FirebaseAuth auth=FirebaseAuth.getInstance();
    String UserID=auth.getCurrentUser().getUid();
    FirebaseDatabase database=FirebaseDatabase.getInstance();
    DatabaseReference myRef=database.getReference().child(UserID).child("Tiết kiệm").child("Đang áp dụng");


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.hoang_fragment_applying,container, false);

        addControls();
        addEvents();

        return view;
    }

    private void addEvents() {
        myRef.addChildEventListener(FragmentApplying.this);

        lvEconomyApplying.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                economy = adapterEconomyApplying.getItem(position);
                Intent intent=new Intent(getActivity(), EditAndDeleteEconomy.class);
                intent.putExtra("TietKiemID", economy.getTietKiemID());
                startActivity(intent);
            }
        });
    }

    private void addControls() {
        lvEconomyApplying=view.findViewById(R.id.lvEconomyApplying);
        arrEconomyAppling=new ArrayList<>();
        adapterEconomyApplying=new AdapterEconomyApplying(
                getActivity(),R.layout.hoang_item_economy,arrEconomyAppling);
        lvEconomyApplying.setAdapter(adapterEconomyApplying);
    }

    @Override
    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
        Economy economy = dataSnapshot.getValue(Economy.class);
        arrEconomyAppling.add(economy);
        keyList.add(dataSnapshot.getKey());
        if(arrEconomyAppling.size()>0)
            adapterEconomyApplying.notifyDataSetChanged();
    }

    @Override
    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
        Economy economy =dataSnapshot.getValue(Economy.class);
        String key=dataSnapshot.getKey();
        int index=keyList.indexOf(key);
        arrEconomyAppling.set(index, economy);
        adapterEconomyApplying.notifyDataSetChanged();
    }

    @Override
    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
        int index=keyList.indexOf(dataSnapshot.getKey());
        arrEconomyAppling.remove(index);
        keyList.remove(index);
        if(arrEconomyAppling.size()>=0)
            adapterEconomyApplying.notifyDataSetChanged();
    }

    @Override
    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {

    }
}
