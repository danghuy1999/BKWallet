package nguyen.huy.moneylover.Report.Controller;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import nguyen.huy.moneylover.R;
import nguyen.huy.moneylover.Report.Model.ReportDayValue;
import nguyen.huy.moneylover.Report.Model.ReportHeader;
import nguyen.huy.moneylover.Tool.Convert;
import nguyen.huy.moneylover.Tool.GetImage;
import nguyen.huy.moneylover.Tool.SetupColor;

public class ReportExpandlistAdapter extends BaseExpandableListAdapter {
    private Activity context;
    private List<ReportHeader> headerList;
    private ArrayList<Integer> listColor;
    public ReportExpandlistAdapter(Activity context, List<ReportHeader> headerList, ArrayList<Integer> listColor) {
        this.context = context;
        this.headerList = headerList;
        this.listColor = listColor;
    }

    @Override
    public int getGroupCount() {
        return this.headerList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this.headerList.get(groupPosition).getDayValueList().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.headerList.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return this.headerList.get(groupPosition).getDayValueList().get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        view = inflater.inflate(R.layout.custom_report_expandlist_header,parent,false);
        ImageView imgExListHeaderIcon = view.findViewById(R.id.imgExListHeaderIcon);
        TextView txtExListHeaderType = view.findViewById(R.id.txtExListHeaderType);
        TextView txtExListHeaderValue = view.findViewById(R.id.txtExListHeaderValue);
        LinearLayout llyHeader = view.findViewById(R.id.llyHeader);
        ReportHeader header = headerList.get(groupPosition);
        txtExListHeaderType.setText(header.getGroup());
        txtExListHeaderValue.setText(Convert.Money(header.getAltogether()));
        Bitmap bitmap = GetImage.getBitmapFromString(context,header.getGroup());
        imgExListHeaderIcon.setImageBitmap(bitmap);
        llyHeader.setBackgroundResource(R.drawable.custom_header_report);
        GradientDrawable drawable = (GradientDrawable) llyHeader.getBackground();
        drawable.setColor(SetupColor.getBestColor(listColor.get(groupPosition)));
        return view;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        @SuppressLint("InflateParams") View view = inflater.inflate(R.layout.custom_report_expandlist_item,null);
        TextView txtExlistItemDay = view.findViewById(R.id.txtExlistItemDay);
        TextView txtExlistItemValue = view.findViewById(R.id.txtExlistItemValue);
        ReportDayValue dayValue = headerList.get(groupPosition).getDayValueList().get(childPosition);
        LinearLayout llyItem = view.findViewById(R.id.llyItem);
        txtExlistItemDay.setText(dayValue.getDay());
        txtExlistItemValue.setText(Convert.Money(dayValue.getValue()));
        llyItem.setBackgroundResource(R.drawable.custom_item_report);
        GradientDrawable drawable = (GradientDrawable) llyItem.getBackground();
        drawable.setColor(SetupColor.getBestColor(listColor.get(groupPosition)));
        return view;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
