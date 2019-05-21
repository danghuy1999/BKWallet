package nguyen.huy.moneylover.MinhLayout;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import nguyen.huy.moneylover.Model.ThuChi;

public class XuLyDatabaseSupport {
    static DatabaseReference databaseReference;
    static String user=FirebaseAuth.getInstance().getCurrentUser().getUid();
    static AlertDialog.Builder dialog;

    //Xử lý khi có giao dịch mới được lưu vào database
    public static void SaveToDatabase(final ThuChi thuChi){
        final String[] result=XuLyChuoiThuChi.chuyenDinhDangNgay(thuChi.getNgay());
        if(XuLyThuChi.checkMoneyIO(thuChi)){
            databaseReference=FirebaseDatabase.getInstance().getReference().child(user).child("Thu chi").child(result[0]).child("Giao dịch vào").child(thuChi.getNhom()).child("Ngày").child(result[1]);
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    long money=0;
                    if(dataSnapshot.getValue()==null){
                        money=Long.parseLong(thuChi.getSotien());
                        setMoneyIn(thuChi.getNhom(),result,money);
                    }
                    else {
                        money=Long.parseLong(dataSnapshot.getValue().toString())+Long.parseLong(thuChi.getSotien());
                        setMoneyIn(thuChi.getNhom(),result,money);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
        else {
            databaseReference=FirebaseDatabase.getInstance().getReference().child(user).child("Thu chi").child(result[0]).child("Giao dịch ra").child(thuChi.getNhom()).child("Ngày").child(result[1]);
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    long money=0;
                    if(dataSnapshot.getValue()==null){
                        money=Long.parseLong(thuChi.getSotien());
                        setMoneyOut(thuChi.getNhom(),result,money);
                    }
                    else {
                        money=Long.parseLong(dataSnapshot.getValue().toString())+Long.parseLong(thuChi.getSotien());
                        setMoneyOut(thuChi.getNhom(),result,money);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
        SetSumTransactionInOut(thuChi.getNgay(),thuChi.getNhom());
    }

    //Hàm lưu vào database của QR
    public static void SaveDataInQR(ArrayList<ThuChi> listThuChi){
        ArrayList<String> arrayListNhom=new ArrayList<>();
        arrayListNhom.add(listThuChi.get(0).getNhom());
        for(int i=1;i<listThuChi.size();i++){
            for(int j=0;j<arrayListNhom.size();j++){
                if(!listThuChi.get(i).getNhom().equals(arrayListNhom.get(j)))
                    arrayListNhom.add(listThuChi.get(i).getNhom());
            }
        }
        String ngay=listThuChi.get(0).getNgay();
        for(int i=0;i<arrayListNhom.size();i++){
            long money=0;
            for(int j=0;j<listThuChi.size();j++){
                if(arrayListNhom.get(i).equals(listThuChi.get(j).getNhom())){
                    money=money+Long.parseLong(listThuChi.get(j).getSotien());
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
        final String[] result=XuLyChuoiThuChi.chuyenDinhDangNgay(ngay);
        if(XuLyThuChi.checkMoneyIOString(nhom)){
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
    public static void DeleteFromDatabase(final ThuChi thuChi){
        final String[] result=XuLyChuoiThuChi.chuyenDinhDangNgay(thuChi.getNgay());
        if(XuLyThuChi.checkMoneyIO(thuChi)){
            databaseReference=FirebaseDatabase.getInstance().getReference().child(user).child("Thu chi").child(result[0]).child("Giao dịch vào").child(thuChi.getNhom()).child("Ngày").child(result[1]);
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    long money=0;
                    if(dataSnapshot.getValue()==null){

                    }
                    else {
                        money=Long.parseLong(dataSnapshot.getValue().toString())- Long.parseLong(thuChi.getSotien());
                        if(money==0){
                            setNullWhenSumEqualZero(thuChi,result);
                        }
                        else
                            setMoneyIn(thuChi.getNhom(),result,money);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
        else {
            databaseReference=FirebaseDatabase.getInstance().getReference().child(user).child("Thu chi").child(result[0]).child("Giao dịch ra").child(thuChi.getNhom()).child("Ngày").child(result[1]);
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    long money=0;
                    if(dataSnapshot.getValue()==null){

                    }
                    else {
                        money=Long.parseLong(dataSnapshot.getValue().toString())-Long.parseLong(thuChi.getSotien());
                        if(money==0){
                            setNullWhenSumEqualZero(thuChi,result);
                        }
                        else
                            setMoneyOut(thuChi.getNhom(),result,money);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
        SetSumTransactionInOut(thuChi.getNgay(),thuChi.getNhom());
    }

    //Tạo rỗng khi xóa về 0
    private static void setNullWhenSumEqualZero(ThuChi thuChi,String[] result){
        if(XuLyThuChi.checkMoneyIO(thuChi)){
            databaseReference= FirebaseDatabase.getInstance().getReference().child(user).child("Thu chi").child(result[0]).child("Giao dịch vào").child(thuChi.getNhom());
            databaseReference.child("Ngày").setValue(null);
            databaseReference.child("Tổng").setValue(null);
        }
        else {
            databaseReference = FirebaseDatabase.getInstance().getReference().child(user).child("Thu chi").child(result[0]).child("Giao dịch ra").child(thuChi.getNhom());
            databaseReference.child("Ngày").setValue(null);
            databaseReference.child("Tổng").setValue(null);
        }
    }


    //Xử lý khi sửa giao dịch
    public static void EditToDatabase(ThuChi thuChilOld, final ThuChi thuChiNew, final Activity activity){
        DeleteFromDatabase(thuChilOld);

        dialog=new AlertDialog.Builder(activity);
        dialog.setTitle("Sửa");
        dialog.setMessage("Sửa thành công");
        dialog.setCancelable(false);
        dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                SaveToDatabase(thuChiNew);
                activity.finish();
            }
        });
        AlertDialog alertDialog=dialog.create();
        alertDialog.show();

        //SaveToDatabase(thuChiNew);
    }

    public static void supportEditToDatabase(final ThuChi thuChiOld, final ThuChi thuChiNew){
        final String[] result=XuLyChuoiThuChi.chuyenDinhDangNgay(thuChiOld.getNgay());
        if(thuChiOld.getNhom().equals(thuChiNew.getNhom()) && thuChiOld.getNgay().equals(thuChiNew.getNgay())){
            if(XuLyThuChi.checkMoneyIOString(thuChiOld.getNhom())){
                databaseReference=FirebaseDatabase.getInstance().getReference().child(user).child("Thu chi").child(result[0]).child("Giao dịch vào").child(thuChiOld.getNhom()).child("Ngày").child(result[1]);
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        long money=0;
                        if(dataSnapshot.getValue()!=null){
                            money=Long.parseLong(dataSnapshot.getValue().toString())-Long.parseLong(thuChiOld.getSotien())+Long.parseLong(thuChiNew.getSotien());
                            setMoneyIn(thuChiNew.getNhom(),result,money);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
            else {
                databaseReference=FirebaseDatabase.getInstance().getReference().child(user).child("Thu chi").child(result[0]).child("Giao dịch ra").child(thuChiOld.getNhom()).child("Ngày").child(result[1]);
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        long money=0;
                        if(dataSnapshot.getValue()!=null){
                            money=Long.parseLong(dataSnapshot.getValue().toString())-Long.parseLong(thuChiOld.getSotien())+Long.parseLong(thuChiNew.getSotien());
                            setMoneyOut(thuChiNew.getNhom(),result,money);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
            SetSumTransactionInOut(thuChiNew.getNgay(),thuChiNew.getNhom());
        }
        else {
            DeleteFromDatabase(thuChiOld);

            SaveToDatabase(thuChiNew);
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
        final String[] result=XuLyChuoiThuChi.chuyenDinhDangNgay(ngay);
        if(XuLyThuChi.checkMoneyIOString(nhom)){
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
