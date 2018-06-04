package ch.epfl.alpano;

import java.util.function.DoubleUnaryOperator;
import static ch.epfl.alpano.Preconditions.checkArgument;
import static java.lang.Math.*;

/**
 * A complement to java.lang.Math
 * 
 * @author Louis Amaudruz (271808)
 * @author Mathieu Chevalley (274698)
 * 
 * @see java.lang#Math
 */
public interface Math2 {

    /**
     * The double of pi
     */
    public final static double PI2 = 2 * Math.PI;

    /**
     * Compute the square of a number
     * 
     * @param x
     *            the number
     * @return the squared number
     */
    public static double sq(double x) {
        return x * x;
    }

    /**
     * Return the default modulo
     * 
     * @param x
     *            the numerator
     * @param y
     *            the divisor
     * @return the modulo
     */
    public static double floorMod(double x, double y) {
        return x - y * floor(x / y);
    }

    /**
     * Compute the haversin of a number
     * 
     * @param x
     *            the number
     * @return the corresponding haversin
     */
    public static double haversin(double x) {
        return sq(sin(x / 2));
    }

    /**
     * The difference between two angles
     * 
     * @param a1
     *            first angle
     * @param a2
     *            second angle
     * @return the difference
     */
    public static double angularDistance(double a1, double a2) {
        return floorMod(a2 - a1 + PI, PI2) - PI;
    }

    /**
     * Linear interpolation of two points
     * 
     * @param y0
     *            first point ordinate at 0
     * @param y1
     *            second point ordinate at 1
     * @param x
     *            the abscissa
     * @return the ordinate of x, using linear interpolation
     */
    public static double lerp(double y0, double y1, double x) {
        return (y1 - y0) * x + y0;
    }

    /**
     * Bilinear interpolation of 4 points
     * 
     * @param z00
     *            elevation at (0,0)
     * @param z10
     *            elevation at (1,0)
     * @param z01
     *            elevation at (0,1)
     * @param z11
     *            elevation at (1,1)
     * @param x
     *            the abscissa
     * @param y
     *            the ordinate
     * @return the result using a bilinear interpolation
     */
    public static double bilerp(double z00, double z10, double z01, double z11,
            double x, double y) {

        return lerp(lerp(z00, z10, x), lerp(z01, z11, x), y);

    }

    /**
     * Find an interval containing a root of a given function
     * 
     * @param f
     *            a function
     * @param minX
     *            the lower bound
     * @param maxX
     *            the upper bound
     * @param dX
     *            the size of the interval
     * @return the lower bound of the interval, infinity if no interval has been
     *         found
     */
    public static double firstIntervalContainingRoot(DoubleUnaryOperator f,
            double minX, double maxX, double dX) {

        checkArgument(minX <= maxX && dX > 0);

        for (double i = minX; i <= maxX - dX; i = i + dX) {

            if (f.applyAsDouble(i) * f.applyAsDouble(i + dX) <= 0) {
                return i;
            }

        }

        return Double.POSITIVE_INFINITY;
    }

    /**
     * Find an interval containing the root of a function using binary search
     * 
     * @param f
     *            the function
     * @param x1
     *            lower bound
     * @param x2
     *            upper bound
     * @param epsilon
     *            a size giving the precision
     * @return the lower bound of the interval
     * @throws IllegalArgumentException
     *             if the given interval do not contain a root
     * @throws IllegalArgumentException
     *             if epsilon is not positive
     */
    public static double improveRoot(DoubleUnaryOperator f, double x1,
            double x2, double epsilon) {

        double valueAtX1 = f.applyAsDouble(x1);
        double valueAtX2 = f.applyAsDouble(x2);

        checkArgument(valueAtX1 * valueAtX2 <= 0);
        checkArgument(epsilon > 0);

        // check if one of the bounds is a zero
        if (valueAtX1 == 0) {
            return x1;
        } else if (valueAtX2 == 0) {
            return x2;
        }

        // middle of the interval
        double xm = (x1 + x2) / 2.0;

        // interval small enough
        if (abs(x1 - x2) <= epsilon) {
            return x1;
        }

        double valueAtXm = f.applyAsDouble(xm);

        // choose the next interval
        if (valueAtXm * valueAtX1 < 0) {
            return improveRoot(f, x1, xm, epsilon);
        } else if (valueAtXm * valueAtX2 < 0) {
            return improveRoot(f, xm, x2, epsilon);
        }

        // f(xm) == 0
        return xm;
    }
}
