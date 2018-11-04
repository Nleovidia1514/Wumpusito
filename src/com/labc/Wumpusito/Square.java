package com.labc.Wumpusito;

public class Square {
	boolean hasWumpus, hasGold, hasPit, hasBreeze, hasStench,
	hasGlitter, hasPlayer, isSafe, visited, unreachable,
	mayHavePit, mayHaveWumpus;
	int pitRisk, wumpusRisk, x, y, timesVisited;
	float visitedRisk;
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
		this.mayHavePit = true;
		this.mayHaveWumpus = true;
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
