package ca.cmpt276.greengoblins.fragments;

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
import android.widget.Toast;

import ca.cmpt276.greengoblins.emission.MainActivity;
import ca.cmpt276.greengoblins.emission.R;
import ca.cmpt276.greengoblins.foodsurveydata.User;

public class MakePledgeFragment extends Fragment {
    MainActivity mMainActivity;
    private Button mSharePledgeButton;
    private Button mPublishPledgeButton;

    private TextView mFirstName;
    private TextView mLastName;
    private TextView mMunicipality;
    private TextView mPledgeAmount;
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

        mFirstName = (TextView) view.findViewById(R.id.input_first_name);
        mFirstName.setText(mMainActivity.getCurrentUser().getEmail());
        mLastName = (TextView) view.findViewById(R.id.input_last_name);
        mMunicipality = (TextView) view.findViewById(R.id.input_municipality);
        mPledgeAmount = (TextView) view.findViewById(R.id.input_pledge_amount);
        mShowNameCheckbox = (CheckBox) view.findViewById(R.id.checkbox_show_name);


        mSharePledgeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if( !mMainActivity.checkUserLogin() ){
                    Toast.makeText( mMainActivity, R.string.error_user_not_logged_in, Toast.LENGTH_LONG ).show();
                } else {
                    User newUser = createUserFromForm( mMainActivity.getCurrentUser().getEmail() );
                    if( newUser != null ) {
                        mMainActivity.updateUserData(newUser);
                        Toast.makeText(mMainActivity, "success", Toast.LENGTH_SHORT).show();
                    }
                }



                //Fragment newFragment = new MakePledgeFragment();
                //mMainActivity.startFragment( newFragment, true, false);
            }
        });
        mPublishPledgeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Fragment newFragment = new PledgeListFragment();
                //mMainActivity.startFragment( newFragment, true, false);
            }
        });
    }

    private User createUserFromForm(String userEmail){
        String firstName = mFirstName.getText().toString();
        String lastName = mLastName.getText().toString();
        String municipality = mMunicipality.getText().toString();
        String pledgeAmount = mPledgeAmount.getText().toString();
        User userData = null;

        if( firstName.isEmpty() ){
            Toast.makeText( mMainActivity, R.string.empty_firstname_message, Toast.LENGTH_SHORT ).show();
            return userData;
        }else if( lastName.isEmpty() ){
            Toast.makeText( mMainActivity, R.string.empty_lastname_message, Toast.LENGTH_SHORT ).show();
            return  userData;
        }else if( pledgeAmount.isEmpty() ){
            Toast.makeText( mMainActivity, R.string.empty_pledge_message, Toast.LENGTH_SHORT ).show();
            return  userData;
        }else{
            double pledgeValue;
            try{
                pledgeValue = Double.parseDouble(pledgeAmount);
            } catch (Exception e){
                pledgeValue = 0;
                Toast.makeText( mMainActivity, R.string.error_bad_pledge_format, Toast.LENGTH_LONG ).show();
            }
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
}
