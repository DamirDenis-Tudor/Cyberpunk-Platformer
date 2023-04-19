package Database;

import Enums.ComponentType;

public class SerializedObject {
    private final ComponentType type;
    private final byte[] data;

    public SerializedObject(ComponentType type, byte[] data) {
        this.type = type;
        this.data = data;
    }

    public ComponentType getType() {
        return type;
    }

    public byte[] getData() {
        return data;
    }
}
