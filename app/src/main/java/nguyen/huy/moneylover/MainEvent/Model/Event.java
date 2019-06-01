package nguyen.huy.moneylover.MainEvent.Model;

import java.io.Serializable;

public class Event implements Serializable {

    private String ten;
    private String ngayketthuc;
    private String donvitiente;
    private String note;
    private String id;
    private int Icon;

    public String getTen() {
        return ten;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }

    public String getNgayketthuc() {
        return ngayketthuc;
    }

    public void setNgayketthuc(String ngayketthuc) {
        this.ngayketthuc = ngayketthuc;
    }

    public String getDonvitiente() {
        return donvitiente;
    }

    public void setDonvitiente(String donvitiente) {
        this.donvitiente = donvitiente;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getIcon() {
        return Icon;
    }

    public void setIcon(int icon) {
        Icon = icon;
    }

    public Event(String ten, String ngayketthuc, String donvitiente, String note, String id, int icon) {
        this.ten = ten;
        this.ngayketthuc = ngayketthuc;
        this.donvitiente = donvitiente;
        this.note = note;
        this.id = id;
        Icon = icon;
    }

    public Event() {
    }
}
