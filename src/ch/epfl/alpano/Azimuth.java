package ch.epfl.alpano;

import static java.lang.Math.PI;

import java.util.Arrays;
import java.util.List;

import static ch.epfl.alpano.Math2.PI2;
import static ch.epfl.alpano.Preconditions.checkArgument;

/**
 * Interface used for computations on azimuths
 * 
 * @author Louis Amaudruz (271808)
 * @author Mathieu Chevalley (274698)
 * 
 */

public interface Azimuth {

    /**
     * Azimuth check
     * 
     * @param azimuth
     * @return <code>true</code> if the azimuth is canonical (between 0 and 2pi)
     */
    public static boolean isCanonical(double azimuth) {

        return azimuth >= 0 && azimuth < PI2;

    }

    /**
     * Canonicalize an azimuth
     * 
     * @param azimuth
     * @return an azimuth between 0 and 2 pi
     */
    public static double canonicalize(double azimuth) {
        return Math2.floorMod(azimuth, PI2);
    }

    /**
     * Return an azimuth in the correct mathematical way
     * 
     * @param azimuth
     *            in geographical manner
     * @return the mathematical azimuth
     * @throws IllegalArgumentException
     *             if the azimuth is not canonical
     */
    public static double toMath(double azimuth) {
        checkArgument(isCanonical(azimuth));

        return canonicalize(PI2 - azimuth);
    }

    /**
     * Return the azimuth in the geographical manner
     * 
     * @param angle
     *            mathematical angle
     * @return the correct azimuth angle
     * @throws IllegalArgumentException
     *             if the angle is not canonical
     */
    public static double fromMath(double angle) {
        checkArgument(isCanonical(angle));

        return canonicalize(PI2 - angle);
    }

    /**
     * Return a string that represents the octant in which the azimuth is
     * 
     * @param azimuth
     *            the azimuth
     * @param n
     *            north string
     * @param e
     *            east string
     * @param s
     *            south string
     * @param w
     *            west string
     * @return a <code>String</code> position in octant
     * @throws IllegalArgumentException
     *             if azimuth is not canonical
     */
    public static String toOctantString(double azimuth, String n, String e,
            String s, String w) {
        checkArgument(isCanonical(azimuth));

        double angle = PI / 8;
        // list of possible octants
        List<String> octants = Arrays.asList(n, n + e, e, s + e, s, s + w, w,
                n + w);

        for (int i = 0; i < 7; ++i) {

            if (azimuth >= angle && azimuth < angle + PI / 4) {
                return octants.get(i + 1);
            }

            angle = canonicalize(angle + PI / 4);
        }

        return octants.get(0); // the only octants that is not checked above

    }

}
