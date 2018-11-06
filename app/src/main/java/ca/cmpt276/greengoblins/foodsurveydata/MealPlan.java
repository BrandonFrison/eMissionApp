package ca.cmpt276.greengoblins.foodsurveydata;

public class MealPlan {
    final static int NUM_VEGETABLES = 2; // ***** This is based on the implementation of ConsumptionTable
    private ConsumptionTable newPlan;

    public MealPlan() {
        newPlan = new ConsumptionTable();
    }

    // Implementation assumes all meats come before vegetables
    public double reduceMeatByHalf(ConsumptionTable oldPlan) {

        newPlan = new ConsumptionTable();

        int i = 0;
        // Reduce meat consumption by half
        while( i < oldPlan.getSize() - NUM_VEGETABLES) {
            newPlan.addServing(oldPlan.getServingSize(i) / 2);
            i++;
        }
        // Keep the vegetable servings as they were
        while(i < oldPlan.getSize()) {
            newPlan.addServing(oldPlan.getServingSize(i));
            i++;
        }

        return newPlan.calculateTotalCO2e();
    }

    // Implementation assumes all meats come before vegetables
    public double removeMeatFromPlan(ConsumptionTable oldPlan) {

        newPlan = new ConsumptionTable();

        int i = 0;
        // Remove meat from new diet
        while( i < oldPlan.getSize() - NUM_VEGETABLES) {
            newPlan.addServing(0);
            i++;
        }
        // Keep the vegetable servings as they were
        while(i < oldPlan.getSize()) {
            newPlan.addServing(oldPlan.getServingSize(i));
            i++;
        }

        return newPlan.calculateTotalCO2e();
    }

    // Implementation assumes all meats come before vegetables
    public double switchToChickenOnly(ConsumptionTable oldPlan) {
        int chickenIndex = oldPlan.getIndexOf("Chicken"); // TO DO: ADD ERROR CHECKING
        newPlan = new ConsumptionTable();

        int i = 0;
        // Remove all meat from diet except chicken
        while( i < oldPlan.getSize() - NUM_VEGETABLES) {

            if(i == chickenIndex) {
                newPlan.addServing(oldPlan.getServingSize(chickenIndex));
            }
            else {
                newPlan.addServing(0);
            }
            i++;
        }
        // Keep the vegetable servings as they were
        while(i < oldPlan.getSize()) {
            newPlan.addServing(oldPlan.getServingSize(i));
            i++;
        }

        return newPlan.calculateTotalCO2e();
    }


    // Implementation assumes all meats come before vegetables
    public double switchToFishOnly(ConsumptionTable oldPlan) {
        int fishIndex = oldPlan.getIndexOf("Fish"); // TO DO: ADD ERROR CHECKING

        newPlan = new ConsumptionTable();

        int i = 0;
        // Remove all meat from diet except fish
        while( i < oldPlan.getSize() - NUM_VEGETABLES) {

            if(i == fishIndex) {
                newPlan.addServing(oldPlan.getServingSize(fishIndex));
            }
            else {
                newPlan.addServing(0);
            }
            i++;
        }
        // Keep the vegetable servings as they were
        while(i < oldPlan.getSize()) {
            newPlan.addServing(oldPlan.getServingSize(i));
            i++;
        }

        return newPlan.calculateTotalCO2e();
    }
}
