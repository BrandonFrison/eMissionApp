package ca.cmpt276.greengoblins.fragments.Meal;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;


import ca.cmpt276.greengoblins.emission.MainActivity;
import ca.cmpt276.greengoblins.emission.R;
import ca.cmpt276.greengoblins.foodsurveydata.Meal;
import ca.cmpt276.greengoblins.foodsurveydata.MealAdapter;


public class MealListFragment extends Fragment {


    private DatabaseReference mMealsDatabase;

    private MainActivity mMainActivity;
    private FloatingActionButton mActionButton;

    private CheckBox mViewMyMeals;

    private ArrayList<Meal> mDatabaseMealList;
    private ArrayList<Meal> mFilteredMealList;

    private Spinner mFilterDropdown;
    private EditText mSearchBox;
    private String[] mFilterOptions;

    private MealAdapter mMealAdapter;
    private RecyclerView mRecyclerView;

    private String userID;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_meal_list, container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mMainActivity = (MainActivity) getActivity();
        mMainActivity.setTitle(R.string.toolbar_pledge_list);

        mActionButton = mMainActivity.getActionButton();
        mActionButton.setImageResource(R.drawable.baseline_add_24);
        mActionButton.show();

        mViewMyMeals = (CheckBox) view.findViewById(R.id.ViewMyMeal);
        userID = mMainActivity.getCurrentUser().getUid();

        mDatabaseMealList = new ArrayList<Meal>();
        mFilteredMealList = new ArrayList<Meal>();

        mSearchBox = (EditText) view.findViewById(R.id.meal_search_box);
        mFilterDropdown = (Spinner) view.findViewById(R.id.meal_filter);
        mFilterOptions = getResources().getStringArray(R.array.meal_list_filters);

        ArrayAdapter<String> dropdownFilterAdapter = new ArrayAdapter<String>(mMainActivity.getBaseContext(),
                R.layout.support_simple_spinner_dropdown_item, mFilterOptions);
        dropdownFilterAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        mFilterDropdown.setAdapter(dropdownFilterAdapter);
        mFilterDropdown.setPrompt(getResources().getString(R.string.filter_prompt));

        mRecyclerView = (RecyclerView) view.findViewById(R.id.meal_list_view) ;
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mMainActivity.getBaseContext()));
        mMealAdapter = new MealAdapter(mMainActivity.getBaseContext(), mFilteredMealList);
        mRecyclerView.setAdapter(mMealAdapter);

        mMealsDatabase = FirebaseDatabase.getInstance().getReference("Meals");
        queryData(mMealsDatabase);

        mViewMyMeals.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                queryData(mMealsDatabase);
            }
        });
        mActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if( !mMainActivity.checkUserLogin() ) {
                    mMainActivity.popupLogin();
                } else {
                    Fragment newFragment = new MakeMealFragment();
                    mMainActivity.startFragment(newFragment, true, false);
                }
            }
        });

        mSearchBox.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if( keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP ){ //enter button handling (physical and soft keyboard)
                    filterList();
                }
                return false;
            }
        });
        mSearchBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filterList();
            }
        });

        mFilterDropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                filterList();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void queryData(Query query) {
//        if(mViewMyMeals.isChecked()){
//            query.orderByChild("mealCreatorID").equalTo(userID);
//        }
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mDatabaseMealList.clear();
                if(mViewMyMeals.isChecked()){
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Meal mealData = (Meal) snapshot.getValue(Meal.class);
                        if(mealData.getMealCreatorID().equalsIgnoreCase(userID)) {
                            mDatabaseMealList.add(mealData);
                        }
                    }
                }
                else{
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Meal mealData = (Meal) snapshot.getValue(Meal.class);
                        mDatabaseMealList.add(mealData);
                    }
                }
                clearFilters();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void clearFilters(){
        mFilteredMealList.clear();
        for ( Meal listedDatabaseMeal : mDatabaseMealList) {
            mFilteredMealList.add( listedDatabaseMeal );
        }
        mMealAdapter.notifyDataSetChanged();
    }

    //category int relates to order of spinner, aka meal_list_filters in values.xml
    private void searchCategory( int category, String searchTerm ) {
        mFilteredMealList.clear();
        for ( Meal listedDatabaseMeal : mDatabaseMealList) {
            switch(category){
                case 2: // Protein Type
                    if ( listedDatabaseMeal.getMainProteinIngredient().contains( searchTerm ) ){
                        mFilteredMealList.add( listedDatabaseMeal );
                    }
                    break;
                case 3: // Location
                    if ( listedDatabaseMeal.getLocation().contains( searchTerm ) ){
                        mFilteredMealList.add( listedDatabaseMeal );
                    }
                    break;
                default: //No Category or Meal Name
                    if ( listedDatabaseMeal.getMealName().contains( searchTerm ) ){
                        mFilteredMealList.add( listedDatabaseMeal );
                    }
            }
        }
        mMealAdapter.notifyDataSetChanged();
    }

    private void filterList(){
        String searchText = mSearchBox.getText().toString().trim().toLowerCase();
        String searchFilter = String.valueOf(mFilterDropdown.getSelectedItem());

        //if searchtext is empty, sort elements alphabetically by filter field
        if( searchText.isEmpty() ){
            clearFilters();
            if( searchFilter.equals(mFilterOptions[0]) ){ //No Filter
                Collections.sort(mFilteredMealList, Meal.COMPARE_BY_MEAL_NAME );
                mMealAdapter.notifyDataSetChanged();
            } else if ( searchFilter.equals(mFilterOptions[1]) ){ //Name
                Collections.sort(mFilteredMealList, Meal.COMPARE_BY_MEAL_NAME );
                mMealAdapter.notifyDataSetChanged();
            } else if ( searchFilter.equals(mFilterOptions[2]) ){ //Protein Type
                Collections.sort(mFilteredMealList, Meal.COMPARE_BY_PROTEIN_TYPE );
                mMealAdapter.notifyDataSetChanged();
            } else if ( searchFilter.equals(mFilterOptions[3]) ){ //Location
                Collections.sort(mFilteredMealList, Meal.COMPARE_BY_LOCATION );
                mMealAdapter.notifyDataSetChanged();
            }
        //if search text not empty, only show elements matching input text
        } else {
            if (searchFilter.equals(mFilterOptions[0])) { //No Filter
                searchCategory( 0, searchText );
            } else if (searchFilter.equals(mFilterOptions[1])) { //Name
                searchCategory( 1, searchText );
            } else if (searchFilter.equals(mFilterOptions[2])) { //Protein Type
                searchCategory( 2, searchText );
            } else if (searchFilter.equals(mFilterOptions[3])) { //Location
                searchCategory( 3, searchText );
            }
        }
    }
}