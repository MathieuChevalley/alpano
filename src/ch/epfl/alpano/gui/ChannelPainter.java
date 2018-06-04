package ch.epfl.alpano.gui;

import java.util.function.DoubleUnaryOperator;
import static java.lang.Math.*;

import ch.epfl.alpano.Math2;
import ch.epfl.alpano.Panorama;

/**
 * Functional interface representing a channel painter
 * 
 * @author Louis Amaudruz (271808)
 * @author Mathieu Chevalley (274698) *
 */
@FunctionalInterface
public interface ChannelPainter {

    /**
     * Give the value of the channel painter at a given point
     * 
     * @param x
     *            first index
     * @param y
     *            second index
     * @return the corresponding value
     */
    public abstract float valueAt(int x, int y);

    /**
     * Compute the Channel painter which will return a value with respect to
     * some inforamtion about the panorama
     * 
     * @param p
     *            the panorama
     * @return The channel painter wanted
     */
    public static ChannelPainter maxDistanceToNeighbors(Panorama p) {

        return (x, y) -> max(
                max(p.distanceAt(x + 1, y, 0), p.distanceAt(x, y + 1, 0)),
                max(p.distanceAt(x - 1, y, 0), p.distanceAt(x, y - 1, 0)));
    }

    /**
     * Add an amount to the value returned by the channel painter
     * 
     * @param d
     *            the amount
     * @return the channel painter with the addition
     */
    public default ChannelPainter add(float d) {
        return (x, y) -> valueAt(x, y) + d;
    }

    /**
     * Multiply by an amount the value returned by the channel painter
     * 
     * @param d
     *            the amount
     * @return the channel painter with the multiplication
     */
    public default ChannelPainter mul(float d) {
        return (x, y) -> valueAt(x, y) * d;
    }

    /**
     * Subtract by an amount the value returned by the channel painter
     * 
     * @param d
     *            the amount
     * @return the channel painter with the subtraction
     */
    public default ChannelPainter sub(float d) {
        return (x, y) -> valueAt(x, y) - d;
    }

    /**
     * Divide by an amount the value returned by the channel painter
     * 
     * @param d
     *            the amount
     * @return the channel painter with the division
     */
    public default ChannelPainter div(float d) {
        return (x, y) -> valueAt(x, y) / d;
    }

    /**
     * Apply the double unary operator the the value of the channel painter
     * 
     * @param p
     *            the double unary operator
     * @return the channel painter to which the function was applied
     */
    public default ChannelPainter map(DoubleUnaryOperator p) {
        return (x, y) -> (float) p.applyAsDouble(valueAt(x, y));
    }

    /**
     * Apply a certain function (clamp) to the channel painter (value between 0
     * and 1)
     * 
     * @return the channel painter to which the function (clamp) was applied
     */
    public default ChannelPainter clamp() {
        return (x, y) -> max(0, min(valueAt(x, y), 1));
    }

    /**
     * Apply a certain function (cycle) to the channel painter (value mod 1)
     * 
     * @return the channel painter to which the function (cycle) was applied
     */
    public default ChannelPainter cycle() {
        return (x, y) -> (float) Math2.floorMod(valueAt(x, y), 1);

    }

    /**
     * Apply a certain function (invert) to the channel painter (1 - value)
     * 
     * @return the channel painter to which the function (invert) was applied
     */
    public default ChannelPainter invert() {
        return (x, y) -> 1 - valueAt(x, y);
    }

}
