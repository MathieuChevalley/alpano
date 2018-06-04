package ch.epfl.alpano;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class Interval1DTests {
	
	@Test(expected = IllegalArgumentException.class)
	public void BuilderTest() {
		Interval1D i = new Interval1D(1,0);
	}
	
	@Test
	public void containsTest() {
		Interval1D i = new Interval1D(0,1);
		Interval1D ii = new Interval1D(0,0);
		
		assertTrue(i.contains(1));
		assertTrue(i.contains(0));
		assertTrue(ii.contains(0));
		assertTrue(!(i.contains(2)));
		assertTrue(!(i.contains(-1)));
	
	}
	
	@Test
	public  void sizeTests() {
		Interval1D i = new Interval1D(0,1);
		Interval1D ii = new Interval1D(0,0);
		
		assertEquals(2, i.size(), 0);
		assertEquals(1, ii.size(), 0);
		
	}
	
	@Test
	public void IntersectionSizeTests() {
		Interval1D i = new Interval1D(0,1);
		Interval1D i2 = new Interval1D(0,0);
		Interval1D i3 = new Interval1D(2,5);
		Interval1D i4 = new Interval1D(4,5);
		Interval1D i5 = new Interval1D(10,100);
		Interval1D i6 = new Interval1D(10,30);


		assertEquals(1, i.sizeOfIntersectionWith(i2), 0);
		assertEquals(0, i2.sizeOfIntersectionWith(i3), 0);
		assertEquals(2, i3.sizeOfIntersectionWith(i4), 0);
		assertEquals(21, i5.sizeOfIntersectionWith(i6), 0);
		assertEquals(21, i6.sizeOfIntersectionWith(i5), 0);
		assertEquals(i6.size(), i6.sizeOfIntersectionWith(i6), 0);



		
	}
	
	@Test
	public void isUnionableTests() {
		
		Interval1D i = new Interval1D(0,1);
		Interval1D i2 = new Interval1D(0,0);
		Interval1D i3 = new Interval1D(2,5);
		
		assertTrue(i.isUnionableWith(i2));
		assertTrue(!(i2.isUnionableWith(i3)));
		assertTrue((i3.isUnionableWith(i)));
		assertTrue(i.isUnionableWith(i) && i2.isUnionableWith(i2) 
				&& i3.isUnionableWith(i3));
		
		
	}
	
	@Test
	public void EqualsOverrideTests() {
		Interval1D i = new Interval1D(0,1);
		Interval1D i2 = new Interval1D(0,0);
		Interval1D i3 = new Interval1D(2,5);
		Interval1D i4 = new Interval1D(2,5);

		
		assertTrue(!i.equals(i2));
		assertTrue(i2.equals(i2));
		assertTrue(!(i3.equals(i)));
		assertTrue((i3.equals(i4)));
		assertTrue(!i.equals("s"));
		
		}
	
	@Test
	public void BoundingUnionTests() {
		Interval1D i = new Interval1D(0,1);
		Interval1D i2 = new Interval1D(0,0);
		Interval1D i3 = new Interval1D(2,5);
		Interval1D i4 = new Interval1D(4,5);
		Interval1D i5 = new Interval1D(10,100);
		
		assertTrue(i.boundingUnion(i2).equals(i));
		assertTrue(i2.boundingUnion(i3).equals(new Interval1D(0, 5)) && i3.boundingUnion(i2).equals(new Interval1D(0, 5)));
		assertTrue(i5.boundingUnion(i3).equals(new Interval1D(2, 100)));
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void UnionTests() {
		Interval1D i = new Interval1D(0,1);
		Interval1D i2 = new Interval1D(0,0);
		Interval1D i3 = new Interval1D(2,5);
		Interval1D i4 = new Interval1D(4,5);

		
		Interval1D g = i.union(i4);
		
		assertTrue(i.union(i3).equals(new Interval1D(0, 5)));
		assertTrue(i.union(i2).equals(new Interval1D(0,1)));
		
	}
	
	
	
	

}
