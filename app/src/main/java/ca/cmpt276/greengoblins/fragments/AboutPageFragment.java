package ca.cmpt276.greengoblins.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import ca.cmpt276.greengoblins.emission.R;

public class AboutPageFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_about_page, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button mTestButton = view.findViewById(R.id.test_button);
        //Since a fragment is not an Activity, we get context using getActivity()
        final Context mActivityContext = getContext();
        mTestButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Toast.makeText(mActivityContext, "You pressed a button in fragment_about_page", Toast.LENGTH_LONG).show();
            }
        });
    }
}
