package nguyen.huy.moneylover.Transaction.View;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import nguyen.huy.moneylover.Transaction.Model.Transaction;
import nguyen.huy.moneylover.Transaction.Controller.DayTimeManager;
import nguyen.huy.moneylover.Transaction.Controller.ReportDatabaseManager;
import nguyen.huy.moneylover.Transaction.Controller.TransactionManager;
import nguyen.huy.moneylover.R;
import nguyen.huy.moneylover.Tool.GetImage;

public class EditTransactionActivity extends AppCompatActivity {

    EditText edtNhapSoTien,edtThemGhiChu ,edtThemBan,edtDatNhacNho,edtChonSuKien;
    public static EditText edtChonNhom;
    public static ImageView imgchonNhom;
    public static EditText edtChonNgay;

    public static EditText edtPhuongThucTT;
    public static ImageView imgPhuongThucTT;
    Transaction transaction;
    Intent intent;
    //TransactionManager transactionManager =new TransactionManager();
    String[] result;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_thu_chi);
        addControlls();
        addEvents();
    }

    private void addEvents() {
        setThuChiToEdit();
        result= DayTimeManager.ConvertFormatDay(transaction.getNgay());
        khoiTaoHinhChonNhom();
    }

    private void khoiTaoHinhChonNhom() {
        /*Resources resources=getResources();
        Drawable drawable=resources.getDrawable(R.drawable.question2);
        if(edtChonNhom.getText().toString().equals("Rút tiền"))
            drawable=resources.getDrawable(R.drawable.ruttien);
        else if(edtChonNhom.getText().toString().equals("Gửi tiền"))
            drawable=resources.getDrawable(R.drawable.guitien);
        else if(edtChonNhom.getText().toString().equals("Tiền lãi"))
            drawable=resources.getDrawable(R.drawable.tienlai);*/

        Bitmap bitmap= GetImage.getBitmapFromString(this,edtChonNhom.getText().toString());
        imgchonNhom.setImageBitmap(bitmap);
        Bitmap bitmap1=GetImage.getBitmapFromString(this, transaction.getThanhtoan());
        imgPhuongThucTT.setImageBitmap(bitmap1);
    }

    private void setThuChiToEdit() {
        edtNhapSoTien.setText(transaction.getSotien());
        edtChonNhom.setText(transaction.getNhom());
        edtThemGhiChu.setText(transaction.getGhichu());
        edtChonNgay.setText(transaction.getNgay());
        edtPhuongThucTT.setText(transaction.getThanhtoan());
        edtThemBan.setText(transaction.getBanbe());
        edtDatNhacNho.setText(transaction.getNhacnho());
        edtChonSuKien.setText(transaction.getSukien());
    }

    private void addControlls() {
        edtNhapSoTien=findViewById(R.id.edtNhapSoTien);
        edtChonNhom=findViewById(R.id.edtChonNhom);
        edtThemGhiChu=findViewById(R.id.edtThemGhiChu);
        edtChonNgay=findViewById(R.id.edtChonNgay);
        edtPhuongThucTT =findViewById(R.id.edtPhuongThucTTEdit);
        edtThemBan=findViewById(R.id.edtThemBan);
        edtDatNhacNho=findViewById(R.id.edtDatNhacNho);
        edtChonSuKien=findViewById(R.id.edtChonSuKien);
        imgchonNhom=findViewById(R.id.imgChonNhom);
        imgPhuongThucTT=findViewById(R.id.imgPhuongThucTTEdit);
        intent = getIntent();
        transaction = (Transaction) intent.getSerializableExtra("Item1");
    }

    private Transaction TaoGiaoDich(){
        String SoTien=edtNhapSoTien.getText().toString();
        String Nhom=edtChonNhom.getText().toString();
        String GhiChu=edtThemGhiChu.getText().toString();
        String Ngay=edtChonNgay.getText().toString();
        String Vi= edtPhuongThucTT.getText().toString();
        String Banbe=edtThemBan.getText().toString();
        String NhacNho=edtDatNhacNho.getText().toString();
        String SuKien=edtChonSuKien.getText().toString();
        //Khởi tạo giao dịch mới
        Transaction giaodich=new Transaction(SoTien,Nhom,GhiChu,Ngay,Vi,Banbe,NhacNho,SuKien);
        return giaodich;
    }

    public void xuLyThoat(View view) {
        finish();
    }

    public void xuLyLuu(View view) {
        TransactionManager.SaveTransactionToDatabaseEdit(transaction,TaoGiaoDich());
        //ReportDatabaseManager.EditToDatabase(transaction,TaoGiaoDich(),EditTransactionActivity.this);
        ReportDatabaseManager.supportEditToDatabase(transaction,TaoGiaoDich());
        /*AlertDialog.Builder dialog;
        ReportDatabaseManager.DeleteFromDatabase(transaction);
        dialog=new AlertDialog.Builder(EditTransactionActivity.this);
        dialog.setTitle("Sửa");
        dialog.setMessage("Sửa thành công");
        dialog.setCancelable(false);
        dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alertDialog=dialog.create();
        alertDialog.show();

        ReportDatabaseManager.SaveTransactionToDatabase(TaoGiaoDich());*/
        finish();
    }

    public void xuLyChonNhom(View view) {
        Intent intent=new Intent(EditTransactionActivity.this, SelectGroupActivity.class);
        startActivity(intent);
    }

    public void xuLyHienThiNgay(View view) {
        TransactionManager.displayDayEditText(view,edtChonNgay, EditTransactionActivity.this);
    }

    public void xuLyChonPhuongThucEdit(View view) {
        Intent intent=new Intent(EditTransactionActivity.this, PaymentMethodActivity.class);
        startActivity(intent);
    }
}
