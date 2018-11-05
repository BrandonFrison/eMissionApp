package ca.cmpt276.greengoblins.emission;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import ca.cmpt276.greengoblins.foodsurveydata.User;
import ca.cmpt276.greengoblins.fragments.AboutPageFragment;
import ca.cmpt276.greengoblins.fragments.HistoryFragment;
import ca.cmpt276.greengoblins.fragments.LoginFragment;
import ca.cmpt276.greengoblins.fragments.PledgeFragment;
import ca.cmpt276.greengoblins.fragments.PledgeListFragment;
import ca.cmpt276.greengoblins.fragments.SurveyFragment;

/**
 * The main activity which handles navigation actions from the nav drawer
 * by switching fragments into content_main.xml
 */
public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    //Firebase authentication fields
    private FirebaseAuth mAuthenticator;
    private FirebaseUser mCurrentUser;

    private User mUserData;

    private FragmentManager mFragmentManager;
    private FragmentTransaction mFragmentTransaction;

    private FloatingActionButton mActionButton;
    private SurveyFragment mSurveyFragment;
    private Fragment mCurrentFragment;

    private TextView mLoginTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(R.string.app_name);

        mActionButton = (FloatingActionButton) findViewById(R.id.fab);
        mActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "Action Button still set to default OnClickListener", Toast.LENGTH_LONG).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View header = navigationView.getHeaderView(0);
        mLoginTextView = (TextView) header.findViewById(R.id.username_textview);

        mAuthenticator = FirebaseAuth.getInstance();
        mCurrentUser = getCurrentUser();
        if(mCurrentUser != null){
            mLoginTextView.setText( mCurrentUser.getEmail() );
        }

        mSurveyFragment = new SurveyFragment();

        mFragmentManager = getSupportFragmentManager();
        mFragmentTransaction = mFragmentManager.beginTransaction();

        mFragmentTransaction.replace(R.id.frame_activity_content, mSurveyFragment);
        mFragmentTransaction.commit();
    }

    public void incrementButton(View view){
        changeFieldValue(view, 10);
    }
    public void decrementButton(View view){
        changeFieldValue(view, -10);
    }
    private void changeFieldValue (View view, int valueToAdd){
        String buttonTag = String.valueOf(view.getTag());
        switch(buttonTag){
            case "beef_button":
                mSurveyFragment.setServing(0, valueToAdd);
                break;
            case "pork_button":
                mSurveyFragment.setServing(1, valueToAdd);
                break;
            case "chicken_button":
                mSurveyFragment.setServing(2, valueToAdd);
                break;
            case "fish_button":
                mSurveyFragment.setServing(3, valueToAdd);
                break;
            case "eggs_button":
                mSurveyFragment.setServing(4, valueToAdd);
                break;
            case "beans_button":
                mSurveyFragment.setServing(5, valueToAdd);
                break;
            case "vegetables_button":
                mSurveyFragment.setServing(6, valueToAdd);
                break;
            default:
                Toast.makeText( MainActivity.this, "Button tag not properly set", Toast.LENGTH_SHORT ).show();
        }
    }

    public FirebaseUser getCurrentUser(){
        return mAuthenticator.getCurrentUser();
    }
    public void updateUserData(User newUserData){
        mUserData = newUserData;
    }

    public void popupLogin(){
        LoginFragment loginFragment = new LoginFragment();
        //Loginfragment.setTargetFragment(MakeYourOwnPledgeFragment.this, 1);
        loginFragment.show(getSupportFragmentManager(), "login");
    }

    public void signUp(String userEmail, String userPassword){
        mAuthenticator.createUserWithEmailAndPassword(userEmail, userPassword)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("SIGNED_UP", "createUserWithEmail:success");
                            mCurrentUser = mAuthenticator.getCurrentUser();
                            updateLoginUI(mCurrentUser);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.d("SIGNED_UP", "createUserWithEmail:failure", task.getException());
                            mCurrentUser = null;
                          //  Toast.makeText(MainActivity.this, "Authentication failed.",Toast.LENGTH_SHORT).show();
                            updateLoginUI(null);
                        }
                    }
                });
    }

    public void signIn(String userEmail, String userPassword){

        if(userEmail.isEmpty()){
            Toast.makeText(MainActivity.this, R.string.empty_username_message, Toast.LENGTH_SHORT).show();
            return;
        }else if( userPassword.isEmpty()){
            Toast.makeText(MainActivity.this, R.string.empty_password_message, Toast.LENGTH_SHORT).show();
            return;
        }else {
            mAuthenticator.signInWithEmailAndPassword(userEmail, userPassword)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d("SIGN_IN", "signInWithEmail:success");
                                mCurrentUser = mAuthenticator.getCurrentUser();
                                Toast.makeText(MainActivity.this, "Authentication success.", Toast.LENGTH_SHORT).show();
                                updateLoginUI(mCurrentUser);
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.d("SIGN_IN", "signInWithEmail:failure", task.getException());
                                mCurrentUser = null;
                                Toast.makeText(MainActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                                updateLoginUI(null);
                            }
                        }
                    });
        }
    }

    public boolean checkUserLogin(){
        FirebaseUser currentUser = mAuthenticator.getCurrentUser();
        if(currentUser == null){
            Log.d("SIGN_IN", "User is not logged in");

            return false;
        }
        Log.d("SIGN_IN", "User is somehow logged in");
        return true;
    }

    public void updateLoginUI(FirebaseUser user){
        if(user == null)
            mLoginTextView.setText(R.string.nav_header_username); //set default
        else{
            mLoginTextView.setText(user.getEmail());
        }
    }

    public FloatingActionButton getActionButton (){
        return mActionButton;
    }

    public boolean startFragment(Fragment newFragment, boolean addToBackStack){
        if ( newFragment == null) return false;
        mFragmentTransaction = mFragmentManager.beginTransaction();

        mFragmentTransaction.replace(R.id.frame_activity_content, newFragment);
        if(addToBackStack) {
            mFragmentTransaction.addToBackStack(null);
        }
        mFragmentTransaction.commit();
        return true;
    }

    public boolean startFragment(Fragment newFragment, boolean addToBackStack, boolean showActionButton ){
        if ((showActionButton)) {
            mActionButton.show();
        } else {
            mActionButton.hide();
        }
        return startFragment( newFragment, addToBackStack );
    }

    //Methods below related to Navigation Drawer

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        Fragment fragment = null;
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        boolean showFloatingActionButton = false;

        if (id == R.id.nav_calculator) {
            fragment = new SurveyFragment();
            mSurveyFragment = (SurveyFragment) fragment;
            showFloatingActionButton = true;
        } else if (id == R.id.nav_pledge) {
            fragment = new PledgeFragment();
        } else if (id == R.id.nav_history) {
            fragment = new HistoryFragment();
        } else if (id == R.id.nav_about) {
            fragment = new AboutPageFragment();
        } else if (id == R.id.nav_community) {
            fragment = new PledgeListFragment();
        }

        startFragment( fragment, true, showFloatingActionButton );

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
