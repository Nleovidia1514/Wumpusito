package com.Wumpusito.labc;

public class Player {
	protected boolean isAlive;
	protected boolean gotGold;
	protected int Facing;
	public Square current;
	public Square previous;
	protected int Score;
	protected int moves;
	protected int arrows;
	
	public Player(Square starting) {
		previous = null;
		current = starting;
		arrows = 1;
		gotGold = false;
		isAlive = true;
		Facing = Board.WEST;
		Score = 0;
		current.hasPlayer = true;
		current.visited = false;
		moves = 0;
		
	}
	
	protected void shoot() {
		Square trail = this.current;
		if (arrows>0) {
			arrows--;
			if(Facing == Board.NORTH)
				while(!trail.hasWumpus && trail.getNeighbors()[Board.NORTH]!=null)
					trail = trail.getNeighbors()[Board.NORTH];
			
			else if(Facing == Board.EAST)
				while(!trail.hasWumpus && trail.getNeighbors()[Board.EAST]!=null)
					trail = trail.getNeighbors()[Board.EAST];			
			
			else if(Facing == Board.SOUTH)
				while(!trail.hasWumpus && trail.getNeighbors()[Board.SOUTH]!=null)
					trail = trail.getNeighbors()[Board.SOUTH];	
			
			else if(Facing == Board.WEST)
				while(!trail.hasWumpus && trail.getNeighbors()[Board.WEST]!=null)
					trail = trail.getNeighbors()[Board.WEST];	
			
			if(trail.hasWumpus) {
				trail.killWumpus();
				this.Score+=1000;
				trail = null;
				for (int i=0;i<=Board.COLS;i++)
					for (int j=0;j<=Board.ROWS;j++)
						Board.squares[i][j].wumpusRisk=0;
			}
			else {
				trail = null;
				this.Score-=1000;

			}
		}
	}
	
	protected void pickupGold() {
		current.hasGold = false;
		this.gotGold = true; 
		this.Score+=10000;
	}
	
	protected void moveForward() {
		Square next = previous;
		if(Facing == Board.NORTH) {
			next = current.getNeighbors()[Facing];
			if(current.x == 4) {
				/*System.out.println("ouch");*/
				current.getNeighbors()[Facing].unreachable = true;
				next = this.current;
				current = previous;
			}		
		}
			
		else if(Facing == Board.SOUTH) {
			next = current.getNeighbors()[Facing];
			if(current.x == 1) {
				System.out.println("ouch");
				current.getNeighbors()[Facing].unreachable = true;
				next = this.current;
				current = previous;
			}
		}
		
		else if(Facing == Board.EAST) {
			next = current.getNeighbors()[Facing];
			if(current.y == 4) {
				/*System.out.println("ouch");*/
				current.getNeighbors()[Facing].unreachable = true;
				next = this.current;
				current = previous;
			}
		}
		
		else if(Facing == Board.WEST) {
			next = current.getNeighbors()[Facing];
			if(current.y == 1) {
				System.out.println("ouch");
				current.getNeighbors()[Facing].unreachable = true;
				next = this.current;
				current = previous;
			}
		}
		this.previous = current;
		this.current = next;
		this.current.timesVisited++;
		this.Score-=10;
	}
	
	protected void Suicide() {
		this.isAlive=false;
		this.gotGold=true;
	}
	
	protected void turn90() {
		this.Facing = Facing==3?0:Facing+1;
		this.Score-=10;
	}
	
	public int getFacing() {
		return this.Facing;
	}
	public boolean getAlive() {
		return this.isAlive;
	}
	public boolean getGold() {
		return this.gotGold;
	}
	public int getScore() {
		return this.Score;
	}
	public int getMoves() {
		return this.moves;
	}
}
