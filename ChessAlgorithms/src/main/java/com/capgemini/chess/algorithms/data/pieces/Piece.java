package com.capgemini.chess.algorithms.data.pieces;

import java.util.ArrayList;

import com.capgemini.chess.algorithms.data.Coordinate;
import com.capgemini.chess.algorithms.data.Path;
import com.capgemini.chess.algorithms.data.enums.Color;
import com.capgemini.chess.algorithms.data.enums.PieceType;

public abstract class Piece {

	private final PieceType type;
    protected final Color color;
    protected boolean movedThisGame;

    public boolean isMovedThisGame() {
		return movedThisGame;
	}

	public void setMovedThisGame(boolean movedThisGame) {
		this.movedThisGame = movedThisGame;
	}

	Piece(PieceType type, Color color) {
        this.type = type;
        this.color = color;
    }

	public PieceType getType() {
		return type;
	}

	public Color getColor() {
		return color;
	}
	
	public abstract ArrayList<Coordinate> possibleStartCoordinates(Color color);
	
	public abstract ArrayList<Path> getMovePaths();
	public abstract ArrayList<Path> getCapturePaths();

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((color == null) ? 0 : color.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Piece other = (Piece) obj;
		if (color != other.color)
			return false;
		if (type != other.type)
			return false;
		return true;
	}
	
}
