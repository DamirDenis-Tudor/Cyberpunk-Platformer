package Utils;

import java.util.Random;

/**
 * This class is a simple id generator.
 */
public class RandomIdGenerator {
    /**
     * Alphabet of generator
     */
    private static final String ALPHABET = "ABCDEFGH123456789abcdefghijklmnopqrstuvwxyzIJKLMNOPQRSTUVWXYZ";

    /**
     * Id length
     */
    private static final int LENGTH = 5;

    /**
     * Shared instance
     */
    private static final Random RANDOM = new Random();

    /**
     * This method generates a string id.
     *
     * @return generated id.
     */
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
