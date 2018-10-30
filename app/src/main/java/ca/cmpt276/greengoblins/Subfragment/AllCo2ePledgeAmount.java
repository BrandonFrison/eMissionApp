package ca.cmpt276.greengoblins.Subfragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ca.cmpt276.greengoblins.emission.R;

public class AllCo2ePledgeAmount extends Fragment {
    View view = null;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.allco2eamount,container,false);
        return view;
    }
}
