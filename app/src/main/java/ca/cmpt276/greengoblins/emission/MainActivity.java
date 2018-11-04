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

import ca.cmpt276.greengoblins.fragments.AboutPageFragment;
import ca.cmpt276.greengoblins.fragments.HistoryFragment;
import ca.cmpt276.greengoblins.fragments.PledgeFragment;
import ca.cmpt276.greengoblins.fragments.SurveyFragment;

/**
 * The main activity which handles navigation actions from the nav drawer
 * by switching fragments into content_main.xml
 */
public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    //Firebase authentication fields
    private FirebaseAuth mAuthenticator;


    FragmentManager mFragmentManager;
    FragmentTransaction mFragmentTransaction;

    FloatingActionButton mActionButton;
    SurveyFragment mSurveyFragment;
    Fragment mCurrentFragment;

    TextView mLoginTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("eMission");

        mLoginTextView = (TextView) findViewById(R.id.username_textview);

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

        mAuthenticator = FirebaseAuth.getInstance();

        mSurveyFragment = new SurveyFragment();

        mFragmentManager = getSupportFragmentManager();
        mFragmentTransaction = mFragmentManager.beginTransaction();

        mFragmentTransaction.replace(R.id.frame_activity_content, mSurveyFragment);
        mFragmentTransaction.commit();
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

    public void signUp(String userEmail, String userPassword){
        mAuthenticator.createUserWithEmailAndPassword(userEmail, userPassword)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("SIGNED_UP", "createUserWithEmail:success");
                            FirebaseUser user = mAuthenticator.getCurrentUser();
                            updateLoginUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.d("SIGNED_UP", "createUserWithEmail:failure", task.getException());
                          //  Toast.makeText(MainActivity.this, "Authentication failed.",Toast.LENGTH_SHORT).show();
                            updateLoginUI(null);
                        }
                    }
                });
    }

    public void signIn(String userEmail, String userPassword){

        if(userEmail.isEmpty() || userPassword.isEmpty()){
            Log.d("SIGN_IN", "empty fields");
            return;
        }

        mAuthenticator.signInWithEmailAndPassword(userEmail, userPassword)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("SIGN_IN", "signInWithEmail:success");
                            FirebaseUser user = mAuthenticator.getCurrentUser();
                            //Toast.makeText(MainActivity.this, "Authentication success.",Toast.LENGTH_SHORT).show();
                            updateLoginUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.d("SIGN_IN", "signInWithEmail:failure", task.getException());
                            //Toast.makeText(MainActivity.this, "Authentication failed.",Toast.LENGTH_SHORT).show();
                            updateLoginUI(null);
                        }
                    }
                });
//        String msg = "finished signin method:: ";
//        if(user == null){
//            msg = msg + "user is null";
//        }else {
//            msg = msg + "email: " + user.getEmail();
//        }
//
//        Log.d("SIGN_IN", msg);
    }

    public void checkUserLogin(){
        FirebaseUser currentUser = mAuthenticator.getCurrentUser();
        if(currentUser == null){
            Log.d("SIGN_IN", "User is not logged in");

            //Toast.makeText(MainActivity.this, "User is not logged in", Toast.LENGTH_LONG).show();
        }else{
            Log.d("SIGN_IN", "User is somehow logged in");
            //Toast.makeText(MainActivity.this, "User is somehow logged in", Toast.LENGTH_LONG).show();
        }
    }

    public void updateLoginUI(FirebaseUser user){
        if(user == null)
            mLoginTextView.setText(R.string.nav_header_username); //set default
        else{
            mLoginTextView.setText(user.getEmail());
        }
    }



    public void incrementButton(View view){
        changeFieldValue(view, 10);
    }
    public void decrementButton(View view){
        changeFieldValue(view, -10);
    }


    public FloatingActionButton getActionButton (){
        return mActionButton;
    }

    public boolean startFragment(Fragment newFragment, boolean addToBackStack){
        if ( newFragment != null ) {
            mFragmentTransaction = mFragmentManager.beginTransaction();

            mFragmentTransaction.replace(R.id.frame_activity_content, newFragment);
            if(addToBackStack) {
                mFragmentTransaction.addToBackStack(null);
            }
            mFragmentTransaction.commit();
            return true;
        }
        return false;
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

        if (id == R.id.nav_calculator) {
            fragment = new SurveyFragment();
            mSurveyFragment = (SurveyFragment) fragment;
            mActionButton.show();
        } else if (id == R.id.nav_pledge) {
            fragment = new PledgeFragment();
            mActionButton.hide();
        } else if (id == R.id.nav_history) {
            fragment = new HistoryFragment();
            mActionButton.hide();
        } else if (id == R.id.nav_about) {
            fragment = new AboutPageFragment();
            mActionButton.hide();
        } else if (id == R.id.nav_community) {

        }

        startFragment( fragment, true );

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
