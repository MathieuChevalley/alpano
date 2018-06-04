package ch.epfl.alpano;

import ch.epfl.alpano.dem.ContinuousElevationModel;
import ch.epfl.alpano.dem.ElevationProfile;
import static ch.epfl.alpano.Distance.EARTH_RADIUS;
import static ch.epfl.alpano.Math2.*;

import static java.util.Objects.requireNonNull;
import static java.lang.Math.*;

import java.util.function.DoubleUnaryOperator;

/**
 * Class that computes a panorama
 * 
 * @author Mathieu Chevalley (274698)
 * @author Louis Amaudruz (271808)
 * @see Panorama
 */
public final class PanoramaComputer {

    private static final double REFRACTION_CONSTANT = 0.13;
    private static final double D = (1.0 - REFRACTION_CONSTANT)
            / (2 * EARTH_RADIUS);
    private static final int SMALL_INTERVAL = 4;
    private static final int INTERVAL = 64;
    private final ContinuousElevationModel dem;

    /**
     * Create a panorama computer from a continuous dem
     * 
     * @param dem
     *            a continuous elevation model used to compute the panorama
     * @throws NullPointerException
     *             if the dem is null
     */
    public PanoramaComputer(ContinuousElevationModel dem) {
        this.dem = requireNonNull(dem);
    }

    /**
     * Function that computes the panorama
     * 
     * @param parameters
     *            the parameters of the panorama
     * @return the panorama
     */
    public Panorama computePanorama(PanoramaParameters parameters) {

        Panorama.Builder panoBuilder = new Panorama.Builder(parameters);

        // iteration on all samples
        for (int x = 0; x <= parameters.width() - 1; ++x) {

            ElevationProfile profile = new ElevationProfile(dem,
                    parameters.observerPosition(), parameters.azimuthForX(x),
                    parameters.maxDistance());

            double lastAbcissa = 0;
            boolean notInfinity = true;

            for (int y = parameters.height() - 1; y >= 0 && notInfinity; y--) {

                double altitudeForY = parameters.altitudeForY(y);
                // The function
                DoubleUnaryOperator function = rayToGroundDistance(profile,
                        parameters.observerElevation(), tan(altitudeForY));

                // first approximation
                double abscissa = firstIntervalContainingRoot(function,
                        lastAbcissa, parameters.maxDistance(), INTERVAL);

                // only if the abscissa is finite
                if (abscissa == Double.POSITIVE_INFINITY) {
                    notInfinity = false;
                } else {
                    // improvement of the first approximation
                    abscissa = improveRoot(function, abscissa,
                            abscissa + INTERVAL, SMALL_INTERVAL);

                    // distance from observer to the point, using the angle
                    // between the function and the axe
                    double distance = abscissa / cos(altitudeForY);

                    // set all found datum
                    GeoPoint position = profile.positionAt(abscissa);
                    panoBuilder.setDistanceAt(x, y, (float) distance)
                            .setElevationAt(x, y,
                                    (float) dem.elevationAt(position))
                            .setLatitudeAt(x, y, (float) position.latitude())
                            .setLongitudeAt(x, y, (float) position.longitude())
                            .setSlopeAt(x, y, (float) dem.slopeAt(position));

                }

                lastAbcissa = abscissa;

            }
        }

        return panoBuilder.build();
    }

    /**
     * Give a function computing the distance between a ray and the ground
     * 
     * @param profile
     *            the profile
     * @param ray0
     *            initial elevation
     * @param raySlope
     *            slope of the function
     * @return a function computing the distance between the ground and the ray
     * @throws NullPointerException
     *             if profile is null
     */
    public static DoubleUnaryOperator rayToGroundDistance(
            ElevationProfile profile, double ray0, double raySlope) {
        return x -> ray0 + x * raySlope - profile.elevationAt(x) + sq(x) * D;

    }
}
