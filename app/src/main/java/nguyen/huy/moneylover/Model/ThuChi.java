package nguyen.huy.moneylover.Model;

public class ThuChi {
    private String NhapSoTien;
    private String ChonNhom;
    private String ThemGhiChu;
    private String ChonNgay;
    private String ChonVi;
    private String ThemBan;
    private String DatNhacNho;
    private String ChonSuKien;

    public ThuChi(){

    }
    public ThuChi(String nhapSoTien, String chonNhom, String themGhiChu, String chonNgay, String chonVi, String themBan, String datNhacNho, String chonSuKien) {
        NhapSoTien = nhapSoTien;
        ChonNhom = chonNhom;
        ThemGhiChu = themGhiChu;
        ChonNgay = chonNgay;
        ChonVi = chonVi;
        ThemBan = themBan;
        DatNhacNho = datNhacNho;
        ChonSuKien = chonSuKien;
    }

    public ThuChi(String nhapSoTien, String chonNhom, String themGhiChu, String chonVi, String themBan, String datNhacNho, String chonSuKien) {
        NhapSoTien = nhapSoTien;
        ChonNhom = chonNhom;
        ThemGhiChu = themGhiChu;
        ChonVi = chonVi;
        ThemBan = themBan;
        DatNhacNho = datNhacNho;
        ChonSuKien = chonSuKien;
    }

    public String getNhapSoTien() {
        return NhapSoTien;
    }

    public void setNhapSoTien(String nhapSoTien) {
        NhapSoTien = nhapSoTien;
    }

    public String getChonNhom() {
        return ChonNhom;
    }

    public void setChonNhom(String chonNhom) {
        ChonNhom = chonNhom;
    }

    public String getThemGhiChu() {
        return ThemGhiChu;
    }

    public void setThemGhiChu(String themGhiChu) {
        ThemGhiChu = themGhiChu;
    }

    public String getChonNgay() {
        return ChonNgay;
    }

    public void setChonNgay(String chonNgay) {
        ChonNgay = chonNgay;
    }

    public String getChonVi() {
        return ChonVi;
    }

    public void setChonVi(String chonVi) {
        ChonVi = chonVi;
    }

    public String getThemBan() {
        return ThemBan;
    }

    public void setThemBan(String themBan) {
        ThemBan = themBan;
    }

    public String getDatNhacNho() {
        return DatNhacNho;
    }

    public void setDatNhacNho(String datNhacNho) {
        DatNhacNho = datNhacNho;
    }

    public String getChonSuKien() {
        return ChonSuKien;
    }

    public void setChonSuKien(String chonSuKien) {
        ChonSuKien = chonSuKien;
    }


}
