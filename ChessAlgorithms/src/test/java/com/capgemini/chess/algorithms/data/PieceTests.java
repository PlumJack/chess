package com.capgemini.chess.algorithms.data;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

import com.capgemini.chess.algorithms.data.enums.Color;
import com.capgemini.chess.algorithms.data.pieces.King;
import com.capgemini.chess.algorithms.data.pieces.Piece;
import com.capgemini.chess.algorithms.data.pieces.Queen;
import com.capgemini.chess.algorithms.data.pieces.Rook;


public class PieceTests {

	@Test
	public void shouldNotBeEqualWhenDifferentColor() {
		// given
		Piece whiteKing = new King(Color.WHITE);
		Piece blackKing = new King(Color.BLACK);

		// when
		boolean equal = whiteKing.equals(blackKing);
		
		// then
		assertFalse(equal);
	}
	
	@Test
	public void shouldNotBeEqualWhenDifferentPieceType() {
		// given
		Piece whiteKing = new King(Color.WHITE);
		Piece whiteQueen = new Queen(Color.WHITE);

		// when
		boolean equal = whiteKing.equals(whiteQueen);
		
		// then
		assertFalse(equal);
	}
	
	@Test
	public void shouldBeEqualWhenSameColorAndPieceType() {
		// given
		Piece blackQueen = new Queen(Color.BLACK);
		Piece blackQueen2 = new Queen(Color.BLACK);

		// when
		boolean equal = blackQueen.equals(blackQueen2);
		
		// then
		assertTrue(equal);
	}

	@Test
	public void shouldBeEqualWhenSelf() {
		// given
		Piece blackQueen = new Queen(Color.BLACK);

		// when
		boolean equal = blackQueen.equals(blackQueen);
		
		// then
		assertTrue(equal);
	}
	
	@Test
	public void shouldBeNotEqualWhenDifferentType() {
		// given
		Piece blackQueen = new Queen(Color.BLACK);
		Coordinate coordinate = new Coordinate(3,3);
		// when
		boolean equal = blackQueen.equals(coordinate);
		
		// then
		assertFalse(equal);
	}
	
	@Test
	public void shouldReturnProperPath() {
		// given
		Piece blackRook = new Rook(Color.BLACK);
		
		// when
		ArrayList<Path> capturePaths = blackRook.getCapturePaths();
		
		// then
		assertTrue(capturePaths.contains(new Path(-1,0,true)));
		assertTrue(capturePaths.contains(new Path(0,-1,true)));
		assertTrue(capturePaths.contains(new Path(0,1,true)));
		assertTrue(capturePaths.contains(new Path(1,0,true)));
	}
	
	@Test
	public void shouldNotReturnImproperPath() {
		// given
		Piece blackRook = new Rook(Color.BLACK);
		
		// when
		ArrayList<Path> capturePaths = blackRook.getCapturePaths();
		
		// then
		assertFalse(capturePaths.contains(new Path(1,0,false)));
		assertFalse(capturePaths.contains(new Path(1,1,true)));
	}
	
	
}
