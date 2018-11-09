package ca.cmpt276.greengoblins.emission;

import org.junit.Test;

import ca.cmpt276.greengoblins.foodsurveydata.ConsumptionTable;

import static org.junit.Assert.*;

public class ConvertorTest {

    private static int DEFAULT_SERVING = 30;
    private static double ACRES_FOREST_PER_TONNE_CO2E = 1.2;
    private static int GALLONS_GASOLINE_CONSUMED_PER_TONNE_CO2E = 113;
    private static int KM_DRIVEN_PER_TONNES_CO2E = 3922;
    private final double EPSILON = 0.01;

    @Test
    public void toForests() {
        ConsumptionTable newTable = new ConsumptionTable();
        for(int i = 0; i < newTable.getSize(); i++) {
            newTable.addServing(DEFAULT_SERVING);
        }
        float testCo2e = newTable.calculateTotalCO2e();
        double testTreeAmt = ACRES_FOREST_PER_TONNE_CO2E * testCo2e;
        assertEquals(800.232, testTreeAmt, EPSILON);
    }

    @Test
    public void toGallonsGasoline() {
        ConsumptionTable newTable = new ConsumptionTable();
        for(int i = 0; i < newTable.getSize(); i++) {
            newTable.addServing(DEFAULT_SERVING);
        }
        float testCo2e = newTable.calculateTotalCO2e();
        double testGasAmt = GALLONS_GASOLINE_CONSUMED_PER_TONNE_CO2E * testCo2e;
        assertEquals(75355.18, testGasAmt, EPSILON);
    }

    @Test
    public void toKmDriven() {
        ConsumptionTable newTable = new ConsumptionTable();
        for(int i = 0; i < newTable.getSize(); i++) {
            newTable.addServing(DEFAULT_SERVING);
        }
        float testCo2e = newTable.calculateTotalCO2e();
        double testKMAmt = KM_DRIVEN_PER_TONNES_CO2E * testCo2e;
        assertEquals(2615424.92, testKMAmt, EPSILON);
    }
}