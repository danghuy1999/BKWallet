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

public class InComeFragment extends Fragment {
    public InComeFragment() {
    }
    ListView lisViewChonNhomKhoanThu;
    AdapterTransaction adapterThuChiKhoanThu;
    ArrayList<String> arrayListChonNhomKhoanThu=new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.minh_khoanthu_fragment,container,false);
        lisViewChonNhomKhoanThu=view.findViewById(R.id.lvChonNhomKhoanThu);
        addEvents();
        adapterThuChiKhoanThu=new AdapterTransaction(getActivity(),R.layout.minh_custom_listview,arrayListChonNhomKhoanThu);
        lisViewChonNhomKhoanThu.setAdapter(adapterThuChiKhoanThu);
        getEventItem();
        return view;
    }

    private void getEventItem() {
        lisViewChonNhomKhoanThu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bitmap bitmap= GetImage.getBitmapFromString(getContext(),arrayListChonNhomKhoanThu.get(position));
                TransactionManager.supportCheckNull(TransactionActivity.edtChonNhom, TransactionActivity.imgchonNhom,bitmap,arrayListChonNhomKhoanThu.get(position));
                TransactionManager.supportCheckNull(DetailTransactionActivity.edtEditNhom, DetailTransactionActivity.imageViewEditNhom,bitmap,arrayListChonNhomKhoanThu.get(position));
                TransactionManager.supportCheckNull(EditTransactionActivity.edtChonNhom, EditTransactionActivity.imgchonNhom,bitmap,arrayListChonNhomKhoanThu.get(position));
                getActivity().finish();
            }
        });
    }

    private void addEvents() {
        //Thêm dữ liệu vào arrayListChonNhomKhoanChi
        arrayListChonNhomKhoanThu.add(getString(R.string.ts_deposit));
        arrayListChonNhomKhoanThu.add(getString(R.string.ts_interest));
        arrayListChonNhomKhoanThu.add(getString(R.string.ts_awarded));
        arrayListChonNhomKhoanThu.add(getString(R.string.ts_reward));
        arrayListChonNhomKhoanThu.add(getString(R.string.ts_salary));
        arrayListChonNhomKhoanThu.add(getString(R.string.ts_sale));
        arrayListChonNhomKhoanThu.add(getString(R.string.ts_income_other));
    }
}
