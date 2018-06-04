package ch.epfl.alpano.gui;

/**
 * Some predefined panoramas
 *
 * @author Mathieu Chevalley (274698)
 * @author Louis Amaudruz (271808)
 *
 */
public interface PredefinedPanoramas {
    public static final int MAX_DISTANCE = 300;
    public static final int WIDTH = 2500;
    public static final int HEIGHT = 800;
    public static final int SUPER_SAMPLING_EXPONENT = 0;

    PanoramaUserParameters NIESEN = new PanoramaUserParameters(76_500, 467_300,
            600, 180, 110, MAX_DISTANCE, WIDTH, HEIGHT,
            SUPER_SAMPLING_EXPONENT);
    PanoramaUserParameters JURA_ALPS = new PanoramaUserParameters(68_087,
            470_085, 1380, 162, 27, MAX_DISTANCE, WIDTH, HEIGHT,
            SUPER_SAMPLING_EXPONENT);
    PanoramaUserParameters MOUNT_RACINE = new PanoramaUserParameters(68_200,
            470_200, 1500, 135, 45, MAX_DISTANCE, WIDTH, HEIGHT,
            SUPER_SAMPLING_EXPONENT);
    PanoramaUserParameters FINSTERAARHORN = new PanoramaUserParameters(81_260,
            465_374, 4300, 205, 20, MAX_DISTANCE, WIDTH, HEIGHT,
            SUPER_SAMPLING_EXPONENT);
    PanoramaUserParameters SAUVABELIN_TOUR = new PanoramaUserParameters(66_385,
            465_353, 700, 135, 100, MAX_DISTANCE, WIDTH, HEIGHT,
            SUPER_SAMPLING_EXPONENT);
    PanoramaUserParameters PELICAN_BEACH = new PanoramaUserParameters(65_728,
            465_132, 380, 135, 60, MAX_DISTANCE, WIDTH, HEIGHT,
            SUPER_SAMPLING_EXPONENT);
}
