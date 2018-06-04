package ch.epfl.alpano.summit;

import ch.epfl.alpano.GeoPoint;
import static java.util.Objects.requireNonNull;

/**
 * Class that represents a summit (immutable)
 * 
 * @author Mathieu Chevalley (274698)
 * @author Louis Amaudruz (271808)
 */
public final class Summit {

    private final String name;
    private final GeoPoint position;
    private final int elevation;

    /**
     * Construct a summit
     * 
     * @param name
     *            of the summit
     * @param position
     * @param elevation
     *            (in meters)
     * @throws NullPointerException
     *             if name or position is null
     */
    public Summit(String name, GeoPoint position, int elevation) {
        this.name = requireNonNull(name);
        this.position = requireNonNull(position);
        this.elevation = elevation;
    }

    /**
     * Name getter
     * 
     * @return name of the summit
     */
    public String name() {
        return name;
    }

    /**
     * Position getter
     * 
     * @return position of the summit
     * @see GeoPoint
     */
    public GeoPoint position() {
        return position;
    }

    /**
     * Elevation getter
     * 
     * @return the elevation of the summit
     */
    public int elevation() {
        return elevation;
    }

    @Override
    public String toString() {
        return name() + " " + position() + " " + elevation();
    }
}
