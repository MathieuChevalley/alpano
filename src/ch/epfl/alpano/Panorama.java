package ch.epfl.alpano;

import java.util.Arrays;

/**
 * Represent a panorama
 * 
 * @author Mathieu Chevalley (274698)
 * @author Louis Amaudruz (271808)
 */
public final class Panorama {

    private final PanoramaParameters panoramaParameters;
    private final float[] distance;
    private final float[] longitude;
    private final float[] latitude;
    private final float[] elevation;
    private final float[] slope;

    private Panorama(PanoramaParameters pano, float[] dist, float[] longi,
            float[] lati, float[] eleva, float[] slo) {
        panoramaParameters = pano;
        distance = dist;
        longitude = longi;
        latitude = lati;
        elevation = eleva;
        slope = slo;
    }

    /**
     * Parameters getter
     * 
     * @return the parameters of the panorama
     * @see PanoramaParameters
     */
    public PanoramaParameters parameters() {
        return panoramaParameters;
    }

    /**
     * Distance at an index
     * 
     * @param x
     *            first index
     * @param y
     *            second index
     * @return the corresponding distance
     * @throws IndexOutOfBoundsException
     *             if the index is out of the field
     */
    public float distanceAt(int x, int y) {
        checkIndex(x, y);
        return distance[parameters().linearSampleIndex(x, y)];
    }

    /**
     * Longitude at an index
     * 
     * @param x
     *            first index
     * @param y
     *            second index
     * @return the corresponding longitude
     * @throws IndexOutOfBoundsException
     *             if the index is out of the field
     */
    public float longitudeAt(int x, int y) {
        checkIndex(x, y);
        return longitude[parameters().linearSampleIndex(x, y)];
    }

    /**
     * Latitude at an index
     * 
     * @param x
     *            first index
     * @param y
     *            second index
     * @return the corresponding latitude
     * @throws IndexOutOfBoundsException
     *             if the index is out of the field
     */
    public float latitudeAt(int x, int y) {
        checkIndex(x, y);
        return latitude[parameters().linearSampleIndex(x, y)];
    }

    /**
     * Elevation at an index
     * 
     * @param x
     *            first index
     * @param y
     *            second index
     * @return the corresponding elevation
     * @throws IndexOutOfBoundsException
     *             if the index is out of the field
     */
    public float elevationAt(int x, int y) {
        checkIndex(x, y);
        return elevation[parameters().linearSampleIndex(x, y)];

    }

    /**
     * Slope at an index
     * 
     * @param x
     *            first index
     * @param y
     *            second index
     * @return the corresponding slope
     * @throws IndexOutOfBoundsException
     *             if the index is out of the field
     */
    public float slopeAt(int x, int y) {
        checkIndex(x, y);
        return slope[parameters().linearSampleIndex(x, y)];
    }

    /**
     * Distance at an index, with a default value
     * 
     * @param x
     *            first index
     * @param y
     *            second index
     * @param d
     *            the default value
     * @return the corresponding distance
     */
    public float distanceAt(int x, int y, float d) {
        if (parameters().isValidSampleIndex(x, y)) {
            return distance[parameters().linearSampleIndex(x, y)];
        }
        return d;
    }

    // check the indexes
    private void checkIndex(int x, int y) {
        if (!panoramaParameters.isValidSampleIndex(x, y)) {
            throw new IndexOutOfBoundsException("not in the field");
        }
    }

    /**
     * Builder for the panorama class
     * 
     * @author Mathieu Chevalley (274698)
     * @author Louis Amaudruz (271808)
     */
    public static final class Builder {

        private final PanoramaParameters parameters;
        private float[] distance;
        private float[] longitude;
        private float[] latitude;
        private float[] elevation;
        private float[] slope;
        private boolean build = false;

        /**
         * Construct the builder
         * 
         * @param parameters
         *            The parameters of the panorama
         * @throws NullPointerException
         *             if parameters is null
         */
        public Builder(PanoramaParameters parameters) {

            this.parameters = parameters;

            int length = parameters.height() * parameters.width();

            distance = new float[length];
            Arrays.fill(distance, Float.POSITIVE_INFINITY);
            longitude = new float[length];
            latitude = new float[length];
            elevation = new float[length];
            slope = new float[length];
        }

        /**
         * Add a distance
         * 
         * @param x
         *            first index
         * @param y
         *            second index
         * @param distance
         *            that corresponds to the index
         * @return this
         * @throws IllegalStateException
         *             if already built
         * @throws IndexOutOfBoundsException
         *             if the index are out of the field
         */
        public Builder setDistanceAt(int x, int y, float distance) {
            requireNonBuild();
            checkIndex(x, y);
            this.distance[parameters.linearSampleIndex(x, y)] = distance;
            return this;
        }

        /**
         * Add a longitude
         * 
         * @param x
         *            first index
         * @param y
         *            second index
         * @param longitude
         *            that corresponds to the index
         * @return this
         * @throws IllegalStateException
         *             if already built
         * @throws IndexOutOfBoundsException
         *             if the index are out of the field
         */
        public Builder setLongitudeAt(int x, int y, float longitude) {
            requireNonBuild();
            checkIndex(x, y);
            this.longitude[parameters.linearSampleIndex(x, y)] = longitude;
            return this;
        }

        /**
         * Add a latitude
         * 
         * @param x
         *            first index
         * @param y
         *            second index
         * @param latitude
         *            that corresponds to the index
         * @return this
         * @throws IllegalStateException
         *             if already built
         * @throws IndexOutOfBoundsException
         *             if the index are out of the field
         */
        public Builder setLatitudeAt(int x, int y, float latitude) {
            requireNonBuild();
            checkIndex(x, y);
            this.latitude[parameters.linearSampleIndex(x, y)] = latitude;
            return this;
        }

        /**
         * Add an elevation
         * 
         * @param x
         *            first index
         * @param y
         *            second index
         * @param elevation
         *            that corresponds to the index
         * @return this
         * @throws IllegalStateException
         *             if already built
         * @throws IndexOutOfBoundsException
         *             if the index are out of the field
         */
        public Builder setElevationAt(int x, int y, float elevation) {
            requireNonBuild();
            checkIndex(x, y);
            this.elevation[parameters.linearSampleIndex(x, y)] = elevation;
            return this;
        }

        /**
         * Add a slope
         * 
         * @param x
         *            first index
         * @param y
         *            second index
         * @param slope
         *            that corresponds to the index
         * @return this
         * @throws IllegalStateException
         *             if already built
         * @throws IndexOutOfBoundsException
         *             if the index are out of the field
         */
        public Builder setSlopeAt(int x, int y, float slope) {
            requireNonBuild();
            checkIndex(x, y);
            this.slope[parameters.linearSampleIndex(x, y)] = slope;
            return this;
        }

        /**
         * Build the panorama
         * 
         * @return the panorama
         * @throws IllegalStateException
         *             if already built
         */
        public Panorama build() {
            requireNonBuild();

            build = true;
            Panorama p = new Panorama(parameters, distance, longitude, latitude,
                    elevation, slope);
            distance = null;
            longitude = null;
            elevation = null;
            slope = null;
            return p;
        }

        private void requireNonBuild() {
            if (build) {
                throw new IllegalStateException("already built");
            }
        }

        private void checkIndex(int x, int y) {
            if (!parameters.isValidSampleIndex(x, y)) {
                throw new IndexOutOfBoundsException("not in the field");
            }
        }
    }

}
