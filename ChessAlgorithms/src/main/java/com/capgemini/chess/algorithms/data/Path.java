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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (repeat ? 1231 : 1237);
		result = prime * result + x;
		result = prime * result + y;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Path other = (Path) obj;
		if (repeat != other.repeat)
			return false;
		if (x != other.x)
			return false;
		if (y != other.y)
			return false;
		return true;
	}
	
	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public boolean isRepeat() {
		return repeat;
	}

}
