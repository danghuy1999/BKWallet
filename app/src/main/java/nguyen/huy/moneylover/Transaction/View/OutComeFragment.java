package nguyen.huy.moneylover.Transaction.View;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import nguyen.huy.moneylover.Transaction.Adapter.AdapterTransaction;
import nguyen.huy.moneylover.Transaction.Controller.TransactionManager;
import nguyen.huy.moneylover.R;
import nguyen.huy.moneylover.Tool.GetImage;

public class OutComeFragment extends Fragment {
    public OutComeFragment() {
    }
    ListView lisViewChonNhomKhoanChi;
    AdapterTransaction adapterTransaction;
    ArrayList<String> arrayListChonNhomKhoanChi =new ArrayList<>();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.minh_khoanthu_fragment,container,false);
        lisViewChonNhomKhoanChi =view.findViewById(R.id.lvChonNhomKhoanThu);
        addEvents();
        adapterTransaction =new AdapterTransaction(getActivity(),R.layout.minh_custom_listview, arrayListChonNhomKhoanChi);
        lisViewChonNhomKhoanChi.setAdapter(adapterTransaction);
        getEventItem();
        return view;
    }
    private void getEventItem() {
        lisViewChonNhomKhoanChi.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bitmap bitmap= GetImage.getBitmapFromString(getContext(), arrayListChonNhomKhoanChi.get(position));
                TransactionManager.supportCheckNull(TransactionActivity.edtChonNhom, TransactionActivity.imgchonNhom,bitmap,arrayListChonNhomKhoanChi.get(position));
                TransactionManager.supportCheckNull(DetailTransactionActivity.edtEditNhom, DetailTransactionActivity.imageViewEditNhom,bitmap,arrayListChonNhomKhoanChi.get(position));
                TransactionManager.supportCheckNull(EditTransactionActivity.edtChonNhom, EditTransactionActivity.imgchonNhom,bitmap,arrayListChonNhomKhoanChi.get(position));
                getActivity().finish();
            }
        });
    }
    private void addEvents() {
        //Thêm dữ liệu vào arrayListChonNhomKhoanChi
        arrayListChonNhomKhoanChi.add(getString(R.string.ts_withdraw));
        arrayListChonNhomKhoanChi.add(getString(R.string.ts_friend_n_love));
        arrayListChonNhomKhoanChi.add(getString(R.string.ts_insurance));
        arrayListChonNhomKhoanChi.add(getString(R.string.ts_fee));
        arrayListChonNhomKhoanChi.add(getString(R.string.ts_transport));
        arrayListChonNhomKhoanChi.add(getString(R.string.ts_home));
        arrayListChonNhomKhoanChi.add(getString(R.string.ts_travel));
        arrayListChonNhomKhoanChi.add(getString(R.string.ts_education));
        arrayListChonNhomKhoanChi.add(getString(R.string.ts_entertainment));
        arrayListChonNhomKhoanChi.add(getString(R.string.ts_bill));
        arrayListChonNhomKhoanChi.add(getString(R.string.ts_business));
        arrayListChonNhomKhoanChi.add(getString(R.string.ts_shopping));
        arrayListChonNhomKhoanChi.add(getString(R.string.ts_gift));
        arrayListChonNhomKhoanChi.add(getString(R.string.ts_health));
        arrayListChonNhomKhoanChi.add(getString(R.string.ts_eating));
        arrayListChonNhomKhoanChi.add(getString(R.string.ts_invest));
        arrayListChonNhomKhoanChi.add(getString(R.string.ts_savemoney));
        arrayListChonNhomKhoanChi.add(getString(R.string.ts_outcome_other));
    }
}
