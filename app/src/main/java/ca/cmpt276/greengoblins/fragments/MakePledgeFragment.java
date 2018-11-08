package ca.cmpt276.greengoblins.fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;

import ca.cmpt276.greengoblins.emission.MainActivity;
import ca.cmpt276.greengoblins.emission.R;
import ca.cmpt276.greengoblins.foodsurveydata.User;
import ca.cmpt276.greengoblins.foodsurveydata.Utils;

import static android.app.Activity.RESULT_OK;

public class MakePledgeFragment extends Fragment {
    MainActivity mMainActivity;
    private Button mSharePledgeButton;
    private Button mPublishPledgeButton;

    EditText mFirstNameInputField;
    EditText mLastNameInputField;
    EditText mMunicipalityInputField;
    EditText mPledgeAmountInputField;
    private CheckBox mShowNameCheckbox;

    protected static final int CHOOSE_PICTURE = 0;
    protected static final int TAKE_PICTURE = 1;
    private static final int CROP_SMALL_PICTURE = 2;
    protected static Uri tempUri;
    private ImageView iv_personal_icon;
    private ca.cmpt276.greengoblins.foodsurveydata.Utils Utils = null;

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
        iv_personal_icon = (ImageView) view.findViewById(R.id.iv_personal_icon);
        Utils = new Utils();

        iv_personal_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showChoosePicDialog();
            }
        });


        mSharePledgeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if( !mMainActivity.checkUserLogin() ) {
                    Toast.makeText(mMainActivity, R.string.error_user_not_logged_in, Toast.LENGTH_LONG).show();
                    mMainActivity.popupLogin();
                } else { //  TO DO: Add another else if condition to check if the account has a valid pledge to share
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

    private void showChoosePicDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mMainActivity);
        builder.setTitle("Edit Avatar");
        String[] items = { "Select from Album", "Take Photo" };
        builder.setNegativeButton("Cancel", null);
        builder.setItems(items, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case CHOOSE_PICTURE: // Select from Album
                        Intent openAlbumIntent = new Intent(Intent.ACTION_GET_CONTENT);
                        openAlbumIntent.setType("image/*");
                        startActivityForResult(openAlbumIntent, CHOOSE_PICTURE);

                        break;
                    case TAKE_PICTURE: //Take Photo
                        Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        tempUri = Uri.fromFile(
                                new File(Environment.getExternalStorageDirectory() + "/AndroidPersonal_icon", "image.jpg"));
                        openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, tempUri);
                        startActivityForResult(openCameraIntent, TAKE_PICTURE);
                        break;
                    default:
                        break;
                }
            }
        });
        builder.create().show();
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case TAKE_PICTURE:
                    startPhotoZoom(tempUri);
                    break;
                case CHOOSE_PICTURE:
                    startPhotoZoom(data.getData());
                    break;
                case CROP_SMALL_PICTURE:
                    if (data != null) {
                        setImageToView(data);
                    }
                    break;
            }
        }
    }

    private void setImageToView(Intent data) {
        Bundle extras = data.getExtras();
        if (extras != null) {
            Bitmap photo = extras.getParcelable("data");
            photo = Utils.toRoundBitmap(photo, tempUri); 
            iv_personal_icon.setImageBitmap(photo);
            uploadPic(photo);
        }
    }


    private void uploadPic(Bitmap photo) {
        String imagePath = Utils.savePhoto(photo,
                Environment.getExternalStorageDirectory().getAbsolutePath() + "/AndroidPersonal_icon", "image_icon");
        Log.d("imagePath", imagePath + "");
        if (imagePath != null) {

        }
    }

    private void startPhotoZoom(Uri uri) {
        if (uri == null) {
            Log.i("tag", "The uri is not exist.");
        }
        tempUri = uri;
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // 设置裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 150);
        intent.putExtra("outputY", 150);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, CROP_SMALL_PICTURE);
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
