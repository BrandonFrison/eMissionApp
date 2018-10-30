package ca.cmpt276.greengoblins.emission;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

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

    FragmentManager mFragmentManager;
    FragmentTransaction mFragmentTransaction;

    FloatingActionButton mActionButton;
    SurveyFragment mSurveyFragment;
    Fragment mCurrentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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
