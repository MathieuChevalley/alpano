package ch.epfl.alpano.gui;

import java.util.List;

import ch.epfl.alpano.Panorama;
import ch.epfl.alpano.PanoramaComputer;
import ch.epfl.alpano.PanoramaParameters;
import ch.epfl.alpano.dem.ContinuousElevationModel;
import ch.epfl.alpano.summit.Summit;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.image.Image;

/**
 * A bean that contains the proprieties of the panorama (image, labels...)
 * 
 * @author Mathieu Chevalley (274698)
 * @author Louis Amaudruz (271808)
 *
 */

public final class PanoramaComputerBean {

    private final ObjectProperty<PanoramaUserParameters> parameters;
    private final ObjectProperty<Image> image;
    private final ObjectProperty<Panorama> panorama;

    private final ObservableList<Node> labels;
    private final ObservableList<Node> unmodifiableLabels;
    private final Labelizer labelizer;
    private final PanoramaComputer computer;

    /**
     * Construct a panorama computer bean given all the summits and a continuous
     * elevation model
     * 
     * @param summits
     *            all the summits
     * @param dem
     *            the continuous elevation model
     */
    public PanoramaComputerBean(List<Summit> summits,
            ContinuousElevationModel dem) {
        computer = new PanoramaComputer(dem);
        labelizer = new Labelizer(dem, summits);

        parameters = new SimpleObjectProperty<>();
        panorama = new SimpleObjectProperty<>();
        image = new SimpleObjectProperty<>();
        labels = FXCollections.observableArrayList();
        unmodifiableLabels = FXCollections.unmodifiableObservableList(labels);

        parameters.addListener(
                (b, oldParameters, newParameters) -> compute(newParameters));

    }

    // Compute all the values with the new parameters
    private void compute(PanoramaUserParameters newParam) {

        PanoramaParameters parameters = newParam.panoramaParameters();

        panorama.set(computer.computePanorama(parameters));

        labels.setAll(labelizer.labels(newParam.panoramaDisplayParameters()));

        image.set(computeImage(getPanorama()));
    }

    // compute the new image
    private Image computeImage(Panorama panorama) {
        ChannelPainter dist = panorama::distanceAt;
        ChannelPainter hue = dist.div(100_000).cycle().mul(360);
        ChannelPainter s = dist.div(200_000).clamp().invert();

        ChannelPainter slo = panorama::slopeAt;
        ChannelPainter b = slo.mul(2).div((float) Math.PI).invert().mul(0.7f)
                .add(0.3f);
        ChannelPainter o = (x,
                y) -> dist.valueAt(x, y) == Float.POSITIVE_INFINITY ? 0 : 1;

        ImagePainter painter = ImagePainter.hsb(hue, s, b, o);
        return PanoramaRenderer.renderPanorama(panorama, painter);
    }

    /**
     * The property of the panorama parameters
     * 
     * @return the property
     */
    public ObjectProperty<PanoramaUserParameters> parametersProperty() {
        return parameters;
    }

    /**
     * The parameters of the panorama
     * 
     * @return the parameters
     */
    public PanoramaUserParameters getParameters() {
        return parametersProperty().get();
    }

    /**
     * Change the parameters of the panorama
     * 
     * @param newParameters
     *            the new parameters
     */
    public void setParameters(PanoramaUserParameters newParameters) {
        parametersProperty().set(newParameters);
    }

    /**
     * The property of the panorama
     * 
     * @return the property
     */
    public ReadOnlyObjectProperty<Panorama> panoramaProperty() {
        return panorama;
    }

    /**
     * The panorama
     * 
     * @return the panorama
     */
    public Panorama getPanorama() {
        return panoramaProperty().get();
    }

    /**
     * The property of the image
     * 
     * @return the property
     */
    public ReadOnlyObjectProperty<Image> imageProperty() {
        return image;
    }

    /**
     * The image
     * 
     * @return the image
     */
    public Image getImage() {
        return imageProperty().get();
    }

    /**
     * An observable list of the labels
     * 
     * @return the list
     */
    public ObservableList<Node> getLabels() {
        return unmodifiableLabels;
    }

}
