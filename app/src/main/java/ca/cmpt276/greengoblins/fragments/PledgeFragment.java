package ca.cmpt276.greengoblins.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import ca.cmpt276.greengoblins.emission.MainActivity;
import ca.cmpt276.greengoblins.emission.R;

public class PledgeFragment extends Fragment{

    MainActivity mMainActivity;
    private Button mMakePledgeButton;
    private Button mViewPledgeListButton;

    private TextView mTotalWorldCO2ePledgeView;
    private TextView mWorldCO2ePledgeSummaryView;
    private TextView mWorldCO2eBreakdownView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_pledge, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mMainActivity = (MainActivity) getActivity();

        mMakePledgeButton = (Button) view.findViewById(R.id.button_make_pledge);
        mViewPledgeListButton = (Button) view.findViewById(R.id.button_view_pledge_list);

        mTotalWorldCO2ePledgeView = (TextView) view.findViewById(R.id.pledge_worldtotal_view);
        mWorldCO2ePledgeSummaryView = (TextView) view.findViewById(R.id.pledge_worldtotal_explanation_view);
        mWorldCO2eBreakdownView = (TextView) view.findViewById(R.id.pledge_worldbreakdown_view);

        int totalWorldCO2ePledged = getCO2ePledgeTotal();
        int totalWorldPledges = getNumOfPledges();
        int perPersonPledges = Math.round( totalWorldCO2ePledged / totalWorldPledges );
        int numTreesSaved = 10;
        int numForestsSaved = 1;
        int numDrivingSaved = 1500;

        String pledgeWorldTotal = String.format( "%,d", totalWorldCO2ePledged );

        String worldPledgeSummary = getString(R.string.textview_world_pledge_summary);
        worldPledgeSummary = String.format(worldPledgeSummary,
                totalWorldPledges,
                perPersonPledges );

        String worldBreakdownSummary = getString(R.string.textview_world_breakdown_summary);
        worldBreakdownSummary = String.format(worldBreakdownSummary,
                numTreesSaved,
                numForestsSaved,
                numDrivingSaved );

        mTotalWorldCO2ePledgeView.setText( pledgeWorldTotal );
        mWorldCO2ePledgeSummaryView.setText( worldPledgeSummary );
        mWorldCO2eBreakdownView.setText( worldBreakdownSummary );

        mMakePledgeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment newFragment = new MakePledgeFragment();
                mMainActivity.startFragment( newFragment, true, false);
            }
        });
        mViewPledgeListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment newFragment = new PledgeListFragment();
                mMainActivity.startFragment( newFragment, true, false);
            }
        });
    }

    public int getCO2ePledgeTotal(){
        return 42069;
    }
    public int getNumOfPledges(){
        return 1337;
    }
}