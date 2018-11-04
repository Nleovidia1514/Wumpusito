package com.labc.Wumpusito;

import java.util.Random;

public class Board {
	public static int NORTH = 0;
	public static int EAST = 1;
	public static int SOUTH = 2;
	public static int WEST = 3;
	public static int ROWSANDCOLS = 4;
	public static int ROWS = ROWSANDCOLS;
	public static int COLS = ROWSANDCOLS;
	public Square[][] squares ;
	private Random rnd = new Random();
	private static int PITS = 3;
	private static int WUMPUS = 1;
	private static int GOLD = 1;
	
	public Board() {
		squares = new Square[COLS+2][ROWS+2];
		for (int i=0;i<=COLS+1;i++)
			for (int j=0;j<=ROWS+1;j++)
				squares[i][j] = new Square(i,j);
		
		for (int i=1;i<COLS+1;i++)
			for (int j=1;j<ROWS+1;j++)
					squares[i][j].neighbors(squares);
	
		int i = 0, j = 0;
		
		for(int z=0;z<PITS;z++) {
			i=0;j=0;
			while(i==0 || j==0 || i==5 || j==5 || (i==1 && j==4) || squares[i][j].hasPit) {
					i=rnd.nextInt(COLS+1);
					j=rnd.nextInt(ROWS+1);
			}	
			/*System.out.println("pits:"+i+"  "+j);*/
			squares[i][j].hasPit = true;
			for (Square percepts : squares[i][j].getNeighbors()) {
				//System.out.println("Breeze: "+percepts.x+" "+percepts.y);
				percepts.hasBreeze = true;
			}
			
		}
		
		i=0;j=0;
		for(int z=0;z<Board.WUMPUS;z++) {
			while(i==0 || j==0 || i==Board.COLS+1 || j==Board.ROWS+1 || (i==1 && j==Board.COLS) || squares[i][j].hasPit 
					|| squares[i][j].hasWumpus) {
				i=rnd.nextInt(COLS+1);
				j=rnd.nextInt(ROWS+1);
			}
			/*System.out.println("wumpus:"+i+"  "+j);*/
			squares[i][j].hasWumpus = true;
			for (Square percepts : squares[i][j].getNeighbors()) {
				//System.out.println("Stench: "+percepts.x+" "+percepts.y);
				percepts.hasStench = true;
			}
		}
			
		i=0;j=0;
		for (int z=0;z<Board.GOLD;z++) {
			while(i==0 || j==0 || i==Board.COLS+1 || j==Board.ROWS || squares[i][j].hasPit || squares[i][j].hasWumpus || (i==1 && j==Board.COLS) 
					|| squares[i][j].hasGold ) {
				i=rnd.nextInt(COLS+1);
				j=rnd.nextInt(ROWS+1);
			}
			/*System.out.println("gold:"+i+"  "+j);*/
			squares[i][j].hasGold = true;
			squares[i][j].hasGlitter = true;
			/*System.out.println("Board has been created");*/
		}
	}

}
