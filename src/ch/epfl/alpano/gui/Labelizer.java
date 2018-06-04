package ch.epfl.alpano.gui;

import java.util.Collections;
import java.util.List;
import java.util.function.DoubleUnaryOperator;

import ch.epfl.alpano.GeoPoint;
import ch.epfl.alpano.Math2;
import ch.epfl.alpano.PanoramaParameters;
import ch.epfl.alpano.dem.ContinuousElevationModel;
import ch.epfl.alpano.dem.ElevationProfile;
import ch.epfl.alpano.summit.Summit;
import javafx.scene.Node;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;

import static ch.epfl.alpano.Math2.angularDistance;
import static java.util.Objects.requireNonNull;
import static java.lang.Math.*;
import static ch.epfl.alpano.PanoramaComputer.rayToGroundDistance;

import java.util.ArrayList;
import java.util.BitSet;

/**
 * Represents a Tagging of the panorama (all the summits to be drawn in the
 * panorama)
 *
 * @author Mathieu Chevalley (274698)
 * @author Louis Amaudruz (271808)
 *
 */
public final class Labelizer {

    private final ContinuousElevationModel cem;
    private final List<Summit> summits;

    /**
     * Construct the labelizer given a continuous elevation model and a list of
     * all the summits
     *
     * @param cem
     *            the continuous elevation model
     * @param summits
     *            the list of all the summits
     * @throws NullPointerException
     *            if one of the input is null
     */
    public Labelizer(ContinuousElevationModel cem, List<Summit> summits) {
        this.cem = requireNonNull(cem);
        this.summits = Collections.unmodifiableList(requireNonNull(summits));
    }

    private static final int ABOVE_BORDER = 170;
    private static final int LINE_TO_SUMMIT_PIXELS = 2;
    private static final int SIDE_BORDER = 20;
    private static final int MIN_LINE_LENGTH = 20;
    private static final int TEXT_ROTATION = -60;

    /**
     * Construct a list of nodes representing all the summits that can be drawn
     * given some constraints in a panorama
     *
     * @param parameters
     *            the parameters of the panorama
     * @return the list of nodes
     */
    public List<Node> labels(PanoramaParameters parameters) {

        final List<Node> labels = new ArrayList<>();
        final List<VisibleSummit> visibleSummits = visibleSummits(parameters);

        Collections.sort(visibleSummits, (x, y) -> {

            int xY = x.getY();
            int yY = y.getY();

            if (xY > yY) {
                return 1;
            } else if (xY < yY) {
                return -1;
            } else {

                int xElevation = x.getSummit().elevation();
                int yElevation = y.getSummit().elevation();

                if (xElevation < yElevation) {
                    return 1;
                } else if (xElevation > yElevation) {
                    return -1;
                } else {
                    return 0;
                }
            }
        });

        int width = parameters.width();
        int maxX = width - SIDE_BORDER;

        BitSet availablePos = new BitSet(width - 2 * SIDE_BORDER);

        int minHeight = 0;
        boolean firstAcceptedSummit = true;

        for (VisibleSummit s : visibleSummits) {

            int x = s.getX();
            int y = s.getY();

            if (x >= SIDE_BORDER && x <= maxX
                    && y >= ABOVE_BORDER
                    && available(availablePos, x - SIDE_BORDER)) {

                if (firstAcceptedSummit) {
                    minHeight = y - MIN_LINE_LENGTH - LINE_TO_SUMMIT_PIXELS;
                    firstAcceptedSummit = false;
                }

                labels.add(
                        new Line(x, minHeight + LINE_TO_SUMMIT_PIXELS, x, y));

                Text text = new Text(s.toString());
                text.getTransforms().addAll(new Translate(x, minHeight),
                        new Rotate(TEXT_ROTATION));
                labels.add(text);
            }
        }

        return labels;
    }

    // Indicates if the certain area of a set of bits is available

    private static final int SPACE = 20;

    private boolean available(BitSet set, int index) {

        // true = place is taken
        for (int i = index; i < index + SPACE; i++) {

            if (set.get(i)) {
                return false;
            }
        }

        set.set(index, index + SPACE, true);

        return true;
    }

    private static final int ERROR_CONSTANT = 200;
    private static final int INTERVAL = 64;

    // Construct a list of all the visible summits in a panorama

    private List<VisibleSummit> visibleSummits(PanoramaParameters parameters) {

        List<VisibleSummit> visibleSummits = new ArrayList<>();


        GeoPoint observerPosition = parameters.observerPosition();
        int observerElevation = parameters.observerElevation();
        int maxDistance = parameters.maxDistance();
        double centerAzimuth = parameters.centerAzimuth();

        double halfHorizontal = parameters.horizontalFieldOfView() / 2d;
        double halfVertical = parameters.verticalFieldOfView() / 2d;

        for (Summit s : summits) {

            GeoPoint summitPosition = s.position();

            double distance = observerPosition.distanceTo(summitPosition);
            double azimuth = observerPosition.azimuthTo(summitPosition);

            ElevationProfile profile = new ElevationProfile(cem,
                    observerPosition, azimuth, distance);

            double height = rayToGroundDistance(profile, observerElevation, 0)
                    .applyAsDouble(distance);
            double slope = -height / distance;

            DoubleUnaryOperator f = rayToGroundDistance(profile,
                    observerElevation, slope);

            double altitude = atan(slope);

            if (distance <= maxDistance
                    && abs(angularDistance(centerAzimuth,
                            azimuth)) <= halfHorizontal
                    && abs(altitude) <= halfVertical
                    && Math2.firstIntervalContainingRoot(f, 0, distance,
                            INTERVAL) >= distance - ERROR_CONSTANT) {

                int x = (int) round(parameters.xForAzimuth(azimuth));
                int y = (int) round(parameters.yForAltitude(altitude));
                visibleSummits.add(new VisibleSummit(s, x, y));
            }
        }

        return visibleSummits;
    }

    // Ease the access to data that have already been calculated
    private static final class VisibleSummit {

        private final Summit summit;
        private final int x;
        private final int y;

        public VisibleSummit(Summit s, int x, int y) {
            summit = s;
            this.x = x;
            this.y = y;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public Summit getSummit() {
            return summit;
        }

        @Override
        public String toString() {
            return summit.name() + " (" + summit.elevation() + ")";
        }
    }
}
