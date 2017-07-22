package com.capgemini.chess.algorithms.data;

import static org.junit.Assert.*;

import org.junit.Test;

public class CoordinateTests {

	@Test
	public void shouldBeEqualWhenTestedAgainstSelf() {
		// given
		Coordinate coordinate = new Coordinate(3,5);

		// when
		boolean equal = coordinate.equals(coordinate);
		
		// then
		assertTrue(equal);
	}
	
	@Test
	public void shouldBeEqualWhenTestedAgainstNull() {
		// given
		Coordinate coordinate = new Coordinate(3,5);

		// when
		boolean equal = coordinate.equals(null);
		
		// then
		assertFalse(equal);
	}
	
	@Test
	public void shouldReturnProperValueWithHashcode() {
		// given
		Coordinate coordinate = new Coordinate(3,5);

		// when
		int hashcode = coordinate.hashCode();
		
		// then
		assertEquals(1059,hashcode);
	}
	

}
