package ca.cmpt276.greengoblins.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import ca.cmpt276.greengoblins.emission.MainActivity;
import ca.cmpt276.greengoblins.emission.R;

public class MakePledgeFragment extends Fragment {
    MainActivity mMainActivity;
    private Button mSharePledgeButton;
    private Button mPublishPledgeButton;

    private TextView mFirstName;
    private TextView mLastName;
    private TextView mMunicipality;
    private TextView mPledgeAmount;
    private CheckBox mShowNameCheckbox;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_pledge_create, container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mMainActivity = (MainActivity) getActivity();
        mSharePledgeButton = (Button) view.findViewById(R.id.button_share_pledge);
        mPublishPledgeButton = (Button) view.findViewById(R.id.button_publish_pledge);

        mFirstName = (TextView) view.findViewById(R.id.input_first_name);
        mLastName = (TextView) view.findViewById(R.id.input_last_name);
        mMunicipality = (TextView) view.findViewById(R.id.input_municipality);
        mPledgeAmount = (TextView) view.findViewById(R.id.input_pledge_amount);
        mShowNameCheckbox = (CheckBox) view.findViewById(R.id.checkbox_show_name);

        mSharePledgeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Fragment newFragment = new MakePledgeFragment();
                //mMainActivity.startFragment( newFragment, true, false);
            }
        });
        mPublishPledgeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Fragment newFragment = new PledgeListFragment();
                //mMainActivity.startFragment( newFragment, true, false);
            }
        });
    }
}
