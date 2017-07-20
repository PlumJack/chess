package com.capgemini.chess.algorithms.data;

public class Path {
	
	private int x;
	private int y;
	boolean repeat;
	
	public Path(int x, int y, boolean repeat) {
		super();
		this.x = x;
		this.y = y;
		this.repeat = repeat;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public boolean isRepeat() {
		return repeat;
	}

	public void setRepeat(boolean repeat) {
		this.repeat = repeat;
	}
	
	


}
