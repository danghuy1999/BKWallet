package nguyen.huy.moneylover.Transaction.View;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

import nguyen.huy.moneylover.Transaction.Model.Transaction;
import nguyen.huy.moneylover.Transaction.Controller.DayTimeManager;
import nguyen.huy.moneylover.Transaction.Controller.ReportDatabaseManager;
import nguyen.huy.moneylover.Transaction.Controller.TransactionManager;
import nguyen.huy.moneylover.R;
import nguyen.huy.moneylover.Tool.GetImage;

public class DetailTransactionActivity extends AppCompatActivity {
    Intent intent;
    Transaction transaction;
    DatabaseReference reference;
    DatabaseReference delReference;
    String ngay;
    String result[];
    TextView txtEditNgay;
    public static EditText edtEditNhom;
    public static EditText edtPhuongThuc;
    EditText edtEditSoTien;
    public static ImageView imageViewEditNhom;
    public static ImageView imgPhuongThuc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doc);
        addControls();

        addEvents();

        int[] ngaythangnam= DayTimeManager.splitDayMonthYear(transaction.getNgay());
        TransactionManager.calendar.set(Calendar.DAY_OF_MONTH,ngaythangnam[0]);
        TransactionManager.calendar.set(Calendar.MONTH,ngaythangnam[1]-1);
        TransactionManager.calendar.set(Calendar.YEAR,ngaythangnam[2]);
    }

    private void addControls() {
        edtEditNhom=findViewById(R.id.edtEditNhom);
        txtEditNgay=findViewById(R.id.txtEditNgay);
        edtPhuongThuc=findViewById(R.id.edtPhuongThucDoc);
        edtEditSoTien=findViewById(R.id.edtEditSoTien);
        imageViewEditNhom=findViewById(R.id.imgViewEditNhom);
        imgPhuongThuc=findViewById(R.id.imgPhuongThucDoc);

        intent = getIntent();
        transaction = (Transaction) intent.getSerializableExtra("Item");
        reference = FirebaseDatabase.getInstance().getReference();
        ngay = transaction.getNgay();
        result= DayTimeManager.ConvertFormatDay(ngay);
    }

    private void addEvents() {
        ganThongTin();
    }

    private void ganThongTin() {
        edtPhuongThuc.setText(transaction.getThanhtoan());
        txtEditNgay.setText(transaction.getNgay());
        edtEditNhom.setText(transaction.getNhom());
        edtEditSoTien.setText(transaction.getSotien());

        /*Resources resources=getResources();
        Drawable drawable=resources.getDrawable(R.drawable.question2);
        if(transaction.getNhom().equals("Rút tiền"))
            drawable=resources.getDrawable(R.drawable.ruttien);
        else if(transaction.getNhom().equals("Gửi tiền"))
            drawable=resources.getDrawable(R.drawable.guitien);
        else if(transaction.getNhom().equals("Tiền lãi"))
            drawable=resources.getDrawable(R.drawable.tienlai);
        imageViewEditNhom.setImageDrawable(drawable);*/
        Bitmap bitmap= GetImage.getBitmapFromString(this, transaction.getNhom());
        imageViewEditNhom.setImageBitmap(bitmap);

        Bitmap bitmap1=GetImage.getBitmapFromString(this, transaction.getThanhtoan());
        imgPhuongThuc.setImageBitmap(bitmap1);
    }

    public void xuLyXoaThuChi() {
        reference.child(TransactionManager.user).child("Thu chi").child(result[0]).child("Ngày").child(result[1]).child("Giao dịch").child(transaction.getThuchiKey()).removeValue();
        ReportDatabaseManager.DeleteFromDatabase(transaction);
        finish();
    }

    public void xuLyThoat(View view) {
        finish();
    }

    public void hienThiAlertLogTruocKhiXoa(View view) {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("Xóa giao dịch");
        builder.setMessage("Bạn có chắc chắn muốn xóa?");
        builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                xuLyXoaThuChi();
            }
        });
        builder.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }

    public void xuLyHienThiNgay(View view) {
        TransactionManager.displayDayTextView(view,txtEditNgay, DetailTransactionActivity.this);
    }

    public void xuLySua(View view) {
        Intent intent=new Intent(DetailTransactionActivity.this, EditTransactionActivity.class);
        intent.putExtra("Item1", transaction);
        startActivity(intent);
        finish();
    }

    public void xuLyChonNhom(View view) {
        Intent intent=new Intent(DetailTransactionActivity.this, SelectGroupActivity.class);
        startActivity(intent);
    }

    public void xuLyLuu(View view) {
        /*reference= FirebaseDatabase.getInstance().getReference().child(TransactionManager.user).child("Thu chi").child(result[0]).child("Ngày").child(result[1]).child("Giao dịch").child(transaction.getThuchiKey());
        reference.child("nhom").setValue(edtEditNhom.getText().toString());
        reference.child("sotien").setValue(edtEditSoTien.getText().toString());
        reference.child("ngay").setValue(txtEditNgay.getText().toString());*/
        TransactionManager.SaveTransactionToDatabaseEdit(transaction,CreateTempTrans());
        //ReportDatabaseManager.EditToDatabase(transaction,CreateTempTrans(),DetailTransactionActivity.this);
        ReportDatabaseManager.supportEditToDatabase(transaction,CreateTempTrans());
        finish();
    }

    private Transaction CreateTempTrans() {
        String SoTien=edtEditSoTien.getText().toString();
        String Nhom=edtEditNhom.getText().toString();
        String GhiChu= transaction.getGhichu();
        String Ngay=txtEditNgay.getText().toString();
        String PhuongThuc=edtPhuongThuc.getText().toString();
        String Banbe= transaction.getBanbe();
        String NhacNho= transaction.getNhacnho();
        String SuKien= transaction.getSukien();
        //Khởi tạo giao dịch mới
        Transaction giaodich=new Transaction(SoTien,Nhom,GhiChu,Ngay,PhuongThuc,Banbe,NhacNho,SuKien);
        return giaodich;
    }

    public void xuLyChonPhuongThucDoc(View view) {
        Intent intent=new Intent(DetailTransactionActivity.this, PaymentMethodActivity.class);
        startActivity(intent);
    }
}
