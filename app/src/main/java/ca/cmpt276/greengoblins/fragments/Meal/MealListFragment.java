package ca.cmpt276.greengoblins.fragments.Meal;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import ca.cmpt276.greengoblins.emission.MainActivity;
import ca.cmpt276.greengoblins.emission.R;
import ca.cmpt276.greengoblins.fragments.MakePledgeFragment;

import static java.lang.Integer.parseInt;

public class MealListFragment extends Fragment {

    private MainActivity mMainActivity;
    private Button mAddMeal;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_meal_list, container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mMainActivity = (MainActivity) getActivity();
        mAddMeal = (Button) view.findViewById(R.id.meal_list_add_meal);

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
}