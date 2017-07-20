package com.capgemini.chess.algorithms.data;

import java.util.ArrayList;

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
	
	public abstract ArrayList<Path> getMovePaths();
	public abstract ArrayList<Path> getCapturePaths();
	
}
