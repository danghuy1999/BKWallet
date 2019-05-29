package nguyen.huy.moneylover.Tool;

import nguyen.huy.moneylover.Transaction.Model.Transaction;
import nguyen.huy.moneylover.Transaction.Controller.TransactionManager;
import nguyen.huy.moneylover.Transaction.Controller.DayTimeManager;
import nguyen.huy.moneylover.Transaction.Controller.ReportDatabaseManager;


public class AddSaveMoney {
    public static void SaveMoney(String ngay, String mucdich, long sotien){
        if(ngay==null || mucdich==null){
            return;
        }
        String[] result= DayTimeManager.ConvertFormatDay(ngay);
        Transaction transaction = TaoGiaoDich(sotien,mucdich,ngay);
        TransactionManager.SaveTransactionToDatabase(transaction,result);
        ReportDatabaseManager.SaveToDatabase(transaction);
    }
    private static Transaction TaoGiaoDich(long sotien, String mucdich, String ngay){
        String SoTien=sotien+"";
        String Nhom= "Tiết kiệm";
        String GhiChu=mucdich;
        String Ngay=ngay;
        String ThanhToan="Tiền mặt";
        String Banbe="";
        String NhacNho="";
        String SuKien="";
        //Khởi tạo giao dịch mới
        Transaction giaodich=new Transaction(SoTien,Nhom,GhiChu,Ngay,ThanhToan,Banbe,NhacNho,SuKien);
        return giaodich;
    }
}
