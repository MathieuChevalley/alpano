
package ch.epfl.alpano;

import static java.util.Objects.requireNonNull;
import static ch.epfl.alpano.Preconditions.*;
import static java.lang.Math.*;

import java.util.Objects;

/**
 * Create a one dimasional interval (immutable class)
 * 
 * @author Mathieu Chevalley (274698)
 * @author Louis Amaudruz (271808)
 */
public final class Interval1D {

    private final int includedFrom; // beginning of the interval
    private final int includedTo; // end of the interval
    private final int size;

    /**
     * Construct a on dimension interval
     * 
     * @param includedFrom
     *            beginning of the interval
     * @param includedTo
     *            end of the interval
     * @throws IllegalArgumentException
     *             if includedTo is strictly smaller than includedFrom
     */
    public Interval1D(int includedFrom, int includedTo) {
        checkArgument(includedFrom <= includedTo);
        this.includedFrom = includedFrom;
        this.includedTo = includedTo;
        size = includedTo - includedFrom + 1;
    }

    /**
     * Interval beginning
     * 
     * @return the beginning of the interval
     */
    public int includedFrom() {
        return includedFrom;
    }

    /**
     * Interval ending
     * 
     * @return end of the interval
     */
    public int includedTo() {
        return includedTo;
    }

    /**
     * Check if v is in the interval
     * 
     * @param v
     *            an integer
     * @return <code>true</code> if v is inside, <code>false</code> otherwise
     */
    public boolean contains(int v) {
        return v >= includedFrom() && v <= includedTo();
    }

    /**
     * Size of the interval
     * 
     * @return number of elements of the interval
     */
    public int size() {
        return size;
    }

    /**
     * Size of the intersection
     * 
     * @param that
     *            an other interval
     * @return the size of the intersection between this and that
     * @throws NullPointerException
     *             if that is null
     */
    public int sizeOfIntersectionWith(Interval1D that) {
        int intersectionFrom = max(includedFrom(), that.includedFrom());
        int intersectionTo = min(includedTo(), that.includedTo());
        return max(0, intersectionTo - intersectionFrom + 1);
    }

    /**
     * The bounding union of two interval
     * 
     * @param that
     *            an other interval
     * @return the bounding union of this and that
     * @throws NullPointerException
     *             if that is null
     */
    public Interval1D boundingUnion(Interval1D that) {
        requireNonNull(that);
        return new Interval1D(min(includedFrom(), that.includedFrom()),
                max(includedTo(), that.includedTo()));
    }

    /**
     * Check if this and that are unionable
     * 
     * @param that
     *            an other interval
     * @return true if this and that are unionable
     * @throws NullPointerException
     *             if that is null
     */
    public boolean isUnionableWith(Interval1D that) {

        return (boundingUnion(that)).size() == size() + that.size()
                - sizeOfIntersectionWith(that);
    }

    /**
     * Union of two interval
     * 
     * @param that
     *            an other interval
     * @return the union of both intervals
     * @throws IllegalArgumentException
     *             if the two interval are not unionable
     * @throws NullPointerException
     *             if that is null
     */
    public Interval1D union(Interval1D that) {
        checkArgument(isUnionableWith(that));
        // is unionable check if that is null
        return boundingUnion(that);
    }

    @Override
    public boolean equals(Object thatO) {

        return thatO instanceof Interval1D
                && (((Interval1D) thatO).includedFrom() == includedFrom()
                        && ((Interval1D) thatO).includedTo() == includedTo());

    }

    @Override
    public int hashCode() {
        return Objects.hash(includedFrom(), includedTo());
    }

    @Override
    public String toString() {
        return "[" + includedFrom() + ".." + includedTo() + "]";
    }
}
