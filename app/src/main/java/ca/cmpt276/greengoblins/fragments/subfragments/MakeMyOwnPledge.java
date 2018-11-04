package ca.cmpt276.greengoblins.fragments.subfragments;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import ca.cmpt276.greengoblins.emission.R;
import ca.cmpt276.greengoblins.fragments.ExplanationDialogFragment;
import ca.cmpt276.greengoblins.fragments.LoginFragment;

public class MakeMyOwnPledge extends Fragment  {
    View view = null;
    TextView userInformation;
    TextView pledgeamount;
    CheckBox ShowName;
    Button SharePledgeButton;
    Button PublishPleDgeButton;
    private ExplanationDialogFragment explanationDialogFragment;
    String FirstName = "John";//should let user input this value
    String LastName = "Smith";//should let user input this value
    int ValueSavedFromMealPlan = 88;//should let user input this value
    int CostomValue = 0;//should let user input this value
    private LoginFragment Loginfragment;

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
        SharePledgeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View popupView = getLayoutInflater().inflate(R.layout.op_up_login, null);

                PopupWindow popupWindow = new PopupWindow(popupView,
                        RelativeLayout.LayoutParams.WRAP_CONTENT, DrawerLayout.LayoutParams.WRAP_CONTENT);


                // If the PopupWindow should be focusable
                popupWindow.setFocusable(true);

                // If you need the PopupWindow to dismiss when when touched outside
                popupWindow.setBackgroundDrawable(new ColorDrawable());

                int location[] = new int[2];

                // Get the View's(the one that was clicked in the Fragment) location
                view.getLocationOnScreen(location);

                // Using location, the PopupWindow will be displayed right under anchorView
                popupWindow.showAtLocation(view, Gravity.NO_GRAVITY,
                        location[1], location[1] + view.getHeight());
            }
        });
        PublishPleDgeButton = view.findViewById(R.id.publishpledgebutton);
        PublishPleDgeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                explanationDialogFragment= new ExplanationDialogFragment();
                explanationDialogFragment.setTargetFragment(MakeMyOwnPledge.this, 1);
                explanationDialogFragment.show(getFragmentManager(), "login");
            }
        });


        SharePledgeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Loginfragment= new LoginFragment();
                Loginfragment.setTargetFragment(MakeMyOwnPledge.this, 1);
                Loginfragment.show(getFragmentManager(), "login");
            }
        });


        return view;
    }
}
