package ch.epfl.alpano.gui;

import ch.epfl.alpano.Panorama;
import javafx.scene.image.Image;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;;

/**
 * Tool class to render a panorama in an image
 * 
 * @author Louis Amaudruz (271808)
 * @author Mathieu Chevalley (274698)
 *
 */
public interface PanoramaRenderer {

    /**
     * Render a panorama in an image
     * 
     * @param panorama
     *            the panorama to be rendered
     * @param painter
     *            the image painter
     * @return the resulting image
     * @see javafx.scene.image#Image
     */
    public static Image renderPanorama(Panorama panorama,
            ImagePainter painter) {

        WritableImage image = new WritableImage(panorama.parameters().width(),
                panorama.parameters().height());
        PixelWriter writer = image.getPixelWriter();

        // drawing of the image using the painter
        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                writer.setColor(x, y, painter.colorAt(x, y));
            }
        }

        return image;
    }
}
