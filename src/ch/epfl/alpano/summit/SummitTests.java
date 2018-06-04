package ch.epfl.alpano.summit;

import org.junit.Test;

import ch.epfl.alpano.GeoPoint;

public class SummitTests {

    private static Summit s = new Summit("Cervin", new GeoPoint(0.5, 0.6), 2450);
    
    @Test
    public void toStringTest(){
        System.out.println(s);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void wrongString(){
        Summit t = new Summit("", new GeoPoint(0.5,0.5), 123);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void wrongGeoPoint(){
        Summit t = new Summit("df", null, 123);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void wrongInt(){
        Summit t = new Summit("dcd", new GeoPoint(0.5,0.5), 0);
        Summit w = new Summit("dcd", new GeoPoint(0.5,0.5), -123);

    }
}
