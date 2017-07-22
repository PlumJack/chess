package com.capgemini.chess.algorithms.data.pieces;

import java.util.ArrayList;

import com.capgemini.chess.algorithms.data.Coordinate;
import com.capgemini.chess.algorithms.data.Path;
import com.capgemini.chess.algorithms.data.enums.Color;
import com.capgemini.chess.algorithms.data.enums.PieceType;

public class Dragon extends Piece{

	public Dragon(Color color){
		super(PieceType.DRAGON, color);
	}
	
	public ArrayList<Path> getMovePaths(){
		ArrayList<Path> paths = new ArrayList<Path>();
		
		for(int i=-3; i<4; i++){
			for(int j=-3; j<4; j++){
				if(i!=0 || j !=0) paths.add(new Path(i,j,false));
			}
		}
		return paths;
	}
	
	public ArrayList<Path> getCapturePaths(){		
		ArrayList<Path> paths = new ArrayList<Path>();
		
		for(int i=-1; i<2; i++){
			for(int j=-1; j<2; j++){
				if(i!=0 || j !=0) paths.add(new Path(i,j,false));
			}
		}
		return paths;
	}
	
	@Override
	public ArrayList<Coordinate> possibleStartCoordinates(Color color) {
		ArrayList<Coordinate> possibleStartCoordinates = new ArrayList<Coordinate>();
		if(color==Color.BLACK){
			possibleStartCoordinates.add(new Coordinate(4,5));
		} else {
			possibleStartCoordinates.add(new Coordinate(4,2));
		}
		return possibleStartCoordinates;
	}
	
}
