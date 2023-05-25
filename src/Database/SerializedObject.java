package Database;

import Enums.ComponentType;

/**
 * This class encapsulates a serialized game object.
 *
 * @param type object identifier
 * @param data serialized object field
 */
public record SerializedObject(ComponentType type, byte[] data) { }
