package nguyen.huy.moneylover.MainLayout;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import nguyen.huy.moneylover.R;

public class FragmentThisMonth extends Fragment {
    public FragmentThisMonth() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_this_month, container,false);

        Button btnTest = view.findViewById(R.id.btnToast);

        btnTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(),"OK",Toast.LENGTH_SHORT).show();
            }
        });

        return view;

    }
}
