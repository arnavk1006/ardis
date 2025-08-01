package resp.types;

/**
 * Represents a RESP array type.
 * This record encapsulates an array of RESP types.
 * Nested Arrays work because RespArray is a RespType.
 * Uses clones to prevent external modification of the array.
 */

public record RespArray(RespType[] elements) implements RespType {
    public RespArray(RespType[] elements) {
        this.elements = (elements == null) ? null : elements.clone();
    }

    public boolean isNull() {
        return elements == null;
    }

    public boolean isEmpty() {
        return !this.isNull() && this.elements.length == 0;
    }

    public RespType getIndex(int index) throws IndexOutOfBoundsException {
        if (this.isNull()) {
            throw new NullPointerException("Array is null");
        } else if (index < 0 || index >= elements.length) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Length: " + elements.length);
        }
        return elements[index];
    }

    @Override
    public RespType[] elements() {
        return this.isNull() ? null : elements.clone();
    }

    public int getLength() {
        if (this.isNull()) {
            throw new NullPointerException("Cannot get the length of a null array");
        }
        return elements.length;
    }
}
