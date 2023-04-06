package Enums;

/**
 *
 */
public enum MapType {
    GreenCityMap("GreenCity"),
    IndustrialCity("IndustrialCityMap");
    private final String stringValue;
    MapType(String s) {
        stringValue = s;
    }
    public String getStringValue() {
        return stringValue;
    }
}
