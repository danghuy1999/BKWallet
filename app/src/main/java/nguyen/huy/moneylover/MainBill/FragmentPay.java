package nguyen.huy.moneylover.MainBill;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import nguyen.huy.moneylover.R;

public class FragmentPay extends Fragment {

    public static final int INTENT_ACTIVITY_PAY = 101;
    public FragmentPay(){};

    View view;

    ListView lvpay;
    AdapterSelectGroup adapterSelectGroup;
    ArrayList<String> arrayListPay=new ArrayList<>();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater,container,savedInstanceState);
        view=inflater.inflate(R.layout.bill_fragment_pay, container,false);

        addControls();
        addEvents();

        return view;
    }

    private void addEvents() {
        lvpay.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(getActivity(),ActivityAddBill.class);
                intent.putExtra("PAY",arrayListPay.get(position));
                getActivity().setResult(Activity.RESULT_OK,intent);
                getActivity().finish();
            }
        });
    }

    private void addControls() {
        lvpay=view.findViewById(R.id.lvPay);

        arrayListPay.add(getString(R.string.ts_withdraw));
        arrayListPay.add(getString(R.string.ts_friend_n_love));
        arrayListPay.add(getString(R.string.ts_insurance));
        arrayListPay.add(getString(R.string.ts_fee));
        arrayListPay.add(getString(R.string.ts_transport));
        arrayListPay.add(getString(R.string.ts_home));
        arrayListPay.add(getString(R.string.ts_travel));
        arrayListPay.add(getString(R.string.ts_education));
        arrayListPay.add(getString(R.string.ts_entertainment));
        arrayListPay.add(getString(R.string.ts_bill));
        arrayListPay.add(getString(R.string.ts_business));
        arrayListPay.add(getString(R.string.ts_shopping));
        arrayListPay.add(getString(R.string.ts_gift));
        arrayListPay.add(getString(R.string.ts_health));
        arrayListPay.add(getString(R.string.ts_eating));
        arrayListPay.add(getString(R.string.ts_invest));
        arrayListPay.add(getString(R.string.ts_outcome_other));

        adapterSelectGroup =new AdapterSelectGroup(getActivity(),R.layout.bill_item_select_group, arrayListPay);
        lvpay.setAdapter(adapterSelectGroup);

    }
}
