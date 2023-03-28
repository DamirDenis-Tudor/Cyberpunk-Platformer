package Enums;

public enum MapNames {
    GreenCityMap("GreenCity"),
    IndustrialCity("IndustrialCityMap");
    private final String stringValue;
    MapNames(String s) {
        stringValue = s;
    }
    public String getStringValue() {
        return stringValue;
    }
}
