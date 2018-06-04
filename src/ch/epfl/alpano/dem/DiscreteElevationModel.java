package ch.epfl.alpano.dem;

import ch.epfl.alpano.Interval2D;

/**
 * An interface used for the methods and constants of a DEM
 * 
 * @author Louis Amaudruz (271808)
 * @author Mathieu Chevalley (274698)
 * 
 * @see AutoCloseable
 *
 */
public interface DiscreteElevationModel extends AutoCloseable {

    /**
     * number of sample per degree
     */
    int SAMPLES_PER_DEGREE = 3600;

    /**
     * number of sample per radian
     */
    double SAMPLES_PER_RADIAN = (SAMPLES_PER_DEGREE * 180 / Math.PI);

    /**
     * Returns the index corresponding to an angle
     * 
     * @param angle
     * @return index
     */
    static double sampleIndex(double angle) {
        return angle * SAMPLES_PER_RADIAN;
    }

    /**
     * The extent of the dem
     * 
     * @return the area of the DEM
     * @see Interval2D
     */
    Interval2D extent();

    /**
     * Method that will give the elevation at a location in the DEM
     * 
     * @param x
     *            the first index of the location
     * @param y
     *            the second index of the location
     * @return the elevation at the location
     * @throws IllegalArgumentException
     *             if the sample is not in the extent
     */
    double elevationSample(int x, int y);

    /**
     * Will make the union between the DEM and an other one
     * 
     * @param that
     *            the other DEM
     * @return the union between the DEM this and an other DEM that
     * @throws IllegalArgumentException
     *             if this and that are not unionable
     */
    default DiscreteElevationModel union(DiscreteElevationModel that) {
        return new CompositeDiscreteElevationModel(this, that);
    }

}
