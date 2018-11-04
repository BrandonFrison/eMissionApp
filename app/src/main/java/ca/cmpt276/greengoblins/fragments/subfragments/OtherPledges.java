package ca.cmpt276.greengoblins.fragments.subfragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ca.cmpt276.greengoblins.emission.R;

public class OtherPledges extends Fragment {
    View view = null;
    TextView CurrentPledge;
    TextView PledgeList;
    boolean MyPledgeExist = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.pledgelist,container,false);
        CurrentPledge = view.findViewById(R.id.mycurrentpledge);
        String CurrentPledge_text;
        if(MyPledgeExist == false){ //if users haven't made a pledge yet
            CurrentPledge_text = "Your Pledge: \n\tYou haven't made a pledge yet!";
        }
        else{
            CurrentPledge_text = "";
        }

        CurrentPledge.setText(CurrentPledge_text);



        PledgeList = view.findViewById(R.id.pledgesList);


        return  view;
    }
}
