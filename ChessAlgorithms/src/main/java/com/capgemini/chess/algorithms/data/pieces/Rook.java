package com.capgemini.chess.algorithms.data.pieces;

import java.util.ArrayList;

import com.capgemini.chess.algorithms.data.Coordinate;
import com.capgemini.chess.algorithms.data.Path;
import com.capgemini.chess.algorithms.data.enums.Color;
import com.capgemini.chess.algorithms.data.enums.PieceType;

public class Rook extends Piece {
	
	public Rook(Color color){
		super(PieceType.ROOK, color);
	}
	
	public ArrayList<Path> getMovePaths(){
		ArrayList<Path> paths = new ArrayList<Path>();
		
		paths.add(new Path(-1,0,true));
		paths.add(new Path(0,-1,true));
		paths.add(new Path(0,1,true));
		paths.add(new Path(1,0,true));
		
		return paths;
	}
	
	public ArrayList<Path> getCapturePaths(){		
		return getMovePaths();
	}

	public ArrayList<Coordinate> possibleStartCoordinates(Color color) {
		ArrayList<Coordinate> possibleStartCoordinates = new ArrayList<Coordinate>();
		if(color==color.BLACK){
			possibleStartCoordinates.add(new Coordinate(0,7));
			possibleStartCoordinates.add(new Coordinate(7,7));
		} else {
			possibleStartCoordinates.add(new Coordinate(0,0));
			possibleStartCoordinates.add(new Coordinate(7,0));
		}
		return possibleStartCoordinates;
	}
}
