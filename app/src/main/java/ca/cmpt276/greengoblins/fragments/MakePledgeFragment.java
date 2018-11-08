package ca.cmpt276.greengoblins.fragments;

import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
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

import com.facebook.CallbackManager;
import com.facebook.share.model.ShareHashtag;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import ca.cmpt276.greengoblins.emission.MainActivity;
import ca.cmpt276.greengoblins.emission.R;
import ca.cmpt276.greengoblins.foodsurveydata.User;

public class MakePledgeFragment extends Fragment {
    MainActivity mMainActivity;
    private Button mSharePledgeButton;
    private Button mPublishPledgeButton;

    CallbackManager mCallbackManager;
    ShareDialog mShareDialog;

    EditText mFirstNameInputField;
    EditText mLastNameInputField;
    EditText mMunicipalityInputField;
    EditText mPledgeAmountInputField;
    private CheckBox mShowNameCheckbox;

    private boolean mUserHasPledged;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_pledge_create, container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        getActivity().setTitle("Make a Pledge");
        mMainActivity = (MainActivity) getActivity();
        mSharePledgeButton = (Button) view.findViewById(R.id.button_share_pledge);
        mPublishPledgeButton = (Button) view.findViewById(R.id.button_publish_pledge);

        mFirstNameInputField = (EditText) view.findViewById(R.id.input_first_name);
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
                    //Toast.makeText(mMainActivity, "TO DO: Implement a share pop up", Toast.LENGTH_SHORT).show();


                    mCallbackManager = CallbackManager.Factory.create();
                    mShareDialog = new ShareDialog(getActivity());

                    if(ShareDialog.canShow(ShareLinkContent.class)){
                        ShareLinkContent mLinkContent = new ShareLinkContent.Builder()
                                .setContentUrl(Uri.parse("http://developers.facebook.com/android"))
                                //this is where we would need to put our app information but im not sure how to get it working
                                .setQuote("I challenge everyone from " + mMunicipalityInputField.getText() + " to the Green Challenge! " +
                                        "Try to beat my pledge of " + mPledgeAmountInputField.getText() + " tonnes of CO2e reduced!\n")
                                .setShareHashtag(new ShareHashtag.Builder()
                                        .setHashtag("#ReduceEmission") //lol we can change this to whatever we want
                                        .build())
                                .build();
                        mShareDialog.show(mLinkContent);
                    }
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
                else if (!pledgeExists(mMainActivity.getCurrentUser().getUid())) {
                    publishPledge();
                }
                else {
                    Toast.makeText(mMainActivity, R.string.pledge_already_exists, Toast.LENGTH_LONG).show();
                }
            }
        });

        // CHANGE PLEDGE BUTTON GOES HERE
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

    private boolean pledgeExists(final String userID) {
        final DatabaseReference usersDatabase;
        usersDatabase = FirebaseDatabase.getInstance().getReference("Users");

        usersDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    User userInfo = (User) dataSnapshot.child(userID).getValue(User.class);
                    if(userInfo.getPledgeAmount() > 0 ) {
                        mUserHasPledged = true;
                    }
                    else
                    {
                        mUserHasPledged = false;
                    }
                }
                catch (Exception e){
                    mUserHasPledged = false;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                mUserHasPledged = false;
                Toast.makeText(mMainActivity, R.string.pledge_does_not_exist, Toast.LENGTH_SHORT).show();
            }
        });

        return mUserHasPledged;
    }


    private void publishPledge() {
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

            final DatabaseReference usersDatabase;
            usersDatabase = FirebaseDatabase.getInstance().getReference("Users");

            final String userID = mMainActivity.getCurrentUser().getUid();
            //pledgeExists(userID);
            User user = new User(email, firstName, lastName, city, pledgeAmount);
            usersDatabase.child(userID).setValue(user);

            Toast.makeText(mMainActivity, R.string.pledge_published, Toast.LENGTH_SHORT).show();
        }
    }
}
