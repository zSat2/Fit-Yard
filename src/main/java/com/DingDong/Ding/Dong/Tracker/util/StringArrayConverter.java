package com.DingDong.Ding.Dong.Tracker.util;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Converter
public class StringArrayConverter implements AttributeConverter<List<String>, String> {

    @Override
    public String convertToDatabaseColumn(List<String> attribute) {
        if (attribute == null || attribute.isEmpty()) {
            return null;
        }
        StringBuilder sb = new StringBuilder("{");
        for (int i = 0; i < attribute.size(); i++) {
            if (i > 0) sb.append(",");
            String val = attribute.get(i);
            // Escape quotes and wrap in quotes if contains comma or special chars
            if (val != null && (val.contains(",") || val.contains("\"") || val.contains("{") || val.contains("}"))) {
                sb.append("\"").append(val.replace("\\", "\\\\").replace("\"", "\\\"")).append("\"");
            } else {
                sb.append(val == null ? "NULL" : val);
            }
        }
        sb.append("}");
        return sb.toString();
    }

    @Override
    public List<String> convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isBlank() || dbData.equals("{}")) {
            return Collections.emptyList();
        }
        // Strip the outer braces
        String inner = dbData.trim();
        if (inner.startsWith("{")) inner = inner.substring(1);
        if (inner.endsWith("}")) inner = inner.substring(0, inner.length() - 1);
        if (inner.isBlank()) return Collections.emptyList();

        // Simple CSV split respecting quoted strings
        java.util.List<String> result = new java.util.ArrayList<>();
        boolean inQuote = false;
        StringBuilder current = new StringBuilder();
        for (int i = 0; i < inner.length(); i++) {
            char c = inner.charAt(i);
            if (c == '"' && (i == 0 || inner.charAt(i - 1) != '\\')) {
                inQuote = !inQuote;
            } else if (c == ',' && !inQuote) {
                result.add(unescapePostgresString(current.toString()));
                current = new StringBuilder();
            } else {
                current.append(c);
            }
        }
        result.add(unescapePostgresString(current.toString()));
        return result;
    }

    private String unescapePostgresString(String s) {
        if (s == null) return null;
        s = s.trim();
        if (s.startsWith("\"") && s.endsWith("\"")) {
            s = s.substring(1, s.length() - 1);
            s = s.replace("\\\"", "\"").replace("\\\\", "\\");
        }
        if (s.equals("NULL")) return null;
        return s;
    }
}
