package ch.epfl.alpano.dem;

import static java.util.Objects.requireNonNull;
import static ch.epfl.alpano.Preconditions.*;

import ch.epfl.alpano.Interval2D;

/**
 * Class that represents a union of two DEM
 * 
 * @author Louis Amaudruz (271808)
 * @author Mathieu Chevalley (274698)
 * 
 * @see DiscreteElevationModel
 */
final class CompositeDiscreteElevationModel implements DiscreteElevationModel {

    private final Interval2D MNT;
    private final DiscreteElevationModel dem1;
    private final DiscreteElevationModel dem2;

    /**
     * Construct a dem with the union of the intervals and their elevations
     * 
     * @param dem1
     *            first dem
     * @param dem2
     *            second dem
     * @throws NullPointerException
     *             if dem1 or dem2 is <code>null</code>
     */
    CompositeDiscreteElevationModel(DiscreteElevationModel dem1,
            DiscreteElevationModel dem2) {

        this.dem1 = requireNonNull(dem1);
        this.dem2 = requireNonNull(dem2);
        this.MNT = dem1.extent().union(dem2.extent());

    }

    @Override
    public Interval2D extent() {
        return MNT;

    }

    @Override
    public double elevationSample(int x, int y) {

        checkArgument(extent().contains(x, y));

        if (dem1.extent().contains(x, y)) {
            return dem1.elevationSample(x, y);
        }

        return dem2.elevationSample(x, y);

    }

    @Override
    public void close() throws Exception {
        dem1.close();
        dem2.close();
    }

}
