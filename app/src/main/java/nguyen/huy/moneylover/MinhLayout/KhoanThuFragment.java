package nguyen.huy.moneylover.MinhLayout;

import android.content.Intent;
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

import nguyen.huy.moneylover.R;
import nguyen.huy.moneylover.Tool.GetImage;

public class KhoanThuFragment extends Fragment {
    public KhoanThuFragment() {
    }
    ListView lisViewChonNhomKhoanThu;
    AdapterThuChi adapterThuChiKhoanThu;
    ArrayList<String> arrayListChonNhomKhoanThu=new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.minh_khoanthu_fragment,container,false);
        lisViewChonNhomKhoanThu=view.findViewById(R.id.lvChonNhomKhoanThu);
        addEvents();
        adapterThuChiKhoanThu=new AdapterThuChi(getActivity(),R.layout.minh_custom_listview,arrayListChonNhomKhoanThu);
        lisViewChonNhomKhoanThu.setAdapter(adapterThuChiKhoanThu);
        getEventItem();
        return view;
    }

    private void getEventItem() {
        lisViewChonNhomKhoanThu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bitmap bitmap= GetImage.getBitmapFromString(getContext(),arrayListChonNhomKhoanThu.get(position));
                XuLyThuChi.supportCheckNull(ThuChiActivity.edtChonNhom,ThuChiActivity.imgchonNhom,bitmap,arrayListChonNhomKhoanThu.get(position));
                XuLyThuChi.supportCheckNull(DocActivity.edtEditNhom,DocActivity.imageViewEditNhom,bitmap,arrayListChonNhomKhoanThu.get(position));
                XuLyThuChi.supportCheckNull(EditThuChiActivity.edtChonNhom,EditThuChiActivity.imgchonNhom,bitmap,arrayListChonNhomKhoanThu.get(position));
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
