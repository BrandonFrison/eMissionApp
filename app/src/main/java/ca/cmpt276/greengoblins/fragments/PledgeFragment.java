package ca.cmpt276.greengoblins.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import ca.cmpt276.greengoblins.emission.MainActivity;
import ca.cmpt276.greengoblins.emission.R;

public class PledgeFragment extends Fragment {

    TextView mUserNameField;
    TextView mPasswordField;

    MainActivity mat;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_pledge, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle("Sign in to eMission Pledge");

        mat = (MainActivity) getActivity();
        FloatingActionButton fab = mat.getActionButton();

        mUserNameField = (TextView) view.findViewById(R.id.username_input_field);
        mPasswordField = (TextView) view.findViewById(R.id.password_input_field);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mat.signIn(String.valueOf(mUserNameField.getText()), String.valueOf(mPasswordField.getText()));
                mat.checkUserLogin();
            }
        });

    }
}
