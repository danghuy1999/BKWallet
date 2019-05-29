package nguyen.huy.moneylover.Transaction.Controller;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import nguyen.huy.moneylover.Transaction.Model.Transaction;

public class ReportDatabaseManager {
    static DatabaseReference databaseReference;
    static String user=FirebaseAuth.getInstance().getCurrentUser().getUid();
    static AlertDialog.Builder dialog;

    //Xử lý khi có giao dịch mới được lưu vào database
    public static void SaveToDatabase(final Transaction transaction){
        final String[] result= DayTimeManager.ConvertFormatDay(transaction.getNgay());
        if(TransactionManager.checkMoneyIO(transaction)){
            databaseReference=FirebaseDatabase.getInstance().getReference().child(user).child("Thu chi").child(result[0]).child("Giao dịch vào").child(transaction.getNhom()).child("Ngày").child(result[1]);
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    long money=0;
                    if(dataSnapshot.getValue()==null){
                        money=Long.parseLong(transaction.getSotien());
                        setMoneyIn(transaction.getNhom(),result,money);
                    }
                    else {
                        money=Long.parseLong(dataSnapshot.getValue().toString())+Long.parseLong(transaction.getSotien());
                        setMoneyIn(transaction.getNhom(),result,money);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
        else {
            databaseReference=FirebaseDatabase.getInstance().getReference().child(user).child("Thu chi").child(result[0]).child("Giao dịch ra").child(transaction.getNhom()).child("Ngày").child(result[1]);
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    long money=0;
                    if(dataSnapshot.getValue()==null){
                        money=Long.parseLong(transaction.getSotien());
                        setMoneyOut(transaction.getNhom(),result,money);
                    }
                    else {
                        money=Long.parseLong(dataSnapshot.getValue().toString())+Long.parseLong(transaction.getSotien());
                        setMoneyOut(transaction.getNhom(),result,money);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
        SetSumTransactionInOut(transaction.getNgay(), transaction.getNhom());
    }

    //Hàm lưu vào database của QR
    public static void SaveDataInQR(ArrayList<Transaction> listTransaction){
        ArrayList<String> arrayListNhom=new ArrayList<>();
        arrayListNhom.add(listTransaction.get(0).getNhom());
        for(int i = 1; i< listTransaction.size(); i++){
            for(int j=0;j<arrayListNhom.size();j++){
                if(!listTransaction.get(i).getNhom().equals(arrayListNhom.get(j)))
                    arrayListNhom.add(listTransaction.get(i).getNhom());
            }
        }
        String ngay= listTransaction.get(0).getNgay();
        for(int i=0;i<arrayListNhom.size();i++){
            long money=0;
            for(int j = 0; j< listTransaction.size(); j++){
                if(arrayListNhom.get(i).equals(listTransaction.get(j).getNhom())){
                    money=money+Long.parseLong(listTransaction.get(j).getSotien());
                }
            }
            SupportSaveDataInQR(ngay,arrayListNhom.get(i),money);
        }
        /*for(int i=0;i<arrayListNhom.size();i++){
            Log.e("TEST NHOM : ",arrayListNhom.get(i));
        }*/
    }

    //Hàm hỗ trợ hàm SaveDataInQR
    private static void SupportSaveDataInQR(String ngay, final String nhom,final long money){
        final String[] result= DayTimeManager.ConvertFormatDay(ngay);
        if(TransactionManager.checkMoneyIOString(nhom)){
            databaseReference=FirebaseDatabase.getInstance().getReference().child(user).child("Thu chi").child(result[0]).child("Giao dịch vào").child(nhom).child("Ngày").child(result[1]);
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    long moneynew=0;
                    if(dataSnapshot.getValue()==null){
                        setMoneyIn(nhom,result,money);
                    }
                    else {
                        moneynew=Long.parseLong(dataSnapshot.getValue().toString())+money;
                        setMoneyIn(nhom,result,moneynew);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
        else {
            databaseReference=FirebaseDatabase.getInstance().getReference().child(user).child("Thu chi").child(result[0]).child("Giao dịch ra").child(nhom).child("Ngày").child(result[1]);
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    long moneynew=0;
                    if(dataSnapshot.getValue()==null){
                        setMoneyOut(nhom,result,money);
                    }
                    else {
                        moneynew=Long.parseLong(dataSnapshot.getValue().toString())+money;
                        setMoneyOut(nhom,result,moneynew);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
        SetSumTransactionInOut(ngay,nhom);
    }

    //Xử lý khi xóa giao dịch
    public static void DeleteFromDatabase(final Transaction transaction){
        final String[] result= DayTimeManager.ConvertFormatDay(transaction.getNgay());
        if(TransactionManager.checkMoneyIO(transaction)){
            databaseReference=FirebaseDatabase.getInstance().getReference().child(user).child("Thu chi").child(result[0]).child("Giao dịch vào").child(transaction.getNhom()).child("Ngày").child(result[1]);
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    long money=0;
                    if(dataSnapshot.getValue()==null){

                    }
                    else {
                        money=Long.parseLong(dataSnapshot.getValue().toString())- Long.parseLong(transaction.getSotien());
                        if(money==0){
                            setNullWhenSumEqualZero(transaction,result);
                        }
                        else
                            setMoneyIn(transaction.getNhom(),result,money);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
        else {
            databaseReference=FirebaseDatabase.getInstance().getReference().child(user).child("Thu chi").child(result[0]).child("Giao dịch ra").child(transaction.getNhom()).child("Ngày").child(result[1]);
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    long money=0;
                    if(dataSnapshot.getValue()==null){

                    }
                    else {
                        money=Long.parseLong(dataSnapshot.getValue().toString())-Long.parseLong(transaction.getSotien());
                        if(money==0){
                            setNullWhenSumEqualZero(transaction,result);
                        }
                        else
                            setMoneyOut(transaction.getNhom(),result,money);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
        SetSumTransactionInOut(transaction.getNgay(), transaction.getNhom());
    }

    //Tạo rỗng khi xóa về 0
    private static void setNullWhenSumEqualZero(Transaction transaction, String[] result){
        if(TransactionManager.checkMoneyIO(transaction)){
            databaseReference= FirebaseDatabase.getInstance().getReference().child(user).child("Thu chi").child(result[0]).child("Giao dịch vào").child(transaction.getNhom());
            databaseReference.child("Ngày").child(result[1]).setValue(null);
            //databaseReference.child("Tổng").setValue(null);
        }
        else {
            databaseReference = FirebaseDatabase.getInstance().getReference().child(user).child("Thu chi").child(result[0]).child("Giao dịch ra").child(transaction.getNhom());
            databaseReference.child("Ngày").child(result[1]).setValue(null);
            //databaseReference.child("Tổng").setValue(null);
        }
    }


    //Xử lý khi sửa giao dịch
    public static void EditToDatabase(Transaction thuChilOld, final Transaction transactionNew, final Activity activity){
        DeleteFromDatabase(thuChilOld);

        dialog=new AlertDialog.Builder(activity);
        dialog.setTitle("Sửa");
        dialog.setMessage("Sửa thành công");
        dialog.setCancelable(false);
        dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                SaveToDatabase(transactionNew);
                activity.finish();
            }
        });
        AlertDialog alertDialog=dialog.create();
        alertDialog.show();

        //SaveTransactionToDatabase(transactionNew);
    }

    public static void supportEditToDatabase(final Transaction transactionOld, final Transaction transactionNew){
        final String[] result= DayTimeManager.ConvertFormatDay(transactionOld.getNgay());
        if(transactionOld.getNhom().equals(transactionNew.getNhom()) && transactionOld.getNgay().equals(transactionNew.getNgay())){
            if(TransactionManager.checkMoneyIOString(transactionOld.getNhom())){
                databaseReference=FirebaseDatabase.getInstance().getReference().child(user).child("Thu chi").child(result[0]).child("Giao dịch vào").child(transactionOld.getNhom()).child("Ngày").child(result[1]);
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        long money=0;
                        if(dataSnapshot.getValue()!=null){
                            money=Long.parseLong(dataSnapshot.getValue().toString())-Long.parseLong(transactionOld.getSotien())+Long.parseLong(transactionNew.getSotien());
                            setMoneyIn(transactionNew.getNhom(),result,money);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
            else {
                databaseReference=FirebaseDatabase.getInstance().getReference().child(user).child("Thu chi").child(result[0]).child("Giao dịch ra").child(transactionOld.getNhom()).child("Ngày").child(result[1]);
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        long money=0;
                        if(dataSnapshot.getValue()!=null){
                            money=Long.parseLong(dataSnapshot.getValue().toString())-Long.parseLong(transactionOld.getSotien())+Long.parseLong(transactionNew.getSotien());
                            setMoneyOut(transactionNew.getNhom(),result,money);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
            SetSumTransactionInOut(transactionNew.getNgay(), transactionNew.getNhom());
        }
        else {
            DeleteFromDatabase(transactionOld);

            SaveToDatabase(transactionNew);
        }
    }

    private static void setMoneyIn(String nhom,String[] result,long money){
        databaseReference= FirebaseDatabase.getInstance().getReference().child(user).child("Thu chi").child(result[0]).child("Giao dịch vào").child(nhom).child("Ngày");
        databaseReference.child(result[1]).setValue(money);
    }

    private static void setMoneyOut(String nhom,String[] result,long money){
        databaseReference= FirebaseDatabase.getInstance().getReference().child(user).child("Thu chi").child(result[0]).child("Giao dịch ra").child(nhom).child("Ngày");
        databaseReference.child(result[1]).setValue(money);
    }


    //Tính tổng của từng loại giao dịch
    private static void SetSumTransactionInOut(final String ngay, final String nhom){
        final String[] result= DayTimeManager.ConvertFormatDay(ngay);
        if(TransactionManager.checkMoneyIOString(nhom)){
           databaseReference=FirebaseDatabase.getInstance().getReference().child(user).child("Thu chi").child(result[0]).child("Giao dịch vào").child(nhom);
           databaseReference.addValueEventListener(new ValueEventListener() {
               @Override
               public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                   long moneySum=0;
                   for(DataSnapshot dataSnapshotNgay:dataSnapshot.child("Ngày").getChildren()){
                        moneySum=moneySum + Long.parseLong(dataSnapshotNgay.getValue().toString());
                   }
                   if(moneySum!=0) {
                       databaseReference = FirebaseDatabase.getInstance().getReference().child(user).child("Thu chi").child(result[0]).child("Giao dịch vào").child(nhom);
                       databaseReference.child("Tổng").setValue(moneySum);
                   }
                   else {
                       databaseReference = FirebaseDatabase.getInstance().getReference().child(user).child("Thu chi").child(result[0]).child("Giao dịch vào").child(nhom);
                       databaseReference.child("Tổng").setValue(null);
                   }
               }

               @Override
               public void onCancelled(@NonNull DatabaseError databaseError) {

               }
           });
        }
        else {
            databaseReference=FirebaseDatabase.getInstance().getReference().child(user).child("Thu chi").child(result[0]).child("Giao dịch ra").child(nhom);
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    long moneySum=0;
                    for(DataSnapshot dataSnapshotNgay:dataSnapshot.child("Ngày").getChildren()){
                        moneySum=moneySum + Long.parseLong(dataSnapshotNgay.getValue().toString());
                    }
                    if(moneySum!=0) {
                        databaseReference = FirebaseDatabase.getInstance().getReference().child(user).child("Thu chi").child(result[0]).child("Giao dịch ra").child(nhom);
                        databaseReference.child("Tổng").setValue(moneySum);
                    }
                    else {
                        databaseReference = FirebaseDatabase.getInstance().getReference().child(user).child("Thu chi").child(result[0]).child("Giao dịch ra").child(nhom);
                        databaseReference.child("Tổng").setValue(null);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }
}
