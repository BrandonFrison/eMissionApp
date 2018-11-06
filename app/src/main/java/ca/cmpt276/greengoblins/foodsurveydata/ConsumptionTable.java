package ca.cmpt276.greengoblins.foodsurveydata;

import android.content.Context;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Class which stores the survey data and performs CO2e calculations.
 */
public class ConsumptionTable implements Serializable {

    // These two lists will hopefully not be in the final version.
    // Just quick prototyping here.
    private ArrayList<String> foodType;
    private ArrayList<Float> foodCO2eConversionRate;

    private int maxSize;
    private ArrayList<Integer> foodServingSizeGrams;
    private int userAge;
    private boolean isMale;

    public ConsumptionTable(){
        // temporary data instantiation from magic numbers in constructor.
        // prototype only.
        foodType = new ArrayList<String>();
        foodType.add("Beef");
        foodType.add("Pork");
        foodType.add("Chicken");
        foodType.add("Fish");
        foodType.add("Eggs");
        foodType.add("Beans");
        foodType.add("Vegetables");
        foodCO2eConversionRate = new ArrayList<Float>();
        foodCO2eConversionRate.add(27f);
        foodCO2eConversionRate.add(12.1f);
        foodCO2eConversionRate.add(6.9f);
        foodCO2eConversionRate.add(6.1f);
        foodCO2eConversionRate.add(4.8f);
        foodCO2eConversionRate.add(2f);
        foodCO2eConversionRate.add(2f);

        maxSize = foodType.size();
        userAge = 0;
        isMale = false;

        foodServingSizeGrams = new ArrayList<Integer>();
    }
    public ConsumptionTable(ArrayList<String> categories, ArrayList<Float> conversionRates ){
        foodType = new ArrayList<String>();
        for ( String importedFoodType : categories ){
            foodType.add(importedFoodType);
        }
        foodCO2eConversionRate = new ArrayList<Float>();
        for ( float importedConversionRate : conversionRates ){
            foodCO2eConversionRate.add(importedConversionRate);
        }
        maxSize = foodType.size();
        userAge = 0;
        isMale = false;
        foodServingSizeGrams = new ArrayList<Integer>();
    }
    public ConsumptionTable(ArrayList<Integer> servingSizes ){
        this();
        int numberOfImportedServings = servingSizes.size();
        for(int i = 0; i < numberOfImportedServings && i < maxSize; i++) {
            foodServingSizeGrams.add(servingSizes.get(i));
        }
    }

    public int getUserAge() {
        return userAge;
    }
    public void setUserAge(int userAge) {
        this.userAge = userAge;
    }
    public boolean isMale() {
        return isMale;
    }
    public void setMale(boolean male) {
        isMale = male;
    }

    public int saveTable(Context context, String fileName) throws FileNotFoundException, IOException {
        FileOutputStream fos = context.openFileOutput(fileName, Context.MODE_PRIVATE);
        ObjectOutputStream os = new ObjectOutputStream(fos);
        os.writeObject(foodServingSizeGrams);
        os.close();
        fos.close();
        return 1;
    }
    public int loadTable(Context context, String fileName)throws FileNotFoundException, IOException, ClassNotFoundException {
        FileInputStream fis = context.openFileInput(fileName);
        ObjectInputStream is = new ObjectInputStream(fis);
        foodServingSizeGrams = (ArrayList<Integer>) is.readObject();
        is.close();
        fis.close();
        return 1;
    }

    public int getSize(){
        return maxSize;
    }

    /**
     * Adds a serving size to the end of the table
     * @param servingSizeGrams
     * @return 0 if serving was not added, 1 if successful
     */
    public int addServing( int servingSizeGrams ){
        if(foodServingSizeGrams.size() >= maxSize) return 0;
        foodServingSizeGrams.add(servingSizeGrams);
        return 1;
    }

    /**
     * Sets the serving size of the food at the specified index
     * @param servingSizeGrams
     * @return 0 if serving was not set, 1 if successful
     */
    public int setServing( int index, int servingSizeGrams ){
        if(index >= foodServingSizeGrams.size() || index < 0) return 0;
        foodServingSizeGrams.set(index, servingSizeGrams);
        return 1;
    }

    /**
     * Removes the last serving in the table
     * @return 0 if serving could not be removed, 1 if successful
     */
    public int removeServing(){
        if(foodServingSizeGrams.isEmpty()) return 0;
        int lastServing = foodServingSizeGrams.size() - 1;
        foodServingSizeGrams.remove(lastServing);
        return 1;
    }

    public int getServingSize( int index ){
        return foodServingSizeGrams.get(index);
    }

    public float calculateServingCO2e( int index ){
        if(index >= foodServingSizeGrams.size())
            return 0;
        float servingSizeKg = foodServingSizeGrams.get(index) / 1000f;
        return 365*servingSizeKg * foodCO2eConversionRate.get(index);
    }

    public float calculateTotalCO2e(){
        float result = 0;
        for(int currentServing = 0; currentServing < maxSize; currentServing++){
            result += calculateServingCO2e( currentServing );
        }
        return result;
    }

    public String toString(){
        String result = "";
        int currentSize = foodServingSizeGrams.size();
        for (int i = 0; i < currentSize; i++){
            result = result + foodType.get(i) + ": ";
            result = result + foodServingSizeGrams.get(i).toString() + "\n";
        }
        return result;
    }

    public int getIndexOf(String key) {
        return foodType.indexOf(key);
    }
}
