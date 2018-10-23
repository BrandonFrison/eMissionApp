package ca.cmpt276.greengoblins.emission;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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

/**
 * The main activity which opens at app start providing a survey for the user to fill in.
 * Updates UI elements based on input and saves the data of the survey into a ConsumptionTable
 * object when the user presses submit button
 */
public class SurveyActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private TextView mBeefTextView;
    private TextView mPorkTextView;
    private TextView mChickenTextView;
    private TextView mFishTextView;
    private TextView mEggsTextView;
    private TextView mBeanTextView;
    private TextView mVegetablesTextView;
    private TextView mTotalTextView;

    private int mBeefServingSize = 0;
    private int mPorkServingSize = 0;
    private int mChickenServingSize = 0;
    private int mFishServingSize = 0;
    private int mEggsServingSize = 0;
    private int mBeansServingSize = 0;
    private int mVegetablesServingSize = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Numeric TextView instantiation
        mBeefTextView = (TextView) findViewById(R.id.textViewBeefAmount);
        mPorkTextView = (TextView) findViewById(R.id.textViewPorkAmount);
        mChickenTextView = (TextView) findViewById(R.id.textViewChickenAmount);
        mFishTextView = (TextView) findViewById(R.id.textViewFishAmount);
        mEggsTextView = (TextView) findViewById(R.id.textViewEggsAmount);
        mBeanTextView = (TextView) findViewById(R.id.textViewBeansAmount);
        mVegetablesTextView = (TextView) findViewById(R.id.textViewVegetablesAmount);
        mTotalTextView = (TextView) findViewById(R.id.textViewTotalAmount);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, R.string.survey_submission_message, Snackbar.LENGTH_LONG).setAction("Action", null);
                //Saving ConsumptionTable goes here
                //Start Result Activity here
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    public int getFieldsTotal(){
        return mBeefServingSize +
                mPorkServingSize +
                mChickenServingSize +
                mFishServingSize +
                mEggsServingSize +
                mBeansServingSize +
                mVegetablesServingSize;
    }

    public void updateValues(){
        mBeefTextView.setText(String.valueOf(mBeefServingSize));
        mPorkTextView.setText(String.valueOf(mPorkServingSize));
        mChickenTextView.setText(String.valueOf(mChickenServingSize));
        mFishTextView.setText(String.valueOf(mFishServingSize));
        mEggsTextView.setText(String.valueOf(mEggsServingSize));
        mBeanTextView.setText(String.valueOf(mBeansServingSize));
        mVegetablesTextView.setText(String.valueOf(mVegetablesServingSize));
        mTotalTextView.setText(String.valueOf(getFieldsTotal()));
    }

    public void decreaseBeef(View view){
        mBeefServingSize -= 10;
        updateValues();
    }

    public void increaseBeef(View view){
        mBeefServingSize += 10;
        updateValues();
    }

    public void decreasePork(View view){
        mPorkServingSize -= 10;
        updateValues();
    }

    public void increasePork(View view){
        mPorkServingSize += 10;
        updateValues();
    }

    public void decreaseChicken(View view){
        mChickenServingSize -= 10;
        updateValues();
    }

    public void increaseChicken(View view){
        mChickenServingSize += 10;
        updateValues();
    }

    public void decreaseFish(View view){
        mFishServingSize -= 10;
        updateValues();
    }

    public void increaseFish(View view){
        mFishServingSize += 10;
        updateValues();
    }

    public void decreaseEggs(View view){
        mEggsServingSize -= 10;
        updateValues();
    }

    public void increaseEggs(View view){
        mEggsServingSize += 10;
        updateValues();
    }

    public void decreaseBeans(View view){
        mBeansServingSize -= 10;
        updateValues();
    }

    public void increaseBeans(View view){
        mBeansServingSize += 10;
        updateValues();
    }
    public void decreaseVegetables(View view){
        mVegetablesServingSize -= 10;
        updateValues();
    }

    public void increaseVegetables(View view){
        mVegetablesServingSize += 10;
        updateValues();
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
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
