package ca.cmpt276.greengoblins.emission;

import org.junit.Test;

import ca.cmpt276.greengoblins.foodsurveydata.ConsumptionTable;
import ca.cmpt276.greengoblins.foodsurveydata.MealPlan;

import static org.junit.Assert.*;

public class MealPlanTest {
    private final int NUMBER_OF_VEGETABLES = 2;
    private final int DEFAULT_SERVING_SIZE = 1000;
    private final double EPSILON = 0.001;

    @Test
    public void reduceMeatByHalf() {
        ConsumptionTable oldMealPlanConsumption = new ConsumptionTable();
        initializeMealPlan(oldMealPlanConsumption);

        MealPlan newMealPlan = new MealPlan();
        double newPlanCO2eConsumption = newMealPlan.reduceMeatByHalf(oldMealPlanConsumption);

        ConsumptionTable expectedMealPlan = new ConsumptionTable();
        for(int i = 0; i < 5; i++)
        {
            expectedMealPlan.addServing(500);
        }
        for(int i = 5; i < 7; i++)
        {
            expectedMealPlan.addServing(2250);
        }
        double expectedNewCO2eConsumption = expectedMealPlan.calculateTotalCO2e();

        assertEquals(expectedNewCO2eConsumption, newPlanCO2eConsumption, EPSILON);
    }

    @Test
    public void removeMeatFromPlan() {
        ConsumptionTable oldMealPlanConsumption = new ConsumptionTable();
        initializeMealPlan(oldMealPlanConsumption); // Set each food's serving to default serving size

        MealPlan newMealPlan = new MealPlan();
        double newPlanCO2eConsumption = newMealPlan.removeMeatFromPlan(oldMealPlanConsumption);

        ConsumptionTable expectedMealPlan = new ConsumptionTable();
        for(int i = 0; i < 5; i++)
        {
            expectedMealPlan.addServing(0);
        }
        for(int i = 5; i < 7; i++)
        {
            expectedMealPlan.addServing(3500);
        }
        double expectedNewCO2eConsumption = expectedMealPlan.calculateTotalCO2e();

        assertEquals(expectedNewCO2eConsumption, newPlanCO2eConsumption, EPSILON);
    }

    @Test
    public void switchToChickenOnly() {
        ConsumptionTable oldMealPlanConsumption = new ConsumptionTable();
        initializeMealPlan(oldMealPlanConsumption);

        int chickenIndex = oldMealPlanConsumption.getIndexOf("Chicken");
        ConsumptionTable expectedMealPlan = new ConsumptionTable();
        for(int i = 0; i < 5; i++)
        {
            if(i == chickenIndex) {
                expectedMealPlan.addServing(5000);
            } else {
                expectedMealPlan.addServing(0);
            }
        }
        for(int i = 5; i < 7; i++)
        {
            expectedMealPlan.addServing(1000);
        }
        double expectedNewCO2eConsumption = expectedMealPlan.calculateTotalCO2e();

        MealPlan newMealPlan = new MealPlan();
        double newPlanCO2eConsumption = newMealPlan.switchToChickenOnly(oldMealPlanConsumption);

        assertEquals(expectedNewCO2eConsumption, newPlanCO2eConsumption, EPSILON);
    }

    @Test
    public void switchToFishOnly() {
        ConsumptionTable oldMealPlanConsumption = new ConsumptionTable();
        initializeMealPlan(oldMealPlanConsumption);

        int fishIndex = oldMealPlanConsumption.getIndexOf("Fish");
        ConsumptionTable expectedMealPlan = new ConsumptionTable();
        for(int i = 0; i < 5; i++)
        {
            if(i == fishIndex) {
                expectedMealPlan.addServing(5000);
            } else {
                expectedMealPlan.addServing(0);
            }
        }
        for(int i = 5; i < 7; i++)
        {
            expectedMealPlan.addServing(1000);
        }
        double expectedNewCO2eConsumption = expectedMealPlan.calculateTotalCO2e();


        MealPlan newMealPlan = new MealPlan();
        double newPlanCO2eConsumption = newMealPlan.switchToFishOnly(oldMealPlanConsumption);

        assertEquals(expectedNewCO2eConsumption, newPlanCO2eConsumption, EPSILON);
    }

    private void initializeMealPlan(ConsumptionTable mealPlan) {
        for(int i = 0; i < mealPlan.getSize(); i++) {
            mealPlan.addServing(DEFAULT_SERVING_SIZE);
        }
    }

    private double calculateCO2eFromMeat(ConsumptionTable mealPlan) {
        double co2eFromMeat = 0;
        for(int i = 0; i < mealPlan.getSize() - NUMBER_OF_VEGETABLES; i++) {
            co2eFromMeat += mealPlan.calculateServingCO2e(i);
        }

        return co2eFromMeat;
    }
}
