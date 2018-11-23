package ca.cmpt276.greengoblins.fragments;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import ca.cmpt276.greengoblins.emission.MainActivity;
import ca.cmpt276.greengoblins.emission.R;

public class LoginFragment extends DialogFragment{

    private MainActivity mMainActivity;
    private EditText mUsername;
    private EditText mPassword;
    private Button LoginBtn;
    private Button SignupBtn;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //Set a transparent background
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mMainActivity = (MainActivity) getActivity();

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view= LayoutInflater.from(getActivity()).inflate(R.layout.pop_up_login, null);
        mUsername= view.findViewById(R.id.login_username);
        LoginBtn= view.findViewById(R.id.login_btn);
        SignupBtn = view.findViewById(R.id.signup_btn);
        mPassword= view.findViewById(R.id.login_passward);
        LoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userEmail = mUsername.getText().toString();
                String userPassword = mPassword.getText().toString();
                if(userEmail.isEmpty()){
                    Toast.makeText(mMainActivity, R.string.empty_username_message, Toast.LENGTH_SHORT).show();
                }else if( userPassword.isEmpty()) {
                    Toast.makeText(mMainActivity, R.string.empty_password_message, Toast.LENGTH_SHORT).show();
                } else {
                    mMainActivity.signIn( userEmail, userPassword );
                    dismiss();
                }
            }
        });
        SignupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userEmail = mUsername.getText().toString();
                String userPassword = mPassword.getText().toString();
                if(userEmail.isEmpty()){
                    Toast.makeText(mMainActivity, R.string.empty_username_message, Toast.LENGTH_SHORT).show();
                }else if( userPassword.isEmpty()) {
                    Toast.makeText(mMainActivity, R.string.empty_password_message, Toast.LENGTH_SHORT).show();
                } else if(userPassword.length() < 6){
                    Toast.makeText(mMainActivity, R.string.error_short_password, Toast.LENGTH_SHORT).show();
                } else {
                    mMainActivity.signUp( userEmail, userPassword );
                    dismiss();
                }
            }
        });

        builder.setView(view);
        return builder.create();
    }
}
