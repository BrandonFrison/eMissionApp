package ca.cmpt276.greengoblins.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
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
import ca.cmpt276.greengoblins.foodsurveydata.Meal;
import ca.cmpt276.greengoblins.foodsurveydata.User;

public class MakePledgeFragment extends Fragment {
    MainActivity mMainActivity;
    private Button mSharePledgeButton;
    private Button mPublishPledgeButton;
    private Button mRemovePledgeButton;

    private CallbackManager mCallbackManager;
    private ShareDialog mShareDialog;

    private EditText mFirstNameInputField;
    private EditText mLastNameInputField;
    private EditText mMunicipalityInputField;
    private EditText mPledgeAmountInputField;
    private CheckBox mShowNameCheckbox;

    private ImageView iv_personal_icon;
    private boolean mUserHasPledged;
    private int id_avatar = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_pledge_create, container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        getActivity().setTitle(R.string.toolbar_pledge_create);
        mMainActivity = (MainActivity) getActivity();
        mSharePledgeButton = (Button) view.findViewById(R.id.button_share_pledge);
        mPublishPledgeButton = (Button) view.findViewById(R.id.button_publish_pledge);
        mRemovePledgeButton = (Button) view.findViewById(R.id.button_remove_pledge);

        mFirstNameInputField = (EditText) view.findViewById(R.id.input_first_name);
        mLastNameInputField = (EditText) view.findViewById(R.id.input_last_name);
        mMunicipalityInputField = (EditText) view.findViewById(R.id.input_municipality);
        mPledgeAmountInputField = (EditText) view.findViewById(R.id.input_pledge_amount);
        mShowNameCheckbox = (CheckBox) view.findViewById(R.id.checkbox_show_name);
        iv_personal_icon = (ImageView) view.findViewById(R.id.iv_personal_icon);

        // These must be here and nowhere else otherwise the profile pic breaks
        id_avatar = mMainActivity.getUserAvatar();
        switchAvater();

        iv_personal_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment newFragment = new ProvidedAvatar();
                mMainActivity.startFragment( newFragment, true, false);
            }
        });

        mUserHasPledged = false;

        populateForm( mMainActivity.getCurrentUser().getUid() );

        fillReductionFromReduceFragment();

        mSharePledgeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if( !mMainActivity.checkUserLogin() ) {
                    Toast.makeText(mMainActivity, R.string.error_user_not_logged_in, Toast.LENGTH_LONG).show();
                    mMainActivity.popupLogin();
                } else { //  TO DO: Add another else if condition to check if the account has a valid pledge to share
                    mCallbackManager = CallbackManager.Factory.create();
                    mShareDialog = new ShareDialog(getActivity());

                    if(ShareDialog.canShow(ShareLinkContent.class)){
                        String facebookQuote = getString(R.string.facebook_share_quote);
                        facebookQuote = String.format(facebookQuote,
                                mMunicipalityInputField.getText(),
                                mPledgeAmountInputField.getText());
                        ShareLinkContent mLinkContent = new ShareLinkContent.Builder()
                                .setContentUrl(Uri.parse("https://drive.google.com/file/d/1y7wCDZhyYm7fMyg1CVy1vYZ-LLhADWEk/view?usp=sharing"))
                                //this is where we would need to put our app information but im not sure how to get it working
                                .setQuote(facebookQuote)
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
                if( publishPledge() ) {
                    if( mUserHasPledged ) {
                        Toast.makeText(mMainActivity, R.string.pledge_updated, Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(mMainActivity, R.string.pledge_published, Toast.LENGTH_SHORT).show();
                    }
                    mUserHasPledged = true;
                    updatePublishButtonText();
                }
            }
        });

        mRemovePledgeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if( !mMainActivity.checkUserLogin() ) {
                    Toast.makeText(mMainActivity, R.string.error_user_not_logged_in, Toast.LENGTH_LONG).show();
                    mMainActivity.popupLogin();
                }
                else if ( !mUserHasPledged ) {
                    Toast.makeText(mMainActivity, R.string.pledge_does_not_exist, Toast.LENGTH_LONG).show();
                }
                else {
                    removePledge();
                    mUserHasPledged = false;
                    clearForm();
                    updatePublishButtonText();
                    Toast.makeText(mMainActivity, R.string.pledge_removed, Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void updatePublishButtonText(){
        if(mUserHasPledged){
            mPublishPledgeButton.setText(R.string.button_update_pledge);
        }else{
            mPublishPledgeButton.setText(R.string.button_publish_pledge);
        }
    }

    private void clearForm(){
        mFirstNameInputField.setText( "" );
        mLastNameInputField.setText( "" );
        mMunicipalityInputField.setText( "" );
        mPledgeAmountInputField.setText( "" );
        mShowNameCheckbox.setChecked( false );
    }

    private void switchAvater() {
        if(id_avatar == 0){

        }
        if(id_avatar == 1){
            iv_personal_icon.setImageResource(R.drawable.avatar1);
        }
        if(id_avatar == 2){
            iv_personal_icon.setImageResource(R.drawable.avatar2);
        }
        if(id_avatar == 3){
            iv_personal_icon.setImageResource(R.drawable.avatar3);
        }
        if(id_avatar == 4){
            iv_personal_icon.setImageResource(R.drawable.avatar4);
        }
        if(id_avatar == 5){
            iv_personal_icon.setImageResource(R.drawable.avatar5);
        }
        if(id_avatar == 6){
            iv_personal_icon.setImageResource(R.drawable.avatar6);
        }
        if(id_avatar == 7){
            iv_personal_icon.setImageResource(R.drawable.avatar7);
        }
        if(id_avatar == 8){
            iv_personal_icon.setImageResource(R.drawable.avatar8);
        }
        if(id_avatar == 9){
            iv_personal_icon.setImageResource(R.drawable.avatar9);
        }

    }

    private void populateForm(final String userID){
        final DatabaseReference usersDatabase;
        usersDatabase = FirebaseDatabase.getInstance().getReference("Users");

        usersDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild(userID)){
                    mUserHasPledged = true;
                    User user = (User) dataSnapshot.child(userID).getValue(User.class);

                    mFirstNameInputField.setText( user.getFirstName() );
                    mLastNameInputField.setText( user.getLastName() );
                    mMunicipalityInputField.setText( user.getCity() );
                    if(mMainActivity.getCO2SavedWithAlternate() > 0){
                        mPledgeAmountInputField.setText( String.valueOf( mMainActivity.getCO2SavedWithAlternate()));
                    }
                    else{
                        mPledgeAmountInputField.setText( String.valueOf( user.getPledgeAmount() ) );
                    }
                    mShowNameCheckbox.setChecked( user.isShowNamePublic() );

                }else{
                    mUserHasPledged = false;
                }
                updatePublishButtonText();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                mUserHasPledged = false;
            }
        });
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

    private void removePledge(){
        DatabaseReference usersDatabase;
        usersDatabase = FirebaseDatabase.getInstance().getReference("Users");

        String userID = mMainActivity.getCurrentUser().getUid();

        usersDatabase.child(userID).removeValue();
    }

    private boolean publishPledge() {
        boolean success = false;

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
        boolean showName = mShowNameCheckbox.isChecked();

        if(isInputValid( firstName, lastName, pledgeAmountString )) {
            pledgeAmount = Double.parseDouble(pledgeAmountString);

            final DatabaseReference usersDatabase;
            usersDatabase = FirebaseDatabase.getInstance().getReference("Users");

            final String userID = mMainActivity.getCurrentUser().getUid();

            User user = new User(email, firstName, lastName, city, pledgeAmount, showName);
            user.setAvatarID(id_avatar);
            usersDatabase.child(userID).setValue(user);

            success = true;
        }
        return success;
    }

    @Override
    public void  onActivityResult(final int requestCode, final int resultCode, final Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void fillReductionFromReduceFragment(){
        Bundle reduceBundle = getArguments();
        if(reduceBundle != null) {
            Double pledgeAmt = reduceBundle.getDouble("savings_data");
            mPledgeAmountInputField.setText(String.format("%.2f", pledgeAmt));
        }
    }


}
