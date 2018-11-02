package com.Wumpusito.labc;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import sun.audio.AudioStream;
import sun.audio.AudioPlayer;
import sun.audio.AudioData;
import sun.audio.ContinuousAudioDataStream;
import javax.sound.sampled.AudioInputStream;

public class Agent extends Player{
	InputStream[] sounds = new InputStream[10];
	AudioStream[] audio = new AudioStream[10];
	ContinuousAudioDataStream wakaCont;
	
	
	public Agent(Square starting) {
		super(starting);
	}
	
	public char decideMove() throws IOException {
		Square nextMove = current.getNeighbors()[Board.WEST];
		sounds[0] = new FileInputStream("sounds/gameover.wav");
		audio[0] = new AudioStream(sounds[0]);
		sounds[1] = new FileInputStream("sounds/victory.wav");
		audio[1] = new AudioStream(sounds[1]);
		
		char next = 0 ; 
			moves++;
			if(moves>100)
				next = 'k';
			else {
				analyze(current);
				/*System.out.println(current.x+" , "+current.y+"[PIT: "+current.hasPit+",WUMPUS: "+current.hasWumpus+",GOLD: "+current.hasGold
					+	",BREEZE: "+current.hasBreeze+",STENCH: "+current.hasStench+")");*/
				for(Square nm : current.getNeighbors()) {
					if( !nm.unreachable && nm.pitRisk<nextMove.pitRisk ) 
						nextMove = nm;
					else
						nextMove = current.getNeighbors()[Facing];
				}
				if(current.hasBreeze) {
					if(previous!=null)
						if(moves<50)
							nextMove=previous;
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
				else if(current.hasStench) {
					if (arrows>0)
						next = 's';
					else {
						if(current.getNeighbors()[Facing]!=nextMove) 
							next = 't';
						else
							next = 'f';
					}	
				}
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
				AudioPlayer.player.stop(wakaCont);
				AudioPlayer.player.start(audio[0]);
				Suicide();
				break;
				
			case 'g':
				AudioPlayer.player.stop(wakaCont);
				AudioPlayer.player.start(audio[1]);
				pickupGold();
				break;
			
			case 'd':
				AudioPlayer.player.stop(wakaCont);
				AudioPlayer.player.start(audio[0]);
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
					if(!risky.isSafe) {
						risky.pitRisk++;
						risky.wumpusRisk++;
					}	
				}
			else if (!tile.hasBreeze && !tile.hasStench) {
				for(Square risky : tile.getNeighbors()) {
					if(!risky.isSafe) {
						risky.wumpusRisk = 0;
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
}
