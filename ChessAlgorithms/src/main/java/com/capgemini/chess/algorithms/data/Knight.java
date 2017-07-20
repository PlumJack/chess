package com.capgemini.chess.algorithms.data;

import java.util.ArrayList;

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

}
