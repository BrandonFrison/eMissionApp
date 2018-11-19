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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

import ca.cmpt276.greengoblins.foodsurveydata.ConsumptionTable;
import ca.cmpt276.greengoblins.foodsurveydata.User;
import ca.cmpt276.greengoblins.fragments.AboutPageFragment;
import ca.cmpt276.greengoblins.fragments.HistoryFragment;
import ca.cmpt276.greengoblins.fragments.ResultFragment;
import ca.cmpt276.greengoblins.fragments.LoginFragment;
import ca.cmpt276.greengoblins.fragments.MealsListFragment;
import ca.cmpt276.greengoblins.fragments.PledgeFragment;
import ca.cmpt276.greengoblins.fragments.SettingsFragment;
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
    private ArrayList<String> mFoodSurveyCategories;
    private ArrayList<Float> mCO2eConversionRates;
    private Fragment mCurrentFragment;

    private TextView mLoginTextView;
    private Spinner mLoginDropdown;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(R.string.app_name);

        mUserData = new User();

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
        mLoginDropdown = (Spinner) header.findViewById(R.id.dropdown_login);

        mAuthenticator = FirebaseAuth.getInstance();

        updateLoginUI();

        mSurveyFragment = new SurveyFragment();
        mFoodSurveyCategories = new ArrayList<String>();
        mFoodSurveyCategories.add(getString( R.string.table_category1 ));   //Beef
        mFoodSurveyCategories.add(getString( R.string.table_category2 ));   //Pork
        mFoodSurveyCategories.add(getString( R.string.table_category3 ));   //Chicken
        mFoodSurveyCategories.add(getString( R.string.table_category4 ));   //Fish
        mFoodSurveyCategories.add(getString( R.string.table_category5 ));   //Eggs
        mFoodSurveyCategories.add(getString( R.string.table_category6 ));   //Beans
        mFoodSurveyCategories.add(getString( R.string.table_category7 ));   //Vegetables
        mCO2eConversionRates = new ArrayList<Float>();
        mCO2eConversionRates.add(27f);
        mCO2eConversionRates.add(12.1f);
        mCO2eConversionRates.add(6.9f);
        mCO2eConversionRates.add(6.1f);
        mCO2eConversionRates.add(4.8f);
        mCO2eConversionRates.add(2f);
        mCO2eConversionRates.add(2f);

        mFragmentManager = getSupportFragmentManager();
        mFragmentTransaction = mFragmentManager.beginTransaction();

        mFragmentTransaction.replace(R.id.frame_activity_content, mSurveyFragment);
        mFragmentTransaction.commit();
    }


    public String getUserDisplayName(){
        FirebaseUser user = mAuthenticator.getCurrentUser();
        if(user == null){
            return getString(R.string.nav_header_username);
        }
        return user.getEmail();
    }

    public FirebaseUser getCurrentUser(){
        return mAuthenticator.getCurrentUser();
    }
    public void updateUserData(User newUserData){
        mUserData = newUserData;
    }
    public void setUserAvatar(int avatarID) {
        mUserData.setAvatarID(avatarID);
    }
    public int getUserAvatar() {
        return mUserData.getAvatarID();
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
                            updateLoginUI();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.d("SIGNED_UP", "createUserWithEmail:failure", task.getException());
                            mCurrentUser = null;
                          //  Toast.makeText(MainActivity.this, "Authentication failed.",Toast.LENGTH_SHORT).show();
                            updateLoginUI();
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
                                updateLoginUI();
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.d("SIGN_IN", "signInWithEmail:failure", task.getException());
                                mCurrentUser = null;
                                Toast.makeText(MainActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                                updateLoginUI();
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

    public void logout(){
        mAuthenticator.signOut();
        mCurrentUser = null;
        updateLoginUI();
    }

    public void updateLoginUI(){
        FirebaseUser currentUser = mAuthenticator.getCurrentUser();

        ArrayList<String> loginDropdownItems = new ArrayList<String>();
        loginDropdownItems.add( getUserDisplayName() );
        if(currentUser == null){
            loginDropdownItems.add( getString(R.string.nav_header_login) );
        } else {
            loginDropdownItems.add( getString(R.string.nav_header_logout) );
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                MainActivity.this,
                android.R.layout.simple_spinner_item,
                loginDropdownItems
        );


        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        mLoginDropdown.setAdapter(adapter);

        mLoginDropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        //popupLogin();
                        break;
                    case 1:
                        if(checkUserLogin()) {
                            logout();
                            mLoginDropdown.setSelection(0);
                        }else{
                            popupLogin();
                            mLoginDropdown.setSelection(0);
                        }
                        break;
                    default:
                        //intentionally empty
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        /*if(currentUser == null)
            mLoginTextView.setText(R.string.nav_header_username); //set default
        else{
            mLoginTextView.setText(currentUser.getEmail());
        }*/
    }

    public FloatingActionButton getActionButton (){
        return mActionButton;
    }

    public ConsumptionTable createDefaultFoodTable(){
        ConsumptionTable defaultCategories = new ConsumptionTable( mFoodSurveyCategories, mCO2eConversionRates );
        return defaultCategories;
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
            Fragment fragment = new SettingsFragment();
            mActionButton.hide();
            startFragment( fragment, true );
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
        }else if (id == R.id.nav_meals) {
            fragment = new MealsListFragment();
        }

        startFragment( fragment, true, showFloatingActionButton );

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
