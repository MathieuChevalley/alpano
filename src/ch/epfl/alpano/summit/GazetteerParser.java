package ch.epfl.alpano.summit;

import java.io.BufferedReader;
import static ch.epfl.alpano.Preconditions.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import static java.lang.Integer.parseInt;

import ch.epfl.alpano.GeoPoint;

/**
 * Class used to read a file containing summits (cannot be instantiate)
 * 
 * @author Mathieu Chevalley (274698)
 * @author Louis Amaudruz (271808)
 */
public class GazetteerParser {

    // private builder, this class cannot be instantiated
    private GazetteerParser() {
    }

    /**
     * Reads the summits from a file
     * 
     * @param file
     *            file containing the summits
     * @return a list of summit
     * @throws IOException
     *             if input is wrongly formatted or unreadable
     */
    public static List<Summit> readSummitsFrom(File file) throws IOException {

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                new FileInputStream(file), StandardCharsets.US_ASCII))) {

            List<Summit> summits = new ArrayList<>();

            String line = "";
            // read a line
            while ((line = reader.readLine()) != null) {
                summits.add(readSummitLine(line));
            }

            return Collections.unmodifiableList(summits);
        }
    }

    // extract a summit given a line
    private static Summit readSummitLine(String line) throws IOException {

        try {

            checkArgument(!line.isEmpty() && line.length() > 36);

            // Longitude
            String longitude = line.substring(0, 9).trim();
            String[] hms = longitude.split(":");
            double longi = toRadians(parseInt(hms[0]), parseInt(hms[1]),
                    parseInt(hms[2]));

            // Latitude
            String latitude = line.substring(10, 18).trim();
            hms = latitude.split(":");
            double lati = toRadians(parseInt(hms[0]), parseInt(hms[1]),
                    parseInt(hms[2]));

            // Position using the latitude and longitude
            GeoPoint position = new GeoPoint(longi, lati);

            // elevation
            int elevation = parseInt(line.substring(20, 24).trim());

            // Name of the summit
            String name = line.substring(36).trim();

            return new Summit(name, position, elevation);
        } catch (Exception e) {
            // if exception, file is wrongly formatted
            throw new IOException("wrongly formatted");
        }

    }

    // Convert an angle in degree, minute and second to radian
    private static double toRadians(double degrees, double minutes,
            double seconds) {
        assert (degrees >= 0 && minutes >= 0 && seconds >= 0);
        double degree = degrees + minutes / 60 + seconds / 3600;

        return Math.toRadians(degree);
    }

}
