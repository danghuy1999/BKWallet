package nguyen.huy.moneylover.Report.View;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.razerdp.widget.animatedpieview.AnimatedPieView;
import com.razerdp.widget.animatedpieview.AnimatedPieViewConfig;
import com.razerdp.widget.animatedpieview.callback.OnPieSelectListener;
import com.razerdp.widget.animatedpieview.data.IPieInfo;

import java.util.ArrayList;
import java.util.Collections;

import nguyen.huy.moneylover.R;
import nguyen.huy.moneylover.Report.Model.Report;
import nguyen.huy.moneylover.Report.Model.ReportDayValue;
import nguyen.huy.moneylover.Report.Controller.ReportExpandlistAdapter;
import nguyen.huy.moneylover.Report.Model.ReportHeader;
import nguyen.huy.moneylover.Tool.Convert;
import nguyen.huy.moneylover.Tool.DateConvert;
import nguyen.huy.moneylover.Tool.FirebaseTool;
import nguyen.huy.moneylover.Tool.GetImage;
import nguyen.huy.moneylover.Tool.SetupColor;

public class FragmentReportOutcome extends Fragment {
    ExpandableListView elvOutcome;
    AnimatedPieView pvOutcome;
    TextView txtOutcomeInfo;
    ImageView imgOutcomeMax;
    TextView txtOutcomeMaxType;
    TextView txtOutcomeMaxValue;

    Long maxOutcome;
    Long allValueOfOutcome;
    ArrayList<Integer> listColor;
    ArrayList<ReportHeader> headerList = new ArrayList<>();
    ReportExpandlistAdapter adapter;
    AnimatedPieViewConfig baseConfig;
    DatabaseReference reference;
    String dateString[];
    Context context;

    public FragmentReportOutcome() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_report_outcome,container,false);
        reference = FirebaseTool.baseReference();
        elvOutcome = view.findViewById(R.id.elvOutcome);
        pvOutcome = view.findViewById(R.id.pvOutcome);
        txtOutcomeInfo = view.findViewById(R.id.txtOutcomeInfo);
        imgOutcomeMax = view.findViewById(R.id.imgOutcomeMax);
        txtOutcomeMaxType = view.findViewById(R.id.txtOutcomeMaxType);
        txtOutcomeMaxValue = view.findViewById(R.id.txtOutcomeMaxValue);
        this.context = getContext();
        listColor = SetupColor.randomListOf16();

        addControls();
        addEvents();
        getReportData();
        return view;
    }
    private void addControls() {
//        elvOutcome.setTranscriptMode(ExpandableListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
//        elvOutcome.setIndicatorBounds(0,0);
//        elvOutcome.setStackFromBottom(true);
        adapter = new ReportExpandlistAdapter(getActivity(),headerList,listColor);
        elvOutcome.setAdapter(adapter);
        dateString = DateConvert.getCurrentDay();

        baseConfig = new AnimatedPieViewConfig();
        setupForPieChart();
    }
    private void setupForPieChart() {
        baseConfig.animOnTouch(true)
                .floatExpandAngle(10f)// Selected pie's angle of expansion
                .floatShadowRadius(10f)// Selected pie's shadow of expansion
                .floatUpDuration(500)
                .floatDownDuration(500)// Last selected pie's float down animation duration
//                .floatExpandSize(15)// Selected pie's size of expansion(only for pie-chart,not ring-chart)
                .strokeMode(false)// Whether to draw ring-chart(default:true)
                .strokeWidth(20)// Stroke width for ring-chart
                .duration(2500)// Animation drawing duration
                .startAngle(-90f)// Starting angle offset
                .selectListener(new OnPieSelectListener<IPieInfo>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onSelectPie(@NonNull IPieInfo pieInfo, boolean isFloatUp) {
                        if (isFloatUp)
                            txtOutcomeInfo.setText(pieInfo.getDesc()+" : "+ Convert.Money((long) pieInfo.getValue()));
                        else
                        {
                            txtOutcomeInfo.setText(getString(R.string.txt_allOutcome)+ Convert.Money(allValueOfOutcome));
                        }
                    }
                })// Click callback
                .drawText(true)// Whether to draw a text description
                .textSize(30)// Text description size
                .textMargin(8)// Margin between text and guide line
                .autoSize(true)// Auto fit chart radius
                .pieRadius(140)// Set chart radius
                .pieRadiusRatio(1)// Chart's radius ratio for parent ViewGroup
                .guidePointRadius(2)// Chart's radius
                .guideLineWidth(4)// Text guide line stroke width
                .guideLineMarginStart(5)// Guide point margin from chart
                .textGravity(AnimatedPieViewConfig.ABOVE)
                .canTouch(true)// Whether to allow the pie click to enlarge
                .splitAngle(1f)// Clearance angle
                .focusAlphaType(AnimatedPieViewConfig.FOCUS_WITH_ALPHA_REV)// Alpha change mode for selected pie
//                .interpolator(new DecelerateInterpolator())// Set animation interpolator
                .focusAlpha(150); // Alpha for selected pie (depend on focusAlphaType)
    }
    private void addEvents() {
        elvOutcome.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                setListViewHeight(groupPosition);
                return false;
            }
        });
    }
    private void setListViewHeight(int group) {
        ReportExpandlistAdapter listAdapter = (ReportExpandlistAdapter) elvOutcome.getExpandableListAdapter();
        int totalHeight = 0;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(elvOutcome.getWidth(),
                View.MeasureSpec.EXACTLY);
        for (int i = 0; i < listAdapter.getGroupCount(); i++) {
            View groupItem = listAdapter.getGroupView(i, false, null, elvOutcome);
            groupItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);

            totalHeight += groupItem.getMeasuredHeight();

            if (((elvOutcome.isGroupExpanded(i)) && (i != group))
                    || ((!elvOutcome.isGroupExpanded(i)) && (i == group))) {
                for (int j = 0; j < listAdapter.getChildrenCount(i); j++) {
                    View listItem = listAdapter.getChildView(i, j, false, null,
                            elvOutcome);
                    listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);

                    totalHeight += listItem.getMeasuredHeight();

                }
                //Add Divider Height
                totalHeight += elvOutcome.getDividerHeight() * (listAdapter.getChildrenCount(i) - 1);
            }
        }
        //Add Divider Height
        totalHeight += elvOutcome.getDividerHeight() * (listAdapter.getGroupCount() - 1);

        ViewGroup.LayoutParams params = elvOutcome.getLayoutParams();
        int height = totalHeight
                + (elvOutcome.getDividerHeight() * (listAdapter.getGroupCount() - 1));
        if (height < 10)
            height = 200;
        params.height = height;
        elvOutcome.setLayoutParams(params);
        elvOutcome.requestLayout();
    }
    private void getReportData() {
        String UID = FirebaseTool.getUserId();
        reference.child(UID).child("Thu chi").child(dateString[0])
                .child("Giao dịch ra").addListenerForSingleValueEvent(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!headerList.isEmpty()) headerList.clear();
                AnimatedPieViewConfig config = new AnimatedPieViewConfig(baseConfig);
                allValueOfOutcome = 0L;
                maxOutcome = 0L;
                int i = 0;
                for (DataSnapshot snapshot:dataSnapshot.getChildren())
                {
                    //data for expandable listview

                    ReportHeader header = new ReportHeader();
                    header.setGroup(snapshot.getKey());
                    header.setAltogether(snapshot.child("Tổng").getValue(Long.class));
                    ArrayList<ReportDayValue> dayList = new ArrayList<>();
                    for (DataSnapshot dayChild:snapshot.child("Ngày").getChildren())
                    {
                        ReportDayValue dayValue = new ReportDayValue();
                        dayValue.setValue(dayChild.getValue(Long.class));
                        String nodeDay = dayChild.getKey();
                        if (nodeDay != null) {
                            dayValue.setDay(DateConvert.firebasenode2StringDay(nodeDay));
                        }
                        dayList.add(dayValue);
                    }
                    header.setDayValueList(dayList);
                    headerList.add(header);

                    //data for piechart
                    Report report = new Report();
                    report.setPieDescription(header.getGroup());
                    report.setPieValue(header.getAltogether());
                    report.setBitmap(GetImage.getBitmapFromString(context,header.getGroup()));
                    report.setPieColor(SetupColor.getBestColor(listColor.get(i)));
                    config.addData(report);
                    i++;
                    allValueOfOutcome+=report.getPieValue();
                }
                if (!headerList.isEmpty())
                {
                    ReportHeader header = Collections.max(headerList);
                    imgOutcomeMax.setImageBitmap(GetImage.getBitmapFromString(context,header.getGroup()));
                    txtOutcomeMaxType.setText(header.getGroup());
                    txtOutcomeMaxValue.setText(Convert.Money(header.getAltogether()));
                }
                adapter.notifyDataSetChanged();
                pvOutcome.applyConfig(config);
                pvOutcome.start();
                txtOutcomeInfo.setText(getString(R.string.txt_allOutcome)+ Convert.Money(allValueOfOutcome));
                setListViewHeight(-1);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
