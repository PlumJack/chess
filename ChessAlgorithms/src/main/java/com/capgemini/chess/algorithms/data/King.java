package com.capgemini.chess.algorithms.data;

import java.util.ArrayList;

import com.capgemini.chess.algorithms.data.enums.Color;
import com.capgemini.chess.algorithms.data.enums.PieceType;

public class King extends Piece {
	
	public King(Color color){
		super(PieceType.KING, color);
	}
	
	public ArrayList<Path> getMovePaths(){
		ArrayList<Path> paths = new ArrayList<Path>();
		
		for(int i=-1; i<2; i++){
			for(int j=-1; j<2; j++){
				if(i!=0 && j !=0) paths.add(new Path(i,j,false));
			}
		}
		//paths.add(new Path(-2,0,false));
		//paths.add(new Path(2,0,false));
		return paths;
	}
	
	public ArrayList<Path> getCapturePaths(){		
		return getMovePaths();
	}
	
	public ArrayList<Path> getCastlingPaths(){
		ArrayList<Path> paths = new ArrayList<Path>();
		
		paths.add(new Path(-2,0,false));
		paths.add(new Path(2,0,false));
		
		return paths;
	}
	

	@Override
	protected ArrayList<Coordinate> possibleStartCoordinates(Color color) {
		ArrayList<Coordinate> possibleStartCoordinates = new ArrayList<Coordinate>();
		if(color==color.BLACK){
			possibleStartCoordinates.add(new Coordinate(4,7));
		} else {
			possibleStartCoordinates.add(new Coordinate(4,0));
		}
		return possibleStartCoordinates;
	}

}
