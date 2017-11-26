package domains;

import javax.annotation.concurrent.Immutable;

/**
 * A DomainSlot is just an encapsulation of job id (i) an time t (t).
 */
@Immutable
public final class DomainSlot {
    final private int i;
    final private int t;

    /**
     * @param i Job id.
     * @param t Time.
     */
    public DomainSlot(int i, int t) {
        this.i = i;
        this.t = t;
    }

    /**
     * @return Job id.
     */
    public int getI() {
        return i;
    }

    /**
     * @return Time.
     */
    public int getT() {
        return t;
    }

    @Override
    public String toString(){
        return "[i :" + i + ", t :" + t + "]";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DomainSlot that = (DomainSlot) o;

        return i == that.i && t == that.t;
    }

    @Override
    public int hashCode() {
        int result = i;
        result = 31 * result + t;
        return result;
    }
}