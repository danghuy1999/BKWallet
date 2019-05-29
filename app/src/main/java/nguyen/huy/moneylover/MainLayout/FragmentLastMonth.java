package nguyen.huy.moneylover.MainLayout;

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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import nguyen.huy.moneylover.Transaction.Model.Transaction;
import nguyen.huy.moneylover.Transaction.Adapter.AdapterParentListView;
import nguyen.huy.moneylover.Transaction.Controller.TransactionManager;
import nguyen.huy.moneylover.Transaction.Controller.DayTimeManager;
import nguyen.huy.moneylover.R;

public class FragmentLastMonth extends Fragment implements FirebaseAuth.AuthStateListener{
    public FragmentLastMonth() {
    }

    AdapterParentListView adapterParentListView;
    ArrayList<ArrayList<Transaction>> arrayObjest = new ArrayList<>();
    ListView listView;
    DatabaseReference databaseReference;
    TextView txtSoTienVao,txtSoTienRa,txtSoDu;
    LinearLayout lyReportLastMonth;
    FirebaseAuth auth;
    FirebaseUser user;
    String[] result;

    @Override
    public void onStart() {
        super.onStart();
        auth.addAuthStateListener(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        auth.removeAuthStateListener(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_last_month,container,false);

        auth=FirebaseAuth.getInstance();
        user=auth.getCurrentUser();
        listView=view.findViewById(R.id.listGiaoDichThuChiLastMonth);
        adapterParentListView=new AdapterParentListView(getActivity(),R.layout.minh_custom_listview_parent,arrayObjest);
        listView.setAdapter(adapterParentListView);
        String ngaythangnam= TransactionManager.simpleDateFormat.format(TransactionManager.calendar.getTime());

        result= DayTimeManager.splitDayGetPreviousMonth(ngaythangnam);
        readAllDayinThisMonth(result[0]);

        txtSoTienVao=view.findViewById(R.id.txtSoTienVaoListViewLastMonth);
        txtSoTienRa=view.findViewById(R.id.txtSoTienRaListViewLastMonth);
        txtSoDu=view.findViewById(R.id.txtSoTienDuListViewLastMonth);

        //lyReportLastMonth=view.findViewById(R.id.lyReportLastMonth);

        readTienVaoTienRa(result);

        //addEvents();

        TransactionManager.setBalance();

        return view;
    }

    /*private void addEvents() {
        lyReportLastMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), ReportActivity.class);
                intent.putExtra("ValueThisMonth",arrayObjest);
                startActivity(intent);
            }
        });
    }*/

    private void readAllDayinThisMonth(final String thang){
        databaseReference=FirebaseDatabase.getInstance().getReference().child(TransactionManager.user).child("Thu chi").child(thang).child("Ngày");
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
                    ArrayList<Transaction> arrTransaction = new ArrayList<>();
                    for (DataSnapshot childSnapshot : snapshot.child("Giao dịch").getChildren())
                    {
                        Transaction transaction = childSnapshot.getValue(Transaction.class);
                        if(TransactionManager.checkMoneyIO(transaction)){
                            tienvaongay=tienvaongay+Long.parseLong(transaction.getSotien());
                        }
                        else{
                            tienrangay=tienrangay+Long.parseLong(transaction.getSotien());
                        }
                        arrTransaction.add(transaction);
                    }

                    tienvaothang=tienvaothang+tienvaongay;
                    tienrathang=tienrathang+tienrangay;
                    if(tienvaongay==0 && tienrangay==0) {
                        DatabaseReference dt = FirebaseDatabase.getInstance().getReference().child(TransactionManager.user).child("Thu chi").child(thang).child("Ngày").child(ngay);
                        dt.child("Tiền vào").setValue(null);
                        dt.child("Tiền ra").setValue(null);
                        break;
                    }

                    arrayObjest.add(arrTransaction);

                    TransactionManager.UpdateMoneyOutcomeInDay(DayTimeManager.ConvertFormatDayGetMonth(ngay),tienvaongay);
                    TransactionManager.UpdateMoneyIncomeDay(DayTimeManager.ConvertFormatDayGetMonth(ngay),tienrangay);


                }
                TransactionManager.UpdateMoneyIncomeMonth(thang,tienvaothang);
                TransactionManager.UpdateMoneyOutcomeMonth(thang,tienrathang);
                adapterParentListView.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    //Đọc lây tiền vào tiền ra
    private void readTienVaoTienRa(String[] result){
        databaseReference=FirebaseDatabase.getInstance().getReference().child(TransactionManager.user).child("Thu chi").child(result[0]);
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

    @Override
    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
        user=FirebaseAuth.getInstance().getCurrentUser();
        if(user!=null){
            readAllDayinThisMonth(result[0]);
            readTienVaoTienRa(result);
            TransactionManager.setBalance();
        }
    }
}
