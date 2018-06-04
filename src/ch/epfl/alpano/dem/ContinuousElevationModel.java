package ch.epfl.alpano.dem;

import ch.epfl.alpano.GeoPoint;
import static ch.epfl.alpano.Math2.*;
import static ch.epfl.alpano.Distance.*;
import ch.epfl.alpano.dem.DiscreteElevationModel;
import static ch.epfl.alpano.dem.DiscreteElevationModel.SAMPLES_PER_RADIAN;
import static java.lang.Math.*;
import static java.util.Objects.requireNonNull;
import static ch.epfl.alpano.dem.DiscreteElevationModel.sampleIndex;

/**
 * Class that represents a continuous elevation model which gives the elevation
 * at any point in an area Which is an expansion to the DEM
 * 
 * @author Louis Amaudruz (271808)
 * @author Mathieu Chevalley (274698)
 *
 */

final public class ContinuousElevationModel {

    private final DiscreteElevationModel dem;
    private static final double d = toMeters(1 / SAMPLES_PER_RADIAN);

    /**
     * The dem to be rendered continuous
     * 
     * @param dem
     *            : the DEM used
     * @throws NullPointerException
     *             if the dem is <code>null</code>
     */
    public ContinuousElevationModel(DiscreteElevationModel dem) {
        this.dem = requireNonNull(dem);
    }

    /**
     * Gives the elevation at any point in the boundaries of the DEM using
     * bilinear interpolation
     * 
     * @param p
     *            : the Geopoint giving the location
     * @return the elevation at the wanted location
     * 
     */
    public double elevationAt(GeoPoint p) {
        // indexes of the point
        double xp = sampleIndex(p.longitude());
        double yp = sampleIndex(p.latitude());

        int x = (int) floor(xp);
        int y = (int) floor(yp);

        // close points used for the interpolation
        double z00 = elevationAtIndex(x, y);
        double z01 = elevationAtIndex(x, y + 1);
        double z10 = elevationAtIndex(x + 1, y);
        double z11 = elevationAtIndex(x + 1, y + 1);

        return bilerp(z00, z10, z01, z11, xp - x, yp - y);
    }

    /**
     * Gives the slope at any point in the boundaries of the DEM using bilinear
     * interpolation
     * 
     * @param p
     *            : the Geopoint giving the location
     * @return the elevation at the wanted location
     */
    public double slopeAt(GeoPoint p) {
        // indexes of the point
        double xp = sampleIndex(p.longitude());
        double yp = sampleIndex(p.latitude());

        int x = (int) floor(xp);
        int y = (int) floor(yp);

        // close points used for the interpolation
        double z00 = slopeAtIndex(x, y);
        double z01 = slopeAtIndex(x, y + 1);
        double z10 = slopeAtIndex(x + 1, y);
        double z11 = slopeAtIndex(x + 1, y + 1);

        return bilerp(z00, z10, z01, z11, xp - x, yp - y);
    }

    private double elevationAtIndex(int x, int y) {
        if (dem.extent().contains(x, y)) {
            return dem.elevationSample(x, y);
        }

        return 0;
    }

    private double slopeAtIndex(int x, int y) {
        return acos(
                d / (sqrt(
                        sq(elevationAtIndex(x, y) - elevationAtIndex(x + 1, y))
                                + sq(elevationAtIndex(x, y)
                                        - elevationAtIndex(x, y + 1))
                                + sq(d))));
    }
}
