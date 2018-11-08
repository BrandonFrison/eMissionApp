package ca.cmpt276.greengoblins.emission;

import android.content.Context;

import org.junit.Test;

import java.util.ArrayList;

import ca.cmpt276.greengoblins.foodsurveydata.ConsumptionTable;

import static org.junit.Assert.*;

public class ConsumptionTableTest {
    @Test
    public void creationTest(){
        ConsumptionTable defaultTable = new ConsumptionTable();
        defaultTable.addServing(10);
        defaultTable.addServing(20);
        defaultTable.addServing(30);
        defaultTable.addServing(40);
        defaultTable.addServing(50);
        defaultTable.addServing(60);
        defaultTable.addServing(70);

        ArrayList<Integer> servings = new ArrayList<Integer>();
        for( int i = 0; i < defaultTable.getSize(); i++ ){
            servings.add(defaultTable.getServingSize(i));
        }
        ConsumptionTable importedList = new ConsumptionTable(servings);
        for( int i = 0; i < importedList.getSize(); i++ ){
            assertEquals((i+1) * 10, importedList.getServingSize(i));
        }

        ArrayList<String> categoryNames = new ArrayList<String>();
        categoryNames.add("Soylent Green");
        categoryNames.add("Duff Beer");
        categoryNames.add("Cookie Cats");
        ArrayList<Float> categoryCO2eRates = new ArrayList<Float>();
        categoryCO2eRates.add(1.2f);
        categoryCO2eRates.add(8.6f);
        categoryCO2eRates.add(6.4f);
        ConsumptionTable newCategories = new ConsumptionTable(categoryNames, categoryCO2eRates);
        newCategories.addServing(10);
        assertEquals(10/1000f * 1.2, newCategories.calculateServingCO2e(0), 0.01);
    }
    @Test
    public void userAge(){
        ConsumptionTable testTable = new ConsumptionTable();
        assertEquals(0, testTable.getUserAge());
        testTable.setUserAge(20);
        assertEquals(20, testTable.getUserAge());
    }
    @Test
    public void userGender(){
        ConsumptionTable testTable = new ConsumptionTable();
        assertEquals(false, testTable.isMale());
        testTable.setMale(true);
        assertEquals(true, testTable.isMale());
    }
    @Test
    public void setServing(){
        ConsumptionTable testTable = new ConsumptionTable();
        testTable.addServing(10);
        testTable.addServing(10);
        testTable.addServing(10);
        testTable.setServing(1, 60);
        assertEquals(10, testTable.getServingSize(0));
        assertEquals(60, testTable.getServingSize(1));
        assertEquals(10, testTable.getServingSize(2));
        assertEquals(0, testTable.setServing(100, 10));
        assertEquals(0, testTable.setServing(-10, 10));
    }

    @Test
    public void getSize() {
        ConsumptionTable testTable = new ConsumptionTable();
        assertEquals(7, testTable.getSize());

        ArrayList<String> categoryNames = new ArrayList<String>();
        categoryNames.add("Soylent Green");
        categoryNames.add("Duff Beer");
        categoryNames.add("Cookie Cats");
        ArrayList<Float> categoryCO2eRates = new ArrayList<Float>();
        categoryCO2eRates.add(1.2f);
        categoryCO2eRates.add(8.6f);
        categoryCO2eRates.add(6.4f);
        ConsumptionTable newCategories = new ConsumptionTable(categoryNames, categoryCO2eRates);
        assertEquals(3, newCategories.getSize());
    }

    @Test
    public void addServing() {
        ConsumptionTable testTable = new ConsumptionTable();
        for (int i = 0; i < 100; i++){
            testTable.addServing((i+1)*10);
            assertEquals(Math.min((i+1) * 10, testTable.getSize() * 10), testTable.getServingSize(Math.min(i,testTable.getSize()-1)) );
        }

    }

    @Test
    public void removeServing() {
        ConsumptionTable testTable = new ConsumptionTable();
        testTable.addServing(10);
        testTable.addServing(10);
        assertEquals(1, testTable.removeServing());
        assertEquals(1, testTable.removeServing());
        assertEquals(0, testTable.removeServing());
    }

    @Test (expected = IndexOutOfBoundsException.class)
    public void getServingSize() {
        ConsumptionTable testTable = new ConsumptionTable();
        testTable.addServing(10);
        assertEquals(10, testTable.getServingSize(0));
        testTable.getServingSize(5);
    }

    @Test
    public void calculateServingCO2e() {
        ArrayList<String> categoryNames = new ArrayList<String>();
        categoryNames.add("Soylent Green");
        categoryNames.add("Duff Beer");
        categoryNames.add("Cookie Cats");
        ArrayList<Float> categoryCO2eRates = new ArrayList<Float>();
        categoryCO2eRates.add(1.2f);
        categoryCO2eRates.add(8.6f);
        categoryCO2eRates.add(6.4f);
        ConsumptionTable newCategories = new ConsumptionTable(categoryNames, categoryCO2eRates);
        newCategories.addServing(10);
        double expected = 10/1000f * 1.2;
        assertEquals(expected, newCategories.calculateServingCO2e(0), 0.01);
        newCategories.addServing(999999999);
        expected = 999999999/1000f * 8.6;
        assertEquals(expected, newCategories.calculateServingCO2e(1), 0.01);
        assertEquals(0, newCategories.calculateServingCO2e(5), 0.01);
    }

    @Test
    public void calculateTotalCO2e() {
        ArrayList<String> categoryNames = new ArrayList<String>();
        categoryNames.add("Soylent Green");
        categoryNames.add("Duff Beer");
        categoryNames.add("Cookie Cats");
        ArrayList<Float> categoryCO2eRates = new ArrayList<Float>();
        categoryCO2eRates.add(1.2f);
        categoryCO2eRates.add(8.6f);
        categoryCO2eRates.add(6.4f);
        ConsumptionTable newCategories = new ConsumptionTable(categoryNames, categoryCO2eRates);
        newCategories.addServing(10);
        newCategories.addServing(10);
        newCategories.addServing(10);
        double expected = (newCategories.getServingSize(0)/1000f * 1.2
                + newCategories.getServingSize(1)/1000f * 8.6
                + newCategories.getServingSize(2)/1000f * 6.4);
        assertEquals(expected, newCategories.calculateTotalCO2e(), 0.01);
    }

    @Test
    public void toStringTest() {
        ConsumptionTable defaultTable = new ConsumptionTable();
        String expected = "";
        assertEquals(expected, defaultTable.toString());
        defaultTable.addServing(10);
        defaultTable.addServing(20);
        expected = "Beef: 10\nPork: 20\n";
        assertEquals(expected, defaultTable.toString());

        ArrayList<String> categoryNames = new ArrayList<String>();
        categoryNames.add("Soylent Green");
        categoryNames.add("Duff Beer");
        categoryNames.add("Cookie Cats");
        ArrayList<Float> categoryCO2eRates = new ArrayList<Float>();
        categoryCO2eRates.add(1.2f);
        categoryCO2eRates.add(8.6f);
        categoryCO2eRates.add(6.4f);
        ConsumptionTable newCategories = new ConsumptionTable(categoryNames, categoryCO2eRates);
        expected = "";
        assertEquals(expected, newCategories.toString());
        newCategories.addServing(17);
        newCategories.addServing(18);
        newCategories.addServing(19);
        expected = "Soylent Green: 17\nDuff Beer: 18\nCookie Cats: 19\n";
    }

    @Test
    public void getIndexOf() {
        ConsumptionTable defaultTable = new ConsumptionTable();
        assertEquals(0, defaultTable.getIndexOf("Beef"));
        assertEquals(1, defaultTable.getIndexOf("Pork"));
        assertEquals(2, defaultTable.getIndexOf("Chicken"));
        assertEquals(3, defaultTable.getIndexOf("Fish"));
        assertEquals(4, defaultTable.getIndexOf("Eggs"));
        assertEquals(5, defaultTable.getIndexOf("Beans"));
        assertEquals(6, defaultTable.getIndexOf("Vegetables"));
        assertEquals(-1, defaultTable.getIndexOf("Stegasaurus"));
    }
}
