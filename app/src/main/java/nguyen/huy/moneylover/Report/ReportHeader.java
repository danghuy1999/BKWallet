package nguyen.huy.moneylover.Report;

import java.util.List;

public class ReportHeader {
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
}
