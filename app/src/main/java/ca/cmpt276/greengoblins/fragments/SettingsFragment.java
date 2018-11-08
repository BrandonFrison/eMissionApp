package ca.cmpt276.greengoblins.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.io.File;

import ca.cmpt276.greengoblins.emission.R;


public class SettingsFragment extends Fragment {

    private Button clear_hist;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Settings");

        clear_hist = (Button)view.findViewById(R.id.buttonClearLocalData);
        clear_hist.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                clearLocalData();
            }
        });

    }

    public boolean clearLocalData(){
        boolean deleted = false;
        File file = new File("table_01.csv");
        return deleted = file.delete();
    }
}
