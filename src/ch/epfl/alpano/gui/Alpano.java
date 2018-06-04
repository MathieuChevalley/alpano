package ch.epfl.alpano.gui;

import javafx.util.StringConverter;

import java.io.File;
import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import ch.epfl.alpano.Azimuth;
import ch.epfl.alpano.Panorama;
import ch.epfl.alpano.PanoramaParameters;
import ch.epfl.alpano.dem.ContinuousElevationModel;
import ch.epfl.alpano.dem.DiscreteElevationModel;
import ch.epfl.alpano.dem.HgtDiscreteElevationModel;
import ch.epfl.alpano.summit.GazetteerParser;
import ch.epfl.alpano.summit.Summit;
import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import static javafx.geometry.Pos.*;
import static java.lang.Math.*;

public final class Alpano extends Application {

    /*
     * Parameters of the GUI
     */
    private static final PanoramaUserParameters FIRST_PANORAMA = PredefinedPanoramas.JURA_ALPS;
    private static final Insets GRID_PADDING = new Insets(7, 5, 5, 5);
    private static final int GRID_VERTICAL_GAP = 3;
    private static final int GRID_HORIZONTAL_GAP = 10;
    private static final Color UPDATE_NOTICE_BACKGROUND_COLOR = new Color(1, 1,
            1, 0.9);
    private static final int UPDATE_TEXT_SIZE = 40;
    private static final String UPDATE_TEXT = "Les paramètres du panorama ont changé."
            + " \nCliquez ici pour mettre le dessin à jour.";

    private final PanoramaParametersBean parametersBean;
    private final PanoramaComputerBean computerBean;
    private final ObjectProperty<String> infoText;

    public Alpano() throws Exception {
        List<Summit> summits = GazetteerParser
                .readSummitsFrom(new File("alps.txt"));
        parametersBean = new PanoramaParametersBean(
                FIRST_PANORAMA);
        computerBean = new PanoramaComputerBean(summits, createDem());

        infoText = new SimpleObjectProperty<>();

    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        BorderPane root = new BorderPane(panoPane(), null, null, paramsGrid(),
                null);
        Scene scene = new Scene(root);

        primaryStage.setTitle("Alpano");
        primaryStage.setScene(scene);
        primaryStage.show();

    }

    @SuppressWarnings("resource")
    private ContinuousElevationModel createDem() throws Exception {
        DiscreteElevationModel dem1 = new HgtDiscreteElevationModel(
                new File("N45E006.hgt")).union(
                        new HgtDiscreteElevationModel(new File("N45E007.hgt")))
                        .union(new HgtDiscreteElevationModel(
                                new File("N45E008.hgt")))
                        .union(new HgtDiscreteElevationModel(
                                new File("N45E009.hgt")));
        DiscreteElevationModel dem2 = new HgtDiscreteElevationModel(
                new File("N46E006.hgt")).union(
                        new HgtDiscreteElevationModel(new File("N46E007.hgt")))
                        .union(new HgtDiscreteElevationModel(
                                new File("N46E008.hgt")))
                        .union(new HgtDiscreteElevationModel(
                                new File("N46E009.hgt")));

        return new ContinuousElevationModel(dem1.union(dem2));
    }

    private GridPane paramsGrid() {

        GridPane grid = new GridPane();

        /*
         * Create the labels
         */
        Label latitudeLabel = newLabel("Latitude (°) :");
        Label longitudeLabel = newLabel("Longitude (°) :");
        Label altitudeLabel = newLabel("Altitude (m) :");
        Label azimuthLabel = newLabel("Azimuth (°) :");
        Label angleLabel = newLabel("Angle de vue (°) :");
        Label visibilityLabel = newLabel("Visibilité (km) :");
        Label widthLabel = newLabel("Largeur (px) :");
        Label heightLabel = newLabel("Hauteur (px) :");
        Label superSamplingLabel = newLabel("Suréchantillonage :");

        /*
         * Place the labels in the grid
         */
        grid.addColumn(0, latitudeLabel, azimuthLabel, widthLabel);
        grid.addColumn(2, longitudeLabel, angleLabel, heightLabel);
        grid.addColumn(4, altitudeLabel, visibilityLabel, superSamplingLabel);

        /*
         * Create all the text fields
         */
        TextField latitudeText = createTextField(7);
        TextField longitudeText = createTextField(7);
        TextField altitudeText = createTextField(4);
        TextField azimuthText = createTextField(3);
        TextField angleText = createTextField(3);
        TextField visibilityText = createTextField(3);
        TextField widthText = createTextField(4);
        TextField heightText = createTextField(4);

        StringConverter<Integer> stringConverter4 = new FixedPointStringConverter(
                4);
        StringConverter<Integer> stringConverter0 = new FixedPointStringConverter(
                0);

        addTextFormatter(latitudeText, stringConverter4,
                parametersBean.observerLatitudeProperty());
        addTextFormatter(longitudeText, stringConverter4,
                parametersBean.observerLongitudeProperty());
        addTextFormatter(altitudeText, stringConverter0,
                parametersBean.observerElevationProperty());
        addTextFormatter(azimuthText, stringConverter0,
                parametersBean.centerAzimuthProperty());
        addTextFormatter(angleText, stringConverter0,
                parametersBean.horizontalFieldOfViewProperty());
        addTextFormatter(visibilityText, stringConverter0,
                parametersBean.maxDistanceProperty());
        addTextFormatter(widthText, stringConverter0,
                parametersBean.widthProperty());
        addTextFormatter(heightText, stringConverter0,
                parametersBean.heightProperty());

        /*
         * Create the choice box with options 'non', '2x' and '4x'
         */
        ObservableList<Integer> boxList = FXCollections
                .observableArrayList(Arrays.asList(0, 1, 2));
        ChoiceBox<Integer> superSampling = new ChoiceBox<>(boxList);

        StringConverter<Integer> converter = new LabeledListStringConverter(
                "non", "2x", "4x");
        superSampling.setConverter(converter);
        superSampling.valueProperty().bindBidirectional(
                parametersBean.superSamplingExponentProperty());

        /*
         * Initialize the text area with information about the point under the
         * mouse
         */
        TextArea informationTextArea = new TextArea();
        informationTextArea.setEditable(false);
        informationTextArea.setPrefRowCount(2);
        informationTextArea.textProperty().bind(infoText);

        /*
         * Place the fields
         */
        grid.addColumn(1, latitudeText, azimuthText, widthText);
        grid.addColumn(3, longitudeText, angleText, heightText);
        grid.addColumn(5, altitudeText, visibilityText, superSampling);
        grid.add(informationTextArea, 6, 0, 1, 3);

        /*
         * Add style to the grid
         */
        grid.setHgap(GRID_HORIZONTAL_GAP);
        grid.setVgap(GRID_VERTICAL_GAP);
        grid.setAlignment(CENTER);
        grid.setPadding(GRID_PADDING);

        return grid;
    }

    private void addTextFormatter(TextField textField,
            StringConverter<Integer> stringConverter,
            ObjectProperty<Integer> parameterProperty) {

        TextFormatter<Integer> textFormatter = new TextFormatter<>(
                stringConverter);
        textFormatter.valueProperty().bindBidirectional(parameterProperty);
        textField.setTextFormatter(textFormatter);
    }

    private Label newLabel(String labelText) {
        Label l = new Label(labelText);
        GridPane.setHalignment(l, HPos.RIGHT);
        return l;
    }

    private TextField createTextField(int prefColumn) {
        TextField field = new TextField();
        field.setPrefColumnCount(prefColumn);
        field.setAlignment(CENTER_RIGHT);
        return field;
    }

    private StackPane panoPane() {

        StackPane panoGroup = new StackPane(panoView(), labelsPane());

        ScrollPane panoScrollPane = new ScrollPane(panoGroup);

        StackPane panoPane = new StackPane(panoScrollPane, updateNotice());

        return panoPane;
    }

    private StackPane updateNotice() {

        Text text = new Text(UPDATE_TEXT);
        text.setFont(new Font(UPDATE_TEXT_SIZE));
        text.setTextAlignment(TextAlignment.CENTER);

        StackPane updateNotice = new StackPane(text);
        // Background is white but transparent
        updateNotice.setBackground(new Background(
                new BackgroundFill(UPDATE_NOTICE_BACKGROUND_COLOR,
                        CornerRadii.EMPTY, Insets.EMPTY)));
        updateNotice.visibleProperty().bind(computerBean.parametersProperty()
                .isNotEqualTo(parametersBean.parametersProperty()));

        // if clicked, the parameters, and thus the output image, are updated
        updateNotice.setOnMouseClicked(x -> computerBean
                .setParameters(parametersBean.parametersProperty().get()));

        return updateNotice;
    }

    private Pane labelsPane() {

        Pane labelsPane = new Pane();

        labelsPane.prefWidthProperty().bind(parametersBean.widthProperty());
        labelsPane.prefHeightProperty().bind(parametersBean.heightProperty());

        Bindings.bindContent(labelsPane.getChildren(),
                computerBean.getLabels());

        labelsPane.setMouseTransparent(true); // User can only interact with the
                                              // panorama image

        return labelsPane;
    }

    private ImageView panoView() {

        ImageView imageView = new ImageView();

        imageView.imageProperty().bind(computerBean.imageProperty());
        imageView.fitWidthProperty().bind(parametersBean.widthProperty());

        imageView.setPreserveRatio(true);
        imageView.setSmooth(true);

        /*
         * Open an online map with the parameters of the mouse
         */
        imageView.setOnMouseClicked(e -> onMouseClicked(e));

        /*
         * Update information about the point under the mouse
         */
        imageView.setOnMouseMoved(e -> onMouseMoved(e));

        return imageView;
    }

    private void onMouseMoved(MouseEvent e) {

        double longitude, latitude, elevation, distance, altitude, azimuth;

        PanoramaUserParameters computerParameters = computerBean
                .getParameters();

        double resize = pow(2, computerParameters.superSamplingExponent());

        double x = e.getX() * resize;
        double y = e.getY() * resize;

        PanoramaParameters panoramaParameters = computerParameters
                .panoramaParameters();

        altitude = panoramaParameters.altitudeForY(y);
        azimuth = panoramaParameters.azimuthForX(x);

        int indexX = (int) round(x);
        int indexY = (int) round(y);

        Panorama panorama = computerBean.getPanorama();

        distance = panorama.distanceAt(indexX, indexY);
        latitude = panorama.latitudeAt(indexX, indexY);
        longitude = panorama.longitudeAt(indexX, indexY);
        elevation = panorama.elevationAt(indexX, indexY);

        Locale l = null;

        String s = String.format(l,
                "Position : %.4f°N %.4f°E" + "\nDistance : %.1f km"
                        + "\nAltitude : %.0f m" + "\nAzimuth : %.1f° ("
                        + Azimuth.toOctantString(azimuth, "N", "E", "S", "W")
                        + ")   Elevation : %.1f°",
                toDegrees(latitude), toDegrees(longitude), distance / 1000,
                elevation, toDegrees(azimuth), toDegrees(altitude));

        infoText.set(s);
    }

    private void onMouseClicked(MouseEvent e) throws Error {

        double resize = pow(2,
                computerBean.getParameters().superSamplingExponent());

        int x = (int) round(e.getX() * resize);
        int y = (int) round(e.getY() * resize);

        Panorama panorama = computerBean.getPanorama();

        double latitude = panorama.latitudeAt(x, y);
        double longitude = panorama.longitudeAt(x, y);

        Locale l = null;

        String qy = String.format(l, "mlat=%f&mlon=%f", toDegrees(latitude),
                toDegrees(longitude));
        String fg = String.format(l, "map=15/%f/%f", toDegrees(latitude),
                toDegrees(longitude));

        try {
            URI osmURI = new URI("http", "www.openstreetmap.org", "/", qy, fg);
            java.awt.Desktop.getDesktop().browse(osmURI);
        } catch (Exception e1) {
            throw new Error(e1);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

}
