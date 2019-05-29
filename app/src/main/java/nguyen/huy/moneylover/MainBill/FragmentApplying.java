package nguyen.huy.moneylover.MainBill;

import android.annotation.SuppressLint;
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
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import nguyen.huy.moneylover.Model.Bill;
import nguyen.huy.moneylover.R;
import nguyen.huy.moneylover.Tool.Convert;

public class FragmentApplying extends Fragment {
    public FragmentApplying(){}

    View view;
    long totalMoneyFuture=0;
    TextView txtMoneyTotal,txtMoneyFuture;
    ListView lvApplyingFuture;
    ArrayList<Bill>arrBillApplying;
    AdapterApplying adapterApplying;
    List<String> keyList= new ArrayList<>();
    FirebaseAuth auth=FirebaseAuth.getInstance();
    String UserID= Objects.requireNonNull(auth.getCurrentUser()).getUid();
    FirebaseDatabase database=FirebaseDatabase.getInstance();
    DatabaseReference myRef=database.getReference().child(UserID);

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.bill_fragment_applying, container,false);

        addControls();

        toProcessDisplayTotalAmount();
        toProcessAddEditDelete();

        addEvents();

        return view;
    }

    private void toProcessDisplayTotalAmount() {
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                txtMoneyTotal.setText(Convert.Money(Long.parseLong(
                        Objects.requireNonNull(dataSnapshot.child("Balance").getValue()).toString())));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void toProcessAddEditDelete() {
        myRef=myRef.child("Hóa đơn").child("Đang áp dụng");
        myRef.addChildEventListener(new ChildEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Bill bill=dataSnapshot.getValue(Bill.class);
                totalMoneyFuture+=Long.parseLong(Objects.requireNonNull(bill).getAmount());
                txtMoneyFuture.setText(Convert.Money(totalMoneyFuture));
                arrBillApplying.add(bill);
                keyList.add(dataSnapshot.getKey());
                adapterApplying.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Bill bill=dataSnapshot.getValue(Bill.class);
                String key=dataSnapshot.getKey();
                int index=keyList.indexOf(key);
                arrBillApplying.set(index,bill);
                adapterApplying.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                int index=keyList.indexOf(dataSnapshot.getKey());
                Bill bill=arrBillApplying.get(index);
                totalMoneyFuture-=Long.parseLong(bill.getAmount());
                txtMoneyFuture.setText(Convert.Money(totalMoneyFuture));
                arrBillApplying.remove(index);
                keyList.remove(index);
                adapterApplying.notifyDataSetChanged();
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
        lvApplyingFuture.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(getActivity(),ActivityDetailBill.class);
                intent.putExtra("BILL",arrBillApplying.get(position));
                startActivity(intent);
            }
        });
    }

    private void addControls() {
        txtMoneyTotal=view.findViewById(R.id.txtMoneyTotal);
        txtMoneyFuture=view.findViewById(R.id.txtMoneyFuture);
        lvApplyingFuture=view.findViewById(R.id.lvApplyingFuture);
        arrBillApplying=new ArrayList<>();
        adapterApplying=new AdapterApplying(getActivity(),R.layout.bill_custom_listview_applying,arrBillApplying);
        lvApplyingFuture.setAdapter(adapterApplying);
    }


}
