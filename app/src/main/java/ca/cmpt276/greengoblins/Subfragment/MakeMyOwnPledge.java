package ca.cmpt276.greengoblins.Subfragment;

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

import ca.cmpt276.greengoblins.emission.R;

public class MakeMyOwnPledge extends Fragment {
    View view = null;
    TextView userInformation;
    TextView pledgeamount;
    CheckBox ShowName;
    Button SharePledgeButton;
    Button PublishPleDgeButton;
    String FirstName = "John";//should let user input this value
    String LastName = "Smith";//should let user input this value
    int ValueSavedFromMealPlan = 88;//should let user input this value
    int CostomValue = 0;//should let user input this value

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.makemyownpledge,container,false);
        userInformation = view.findViewById(R.id.UserInformation);
        String userInformation_text = "First Name: \t"+FirstName+
                "Last Name: \t"+LastName;
        userInformation.setText(userInformation_text);


        pledgeamount = view.findViewById(R.id.PledgeAmount);
        String pledgeamount_text ="Pledge Amount:" +
                "   Saving from Meal Plan:\t"+ValueSavedFromMealPlan+"\tTonnes CO2e"+
                "Custom: \t\t\t"+CostomValue+"\tTonnes CO2e";
        pledgeamount.setText(pledgeamount_text);

        ShowName = view.findViewById(R.id.checkBox);
        SharePledgeButton = view.findViewById(R.id.sharepledgebutton);
        PublishPleDgeButton = view.findViewById(R.id.publishpledgebutton);

        return view;
    }
}
