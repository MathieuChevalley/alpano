package ch.epfl.alpano;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class Interval2DTests {
	
	@Test(expected = NullPointerException.class)
	public void BuilderExceptionTests() {
		Interval1D g = null;
		Interval1D j = new Interval1D(0, 15);
		
		Interval2D k = new Interval2D(g,j);
		
	}
	
	@Test
	public void containsTests() {
		Interval1D i = new Interval1D(0, 15);
		Interval1D i1 = new Interval1D(0, 0);
		Interval1D i2 = new Interval1D(16, 20);
		
		Interval2D ii = new Interval2D(i, i1);
		Interval2D ii1 = new Interval2D(i, i2);
		Interval2D ii2 = new Interval2D(i2, i1);
		
		assertTrue(ii.contains(0, 0));
		assertTrue(!ii.contains(0, 10));
		assertTrue(ii.contains(10,0));
		assertTrue(ii1.contains(6, 18));
		assertTrue(ii2.contains(20, 0));

	}
	
	@Test
	public void sizeTests() {
		
		Interval1D i = new Interval1D(0, 15);
		Interval1D i1 = new Interval1D(0, 0);
		Interval1D i2 = new Interval1D(16, 20);
		
		Interval2D ii = new Interval2D(i, i1);
		Interval2D ii1 = new Interval2D(i, i2);
		Interval2D ii2 = new Interval2D(i2, i1);
		Interval2D ii3 = new Interval2D(i1, i1);
		
		assertEquals(16, ii.size(), 0);
		assertEquals(80 , ii1.size(), 0);
		assertEquals(5, ii2.size(), 0);
		assertEquals(1, ii3.size(), 0);
		
		
	}
	
	
	@Test 
	public void sizeOfIntersectionTest() {
		Interval1D i = new Interval1D(0, 15);
		Interval1D i1 = new Interval1D(0, 0);
		Interval1D i2 = new Interval1D(16, 20);
		
		Interval2D ii = new Interval2D(i, i1);
		Interval2D ii1 = new Interval2D(i, i2);
		Interval2D ii2 = new Interval2D(i2, i1);
		Interval2D ii3 = new Interval2D(i1, i1);
		Interval2D ii4 = new Interval2D(i1, i2);
		
		assertEquals(0, ii2.sizeOfIntersectionWith(ii1));
		assertEquals(0, ii.sizeOfIntersectionWith(ii1));
		assertEquals(1, ii3.sizeOfIntersectionWith(ii3));
		assertEquals(i2.size(), ii4.sizeOfIntersectionWith(ii1));

		
	}
	
	
	@Test 
	public void boundingUnionTests() {
		Interval1D i = new Interval1D(0, 15);
		Interval1D i1 = new Interval1D(0, 0);
		Interval1D i2 = new Interval1D(16, 20);
		Interval1D i3 = new Interval1D(10,21);
		
		Interval1D james = new Interval1D(0,20);
		Interval1D Harden = new Interval1D(0,21);

		Interval2D ii = new Interval2D(i, i1);
		Interval2D ii1 = new Interval2D(i2, i3);
		
		assertTrue(ii.boundingUnion(ii1).equals(new Interval2D(james, Harden)));
	}
	
	
	@Test
	public void EqualsTests() {
		Interval1D i = new Interval1D(0, 15);
		Interval1D i1 = new Interval1D(0, 0);
		Interval1D i2 = new Interval1D(16, 20);
		
		Interval2D ii = new Interval2D(i, i1);
		Interval2D ii1 = new Interval2D(i, i2);
		Interval2D ii2 = new Interval2D(i2, i1);
		Interval2D ii3 = new Interval2D(i1, i1);
		Interval2D ii4 = new Interval2D(i1, i2);
		Interval2D ii6 = new Interval2D(i, i1);

		
		assertTrue(!(ii.equals(ii1)));
		assertTrue(!(ii1.equals(ii2)));
		assertTrue(!(ii2.equals(ii3)));
		assertTrue(!(ii2.equals(ii4)));
		assertTrue((ii.equals(ii)));
		assertTrue((ii.equals(ii6)));
		assertTrue(!ii.equals("huiuh"));



	}
	
	
	
	

}
