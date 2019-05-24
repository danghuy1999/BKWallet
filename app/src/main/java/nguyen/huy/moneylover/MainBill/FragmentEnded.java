package nguyen.huy.moneylover.MainBill;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import nguyen.huy.moneylover.Model.Bill;
import nguyen.huy.moneylover.R;

public class FragmentEnded extends Fragment {
    public FragmentEnded(){};

    View view;

    ListView lvEnded;
    ArrayList<Bill> arrBillEnded;
    AdapterEnded adapterEnded;
    List<String> keyList=new ArrayList<String>();
    FirebaseAuth auth=FirebaseAuth.getInstance();
    String UserID=auth.getCurrentUser().getUid();
    FirebaseDatabase database=FirebaseDatabase.getInstance();
    DatabaseReference myRef=database.getReference().child(UserID);


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.bill_fragment_ended, container,false);

        addControls();

        toProcessAddEditDelete();

        addEvents();

        return view;
    }

    private void toProcessAddEditDelete() {
        myRef=myRef.child("Hóa đơn").child("Đã thanh toán");
        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if(dataSnapshot.getValue()!=null) {
                    Bill bill=dataSnapshot.getValue(Bill.class);
                    arrBillEnded.add(bill);
                    keyList.add(dataSnapshot.getKey());
                    adapterEnded.notifyDataSetChanged();
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

    private void addEvents() {

    }

    private void addControls() {
        lvEnded=view.findViewById(R.id.lvEnded);
        arrBillEnded=new ArrayList<>();
        adapterEnded=new AdapterEnded(getActivity(),R.layout.bill_custom_listview_ended,arrBillEnded);
        lvEnded.setAdapter(adapterEnded);
    }
}
