package ch.epfl.alpano;

import static java.util.Objects.requireNonNull;
import static ch.epfl.alpano.Preconditions.*;
import java.util.Objects;

/**
 * Create a two dimensional interval given two one dimensional intervals
 * (cartesian product) Immutable class
 * 
 * @author Mathieu Chevalley (274698)
 * @see Interval1D
 */
public final class Interval2D {

    private final Interval1D iX;
    private final Interval1D iY;
    private final int size;

    /**
     * Construct the cartesian product of two intervals
     * 
     * @param iX
     *            a 1d interval
     * @param iY
     *            a 1d interval
     * @throws NullPointerException
     *             if either of the intervals are null
     */
    public Interval2D(Interval1D iX, Interval1D iY) {
        this.iX = requireNonNull(iX);
        this.iY = requireNonNull(iY);
        size = iX.size() * iY.size();
    }

    /**
     * First interval
     * 
     * @return the first interval
     */
    public Interval1D iX() {
        return iX;
    }

    /**
     * Second interval
     * 
     * @return the second interval
     */
    public Interval1D iY() {
        return iY;
    }

    /**
     * Check if the pair is in the interval
     * 
     * @param x
     *            first element of the pair
     * @param y
     *            second element of the pair
     * @return <code>true</code> if this contains the pair
     */
    public boolean contains(int x, int y) {
        return iX().contains(x) && iY().contains(y);
    }

    /**
     * Size of the interval
     * 
     * @return the size of the interval
     */
    public int size() {
        return size;
    }

    /**
     * Size of an intersection
     * 
     * @param that
     *            an other interval
     * @return the size of their intersection
     * @see Interval1D#sizeOfIntersectionWith(Interval1D)
     */
    public int sizeOfIntersectionWith(Interval2D that) {
        return iX().sizeOfIntersectionWith(that.iX())
                * iY().sizeOfIntersectionWith(that.iY());
    }

    /**
     * Bounding union of two intervals
     * 
     * @param that
     *            an other interval
     * @return the bounding union of both intervals
     * @throws NullPointerException
     *             if that is null
     */
    public Interval2D boundingUnion(Interval2D that) {

        return new Interval2D(iX().boundingUnion(requireNonNull(that).iX()),
                iY().boundingUnion(that.iY()));
    }

    /**
     * Check if both intervals are unionable
     * 
     * @param that
     *            an other interval
     * @return <code>true</code> iff this and that are unionable
     * @throws NullPointerException
     *             if that is null
     */
    public boolean isUnionableWith(Interval2D that) {
        // boundingUnion checks if that is null
        return boundingUnion(that).size() == size() + that.size()
                - sizeOfIntersectionWith(that);
    }

    /**
     * Union of two intervals
     * 
     * @param that
     *            an other interval
     * @return the union of both intervals
     * @throws IllegalArgumentException
     *             if this and that are not unionable
     * @throws NullPointerException
     *             if that is null
     */
    public Interval2D union(Interval2D that) {
        // isUnionableWith checks if that is null
        checkArgument(isUnionableWith(that));
        return boundingUnion(that);
    }

    @Override
    public boolean equals(Object thatO) {
        return thatO instanceof Interval2D
                && iX().equals(((Interval2D) thatO).iX())
                && iY().equals(((Interval2D) thatO).iY());

    }

    @Override
    public int hashCode() {
        return Objects.hash(iX(), iY());
    }

    @Override
    public String toString() {
        return iX() + "x" + iY();
    }
}
