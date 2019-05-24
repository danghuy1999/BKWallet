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
import android.widget.ListView;

import java.util.ArrayList;

import nguyen.huy.moneylover.R;

public class FragmentLoan extends Fragment {

    public static final int INTENT_ACTIVITY_LOAN = 102;
    public FragmentLoan(){};

    View view;

    ListView lvLoan;
    AdapterSelectGroup adapterSelectGroup;
    ArrayList<String> arrayListLoan=new ArrayList<>();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.bill_fragment_loan, container,false);

        addControls();
        addEvents();

        return view;
    }

    private void addEvents() {
        lvLoan.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(getActivity(),ActivityAddBill.class);
                intent.putExtra("LOAN",arrayListLoan.get(position));
                getActivity().setResult(1,intent);
                getActivity().finish();
            }
        });
    }

    private void addControls() {
        lvLoan=view.findViewById(R.id.lvLoan);

        arrayListLoan.add(getString(R.string.ts_lend));
        arrayListLoan.add(getString(R.string.ts_pay));

        adapterSelectGroup =new AdapterSelectGroup(getActivity(),R.layout.bill_item_select_group, arrayListLoan);
        lvLoan.setAdapter(adapterSelectGroup);
    }

}
