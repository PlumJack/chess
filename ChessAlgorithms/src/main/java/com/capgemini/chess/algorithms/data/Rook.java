package com.capgemini.chess.algorithms.data;

import java.util.ArrayList;

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

}
