package nguyen.huy.moneylover.MainSuKien;

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

import nguyen.huy.moneylover.Model.SuKien;
import nguyen.huy.moneylover.R;

public class FragmentDaKetThuc extends Fragment implements ChildEventListener {

    public FragmentDaKetThuc(){
    }

    View view;
    ListView lvSuKienDaKetThuc;
    ArrayList<SuKien> arrSuKienDaKetThuc;
    AdapterSuKienDangApDung adapterSuKienDaKetThuc;
    List<String> keyList=new ArrayList<String>();
    FirebaseAuth auth=FirebaseAuth.getInstance();
    String UserID=auth.getCurrentUser().getUid();
    FirebaseDatabase database=FirebaseDatabase.getInstance();
    DatabaseReference myRef=database.getReference().child(UserID).child("Sự kiện").child("Đã kết thúc");

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.truong_fragment_daketthuc,container, false);

        addControls();
        addEvents();
        return view;
    }

    private void addEvents() {
        myRef.addChildEventListener(FragmentDaKetThuc.this);

        lvSuKienDaKetThuc.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(getActivity(), ActivityChiTietSuKien.class);
                intent.putExtra("SUKIEN",arrSuKienDaKetThuc.get(position));
                startActivity(intent);
            }
        });
    }

    private void addControls() {
        lvSuKienDaKetThuc=view.findViewById(R.id.lvKeHoachDaKetThuc1);
        arrSuKienDaKetThuc=new ArrayList<>();
        adapterSuKienDaKetThuc=new AdapterSuKienDangApDung(
                getActivity(),R.layout.truong_item_kehoach,arrSuKienDaKetThuc);
        lvSuKienDaKetThuc.setAdapter(adapterSuKienDaKetThuc);
    }

    @Override
    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
        SuKien suKien=dataSnapshot.getValue(SuKien.class);
        arrSuKienDaKetThuc.add(suKien);
        keyList.add(dataSnapshot.getKey());
        if(arrSuKienDaKetThuc.size()>=0)
            adapterSuKienDaKetThuc.notifyDataSetChanged();
    }

    @Override
    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
        SuKien suKien=dataSnapshot.getValue(SuKien.class);
        String key=dataSnapshot.getKey();
        int index=keyList.indexOf(key);
        arrSuKienDaKetThuc.set(index,suKien);
        adapterSuKienDaKetThuc.notifyDataSetChanged();
    }

    @Override
    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
        int index=keyList.indexOf(dataSnapshot.getKey());
        arrSuKienDaKetThuc.remove(index);
        keyList.remove(index);
        if(arrSuKienDaKetThuc.size()>=0)
            adapterSuKienDaKetThuc.notifyDataSetChanged();
    }

    @Override
    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {

    }
}
