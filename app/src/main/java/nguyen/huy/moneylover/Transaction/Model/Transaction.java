package nguyen.huy.moneylover.Transaction.Model;

import java.io.Serializable;

public class Transaction implements Serializable {

    private String thuchiKey;
    private String sotien;
    private String nhom;
    private String ghichu;
    private String ngay;
    private String thanhtoan;
    private String banbe;
    private String nhacnho;
    private String sukien;
    private int IDSuKien;

    public int getIDSuKien() {
        return IDSuKien;
    }

    public void setIDSuKien(int IDSuKien) {
        this.IDSuKien = IDSuKien;
    }

    public Transaction(){
        this.IDSuKien=0;
    }
    public Transaction(String sotien, String nhom, String ghichu, String ngay, String thanhtoan, String banbe, String nhacnho, String sukien) {
        this.sotien = sotien;
        this.nhom = nhom;
        this.ghichu = ghichu;
        this.ngay = ngay;
        this.thanhtoan = thanhtoan;
        this.banbe = banbe;
        this.nhacnho = nhacnho;
        this.sukien = sukien;
    }

    public Transaction(String sotien, String nhom, String ghichu, String thanhtoan, String banbe, String nhacnho, String sukien) {
        this.sotien = sotien;
        this.nhom = nhom;
        this.ghichu = ghichu;
        this.thanhtoan = thanhtoan;
        this.banbe = banbe;
        this.nhacnho = nhacnho;
        this.sukien = sukien;
    }

    public String getThuchiKey() {
        return thuchiKey;
    }

    public void setThuchiKey(String thuchiKey) {
        this.thuchiKey = thuchiKey;
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

    public String getThanhtoan() {
        return thanhtoan;
    }

    public void setThanhtoan(String thanhtoan) {
        this.thanhtoan = thanhtoan;
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
