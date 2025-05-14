package apr.reflection;

import lombok.EqualsAndHashCode;

/**
 * TestRecord
 */
@EqualsAndHashCode
public class TestRecord {
    public Double x;
    public Double y;
    public Double z;
    public String name;

    public String toString() {
        return String.format("%s[%f, %f, %f, %s]", this.getClass().getSimpleName(), x, y, z, name);
    }
}
