package ch.epfl.alpano;

/**
 * Interface to convert radians to meters, and vice versa
 * 
 * @author Louis Amaudruz (271808)
 * @author Mathieu Chevalley (274698)
 * 
 */
public interface Distance {

    /**
     * Approximate radius of earth
     */
    public final static double EARTH_RADIUS = 6371000;

    /**
     * Convert a distante to radians
     * 
     * @param distanceInMeters
     * @return the corresponding radian
     */
    public static double toRadians(double distanceInMeters) {

        return distanceInMeters / EARTH_RADIUS;

    }

    /**
     * Convert a radian distance to meters
     * 
     * @param distanceInRadians
     * @return the corresponding distance
     */
    public static double toMeters(double distanceInRadians) {

        return EARTH_RADIUS * distanceInRadians;

    }
}
