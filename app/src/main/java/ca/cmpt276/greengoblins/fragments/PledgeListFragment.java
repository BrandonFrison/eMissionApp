package ca.cmpt276.greengoblins.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import ca.cmpt276.greengoblins.emission.MainActivity;
import ca.cmpt276.greengoblins.emission.R;

public class PledgeListFragment extends Fragment {

    MainActivity mMainActivity;
    private TextView mPledgeListView;
    private Button mMakePledgeButton;
    private Spinner mFilterDropdown;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_pledge_list, container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        getActivity().setTitle("Viewing All Pledges");
        mMainActivity = (MainActivity) getActivity();

        mPledgeListView = (TextView) view.findViewById(R.id.pledge_list_text);
        mPledgeListView.setText(R.string.pledge_list_text);

        mMakePledgeButton = (Button) view.findViewById(R.id.button_make_pledge);

        mFilterDropdown = (Spinner) view.findViewById(R.id.dropdown_filter);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(mMainActivity,
                R.array.pledge_list_filters, android.R.layout.simple_spinner_item);

        //adapter.setDropDownViewResource(R.layout.);

        mFilterDropdown.setAdapter(adapter);


        mMakePledgeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment newFragment = new MakePledgeFragment();
                mMainActivity.startFragment( newFragment, true, false);
            }
        });
    }
}