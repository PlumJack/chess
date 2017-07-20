package com.capgemini.chess.algorithms.data;

import java.util.ArrayList;

import com.capgemini.chess.algorithms.data.enums.Color;
import com.capgemini.chess.algorithms.data.enums.PieceType;

public class Bishop extends Piece {
	
	public Bishop(Color color){
		super(PieceType.BISHOP, color);
	}

	public ArrayList<Path> getMovePaths(){
		ArrayList<Path> paths = new ArrayList<Path>();
		
		paths.add(new Path(-1,-1,true));
		paths.add(new Path(-1,1,true));
		paths.add(new Path(1,-1,true));
		paths.add(new Path(1,1,true));
		
		
		return paths;
	}
	
	public ArrayList<Path> getCapturePaths(){		
		return getMovePaths();
	}
	
}
