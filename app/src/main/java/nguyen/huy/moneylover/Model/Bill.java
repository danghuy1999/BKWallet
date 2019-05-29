package nguyen.huy.moneylover.Model;

import java.io.Serializable;

public class Bill implements Serializable {
    private String amount;
    private String group;
    private String note;
    private String repeat;
    private String idbill;

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getRepeat() {
        return repeat;
    }

    public void setRepeat(String repeat) {
        this.repeat = repeat;
    }

    public String getIdbill() {
        return idbill;
    }

    public void setIdbill(String idbill) {
        this.idbill = idbill;
    }

    public Bill(String amount, String group, String note, String repeat, String idbill) {
        this.amount = amount;
        this.group = group;
        this.note = note;
        this.repeat = repeat;
        this.idbill = idbill;
    }

    public Bill() {}
}
