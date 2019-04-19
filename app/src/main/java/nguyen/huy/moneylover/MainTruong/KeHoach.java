package nguyen.huy.moneylover.MainTruong;

public class KeHoach {
    private String tenkehoach;
    private String vi;
    private String thoigian;
    private String diadiem;
    private String nhom;
    private String nhacnho;

    public String getTenkehoach() {
        return tenkehoach;
    }

    public void setTenkehoach(String tenkehoach) {
        this.tenkehoach = tenkehoach;
    }

    public String getVi() {
        return vi;
    }

    public void setVi(String vi) {
        this.vi = vi;
    }

    public String getThoigian() {
        return thoigian;
    }

    public void setThoigian(String thoigian) {
        this.thoigian = thoigian;
    }

    public String getDiadiem() {
        return diadiem;
    }

    public void setDiadiem(String diadiem) {
        this.diadiem = diadiem;
    }

    public String getNhom() {
        return nhom;
    }

    public void setNhom(String nhom) {
        this.nhom = nhom;
    }

    public String getNhacnho() {
        return nhacnho;
    }

    public void setNhacnho(String nhacnho) {
        this.nhacnho = nhacnho;
    }

    public KeHoach() {
        super();
    }

    public KeHoach(String tenkehoach, String vi, String thoigian, String diadiem, String nhom, String nhacnho) {
        this.tenkehoach = tenkehoach;
        this.vi = vi;
        this.thoigian = thoigian;
        this.diadiem = diadiem;
        this.nhom = nhom;
        this.nhacnho = nhacnho;
    }

    @Override
    public String toString() {
        return this.tenkehoach;
    }
}
