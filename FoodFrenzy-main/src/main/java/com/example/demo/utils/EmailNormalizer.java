package com.example.demo.utils;

public class EmailNormalizer {

    /**
     * Normalizes an email address.
     * 1. Trims whitespace.
     * 2. Converts to lowercase.
     * 3. For Gmail/Googlemail addresses:
     * - Removes dots from the local part.
     * - Removes everything after '+' (alias support).
     * 
     * @param email The raw email address
     * @return The normalized email address
     */
    public static String normalize(String email) {
        if (email == null) {
            return null;
        }

        email = email.toLowerCase().trim();

        if (email.contains("@")) {
            String[] parts = email.split("@");
            if (parts.length == 2) {
                String localPart = parts[0];
                String domain = parts[1];

                // Gmail and Googlemail handle dots and aliases similarly
                if (domain.equals("gmail.com") || domain.equals("googlemail.com")) {
                    // Remove alles after '+'
                    if (localPart.contains("+")) {
                        localPart = localPart.substring(0, localPart.indexOf("+"));
                    }
                    // Remove all dots
                    localPart = localPart.replace(".", "");

                    return localPart + "@gmail.com"; // Standardize to gmail.com
                }
            }
        }

        return email;
    }
}
