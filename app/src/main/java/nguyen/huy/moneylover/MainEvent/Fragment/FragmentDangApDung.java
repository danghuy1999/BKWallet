package nguyen.huy.moneylover.MainEvent.Fragment;

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
import java.util.Objects;

import nguyen.huy.moneylover.MainEvent.Adapter.AdapterEventApplying;
import nguyen.huy.moneylover.MainEvent.View.DetailEventActivity;
import nguyen.huy.moneylover.MainEvent.Model.Event;
import nguyen.huy.moneylover.R;

public class FragmentDangApDung extends Fragment implements ChildEventListener {

    public FragmentDangApDung(){
    }

    View view;
    ListView lvSuKienDangApDung;
    ArrayList<Event> arrSuKienDangApDung;
    AdapterEventApplying adapterSuKienDangApDung;
    List<String> keyList=new ArrayList<>();
    FirebaseAuth auth=FirebaseAuth.getInstance();
    String UserID= Objects.requireNonNull(auth.getCurrentUser()).getUid();
    FirebaseDatabase database=FirebaseDatabase.getInstance();
    DatabaseReference myRef=database.getReference().child(UserID).child("Sự kiện").child("Đang áp dụng");

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.truong_fragment_dangapdung,container, false);

        addControls();
        addEvents();

        return view;
    }

    private void addEvents() {
        myRef.addChildEventListener(FragmentDangApDung.this);

        lvSuKienDangApDung.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(getActivity(), DetailEventActivity.class);
                intent.putExtra("SUKIEN",arrSuKienDangApDung.get(position));
                startActivity(intent);
            }
        });
    }

    private void addControls() {
        lvSuKienDangApDung=view.findViewById(R.id.lvkeHoachDangApDung);
        arrSuKienDangApDung=new ArrayList<>();
        adapterSuKienDangApDung=new AdapterEventApplying(
                getActivity(),R.layout.truong_item_kehoach,arrSuKienDangApDung);
        lvSuKienDangApDung.setAdapter(adapterSuKienDangApDung);
    }

    @Override
    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
        Event suKien=dataSnapshot.getValue(Event.class);
        arrSuKienDangApDung.add(suKien);
        keyList.add(dataSnapshot.getKey());
        adapterSuKienDangApDung.notifyDataSetChanged();
    }

    @Override
    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
        Event suKien=dataSnapshot.getValue(Event.class);
        String key=dataSnapshot.getKey();
        int index=keyList.indexOf(key);
        arrSuKienDangApDung.set(index,suKien);
        adapterSuKienDangApDung.notifyDataSetChanged();
    }

    @Override
    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
        int index=keyList.indexOf(dataSnapshot.getKey());
        arrSuKienDangApDung.remove(index);
        keyList.remove(index);
        adapterSuKienDangApDung.notifyDataSetChanged();
    }

    @Override
    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {

    }
}
