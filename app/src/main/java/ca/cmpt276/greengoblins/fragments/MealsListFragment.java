package ca.cmpt276.greengoblins.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.List;

import ca.cmpt276.greengoblins.emission.AddMeals;
import ca.cmpt276.greengoblins.emission.MainActivity;
import ca.cmpt276.greengoblins.emission.R;
import ca.cmpt276.greengoblins.foodsurveydata.User;
import ca.cmpt276.greengoblins.foodsurveydata.UserAdapter;


public class MealsListFragment  extends Fragment {
    private MainActivity mMainActivity;
    private TextView mMealListView;
    private Spinner mFilterMeal;
    private Button mAddMyMeal;
    private EditText mMealSearchBox;
    private String mMealFilterOptions[];

    private RecyclerView mRecyclerView;
    private UserAdapter mAdapter;
    private List<User> mUserMealList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_mealslist, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mMainActivity = (MainActivity) getActivity();
        mAddMyMeal = (Button)view.findViewById(R.id.button_add_meals);
        mFilterMeal = (Spinner)view.findViewById(R.id.meal_filter);
        mMealSearchBox = (EditText) view.findViewById(R.id.meal_search_box);
        mMealListView = (TextView) view.findViewById(R.id.Meals_list_text);
        mMealListView.setText("         ");

        //mUserMealList = new ArrayList<User>();

        mRecyclerView = (RecyclerView) view.findViewById(R.id.Meals_list_view) ;
        //mRecyclerView.setHasFixedSize(true);
        //mRecyclerView.setLayoutManager(new LinearLayoutManager(mMainActivity.getBaseContext()));
        //mAdapter = new UserAdapter(mMainActivity.getBaseContext(), mUserMealList);
        //mRecyclerView.setAdapter(mAdapter);

        mMealFilterOptions = getResources().getStringArray(R.array.meal_list_filters);

        ArrayAdapter<String> MealFilterAdapter = new ArrayAdapter<String>(mMainActivity.getBaseContext(),
                R.layout.support_simple_spinner_dropdown_item, mMealFilterOptions);
        MealFilterAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        mFilterMeal.setAdapter(MealFilterAdapter);
        mFilterMeal.setPrompt(getResources().getString(R.string.filter_prompt));

        displayMeals();

        mAddMyMeal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if( !mMainActivity.checkUserLogin() ) {
                    mMainActivity.popupLogin();
                } else {
                    Intent AddMealPage= new Intent(getContext(), AddMeals.class);
                    startActivity(AddMealPage);
                }
            }
        });
    }

    private void displayMeals() {
        //mUsersDatabase = FirebaseDatabase.getInstance().getReference("Users");
        //queryData(mUsersDatabase);

        mMealSearchBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filterList();
            }
        });
        mMealSearchBox.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                filterList();
                return false;
            }
        });

        mFilterMeal.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                filterList();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void filterList() {
            String searchText = mMealSearchBox.getText().toString().trim().toLowerCase();
            String searchFilter = String.valueOf(mFilterMeal.getSelectedItem());

            if (searchText.isEmpty()) return;

            if (searchFilter.equals(mMealFilterOptions[0])) {
                //queryData(mUsersDatabase);
            } else if (searchFilter.equals(mMealFilterOptions[1])) {

            } else if (searchFilter.equals(mMealFilterOptions[2])) {

            } else if (searchFilter.equals(mMealFilterOptions[3])) {

            } else if (searchFilter.equals(mMealFilterOptions[4])) {

            }
        }

}
