package Components;

/**
 * This interface gives the option to interact with another object.
 *
 * @note First of all, an UPCASTING is made by the JVM, and then
 * the DOWN-CASTING to the required type must be made by the programmer.
 */
public interface Interactive {
    void interactionWith(Object object);
}
