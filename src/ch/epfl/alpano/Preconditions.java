package ch.epfl.alpano;

/**
 * Interface to check preconditions
 * 
 * @author Louis Amaudruz (271808)
 * @author Mathieu Chevalley (274698)
 */
public interface Preconditions {

    /**
     * Check a condition
     * 
     * @param b
     *            a condition
     * @throws IllegalArgumentException
     *             if b is false
     */
    public static void checkArgument(boolean b) {
        if (!b) {
            throw new IllegalArgumentException();
        }
    }

    /**
     * Check a condition, with an error message
     * 
     * @param b
     *            a condition
     * @param message
     *            a message
     * @throws IllegalArgumentException
     *             if b is false, with the message
     */
    public static void checkArgument(boolean b, String message) {
        if (!b) {
            throw new IllegalArgumentException(message);
        }
    }

}
