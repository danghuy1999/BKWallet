package nguyen.huy.moneylover.Model;

import java.io.Serializable;

public class SuKien implements Serializable {

    private String ten;
    private String ngayketthuc;
    private String donvitiente;
    private String vi;
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

    public String getVi() {
        return vi;
    }

    public void setVi(String vi) {
        this.vi = vi;
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

    public SuKien(String ten, String ngayketthuc, String donvitiente, String vi, String id, int icon) {
        this.ten = ten;
        this.ngayketthuc = ngayketthuc;
        this.donvitiente = donvitiente;
        this.vi = vi;
        this.id = id;
        Icon = icon;
    }

    public SuKien() {
    }
}
