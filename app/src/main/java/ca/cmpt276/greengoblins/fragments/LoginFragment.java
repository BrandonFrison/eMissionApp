package ca.cmpt276.greengoblins.fragments;

import android.app.Dialog;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import ca.cmpt276.greengoblins.emission.R;

public class LoginFragment extends DialogFragment implements View.OnClickListener{
    private EditText mUsername;
    private EditText mPassword;
    private Button LoginBtn;
    private TextView toReg;


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
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view= LayoutInflater.from(getActivity()).inflate(R.layout.op_up_login, null);
        toReg= view.findViewById(R.id.login_register);
        mUsername= view.findViewById(R.id.login_username);
        LoginBtn= view.findViewById(R.id.login_btn);
        mPassword= view.findViewById(R.id.login_passward);
        toReg.setOnClickListener(this);
        LoginBtn.setOnClickListener(this);

        builder.setView(view);
        return builder.create();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.login_btn:// if u want to send data to an activity/fragment:
                //LoginInputListener listener= (LoginInputListener) getActivity();
                //listener.onLoginInputComplete(mUsername.getText().toString(), mPassword.getText().toString());


                //then,in that activity/fragment:
                //public class MainActivity extends AppCompatActivity implements LoginFragment.LoginInputListener{
                //and create a method:
                //public void onLoginInputComplete(String userName, String userPassword) {
                //name.setText(userName);
                //password.setText(userPassword);
                dismiss();
                break;
        }
    }
    //public interface LoginInputListener{
    //    void onLoginInputComplete(String userName, String password);
   // }
}
