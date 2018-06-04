package ch.epfl.alpano.gui;

import javafx.scene.paint.Color;

/**
 * Functional interface that creates an image painter
 * 
 * @author Louis Amaudruz (271808)
 * @author Mathieu Chevalley (274698)
 */

@FunctionalInterface
public interface ImagePainter {

    /**
     * Give the color at a given index
     * 
     * @param x
     *            the first index
     * @param y
     *            the second index
     * @return the color at this index
     * @see javafx.scene.paint#Color
     */
    public abstract Color colorAt(int x, int y);

    /**
     * Give an image painter given the hue, the saturation, the brightness and
     * the opacity channels at a point
     * 
     * @param hue
     *            the hue channel
     * @param saturation
     *            the saturation channel
     * @param brightness
     *            the brightness channel
     * @param opacity
     *            the opacity channel
     * @return the image painter
     * @see ChannelPainter
     */
    public static ImagePainter hsb(ChannelPainter hue,
            ChannelPainter saturation, ChannelPainter brightness,
            ChannelPainter opacity) {
        return (x, y) -> Color.hsb(hue.valueAt(x, y), saturation.valueAt(x, y),
                brightness.valueAt(x, y), opacity.valueAt(x, y));
    }

    /**
     * Give an image painter given a gray channel and an opacity channel
     * 
     * @param gray
     *            the gray channel
     * @param opacity
     *            the opacity channel
     * @return the gray Image painter
     * @see ChannelPainter
     */
    public static ImagePainter gray(ChannelPainter gray,
            ChannelPainter opacity) {
        return (x, y) -> Color.gray(gray.valueAt(x, y), opacity.valueAt(x, y));
    }
}
