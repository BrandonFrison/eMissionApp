package ca.cmpt276.greengoblins.emission;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import ca.cmpt276.greengoblins.foodsurveydata.Meal;

public class PopupMealDetail extends AppCompatActivity {

    boolean mViewMyMeal = false;  // true when user click the check box to view his own meals
    private ImageView mMealImage;
    private TextView mMealName;
    private TextView mRestaurantName;
    private TextView mMealLocation;
    private TextView mMealProtein;
    private TextView mMealDescription;
    private Button mDeleteButton;
    private Meal mSelectedMeal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popup_meal_detail);
        setTitle(R.string.popup_header_meal_detail);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;

        getWindow().setLayout((int)(width*0.8),(int)(height*0.8));

        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.gravity = Gravity.CENTER;
        params.x = 0;
        params.y = -20;

        getWindow().setAttributes(params);
        //===============================================================
        mDeleteButton = findViewById(R.id.delete_button);
        mDeleteButton.setVisibility(View.INVISIBLE);
        mDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteMeal();
            }
        });

        mMealImage = (ImageView) findViewById(R.id.meal_detail_image);
        mMealName = (TextView) findViewById(R.id.meal_detail_meal_name);
        mRestaurantName = (TextView) findViewById(R.id.meal_detail_restaurant_name);
        mMealLocation = (TextView) findViewById(R.id.meal_detail_meal_location);
        mMealProtein = (TextView) findViewById(R.id.meal_detail_meal_protein);
        mMealDescription = (TextView) findViewById(R.id.meal_detail_meal_description);

        getMealInfo();

    }

    private void getMealInfo() {
        Bundle bundle = getIntent().getExtras();
        mSelectedMeal = (Meal) bundle.getSerializable("meal");
        String userID = bundle.getString("userID");

        String mealName = getString(R.string.meal_detail_meal_name);
        mealName = String.format( mealName, mSelectedMeal.getMealName() );
        String restaurantName = getString(R.string.meal_detail_restaurant_name);
        restaurantName = String.format( restaurantName, mSelectedMeal.getRestaurantName() );
        String mealLocation = getString(R.string.meal_detail_meal_location);
        mealLocation = String.format( mealLocation,  mSelectedMeal.getLocation() );
        String mealProtein = getString(R.string.meal_detail_meal_protein);
        mealProtein = String.format( mealProtein, mSelectedMeal.getMainProteinIngredient() );
        String mealDescription = getString(R.string.meal_detail_meal_description);
        mealDescription = String.format( mealDescription, mSelectedMeal.getDescription() );

        mMealName.setText( mealName );
        mRestaurantName.setText( restaurantName );
        mMealLocation.setText( mealLocation );
        mMealProtein.setText( mealProtein );
        mMealDescription.setText( mealDescription );

        if( mSelectedMeal.getMealCreatorID().equals(userID) ){
            mDeleteButton.setVisibility(View.VISIBLE);
        }
    }

    private void deleteMeal() {
//        DatabaseReference usersDatabase;
//        Query toBeDeleted;
//        usersDatabase = FirebaseDatabase.getInstance().getReference("Meals");
//
//        String userID = mMainActivity.getCurrentUser().getUid();
//
//        toBeDeleted = usersDatabase.orderByChild("mealCreatorID").equalTo(userID);
//        usersDatabase = toBeDeleted.getRef();
//        usersDatabase.getParent().removeValue();
    }
}
