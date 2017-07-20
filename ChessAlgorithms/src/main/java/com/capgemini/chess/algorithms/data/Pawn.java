package com.capgemini.chess.algorithms.data;

import java.util.ArrayList;

import com.capgemini.chess.algorithms.data.enums.Color;
import com.capgemini.chess.algorithms.data.enums.PieceType;
import com.capgemini.chess.algorithms.data.generated.Board;

public class Pawn extends Piece {
	
	public Pawn(Color color){
		super(PieceType.PAWN, color);
	}
	
	public ArrayList<Path> getMovePaths(){
		ArrayList<Path> paths = new ArrayList<Path>();
		
		int colorModifier = 1;
		if(color == Color.BLACK){
			colorModifier = -1;
		}
		paths.add(new Path(0,1*colorModifier,false));
		
		/*
		if(!movedThisGame) {
			paths.add(new Path(0,2*colorModifier,false));
		}
		*/
		return paths;
	}
	
	public ArrayList<Path> getDoubleMovePaths(){
		ArrayList<Path> paths = new ArrayList<Path>();
		
		int colorModifier = 1;
		if(color == Color.BLACK){
			colorModifier = -1;
		}
		if(!movedThisGame) {
			paths.add(new Path(0,1*colorModifier,false));
			paths.add(new Path(0,2*colorModifier,false));
		}
		
		return paths;
	}
	
	public ArrayList<Path> getEnPassantPaths(){
		return getCapturePaths();
	}
	
	public ArrayList<Path> getCapturePaths(){		
		ArrayList<Path> paths = new ArrayList<Path>();
		
		int colorModifier = 1;
		if(color == Color.BLACK){
			colorModifier = -1;
		}
		
		paths.add(new Path(-1,1*colorModifier,false));
		paths.add(new Path(1,1*colorModifier,false));
		
		return paths;	
	}
	
	@Override
	public ArrayList<Coordinate> possibleStartCoordinates(Color color) {
		ArrayList<Coordinate> possibleStartCoordinates = new ArrayList<Coordinate>();
		if(color==color.BLACK){
			for (int x = 0; x < 8; x++) {
				possibleStartCoordinates.add(new Coordinate(x, 6));
			}
		} else {
			for (int x = 0; x < 8; x++) {
				possibleStartCoordinates.add(new Coordinate(x, 1));
			}
		}
		return possibleStartCoordinates;
	}
	

}
