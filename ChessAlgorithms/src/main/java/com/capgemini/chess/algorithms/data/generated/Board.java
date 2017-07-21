package com.capgemini.chess.algorithms.data.generated;

import java.util.ArrayList;
import java.util.List;

import com.capgemini.chess.algorithms.data.Coordinate;
import com.capgemini.chess.algorithms.data.Move;
import com.capgemini.chess.algorithms.data.Piece;
import com.capgemini.chess.algorithms.data.enums.BoardState;

/**
 * Board representation.
 * Board objects are generated based on move history.
 * 
 * @author Michal Bejm
 *
 */
public class Board {
	
	public static final int SIZE = 8;
	
	private Piece[][] pieces = new Piece[SIZE][SIZE];
	private List<Move> moveHistory = new ArrayList<>();
	private BoardState state;
	
	public Board() {
	}

	public Board(Piece[][] pieces, List<Move> moveHistory, BoardState state) {
		this.pieces = pieces;
		this.moveHistory = moveHistory;
		this.state = state;
	}


	public List<Move> getMoveHistory() {
		return moveHistory;
	}

	public Piece[][] getPieces() {
		return pieces;
	}

	public BoardState getState() {
		return state;
	}

	public void setState(BoardState state) {
		this.state = state;
	}
	
	/**
	 * Sets chess piece on board based on given coordinates
	 * 
	 * @param piece chess piece
	 * @param board chess board
	 * @param coordinate given coordinates
	 */
	public void setPieceAt(Piece piece, Coordinate coordinate) {
		
		if(piece!=null){
			ArrayList<Coordinate> possibleStartCoordinates = piece.possibleStartCoordinates(piece.getColor());
			
			if(!possibleStartCoordinates.contains(coordinate)){
				piece.setMovedThisGame(true);
			}
		}
		
		
		
		pieces[coordinate.getX()][coordinate.getY()] = piece;
	}
	
	/**
	 * Gets chess piece from board based on given coordinates
	 * 
	 * @param coordinate given coordinates
	 * @return chess piece
	 */
	public Piece getPieceAt(Coordinate coordinate) {
		return pieces[coordinate.getX()][coordinate.getY()];
	}
	
	public Board generateCopy(){
		Piece[][] piecesCopy = new Piece[SIZE][SIZE];
		for(int i=0; i<pieces.length; i++){
			for(int j=0; j<pieces[i].length; j++){
				piecesCopy[i][j]=pieces[i][j];
			}
		}		  		    
		List<Move> moveHistoryCopy = new ArrayList<Move>();
		for(Move move: moveHistory){
			moveHistoryCopy.add(move);
		}
		BoardState stateCopy = state;
		Board boardCopy = new Board(piecesCopy,moveHistoryCopy,stateCopy);
		return boardCopy;
	}
	
	
	public ArrayList<Move> getMoveHistoryCopy() {
		ArrayList<Move> moveHistoryCopy = new ArrayList<Move>();
		for(Move move: moveHistory){
			moveHistoryCopy.add(move);
		}
		return moveHistoryCopy;
	}
}
