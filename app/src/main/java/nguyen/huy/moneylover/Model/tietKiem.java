package nguyen.huy.moneylover.Model;



public class tietKiem {
    private String TietKiemID;
    private String MucDichTietKiem;
    private String MucTieuTietKiem;
    private String SoTienHienCo;
    private String NgayThangNam;
    private boolean ThongBao;
    private boolean LoaiTienTe;

    public tietKiem(String mucDichTietKiem, String mucTieuTietKiem, String soTienHienCo, String ngayThangNam, boolean thongBao, boolean loaiTienTe) {
        MucDichTietKiem = mucDichTietKiem;
        MucTieuTietKiem = mucTieuTietKiem;
        SoTienHienCo = soTienHienCo;
        NgayThangNam = ngayThangNam;
        ThongBao = thongBao;
        LoaiTienTe = loaiTienTe;
    }

    public tietKiem() {
        super();
    }

    public tietKiem(String mucDichTietKiem, String mucTieuTietKiem, String soTienHienCo, String ngayThangNam)
    {
        MucDichTietKiem = mucDichTietKiem;
        MucTieuTietKiem = mucTieuTietKiem;
        SoTienHienCo = soTienHienCo;
        NgayThangNam = ngayThangNam;
    }

    public String getTietKiemID() {
        return TietKiemID;
    }

    public void setTietKiemID(String tietKiemID) {
        TietKiemID = tietKiemID;
    }

    public String getMucDichTietKiem() {
        return MucDichTietKiem;
    }

    public void setMucDichTietKiem(String mucDichTietKiem) {
        MucDichTietKiem = mucDichTietKiem;
    }

    public String getMucTieuTietKiem() {
        return MucTieuTietKiem;
    }

    public void setMucTieuTietKiem(String mucTieuTietKiem) {
        MucTieuTietKiem = mucTieuTietKiem;
    }

    public String getSoTienHienCo() {
        return SoTienHienCo;
    }

    public void setSoTienHienCo(String soTienHienCo) {
        SoTienHienCo = soTienHienCo;
    }

    public String getNgayThangNam() {
        return NgayThangNam;
    }

    public void setNgayThangNam(String ngayThangNam) {
        NgayThangNam = ngayThangNam;
    }

    public boolean isThongBao() {
        return ThongBao;
    }

    public void setThongBao(boolean thongBao) {
        ThongBao = thongBao;
    }

    public boolean isLoaiTienTe() {
        return LoaiTienTe;
    }

    public void setLoaiTienTe(boolean loaiTienTe) {
        LoaiTienTe = loaiTienTe;
    }

    @Override
    public String toString() {
        return this.MucDichTietKiem;
    }
}
