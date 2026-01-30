package com.javablog.domain;

public final class Guard {

    private Guard() {
    }

    public static void againstNull(Object value, String fieldName) {
        if (value == null) {
            throw new ConstraintViolationException(fieldName + " must not be null");
        }
    }

    public static void againstEmpty(String value, String fieldName) {
        againstNull(value, fieldName);
        if (value.isEmpty()) {
            throw new ConstraintViolationException(fieldName + " must not be empty");
        }
    }

    public static void againstBlank(String value, String fieldName) {
        againstNull(value, fieldName);
        if (value.isBlank()) {
            throw new ConstraintViolationException(fieldName + " must not be blank");
        }
    }

    public static void againstInvalidMaxLength(String value, String fieldName, int maxLength) {
        againstNull(value, fieldName);
        if (value.length() > maxLength) {
            throw new ConstraintViolationException(fieldName + " must not exceed " + maxLength + " characters");
        }
    }
}
