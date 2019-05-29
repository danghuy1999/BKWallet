package nguyen.huy.moneylover.MainLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
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

import nguyen.huy.moneylover.MainBill.ActivityDetailBill;
import nguyen.huy.moneylover.MainBill.AdapterApplying;
import nguyen.huy.moneylover.Model.Bill;
import nguyen.huy.moneylover.R;
import nguyen.huy.moneylover.Tool.Convert;

public class FragmentFuture extends Fragment {
    public FragmentFuture() {}
    View view;

    ListView lvFuture;
    TextView txtMoneyIn,txtMoneyOut,txtMoneyAbout;
    long totalBalance=0;
    long totalMoneyFuture=0;
    long totalMoneyAbout=0;

    ArrayList<Bill>arrBillApplying;
    AdapterApplying adapterApplying;
    List<String> keyList= new ArrayList<>();
    FirebaseAuth auth=FirebaseAuth.getInstance();
    String UserID= Objects.requireNonNull(auth.getCurrentUser()).getUid();
    FirebaseDatabase database=FirebaseDatabase.getInstance();
    DatabaseReference myRef=database.getReference().child(UserID);

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_future, container,false);

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
                if(dataSnapshot.child("Balance").getValue()!=null)
                    totalBalance=Long.parseLong(Objects.requireNonNull(dataSnapshot.child("Balance").getValue()).toString());
                txtMoneyIn.setText(Convert.Money(totalBalance));
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
                txtMoneyOut.setText(Convert.Money(totalMoneyFuture));
                totalMoneyAbout=totalBalance-totalMoneyFuture;
                txtMoneyAbout.setText(Convert.Money(totalMoneyAbout));
                arrBillApplying.add(bill);
                keyList.add(dataSnapshot.getKey());
                txtMoneyIn.setTextColor(Color.BLUE);
                txtMoneyOut.setTextColor(Color.RED);
                txtMoneyAbout.setTextColor(Color.BLACK);
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
                txtMoneyOut.setText(Convert.Money(totalMoneyFuture));
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
        lvFuture.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(getActivity(), ActivityDetailBill.class);
                intent.putExtra("BILL",arrBillApplying.get(position));
                startActivity(intent);
            }
        });
    }

    private void addControls() {
        txtMoneyIn=view.findViewById(R.id.txtMoneyIn);
        txtMoneyOut=view.findViewById(R.id.txtMoneyOut);
        txtMoneyAbout=view.findViewById(R.id.txtMoneyAbout);
        lvFuture=view.findViewById(R.id.lvFuture);
        arrBillApplying=new ArrayList<>();
        adapterApplying=new AdapterApplying(getActivity(),R.layout.bill_custom_listview_applying,arrBillApplying);
        lvFuture.setAdapter(adapterApplying);
    }
}
