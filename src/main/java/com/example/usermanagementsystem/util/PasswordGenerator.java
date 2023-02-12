package com.example.usermanagementsystem.util;


import java.security.SecureRandom;

public class PasswordGenerator {

    private static final int PASSWORD_LENGTH = 10;
    private static final String CHAR_LOWER = "abcdefghijklmnopqrstuvwxyz";
    private static final String CHAR_UPPER = CHAR_LOWER.toUpperCase();
    private static final String NUMBER = "0123456789";
    private static final String OTHER_CHAR = "!@#$%^&*()_+-=[]?";

    public static String generatePassword() {
        StringBuilder password = new StringBuilder();
        SecureRandom random = new SecureRandom();

        password.append(CHAR_LOWER.charAt(random.nextInt(CHAR_LOWER.length())));
        password.append(CHAR_UPPER.charAt(random.nextInt(CHAR_UPPER.length())));
        password.append(NUMBER.charAt(random.nextInt(NUMBER.length())));
        password.append(OTHER_CHAR.charAt(random.nextInt(OTHER_CHAR.length())));

        for (int i = password.length(); i < PASSWORD_LENGTH; i++) {
            String charSet = CHAR_LOWER + CHAR_UPPER + NUMBER + OTHER_CHAR;
            password.append(charSet.charAt(random.nextInt(charSet.length())));
        }

        return password.toString();
    }
}

