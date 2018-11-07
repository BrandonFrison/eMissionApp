package ca.cmpt276.greengoblins.foodsurveydata;

public class Convertor {
    // Source: https://www.epa.gov/energy/greenhouse-gas-equivalencies-calculator
    private static double ACRES_FOREST_PER_TONNE_CO2E = 1.2;
    private static int GALLONS_GASOLINE_CONSUMED_PER_TONNE_CO2E = 113;
    private static int KM_DRIVEN_PER_TONNES_CO2E = 3922;

    public static int toForests(double tonnesCO2e) {
        return (int) (tonnesCO2e * ACRES_FOREST_PER_TONNE_CO2E);
    }

    public static int toGallonsGasoline(double tonnesCO2e) {
        return (int) (tonnesCO2e * GALLONS_GASOLINE_CONSUMED_PER_TONNE_CO2E);
    }

    public static int toKmDriven(double tonnesCO2e) {
        return (int) (tonnesCO2e * KM_DRIVEN_PER_TONNES_CO2E);
    }
}
