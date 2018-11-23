package ca.cmpt276.greengoblins.fragments;

import android.content.Intent;
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
import android.widget.GridView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;


import ca.cmpt276.greengoblins.emission.AddMealActivity;
import ca.cmpt276.greengoblins.emission.MainActivity;
import ca.cmpt276.greengoblins.emission.PopupMealDetail;
import ca.cmpt276.greengoblins.emission.R;
import ca.cmpt276.greengoblins.foodsurveydata.Meal;
import ca.cmpt276.greengoblins.foodsurveydata.MealAdapter;
import ca.cmpt276.greengoblins.foodsurveydata.MealImageAdapter;


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
    private MealImageAdapter mMealImageAdapter;
    private RecyclerView mRecyclerView;
    private GridView mGridView;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_meal_list, container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mMainActivity = (MainActivity) getActivity();
        mMainActivity.setTitle(R.string.toolbar_meal_list);

        mActionButton = mMainActivity.getActionButton();
        mActionButton.setImageResource(R.drawable.baseline_add_24);
        mActionButton.show();
        mMainActivity.showActionButtonLabel(R.string.fablabel_add_meal);

        mViewMyMeals = (CheckBox) view.findViewById(R.id.ViewMyMeal);

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
        mMealAdapter = new MealAdapter(mMainActivity.getBaseContext(), mFilteredMealList, mRecyclerView, mMainActivity);
        mRecyclerView.setAdapter(mMealAdapter);

        mGridView = (GridView) view.findViewById(R.id.meal_gridview);
        mMealImageAdapter = new MealImageAdapter(mMainActivity.getBaseContext(), mFilteredMealList);
        mGridView.setAdapter(mMealImageAdapter);
        mGridView.setVisibility(View.GONE);

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
                    Intent AddMealPage= new Intent(getContext(), AddMealActivity.class);

                    Bundle bundle = new Bundle();
                    bundle.putString("userID", mMainActivity.getCurrentUser().getUid());
                    AddMealPage.putExtras(bundle);

                    startActivity(AddMealPage);
                }
            }
        });

        mSearchBox.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if( keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP ){ //enter button handling (physical and soft keyboard)
                    String searchText = mSearchBox.getText().toString().trim().toLowerCase();
                    filterList();
                    searchCategory(4, searchText);
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
                if(i == 4){
                    mRecyclerView.setVisibility(View.GONE);
                    mGridView.setVisibility(View.VISIBLE);
                } else {
                    mGridView.setVisibility(View.GONE);
                    mRecyclerView.setVisibility(View.VISIBLE);
                    filterList();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

                Meal meal = mFilteredMealList.get(position);

                Intent popupMealDetailIntent = new Intent(mMainActivity, PopupMealDetail.class);

                Bundle bundle = new Bundle();
                if(mMainActivity.checkUserLogin()) {
                    bundle.putString("userID", mMainActivity.getCurrentUser().getUid());
                }
                bundle.putSerializable("meal", meal);
                popupMealDetailIntent.putExtras(bundle);

                mMainActivity.startActivity(popupMealDetailIntent);
            }
        });


    }

    private void queryData(Query query) {
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mDatabaseMealList.clear();
                if(mViewMyMeals.isChecked()){
                    if(mMainActivity.checkUserLogin()) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Meal mealData = (Meal) snapshot.getValue(Meal.class);
                            if (mealData.getMealCreatorID().equalsIgnoreCase(mMainActivity.getCurrentUser().getUid())) {
                                mDatabaseMealList.add(mealData);
                            }
                        }
                    }
                }else{
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

    private void updateAdapters(){
        mMealAdapter.notifyDataSetChanged();
        mMealImageAdapter.notifyDataSetChanged();
    }

    private void clearFilters(){
        mFilteredMealList.clear();
        for ( Meal listedDatabaseMeal : mDatabaseMealList) {
            mFilteredMealList.add( listedDatabaseMeal );
        }
        updateAdapters();
    }

    //category int relates to order of spinner, aka meal_list_filters in values.xml
    private void searchCategory( int category, String searchTerm ) {
        mFilteredMealList.clear();
        for ( Meal listedDatabaseMeal : mDatabaseMealList) {
            switch(category){
                case 1:
                    if ( listedDatabaseMeal.getMealName().toLowerCase().contains( searchTerm ) ){
                        mFilteredMealList.add( listedDatabaseMeal );
                    }
                    break;
                case 2: // Protein Type
                    if ( listedDatabaseMeal.getMainProteinIngredient().toLowerCase().contains( searchTerm ) ){
                        mFilteredMealList.add( listedDatabaseMeal );
                    }
                    break;
                case 3: // Location
                    if ( listedDatabaseMeal.getLocation().toLowerCase().contains( searchTerm ) ){
                        mFilteredMealList.add( listedDatabaseMeal );
                    }
                    break;
                default: //No Category
                    if ( listedDatabaseMeal.getMealName().toLowerCase().contains( searchTerm ) ||
                            listedDatabaseMeal.getLocation().toLowerCase().contains( searchTerm ) ||
                            listedDatabaseMeal.getRestaurantName().toLowerCase().contains( searchTerm ) ||
                            listedDatabaseMeal.getMainProteinIngredient().toLowerCase().contains( searchTerm ) ){
                        mFilteredMealList.add( listedDatabaseMeal );
                    }
            }
        }
        updateAdapters();
    }

    private void filterList() {
        String searchText = mSearchBox.getText().toString().trim().toLowerCase();
        String searchFilter = String.valueOf(mFilterDropdown.getSelectedItem());

        //if searchtext is empty, sort elements alphabetically by filter field
        if (searchText.isEmpty()) {
            clearFilters();
            if (searchFilter.equals(mFilterOptions[0])) { //No Filter
                Collections.sort(mFilteredMealList, Meal.COMPARE_BY_MEAL_NAME);
            } else if (searchFilter.equals(mFilterOptions[1])) { //Name
                Collections.sort(mFilteredMealList, Meal.COMPARE_BY_MEAL_NAME);
            } else if (searchFilter.equals(mFilterOptions[2])) { //Protein Type
                Collections.sort(mFilteredMealList, Meal.COMPARE_BY_PROTEIN_TYPE);
            } else if (searchFilter.equals(mFilterOptions[3])) { //Location
                Collections.sort(mFilteredMealList, Meal.COMPARE_BY_LOCATION);
            } else if (searchFilter.equals(mFilterOptions[4])) { //Name
                Collections.sort(mFilteredMealList, Meal.COMPARE_BY_MEAL_NAME);
            }
            updateAdapters();
        //if search text not empty, only show elements matching input text
        } else {
            if (searchFilter.equals(mFilterOptions[0])) { //No Filter
                searchCategory(0, searchText);
            } else if (searchFilter.equals(mFilterOptions[1])) { //Name
                searchCategory(1, searchText);
            } else if (searchFilter.equals(mFilterOptions[2])) { //Protein Type
                searchCategory(2, searchText);
            } else if (searchFilter.equals(mFilterOptions[3])) { //Location
                searchCategory(3, searchText);
            }
        }
    }


}