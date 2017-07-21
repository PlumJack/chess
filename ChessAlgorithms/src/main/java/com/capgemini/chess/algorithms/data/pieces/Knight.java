package com.capgemini.chess.algorithms.data.pieces;

import java.util.ArrayList;

import com.capgemini.chess.algorithms.data.Coordinate;
import com.capgemini.chess.algorithms.data.Path;
import com.capgemini.chess.algorithms.data.enums.Color;
import com.capgemini.chess.algorithms.data.enums.PieceType;

public class Knight extends Piece {
	
	public Knight(Color color){
		super(PieceType.KNIGHT, color);
	}
	
	public ArrayList<Path> getMovePaths(){
		ArrayList<Path> paths = new ArrayList<Path>();
		
		paths.add(new Path(-2,-1,false));
		paths.add(new Path(-2,1,false));
		paths.add(new Path(-1,-2,false));
		paths.add(new Path(-1,2,false));
		paths.add(new Path(1,-2,false));
		paths.add(new Path(1,2,false));
		paths.add(new Path(2,-1,false));
		paths.add(new Path(2,1,false));
		
		return paths;
	}
	
	public ArrayList<Path> getCapturePaths(){		
		return getMovePaths();
	}
	
	public ArrayList<Coordinate> possibleStartCoordinates(Color color) {
		ArrayList<Coordinate> possibleStartCoordinates = new ArrayList<Coordinate>();
		if(color==color.BLACK){
			possibleStartCoordinates.add(new Coordinate(1,7));
			possibleStartCoordinates.add(new Coordinate(6,7));
		} else {
			possibleStartCoordinates.add(new Coordinate(1,0));
			possibleStartCoordinates.add(new Coordinate(6,0));
		}
		return possibleStartCoordinates;
	}

}
