package com.capgemini.chess.algorithms.data;

import com.capgemini.chess.algorithms.data.Coordinate;
import com.capgemini.chess.algorithms.data.Piece;

public class PieceCoordinate {
	
	Piece piece;
	Coordinate position;
	
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
