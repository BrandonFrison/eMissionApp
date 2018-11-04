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

import ca.cmpt276.greengoblins.emission.MainActivity;
import ca.cmpt276.greengoblins.emission.R;

public class PledgeListFragment extends Fragment {
   /* View view = null;
    TextView CurrentPledge;
    TextView PledgeList;
    boolean MyPledgeExist = false;*/
    MainActivity mMainActivity;
    private Button mMakePledgeButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_pledge_list, container,false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mMainActivity = (MainActivity) getActivity();

        mMakePledgeButton = (Button) view.findViewById(R.id.button_make_pledge);

        mMakePledgeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment newFragment = new MakePledgeFragment();
                mMainActivity.startFragment( newFragment, true, false);
            }
        });
        /*CurrentPledge = view.findViewById(R.id.mycurrentpledge);
        String CurrentPledge_text;
        if(MyPledgeExist == false){ //if users haven't made a pledge yet
            CurrentPledge_text = "Your Pledge: \n\tYou haven't made a pledge yet!";
        }
        else{
            CurrentPledge_text = "";
        }

        CurrentPledge.setText(CurrentPledge_text);



        PledgeList = view.findViewById(R.id.pledgesList);


        return  view;*/
    }
}
