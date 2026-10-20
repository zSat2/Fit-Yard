package com.DingDong.Ding.Dong.Tracker.util;

public class ValidationUtil {

    private ValidationUtil() {}

    public static String toSlug(String name) {
        if (name == null) return "";
        return name.toLowerCase()
                .replaceAll("[^a-z0-9]+", "-")
                .replaceAll("^-|-$", "");
    }
}
