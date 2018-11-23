package ca.cmpt276.greengoblins.foodsurveydata;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import ca.cmpt276.greengoblins.emission.MainActivity;
import ca.cmpt276.greengoblins.emission.PopupMealDetail;

import java.io.Serializable;
import java.util.List;

import ca.cmpt276.greengoblins.emission.R;

public class MealAdapter extends RecyclerView.Adapter<MealAdapter.MealViewHolder> implements Serializable {

    private final View.OnClickListener mOnClickListener = new mealOnClickListener();

    private Context mContext;
    private List<Meal> mealList;
    private RecyclerView mRecyclerView;
    private MainActivity mMainActivity;

    public MealAdapter(Context mContext, List<Meal> mealList, RecyclerView recyclerView, MainActivity mainActivity) {
        this.mContext = mContext;
        this.mealList = mealList;
        this.mRecyclerView = recyclerView;
        this.mMainActivity = mainActivity;
    }

    @NonNull
    @Override
    public MealAdapter.MealViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.layout_meal, null);
        view.setOnClickListener(mOnClickListener);
        return new MealAdapter.MealViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MealAdapter.MealViewHolder mealViewHolder, int i) {
        Meal meal = mealList.get(i);

        // CODE TO GET MEAL PIC GOES HERE **************************

        mealViewHolder.mealName.setText(meal.getMealName());
        mealViewHolder.location.setText(meal.getLocation());
        mealViewHolder.restaurantName.setText(meal.getRestaurantName());

    }

    @Override
    public int getItemCount() {
        return mealList.size();
    }

    public void setMealList( List<Meal> newList ){
        mealList = newList;
    }

    // Contains UI elements
    class MealViewHolder extends RecyclerView.ViewHolder {

        ImageView mealPic;
        TextView mealName;
        TextView location;
        TextView restaurantName;

        public MealViewHolder(@NonNull View itemView) {
            super(itemView);

            mealPic = itemView.findViewById(R.id.fragment_meal_list_pic);
            mealName = itemView.findViewById(R.id.fragment_meal_list_name);
            location = itemView.findViewById(R.id.fragment_meal_list_location);
            restaurantName = itemView.findViewById(R.id.fragment_meal_list_restaurant_name);
        }
    }

    private class mealOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(final View view) {
            int itemPosition = mRecyclerView.getChildAdapterPosition(view);
            Meal meal = mealList.get(itemPosition);
            //String mealID = meal.getMealID();

            Intent popupMealDetailIntent = new Intent(mMainActivity, PopupMealDetail.class);

            Bundle bundle = new Bundle();
            if(mMainActivity.checkUserLogin()) {
                bundle.putString("userID", mMainActivity.getCurrentUser().getUid());
            }
            bundle.putSerializable("meal", meal);
            popupMealDetailIntent.putExtras(bundle);

            mMainActivity.startActivity(popupMealDetailIntent);
        }
    }
}
