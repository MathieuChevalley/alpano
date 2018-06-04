package ch.epfl.alpano.gui;

import java.io.File;
import java.util.List;



import ch.epfl.alpano.dem.ContinuousElevationModel;
import ch.epfl.alpano.dem.HgtDiscreteElevationModel;
import ch.epfl.alpano.summit.GazetteerParser;
import ch.epfl.alpano.summit.Summit;

import javafx.scene.Node;


    import javafx.application.Application;
    import javafx.application.Platform;
    import javafx.stage.Stage;

    public final class LabelizerTest extends Application {
        public static void main(String[] args) {
            launch(args);
        }

        @Override
        public void start(Stage primaryStage) throws Exception {
            ContinuousElevationModel cem = new ContinuousElevationModel(new HgtDiscreteElevationModel(new File("N46E007.hgt")));
            List<Summit> summit = GazetteerParser.readSummitsFrom(new File("alps.txt"));
            System.out.println(summit.isEmpty());
            Labelizer labelizer = new Labelizer(cem, summit);
            //List<Labelizer.VisibleSummit> l = labelizer.visibleSummits(PredefinedPanoramas.NIESEN.panoramaDisplayParameters());
            List<Node> l = labelizer.labels(PredefinedPanoramas.NIESEN.panoramaDisplayParameters());
            l.forEach(System.out::println);
           // l.forEach(x -> System.out.println(x.getSummit() + " " + x.getX() + " " + x.getY()));
            //System.out.println(l.isEmpty());
            System.out.println(l.size()/2);
            Platform.exit();
        }
    }
