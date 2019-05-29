package nguyen.huy.moneylover.Report.Model;

import java.util.List;

@SuppressWarnings("WeakerAccess")
public class ReportHeader implements Comparable<ReportHeader> {
    private String group;
    private List<ReportDayValue> dayValueList;
    private Long altogether;

    public ReportHeader() {
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public List<ReportDayValue> getDayValueList() {
        return dayValueList;
    }

    public void setDayValueList(List<ReportDayValue> dayValueList) {
        this.dayValueList = dayValueList;
    }

    public Long getAltogether() {
        return altogether;
    }

    public void setAltogether(Long altogether) {
        this.altogether = altogether;
    }

    @Override
    public int compareTo(ReportHeader h2) {
        if (this.altogether>h2.altogether) return 1;
        else if (this.altogether.equals(h2.altogether)) return 0;
        else return -1;
    }
}
