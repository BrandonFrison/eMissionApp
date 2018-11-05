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
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import ca.cmpt276.greengoblins.emission.R;
import ca.cmpt276.greengoblins.foodsurveydata.User;
import ca.cmpt276.greengoblins.fragments.ExplanationDialogFragment;
import ca.cmpt276.greengoblins.fragments.LoginFragment;

public class MakeYourOwnPledgeFragment extends Fragment  {
    View view = null;

    EditText mFirstNameInputField;
    EditText mLastNameInputField;
    EditText mCityInputField;
    EditText mCO2ePledgedInputField;

    Button SharePledgeButton;
    Button PublishPleDgeButton;

    private ExplanationDialogFragment explanationDialogFragment;

    private LoginFragment Loginfragment;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_make_your_own_pledge,container,false);

        mFirstNameInputField = (EditText) view.findViewById(R.id.first_name_input);
        mLastNameInputField = (EditText) view.findViewById(R.id.last_name_input);
        mCityInputField = (EditText) view.findViewById(R.id.city_input);
        mCO2ePledgedInputField = (EditText) view.findViewById(R.id.co2e_input);




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

                // *********************   NEED TO IMPLEMENT INPUT CHECKING (VERY IMPORTANT)  *************************************
                String firstName = "";
                String lastName = "";
                String city = "";
                Double pledgeAmount = 0.0;


                firstName = mFirstNameInputField.getText().toString().trim();
                lastName = mLastNameInputField.getText().toString().trim();
                city = mCityInputField.getText().toString().trim();

                String pledgeAmountString = mCO2ePledgedInputField.getText().toString().trim();
                pledgeAmount = Double.parseDouble(pledgeAmountString);

                DatabaseReference usersDatabase;
                usersDatabase = FirebaseDatabase.getInstance().getReference("Users");

                String uniqueID = usersDatabase.push().getKey();
                User user = new User("tempEmail@test.com", firstName, lastName, city, pledgeAmount);
                usersDatabase.child(uniqueID).setValue(user);

            }
        });


        SharePledgeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Loginfragment= new LoginFragment();
                Loginfragment.setTargetFragment(MakeYourOwnPledgeFragment.this, 1);
                Loginfragment.show(getFragmentManager(), "login");
            }
        });


        return view;
    }
}
