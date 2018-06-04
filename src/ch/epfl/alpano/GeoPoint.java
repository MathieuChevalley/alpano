package ch.epfl.alpano;

import static ch.epfl.alpano.Preconditions.checkArgument;
import static java.util.Objects.requireNonNull;
import static java.lang.Math.*;

import java.util.Locale;

import static ch.epfl.alpano.Math2.*;
import static ch.epfl.alpano.Distance.*;
import static ch.epfl.alpano.Azimuth.*;

/**
 * Represents a point on earth surface (immutable class)
 * 
 * @author Louis Amaudruz (271808)
 * @author Mathieu Chevalley (274698)
 *
 */
public final class GeoPoint {

    private final double longitude;
    private final double latitude;

    /**
     * Construct a geo point
     * 
     * @param longitude
     *            a longitude between - pi and pi (included)
     * @param latitude
     *            a latitude between -pi/2 and pi/2 (included)
     * @throws IllegalArgumentException
     *             if longitude or latitude is not properly formated
     */
    public GeoPoint(double longitude, double latitude) {
        checkArgument(longitude <= PI && longitude >= -PI);
        checkArgument(latitude <= PI / 2 && latitude >= -PI / 2);

        this.latitude = latitude;
        this.longitude = longitude;
    }

    /**
     * GeoPoint longitude
     * 
     * @return its longitude
     */
    public double longitude() {
        return longitude;
    }

    /**
     * GeoPoint latitude
     * 
     * @return its latitude
     */
    public double latitude() {
        return latitude;
    }

    /**
     * Compute the distance between two points
     * 
     * @param that
     *            an other point
     * @return the distance in meters
     * @throws NullPointerException
     *             if that is null
     */
    public double distanceTo(GeoPoint that) {
        requireNonNull(that);
        // compute the angle separating this and that
        double angle = 2 * asin(sqrt(haversin(latitude() - that.latitude())
                + cos(latitude()) * cos(that.latitude())
                        * haversin(longitude() - that.longitude())));
        return toMeters(angle);
    }

    /**
     * Give the azimuth from the current to a given position
     * 
     * @param that
     *            the other position
     * @return the azimuth
     * @throws NullPointerException
     *             if that is null
     */
    public double azimuthTo(GeoPoint that) {
        requireNonNull(that);
        // compute the azimuth leading to that
        double angle = atan2(
                sin(longitude() - that.longitude()) * cos(that.latitude()),
                (cos(latitude()) * sin(that.latitude())
                        - sin(latitude()) * cos(that.latitude())
                                * cos(longitude() - that.longitude())));

        return fromMath(canonicalize(angle));
    }

    @Override
    public String toString() {
        Locale l = null;
        return String.format(l, "[%.3f; %.3f]", toDegrees(longitude()),
                toDegrees(latitude()));
    }

}
