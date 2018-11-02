package com.Wumpusito.labc;

import java.io.IOException;

public class Agent {
	private boolean hasArrow;
	private boolean isAlive;
	private boolean gotGold;
	private int Facing;
	public Square current;
	public Square previous;
	private int Score;
	private int moves;
	
	public Agent(Square starting) {
		this.previous = null;
		this.current = starting;
		this.hasArrow = true;
		this.gotGold = false;
		this.isAlive = true;
		this.Facing = Board.WEST;
		this.Score = 0;
		current.hasPlayer = true;
		current.visited = false;
		this.moves = 0;
	}
	
	public char decideMove() throws IOException {
		Square nextMove = current.getNeighbors()[Board.WEST];
		char next = 0 ; 
			moves++;
			if(moves>100)
				next = 'k';
			else {
				analyze(current);
				/*System.out.println(current.x+" , "+current.y+"[PIT: "+current.hasPit+",WUMPUS: "+current.hasWumpus+",GOLD: "+current.hasGold
					+	",BREEZE: "+current.hasBreeze+",STENCH: "+current.hasStench+")");*/
				for(Square nm : current.getNeighbors()) {
					if( !nm.unreachable && nm.wumpusRisk>3  ) {
						if(current.getNeighbors()[Facing]!=nm) {
							next = 't';
							break;
						}
						else {
							next = 's';
							break;
						}	
					}	
					if( !nm.unreachable && nm.pitRisk<nextMove.pitRisk ) 
						nextMove = nm;
					
					else
						nextMove = current.getNeighbors()[Facing];
		
				}
				if(current.getNeighbors()[Facing]!=nextMove) 
					next = 't';
				
				else 
					next = 'f';
				
				
				if(nextMove.unreachable)
					next='t';
				
				if(current.hasGlitter) 
					next = 'g';
				else if(current.hasWumpus || current.hasPit)
					next = 'd';
			}
			/*System.out.println(next);*/
			/*System.in.read();*/
			switch(next) {
			case 's':
				shoot();
				/*System.out.println(current.x+" , "+current.y);*/
				break;
			
			case 'f':
				current.visited = true;
				moveForward();
				/*System.out.println("FORWARD: "+current.x+" , "+current.y+"   , to the   "+Facing);*/
				break;
			
			case 't':
				turn90();
				/*System.out.println("turned 90 deegres");*/
				break;
			
			case 'k':
				Suicide();
				break;
				
			case 'g':
				pickupGold();
				break;
			
			case 'd':
				Suicide();
				break;
				
			}
		return next;
		}
		/*System.out.print("SCORE: "+this.Score);*/
	
	private void analyze(Square tile) {
		if(!tile.visited) {
			if (tile.hasBreeze && tile.hasStench)
				for (Square risky : tile.getNeighbors()) {
					if(!risky.isSafe ) {
						risky.pitRisk++;
						risky.wumpusRisk++;
					}	
				}
			else if (!tile.hasBreeze && !tile.hasStench) {
				for(Square risky : tile.getNeighbors()) {
					if(!risky.isSafe) {
						risky.wumpusRisk=0;
						risky.pitRisk = 0;
						risky.isSafe = true;
					}
				}
			}
			else if(tile.hasBreeze && !tile.hasStench) {
				for(Square risky : tile.getNeighbors()) {
					if(!risky.isSafe)
						risky.pitRisk++;
						risky.wumpusRisk=0;
				}
			}
			else if(!tile.hasBreeze && tile.hasStench) {
				for(Square risky : tile.getNeighbors()) {
					if(!risky.isSafe)
						risky.wumpusRisk++;
						risky.pitRisk=0;
				}
			}
		}
	}
	
	private void shoot() {
		Square trail = this.current;
		if (hasArrow) {
			hasArrow = false;
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
				
				for (int i=0;i<=Board.COLS;i++)
					for (int j=0;j<=Board.ROWS;j++)
						Board.squares[i][j].wumpusRisk=0;
			}
				
			else
				this.Score-=1000;
		
		}
	}
	
	private void pickupGold() {
		current.hasGold = false;
		this.gotGold = true; 
		this.Score+=10000;
	}
	
	private void moveForward() {
		Square next = previous;
		if(Facing == Board.NORTH) {
			next = current.getNeighbors()[Facing];
			if(current.x == 4) {
				/*System.out.println("ouch");*/
				current.getNeighbors()[Facing].unreachable = true;
				next = this.current;
			}		
		}
			
		else if(Facing == Board.SOUTH) {
			next = current.getNeighbors()[Facing];
			if(current.x == 1) {
				/*System.out.println("ouch");*/
				current.getNeighbors()[Facing].unreachable = true;
				next = this.current;
			}
		}
		
		else if(Facing == Board.EAST) {
			next = current.getNeighbors()[Facing];
			if(current.y == 4) {
				/*System.out.println("ouch");*/
				current.getNeighbors()[Facing].unreachable = true;
				next = this.current;
			}
		}
		
		else if(Facing == Board.WEST) {
			next = current.getNeighbors()[Facing];
			if(current.y == 1) {
				/*System.out.println("ouch");*/
				current.getNeighbors()[Facing].unreachable = true;
				next = this.current;
			}
		}
		isAlive = next.hasPit?false:true;
		isAlive = next.hasWumpus?false:true;
		this.previous = current;
		this.current = next;
		this.Score-=10;
	}
	
	private void Suicide() {
		this.isAlive=false;
		this.gotGold=true;
	}
	
	private void turn90() {
		this.Facing = Facing==0?3:Facing-1;
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

}
