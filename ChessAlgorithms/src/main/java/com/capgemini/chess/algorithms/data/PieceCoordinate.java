package com.capgemini.chess.algorithms.data;

import com.capgemini.chess.algorithms.data.Coordinate;
import com.capgemini.chess.algorithms.data.pieces.Piece;

public class PieceCoordinate {
	
	private Piece piece;
	private Coordinate position;
	
	public PieceCoordinate(Piece piece, Coordinate position) {
		this.piece = piece;
		this.position = position;
	}

	public Piece getPiece() {
		return piece;
	}

	public Coordinate getPosition() {
		return position;
	}	

}
