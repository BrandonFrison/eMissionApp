package ca.cmpt276.greengoblins.foodsurveydata;

public class Meal {

    private String mealName;
    private String mainProteinIngredient;
    private String restaurantName;
    private String location;
    private String description;
    private String mealCreatorID;

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
}
