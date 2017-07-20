package com.capgemini.chess.algorithms.data;

import java.util.ArrayList;

import com.capgemini.chess.algorithms.data.enums.Color;
import com.capgemini.chess.algorithms.data.enums.PieceType;

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
		if(!movedThisGame) {
			paths.add(new Path(0,2*colorModifier,false));
		}
		
		return paths;
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

}
