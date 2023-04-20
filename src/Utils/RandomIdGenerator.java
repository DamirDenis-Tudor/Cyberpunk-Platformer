package Utils;

import java.util.Random;

/**
 * This class is a simple id generator.
 */
public class RandomIdGenerator {
    private static final String ALPHABET = "ABCDEFGH123456789abcdefghijklmnopqrstuvwxyzIJKLMNOPQRSTUVWXYZ";
    private static final int LENGTH = 5;
    private static final Random RANDOM = new Random();

    public static String generate() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < LENGTH; i++) {
            int randomIndex = RANDOM.nextInt(ALPHABET.length());
            char randomChar = ALPHABET.charAt(randomIndex);
            sb.append(randomChar);
        }
        return sb.toString();
    }
}
