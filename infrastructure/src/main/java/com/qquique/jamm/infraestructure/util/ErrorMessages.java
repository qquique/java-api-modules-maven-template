package com.qquique.jamm.infrastructure.util;

import java.util.ResourceBundle;

public class ErrorMessages {
    private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle("com.qquique.jamm.infrastructure.errormessages");

    private ErrorMessages() {
    }

    public static String getErrorMessage(String messageKey, Object... args) {
        String message = RESOURCE_BUNDLE.getString(messageKey);
        return String.format(message, args);
    }

    public static String getErrorMessage(String messageKey) {
        RESOURCE_BUNDLE.getString(messageKey);
        return RESOURCE_BUNDLE.getString(messageKey);
    }
}
