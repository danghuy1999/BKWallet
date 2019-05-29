package nguyen.huy.moneylover.Tool;

import android.os.Build;
import android.support.annotation.RequiresApi;

import java.util.Objects;

import nguyen.huy.moneylover.MinhLayout.XuLyChuoiThuChi;
import nguyen.huy.moneylover.MinhLayout.XuLyDatabaseSupport;
import nguyen.huy.moneylover.MinhLayout.XuLyThuChi;
import nguyen.huy.moneylover.Model.ThuChi;


public class AddSaveMoney {
    public static void SaveMoney(String ngay, String mucdich, long sotien){
        if(ngay==null || mucdich==null){
            return;
        }
        String[] result= XuLyChuoiThuChi.chuyenDinhDangNgay(ngay);
        ThuChi thuChi= TaoGiaoDich(sotien,mucdich,ngay);
        XuLyThuChi.xuLyLuuVaoDatabase(thuChi,result);
        XuLyDatabaseSupport.SaveToDatabase(thuChi);
    }
    private static ThuChi TaoGiaoDich(long sotien,String mucdich,String ngay){
        String SoTien=sotien+"";
        String Nhom= "Tiết kiệm";
        String GhiChu=mucdich;
        String Ngay=ngay;
        String Vi="";
        String Banbe="";
        String NhacNho="";
        String SuKien="";
        //Khởi tạo giao dịch mới
        ThuChi giaodich=new ThuChi(SoTien,Nhom,GhiChu,Ngay,Vi,Banbe,NhacNho,SuKien);
        return giaodich;
    }
}
