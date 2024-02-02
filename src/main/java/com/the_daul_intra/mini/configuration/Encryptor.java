package com.the_daul_intra.mini.configuration;

public class Encryptor {
    public static String encrypt(String input) {
        StringBuilder encrypted = new StringBuilder();
        for (char c : input.toCharArray()) {
            encrypted.append(rotateRight(c, 1));
        }
        return encrypted.toString();
    }

    public static String decrypt(String input) {
        StringBuilder decrypted = new StringBuilder();
        for (char c : input.toCharArray()) {
            decrypted.append((rotateLeft(c, 1)));
        }
        return decrypted.toString();
    }

    public static boolean checkPassword(String inputPassword, String storedPassword) {
        String encryptedInputPassword = encrypt(inputPassword);
        return encryptedInputPassword.equals(storedPassword);
    }

    private static char rotateLeft(char c, int d) {
        return (char) ((c << d) | (c >>> (16 - d)));
    }

    private static char rotateRight(char c, int d) {
        return (char) ((c >>> d) | (c << (16 - d)));
    }
}
