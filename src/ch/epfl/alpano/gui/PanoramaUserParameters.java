package ch.epfl.alpano.gui;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;

import ch.epfl.alpano.GeoPoint;
import ch.epfl.alpano.PanoramaParameters;

import static ch.epfl.alpano.gui.UserParameter.*;
import static java.lang.Math.*;

/**
 * The parameters given by the user for the panorama
 * 
 * @author Mathieu Chevalley (274698)
 * @author Louis Amaudruz (271808)
 *
 */
public final class PanoramaUserParameters {

    private final Map<UserParameter, Integer> userParameters;
    private final PanoramaParameters panoramaParameters;
    private final PanoramaParameters panoramaDisplayParameters;

    private static final int VERTICAL_LIMIT = 170;

    /**
     * Construct a panorama given the user parameters
     * 
     * @param parameters
     *            a map of the user parameters with their value
     * @see UserParameter
     */
    public PanoramaUserParameters(Map<UserParameter, Integer> parameters) {

        for (UserParameter m : parameters.keySet()) {
            parameters.put(m, m.sanitize(parameters.get(m)));
        }

        // correct the maximum height
        int maxHeight = (VERTICAL_LIMIT * (parameters.get(WIDTH) - 1)
                / parameters.get(HORIZONTAL_FIELD_OF_VIEW)) + 1;

        if (parameters.get(HEIGHT) > maxHeight) {
            parameters.put(HEIGHT, maxHeight);
        }

        userParameters = Collections.unmodifiableMap(new EnumMap<>(parameters));

        panoramaDisplayParameters = createPanoramaParameters(1);
        panoramaParameters = createPanoramaParameters(
                (int) Math.pow(2, superSamplingExponent()));

    }

    /**
     * Construct a panorama given the user parameters
     * 
     * @param longitude
     *            longitude of the observer
     * @param latitude
     *            latitude of the observer
     * @param elevation
     *            elevation of the observer
     * @param azimuth
     *            central azimuth of the view
     * @param horizontalField
     *            horizontal field of view
     * @param maxDistance
     *            maximum distance that can be seen
     * @param width
     *            width of the image
     * @param height
     *            height of the image
     * @param superSamplingExponent
     *            exponent giving the amount of super sampling
     */
    public PanoramaUserParameters(int longitude, int latitude, int elevation,
            int azimuth, int horizontalField, int maxDistance, int width,
            int height, int superSamplingExponent) {

        this(createMap(longitude, latitude, elevation, azimuth, horizontalField,
                maxDistance, width, height, superSamplingExponent));
    }

    // create a map given all the parameters
    private static Map<UserParameter, Integer> createMap(int longitude,
            int latitude, int elevation, int azimuth, int horizontalField,
            int maxDistance, int width, int height, int superSamplingExponent) {

        Map<UserParameter, Integer> userParameters = new EnumMap<>(
                UserParameter.class);

        userParameters.put(OBSERVER_LONGITUDE, longitude);
        userParameters.put(OBSERVER_LATITUDE, latitude);
        userParameters.put(OBSERVER_ELEVATION, elevation);
        userParameters.put(CENTER_AZIMUTH, azimuth);
        userParameters.put(HORIZONTAL_FIELD_OF_VIEW, horizontalField);
        userParameters.put(MAX_DISTANCE, maxDistance);
        userParameters.put(WIDTH, width);
        userParameters.put(HEIGHT, height);
        userParameters.put(SUPER_SAMPLING_EXPONENT, superSamplingExponent);

        return userParameters;
    }

    /**
     * Construct the panorama parameters that will be calculated (using
     * supersampling)
     * 
     * @return the Panorama parameters using Oversampling
     */
    public PanoramaParameters panoramaParameters() {
        return panoramaParameters;
    }

    /**
     * Construct the panorama parameters that will be drawn (without using
     * supersampling)
     * 
     * @return the Panorama parameters without using Oversampling
     */
    public PanoramaParameters panoramaDisplayParameters() {
        return panoramaDisplayParameters;
    }

    private static final double TO_DOUBLE_FORMAT = 10000d;
    private static final int TO_METERS_FORMAT = 1000;

    private PanoramaParameters createPanoramaParameters(int superSampling) {
        return new PanoramaParameters(
                new GeoPoint(toRadians(observerLongitude() / TO_DOUBLE_FORMAT),
                        toRadians(observerLatitude() / TO_DOUBLE_FORMAT)),
                observerElevation(), toRadians(azimuth()),
                toRadians(HorizontalFieldOfView()),
                maxDistance() * TO_METERS_FORMAT, superSampling * width(),
                superSampling * height());
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof PanoramaUserParameters && userParameters
                .equals(((PanoramaUserParameters) other).userParameters);
    }

    @Override
    public int hashCode() {
        return userParameters.hashCode();
    }

    /**
     * Get the value given a parameter name
     * 
     * @param parameter
     *            the parameter
     * @return the value of this parameter
     */
    public int get(UserParameter parameter) {
        return userParameters.get(parameter);
    }

    /**
     * The longitude of the observer
     * 
     * @return the longitude
     */
    public int observerLongitude() {
        return get(OBSERVER_LONGITUDE);
    }

    /**
     * The latitude of the observer
     * 
     * @return the latitude
     */
    public int observerLatitude() {
        return get(OBSERVER_LATITUDE);
    }

    /**
     * The elevation of the observer
     * 
     * @return the elevation
     */
    public int observerElevation() {
        return get(OBSERVER_ELEVATION);
    }

    /**
     * The central azimuth of the panorama
     * 
     * @return the azimuth
     */
    public int azimuth() {
        return get(CENTER_AZIMUTH);
    }

    /**
     * The total horizontal field of view
     * 
     * @return the horizontal field of view
     */
    public int HorizontalFieldOfView() {
        return get(HORIZONTAL_FIELD_OF_VIEW);
    }

    /**
     * The maximum distance that can be seen
     * 
     * @return the maximum distance
     */
    public int maxDistance() {
        return get(MAX_DISTANCE);
    }

    /**
     * The width of the image
     * 
     * @return the width
     */
    public int width() {
        return get(WIDTH);
    }

    /**
     * The height of the image
     * 
     * @return the height
     */
    public int height() {
        return get(HEIGHT);
    }

    /**
     * The exponent of super sampling to compute the image
     * 
     * @return the super sampling exponent
     */
    public int superSamplingExponent() {
        return get(SUPER_SAMPLING_EXPONENT);
    }
}
