package com.capgemini.chess.algorithms.data;

import static org.junit.Assert.*;

import org.junit.Test;

import com.capgemini.chess.algorithms.data.enums.Color;
import com.capgemini.chess.algorithms.data.generated.Board;
import com.capgemini.chess.algorithms.data.pieces.Piece;
import com.capgemini.chess.algorithms.data.pieces.Rook;

public class BoardTests {

	@Test
	public void shouldGetNullFromEmptyField() {
		// given
		Board board = new Board();
		
		// when
		Piece piece = board.getPieceAt(new Coordinate(0, 0));
		
		// then

		assertEquals(null, piece);
	}
	
	@Test
	public void shouldGetPieceFromNotEmptyField() {
		// given
		Board board = new Board();
		
		// when
		board.setPieceAt(new Rook(Color.WHITE), new Coordinate(0, 0));
		Piece piece = board.getPieceAt(new Coordinate(0, 0));
		
		// then

		assertEquals(new Rook(Color.WHITE), piece);
	}

}
