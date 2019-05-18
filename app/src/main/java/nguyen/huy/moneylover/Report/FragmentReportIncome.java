package nguyen.huy.moneylover.Report;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.ScrollView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import nguyen.huy.moneylover.MainLayout.MyExpandableListview;
import nguyen.huy.moneylover.R;
import nguyen.huy.moneylover.Tool.DateConvert;
import nguyen.huy.moneylover.Tool.FirebaseTool;

public class FragmentReportIncome extends Fragment {
    ScrollView scvIncome;
    View vieFooter;
    ArrayList<ReportHeader> headerList = new ArrayList<>();
    MyExpandableListview elvIncome;
    ReportExpandlistAdapter adapter;
    DatabaseReference reference;
    String dateString[];
    public FragmentReportIncome() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_report_income,container,false);
        reference = FirebaseTool.baseReference();
        scvIncome = view.findViewById(R.id.scvIncome);
        elvIncome = view.findViewById(R.id.elvIncome);
        adapter = new ReportExpandlistAdapter(getActivity(),headerList);
        elvIncome.setAdapter(adapter);
        vieFooter = view.findViewById(R.id.vieFooter);
        addControls();
        addEvents();
        dateString = DateConvert.getCurrentDay();
        getReportData();

        return view;
    }

    private void addControls() {
        elvIncome.setTranscriptMode(ExpandableListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        elvIncome.setIndicatorBounds(0,0);
        elvIncome.setChildIndicatorBounds(0,0);
        elvIncome.setStackFromBottom(true);
    }

    private void addEvents() {
        elvIncome.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                scvIncome.refreshDrawableState();
                return false;
            }
        });
    }


    private void getReportData() {
        String UID = FirebaseTool.getUserId();
        reference.child(UID).child("Thu chi").child(dateString[0])
                .child("Giao dịch vào").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.e("gothere?","yes");
                if (!headerList.isEmpty()) headerList.clear();
                for (DataSnapshot snapshot:dataSnapshot.getChildren())
                {
                    ReportHeader header = new ReportHeader();
                    header.setGroup(snapshot.getKey());
                    header.setAltogether(snapshot.child("Tổng").getValue(Long.class));
                    ArrayList<ReportDayValue> dayList = new ArrayList<>();
                    for (DataSnapshot dayChild:snapshot.child("Ngày").getChildren())
                    {
                        ReportDayValue dayValue = new ReportDayValue();
                        dayValue.setValue(dayChild.getValue(Long.class));
                        String nodeDay = dayChild.getKey();
                        dayValue.setDay(DateConvert.firebasenode2StringDay(nodeDay));
                        dayList.add(dayValue);
                    }
                    header.setDayValueList(dayList);
                    headerList.add(header);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
