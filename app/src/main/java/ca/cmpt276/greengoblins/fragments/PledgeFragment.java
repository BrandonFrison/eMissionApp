package ca.cmpt276.greengoblins.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import ca.cmpt276.greengoblins.emission.MainActivity;
import ca.cmpt276.greengoblins.emission.R;
import ca.cmpt276.greengoblins.foodsurveydata.Convertor;
import ca.cmpt276.greengoblins.foodsurveydata.User;

public class PledgeFragment extends Fragment{

    MainActivity mMainActivity;
    private Button mMakePledgeButton;
    private Button mViewPledgeListButton;

    private TextView mTotalWorldCO2ePledgeView;
    private TextView mWorldCO2ePledgeSummaryView;
    private TextView mWorldCO2eBreakdownView;

    private int mTotalPledged = 0;

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

        // Is responsible for displaying all the information on the page
        // Displays total pledged, number of pledges, average pledge, and CO2e saved in understandable units
        displayData();

        mMakePledgeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if( !mMainActivity.checkUserLogin() ) {
                    mMainActivity.popupLogin();
                } else {
                    Fragment newFragment = new MakePledgeFragment();
                    mMainActivity.startFragment( newFragment, true, false);
                }
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

    public void displayData() {
        final DatabaseReference usersDatabase;
        usersDatabase = FirebaseDatabase.getInstance().getReference("Users");

        mTotalPledged = 0;
        usersDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                // Get number of pledges and total pledged from all users
                int numberOfPledges = 0;
                for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    numberOfPledges++;
                    User userInfo = (User) snapshot.getValue(User.class);
                    double userPledge = userInfo.getPledgeAmount();
                    mTotalPledged += userPledge;
                }

                // Display total pledged
                mTotalWorldCO2ePledgeView.setText( Integer.toString(mTotalPledged) );

                // Display number of pledges and average pledge
                double averagePledge = (mTotalPledged / (double) numberOfPledges);
                String worldPledgeSummary = getString(R.string.textview_world_pledge_summary);
                worldPledgeSummary = String.format(worldPledgeSummary,
                        numberOfPledges,
                        averagePledge );
                mWorldCO2ePledgeSummaryView.setText( worldPledgeSummary );

                // Display total amount pledged in understandable units such as forests saved, gallons gasolin, km driven
                String worldBreakdownSummary = getString(R.string.textview_world_breakdown_summary);
                worldBreakdownSummary = String.format(worldBreakdownSummary,
                        Convertor.toForests(mTotalPledged),
                        Convertor.toGallonsGasoline(mTotalPledged),
                        Convertor.toKmDriven(mTotalPledged) );
                mWorldCO2eBreakdownView.setText( worldBreakdownSummary );

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}