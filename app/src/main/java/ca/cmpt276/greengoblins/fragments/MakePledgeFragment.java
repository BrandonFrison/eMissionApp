package ca.cmpt276.greengoblins.fragments;

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
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import ca.cmpt276.greengoblins.emission.MainActivity;
import ca.cmpt276.greengoblins.emission.R;
import ca.cmpt276.greengoblins.foodsurveydata.User;

public class MakePledgeFragment extends Fragment {
    MainActivity mMainActivity;
    private Button mSharePledgeButton;
    private Button mPublishPledgeButton;

    EditText mFirstNameInputField;
    EditText mLastNameInputField;
    EditText mMunicipalityInputField;
    EditText mPledgeAmountInputField;
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

        mFirstNameInputField = (EditText) view.findViewById(R.id.input_first_name);
        //mFirstNameInputField.setText(mMainActivity.getCurrentUser().getU());
        mLastNameInputField = (EditText) view.findViewById(R.id.input_last_name);
        mMunicipalityInputField = (EditText) view.findViewById(R.id.input_municipality);
        mPledgeAmountInputField = (EditText) view.findViewById(R.id.input_pledge_amount);
        mShowNameCheckbox = (CheckBox) view.findViewById(R.id.checkbox_show_name);


        mSharePledgeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if( !mMainActivity.checkUserLogin() ) {
                    Toast.makeText(mMainActivity, R.string.error_user_not_logged_in, Toast.LENGTH_LONG).show();
                    mMainActivity.popupLogin();
                } else { //  TO DO: Add another else if condition to check if the account has a valid pledge to share

                    // Just commented out temporarily since it could potentially lead to bugs
                    /*
                    User newUser = createUserFromForm( mMainActivity.getCurrentUser().getEmail() );
                    if( newUser != null ) {
                        mMainActivity.updateUserData(newUser);
                        Toast.makeText(mMainActivity, "success", Toast.LENGTH_SHORT).show();
                    }
                    */
                    Toast.makeText(mMainActivity, "TO DO: Implement a share pop up", Toast.LENGTH_SHORT).show();
                }
            }
        });
        mPublishPledgeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if( !mMainActivity.checkUserLogin() ) {
                    Toast.makeText(mMainActivity, R.string.error_user_not_logged_in, Toast.LENGTH_LONG).show();
                    mMainActivity.popupLogin();
                }
                else {
                    String firstName = "";
                    String lastName = "";
                    String city = "";
                    String email = "";
                    Double pledgeAmount = 0.0;

                    firstName = mFirstNameInputField.getText().toString().trim();
                    lastName = mLastNameInputField.getText().toString().trim();
                    city = mMunicipalityInputField.getText().toString().trim();
                    email = mMainActivity.getCurrentUser().getEmail();
                    String pledgeAmountString = mPledgeAmountInputField.getText().toString().trim();

                    if(isInputValid(firstName, lastName, pledgeAmountString)) {
                        pledgeAmount = Double.parseDouble(pledgeAmountString);

                        DatabaseReference usersDatabase;
                        usersDatabase = FirebaseDatabase.getInstance().getReference("Users");

                        String userID = mMainActivity.getCurrentUser().getUid();
                        User user = new User(email, firstName, lastName, city, pledgeAmount);
                        usersDatabase.child(userID).setValue(user);
                    }
                }
            }
        });
    }

    private User createUserFromForm(String userEmail){
        String firstName = mFirstNameInputField.getText().toString();
        String lastName = mLastNameInputField.getText().toString();
        String municipality = mMunicipalityInputField.getText().toString();
        String pledgeAmount = mPledgeAmountInputField.getText().toString();
        User userData = null;

        if( !isInputValid(firstName, lastName, pledgeAmount) ) {
            return userData;
        }else {
            double pledgeValue = Double.parseDouble(pledgeAmount);
            userData = new User(
                    userEmail,
                    firstName,
                    lastName,
                    pledgeValue );
            if( !municipality.isEmpty() ){
                userData.setCity( municipality );
            }
            if( mShowNameCheckbox.isChecked() ){
                userData.setShowNamePublic( true );
            }
        }
        return userData;
    }

    private boolean isInputValid(String firstName, String lastName, String pledgeAmount) {
        boolean isValid = true;

        if( firstName.isEmpty() ){
            Toast.makeText( mMainActivity, R.string.empty_firstname_message, Toast.LENGTH_SHORT ).show();
            isValid = false;
        }
        else if( lastName.isEmpty() ){
            Toast.makeText( mMainActivity, R.string.empty_lastname_message, Toast.LENGTH_SHORT ).show();
            isValid = false;
        }
        else if( pledgeAmount.isEmpty() ) {
            Toast.makeText(mMainActivity, R.string.empty_pledge_message, Toast.LENGTH_SHORT).show();
            isValid = false;
        }
        else {
            double pledgeValue = 0;
            try{
                pledgeValue = Double.parseDouble(pledgeAmount);
            } catch (Exception e) {
                Toast.makeText( mMainActivity, R.string.error_bad_pledge_format, Toast.LENGTH_LONG ).show();
                isValid = false;
            }

            if(pledgeValue < 0) {
                Toast.makeText( mMainActivity, R.string.error_bad_pledge_format, Toast.LENGTH_LONG ).show();
                isValid = false;
            }
        }

        return isValid;
    }
}
