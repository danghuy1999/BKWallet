package nguyen.huy.moneylover.Model;

public class ThuChi {
    private String sotien;
    private String nhom;
    private String ghichu;
    private String ngay;
    private String vi;
    private String banbe;
    private String nhacnho;
    private String sukien;

    public ThuChi(){

    }
    public ThuChi(String sotien, String nhom, String ghichu, String ngay, String vi, String banbe, String nhacnho, String sukien) {
        this.sotien = sotien;
        this.nhom = nhom;
        this.ghichu = ghichu;
        this.ngay = ngay;
        this.vi = vi;
        this.banbe = banbe;
        this.nhacnho = nhacnho;
        this.sukien = sukien;
    }

    public ThuChi(String sotien, String nhom, String ghichu, String vi, String banbe, String nhacnho, String sukien) {
        this.sotien = sotien;
        this.nhom = nhom;
        this.ghichu = ghichu;
        this.vi = vi;
        this.banbe = banbe;
        this.nhacnho = nhacnho;
        this.sukien = sukien;
    }

    public String getSotien() {
        return sotien;
    }

    public void setSotien(String sotien) {
        this.sotien = sotien;
    }

    public String getNhom() {
        return nhom;
    }

    public void setNhom(String nhom) {
        this.nhom = nhom;
    }

    public String getGhichu() {
        return ghichu;
    }

    public void setGhichu(String ghichu) {
        this.ghichu = ghichu;
    }

    public String getNgay() {
        return ngay;
    }

    public void setNgay(String ngay) {
        this.ngay = ngay;
    }

    public String getVi() {
        return vi;
    }

    public void setVi(String vi) {
        this.vi = vi;
    }

    public String getBanbe() {
        return banbe;
    }

    public void setBanbe(String banbe) {
        this.banbe = banbe;
    }

    public String getNhacnho() {
        return nhacnho;
    }

    public void setNhacnho(String nhacnho) {
        this.nhacnho = nhacnho;
    }

    public String getSukien() {
        return sukien;
    }

    public void setSukien(String sukien) {
        this.sukien = sukien;
    }


}
