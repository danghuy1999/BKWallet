package nguyen.huy.moneylover.Report;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import nguyen.huy.moneylover.R;
import nguyen.huy.moneylover.Tool.GetImage;

public class ReportExpandlistAdapter extends BaseExpandableListAdapter {
    private Activity context;
    private List<ReportHeader> headerList;

    public ReportExpandlistAdapter(Activity context, List<ReportHeader> headerList) {
        this.context = context;
        this.headerList = headerList;
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
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View view = inflater.inflate(R.layout.custom_report_expandlist_header,null);
        ImageView imgExListHeaderIcon = view.findViewById(R.id.imgExListHeaderIcon);
        TextView txtExListHeaderType = view.findViewById(R.id.txtExListHeaderType);
        TextView txtExListHeaderValue = view.findViewById(R.id.txtExListHeaderValue);
        ReportHeader header = headerList.get(groupPosition);
        txtExListHeaderType.setText(header.getGroup());
        txtExListHeaderValue.setText(header.getAltogether()+"");
        Bitmap bitmap = GetImage.getBitmapFromString(context,header.getGroup());
        imgExListHeaderIcon.setImageBitmap(bitmap);
        return view;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View view = inflater.inflate(R.layout.custom_report_expandlist_item,null);
        TextView txtExlistItemDay = view.findViewById(R.id.txtExlistItemDay);
        TextView txtExlistItemValue = view.findViewById(R.id.txtExlistItemValue);
        ReportDayValue dayValue = headerList.get(groupPosition).getDayValueList().get(childPosition);
        txtExlistItemDay.setText(dayValue.getDay());
        txtExlistItemValue.setText(dayValue.getValue()+"");
        return view;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
