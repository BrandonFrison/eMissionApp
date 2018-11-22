package ca.cmpt276.greengoblins.fragments.Meal;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import ca.cmpt276.greengoblins.emission.MainActivity;
import ca.cmpt276.greengoblins.emission.R;
import ca.cmpt276.greengoblins.foodsurveydata.Meal;

public class MakeMealFragment extends Fragment {

    private MainActivity mMainActivity;

    private EditText mMealNameInputField;
    private EditText mMainProteinIngredientInputField;
    private EditText mRestaurantNameInputField;
    private EditText mLocationInputField;
    private EditText mDescriptionInputField;

    private FloatingActionButton mActionButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_make_meal, container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mMainActivity = (MainActivity) getActivity();

        mActionButton = mMainActivity.getActionButton();
        mActionButton.setImageResource(R.drawable.ic_menu_send);
        mActionButton.show();

        mMealNameInputField = (EditText) view.findViewById(R.id.fragment_make_meal_input_meal_name);
        mMainProteinIngredientInputField = (EditText) view.findViewById(R.id.fragment_make_meal_input_main_protein_ingredient);
        mRestaurantNameInputField = (EditText) view.findViewById(R.id.fragment_make_meal_input_restaurant_name);
        mLocationInputField = (EditText) view.findViewById(R.id.fragment_make_meal_input_location);
        mDescriptionInputField = (EditText) view.findViewById(R.id.fragment_make_meal_input_description);

        mActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                publishMeal();
            }
        });
    }

    private void publishMeal() {
        String mealName = mMealNameInputField.getText().toString().trim().toLowerCase();
        String mainProteinIngredient = mMainProteinIngredientInputField.getText().toString().trim().toLowerCase();
        String restaurantName = mRestaurantNameInputField.getText().toString().trim().toLowerCase();
        String location = mLocationInputField.getText().toString().trim().toLowerCase();
        String description = mDescriptionInputField.getText().toString().trim().toLowerCase();

        if(isInputValid(mealName, mainProteinIngredient, restaurantName, location)) {
            final DatabaseReference mealDatabase;
            mealDatabase = FirebaseDatabase.getInstance().getReference("Meals");

            final String mealCreatorID = mMainActivity.getCurrentUser().getUid();

            String mealID = mealDatabase.push().getKey();
            Meal meal = new Meal(mealName, mainProteinIngredient, restaurantName, location, description, mealCreatorID);
            mealDatabase.child(mealID).setValue(meal);

            clearInputFields();
            Toast.makeText(mMainActivity, R.string.meal_successfully_published, Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isInputValid(String mealName, String mainProteinIngredient, String restaurantName, String location) {
        boolean isValid = true;

        if( mealName.isEmpty() ){
            Toast.makeText( mMainActivity, R.string.empty_meal_name_message, Toast.LENGTH_SHORT ).show();
            isValid = false;
        }
        else if( mainProteinIngredient.isEmpty() ){
            Toast.makeText( mMainActivity, R.string.empty_main_protein_ingredient_message, Toast.LENGTH_SHORT ).show();
            isValid = false;
        }
        else if( restaurantName.isEmpty() ) {
            Toast.makeText(mMainActivity, R.string.empty_restaurant_name_message, Toast.LENGTH_SHORT).show();
            isValid = false;
        }
        else if( location.isEmpty() ) {
            Toast.makeText(mMainActivity, R.string.empty_location_message, Toast.LENGTH_SHORT).show();
            isValid = false;
        }

        return isValid;
    }

    void clearInputFields() {
        mMealNameInputField.getText().clear();
        mMainProteinIngredientInputField.getText().clear();
        mRestaurantNameInputField.getText().clear();
        mLocationInputField.getText().clear();
        mDescriptionInputField.getText().clear();
    }
}
