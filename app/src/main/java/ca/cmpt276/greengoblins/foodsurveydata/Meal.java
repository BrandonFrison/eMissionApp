package ca.cmpt276.greengoblins.foodsurveydata;

import java.util.Comparator;

public class Meal {

    private String mealName;
    private String mainProteinIngredient;
    private String restaurantName;
    private String location;
    private String description;
    private String mealCreatorID;
    private String mealID;

    // Need this default constructor in order for datasnapshot to work
    public Meal() {
    }

    public Meal(String mealName, String mainProteinIngredient, String restaurantName, String location, String description, String mealCreatorID, String mealID) {
        this.mealName = mealName;
        this.mainProteinIngredient = mainProteinIngredient;
        this.restaurantName = restaurantName;
        this.location = location;
        this.description = description;
        this.mealCreatorID = mealCreatorID;
        this.mealID = mealID;
    }

    public String getMealName() {
        return mealName;
    }

    public void setMealName(String mealName) {
        this.mealName = mealName;
    }

    public String getMainProteinIngredient() {
        return mainProteinIngredient;
    }

    public void setMainProteinIngredient(String mainProteinIngredient) {
        this.mainProteinIngredient = mainProteinIngredient;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMealCreatorID() {
        return mealCreatorID;
    }

    public void setMealCreatorID(String mealCreatorID) {
        this.mealCreatorID = mealCreatorID;
    }

    public static Comparator<Meal> COMPARE_BY_MEAL_NAME = new Comparator<Meal>() {
        @Override
        public int compare(Meal a, Meal b) {
            return a.getMealName().compareToIgnoreCase( b.getMealName() );
        }
    };

    public static Comparator<Meal> COMPARE_BY_PROTEIN_TYPE = new Comparator<Meal>() {
        @Override
        public int compare(Meal a, Meal b) {
            return a.getMainProteinIngredient().compareToIgnoreCase( b.getMainProteinIngredient() );
        }
    };

    public static Comparator<Meal> COMPARE_BY_LOCATION = new Comparator<Meal>() {
        @Override
        public int compare(Meal a, Meal b) {
            return a.getLocation().compareToIgnoreCase( b.getLocation() );
        }
    };

    public String getMealID() {
        return mealID;
    }

    public void setMealID(String mealID) {
        this.mealID = mealID;
    }
}
