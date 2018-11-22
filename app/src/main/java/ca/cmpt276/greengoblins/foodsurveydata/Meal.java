package ca.cmpt276.greengoblins.foodsurveydata;

import java.util.Comparator;

public class Meal {

    private String mealName;
    private String mainProteinIngredient;
    private String restaurantName;
    private String location;
    private String description;
    private String mealCreatorID;
    private Double latitude;
    private Double longitude;

    // Need this default constructor in order for datasnapshot to work
    public Meal() {
    }

    public Meal(String mealName, String mainProteinIngredient, String restaurantName, String location, String description, String mealCreatorID) {
        this.mealName = mealName;
        this.mainProteinIngredient = mainProteinIngredient;
        this.restaurantName = restaurantName;
        this.location = location;
        this.description = description;
        this.mealCreatorID = mealCreatorID;
    }
//constructor for having latitude and longitude
    public Meal(String mealName, String mainProteinIngredient, String restaurantName, String location, String description, String mealCreatorID, Double latitude, Double longitude) {
        this.mealName = mealName;
        this.mainProteinIngredient = mainProteinIngredient;
        this.restaurantName = restaurantName;
        this.location = location;
        this.description = description;
        this.mealCreatorID = mealCreatorID;
        this.latitude = latitude;
        this.longitude = longitude;
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

    public Double getLatitude(){
        return latitude;
    }

    public void setLatitude(double latitude){
        this.latitude = latitude;
    }

    public Double getLongitude(){
        return longitude;
    }

    public void setLongitude(double longitude){
        this.longitude = longitude;
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
}
