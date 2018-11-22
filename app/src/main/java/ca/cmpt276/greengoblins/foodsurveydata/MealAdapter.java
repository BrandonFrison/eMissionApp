package ca.cmpt276.greengoblins.foodsurveydata;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import ca.cmpt276.greengoblins.emission.R;

public class MealAdapter extends RecyclerView.Adapter<MealAdapter.MealViewHolder> {

    private Context mContext;
    private List<Meal> mealList;

    public MealAdapter(Context mContext, List<Meal> mealList) {
        this.mContext = mContext;
        this.mealList = mealList;
    }

    @NonNull
    @Override
    public MealAdapter.MealViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.layout_meal, null);
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

}
