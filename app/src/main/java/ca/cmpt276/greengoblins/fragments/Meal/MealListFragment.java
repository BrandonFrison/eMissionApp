package ca.cmpt276.greengoblins.fragments.Meal;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import ca.cmpt276.greengoblins.emission.MainActivity;
import ca.cmpt276.greengoblins.emission.R;
import ca.cmpt276.greengoblins.foodsurveydata.Meal;
import ca.cmpt276.greengoblins.foodsurveydata.MealAdapter;


public class MealListFragment extends Fragment {


    private DatabaseReference mMealsDatabase;

    private MainActivity mMainActivity;
    private Button mAddMeal;

    private List mMealList;

    private MealAdapter mMealAdapter;
    private RecyclerView mRecyclerView;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_meal_list, container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mMainActivity = (MainActivity) getActivity();
        mAddMeal = (Button) view.findViewById(R.id.meal_list_add_meal);
        mMealList = new ArrayList<Meal>();


        mRecyclerView = (RecyclerView) view.findViewById(R.id.meal_list_view) ;
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mMainActivity.getBaseContext()));
        mMealAdapter = new MealAdapter(mMainActivity.getBaseContext(), mMealList);
        mRecyclerView.setAdapter(mMealAdapter);

        displayPledges();

        mAddMeal.setOnClickListener(new View.OnClickListener() {
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
    }

    private void displayPledges() {
        mMealsDatabase = FirebaseDatabase.getInstance().getReference("Meals");
        queryData(mMealsDatabase);
/*
        mSearchBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filterList();
            }
        });
        mSearchBox.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                filterList();
                return false;
            }
        });*/
/*
        mFilterDropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                filterList();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });*/
    }

    private void queryData(Query query) {
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mMealList.clear();

                for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Meal mealData = (Meal) snapshot.getValue(Meal.class);
                    mMealList.add(mealData);
                }
                mMealAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}