package ca.cmpt276.greengoblins.fragments.Meal;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;

import ca.cmpt276.greengoblins.emission.MainActivity;
import ca.cmpt276.greengoblins.emission.MapActivity;
import ca.cmpt276.greengoblins.emission.R;
import ca.cmpt276.greengoblins.foodsurveydata.Meal;

public class MakeMealFragment extends Fragment {

    private MainActivity mMainActivity;
    private MapActivity mMapActivity;

    private EditText mMealNameInputField;
    private EditText mMainProteinIngredientInputField;
    private EditText mRestaurantNameInputField;
    private EditText mLocationInputField;
    private EditText mDescriptionInputField;
    private Meal meal;
    private String[] locationInfo;
    private Bundle locationBundle;

    private FloatingActionButton mActionButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_make_meal, container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        mMainActivity = (MainActivity)getActivity();

        mActionButton = mMainActivity.getActionButton();
        mActionButton.setImageResource(R.drawable.ic_menu_send);
        mActionButton.show();

        mMealNameInputField = (EditText) view.findViewById(R.id.fragment_make_meal_input_meal_name);
        mMainProteinIngredientInputField = (EditText) view.findViewById(R.id.fragment_make_meal_input_main_protein_ingredient);
        mRestaurantNameInputField = (EditText) view.findViewById(R.id.fragment_make_meal_input_restaurant_name);
        mLocationInputField = (EditText) view.findViewById(R.id.fragment_make_meal_input_location);
        mDescriptionInputField = (EditText) view.findViewById(R.id.fragment_make_meal_input_description);


        if(locationInfo != null) {

        }
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
            if(locationInfo != null) {
                meal = new Meal(mealName, mainProteinIngredient, restaurantName, location, description, mealCreatorID, Double.parseDouble(locationInfo[1]), Double.parseDouble(locationInfo[2]));
            }else{
                meal = new Meal(mealName, mainProteinIngredient, restaurantName, location, description, mealCreatorID);
            }
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

    private void fillLocationFromMap(){
        String fileName = "locationdata.txt";
        String line = "";
        Toast.makeText(mMainActivity, fileName, Toast.LENGTH_SHORT).show();
        try {
            FileReader fileReader =
                    new FileReader(fileName);

            // Always wrap FileReader in BufferedReader.
            BufferedReader bufferedReader =
                    new BufferedReader(fileReader);

            line = bufferedReader.readLine();
            locationInfo = line.split(",");
            mLocationInputField.setText(locationInfo[0]);
            // Always close files.
            bufferedReader.close();
        }
        catch(FileNotFoundException ex) {
            System.out.println(
                    "Unable to open file " +
                            fileName);
        }
        catch(IOException ex) {
             ex.printStackTrace();
        }
        /*
        if(locationBundle != null) {
            locationInfo = locationBundle.getStringArray("location_data");
            mLocationInputField.setText(locationInfo[0]);
        }*/
    }
}
