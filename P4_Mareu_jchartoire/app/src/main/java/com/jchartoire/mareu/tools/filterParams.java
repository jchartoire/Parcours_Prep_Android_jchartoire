package com.jchartoire.mareu.tools;

public class filterParams {
    private static int filterType = 0;
    private static String filterPattern = null;

    public static int getFilterType() {
        return filterType;
    }

    public static void setFilterType(int filterType) {
        filterParams.filterType = filterType;
    }

    public static String getFilterPattern() {
        return filterPattern;
    }

    public static void setFilterPattern(String filterPattern) {
        filterParams.filterPattern = filterPattern;
    }
}
