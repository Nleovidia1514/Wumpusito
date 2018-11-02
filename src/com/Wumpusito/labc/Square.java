package com.Wumpusito.labc;

public class Square {
	boolean hasWumpus;
	boolean hasGold;
	boolean hasPit;
	boolean hasBreeze;
	boolean hasStench;
	boolean hasGlitter;
	boolean hasPlayer;
	boolean isSafe;
	boolean visited;
	boolean unreachable;
	int pitRisk;
	int wumpusRisk;
	int x,y;
	int timesVisited;
	Square[] neighbors = new Square[4];
	
	public Square(int x,int y) {
		this.x = x;
		this.y = y;
		this.hasWumpus = false;
		this.hasGold = false;
		this.hasPit = false;
		this.hasBreeze = false;
		this.hasStench = false;
		this.hasPlayer = false;
		this.hasGlitter = false;
		this.isSafe = false;
		this.pitRisk = 0;
		this.wumpusRisk = 0;
		this.visited = false;
		this.unreachable = false;
		this.timesVisited = 0;
	}
	
	public void neighbors(Square[][] square) {
		neighbors[Board.NORTH]=square[x+1][y];
		neighbors[Board.EAST] =square[x][y+1];
		neighbors[Board.SOUTH]=square[x-1][y];
		neighbors[Board.WEST] =square[x][y-1];
	}
	
	public Square[] getNeighbors() {
		return neighbors;
	}
	
	public void killWumpus() {
		this.hasWumpus = false;
		for (Square idk : this.getNeighbors())
			idk.hasStench = false;
	}

}
