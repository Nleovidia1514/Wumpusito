package com.labc.Wumpusito;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

import sun.audio.AudioStream;
import sun.audio.AudioPlayer;
import sun.audio.AudioData;
import sun.audio.ContinuousAudioDataStream;
import javax.sound.sampled.AudioInputStream;

public class Agent extends Player{
	InputStream[] sounds = new InputStream[10];
	AudioStream[] audio = new AudioStream[10];
	ContinuousAudioDataStream wakaCont;
	Random rnd = new Random();
	int looping = 0;
	
	public Agent(Square starting,Board board) {
		super(starting, board);
	}
	
	public char decideMove() throws IOException {
		Square nextMove = current.getNeighbors()[Facing];
		sounds[0] = new FileInputStream("sounds/gameover.wav");
		audio[0] = new AudioStream(sounds[0]);
		sounds[1] = new FileInputStream("sounds/victory.wav");
		audio[1] = new AudioStream(sounds[1]);
		sounds[3] = new FileInputStream("sounds/roar.wav");
		audio[3] = new AudioStream(sounds[3]);
		sounds[4] = new FileInputStream("sounds/arrow.wav");
		audio[4] = new AudioStream(sounds[4]);
		sounds[5] = new FileInputStream("sounds/ow.wav");
		audio[5] = new AudioStream(sounds[5]);
		
		char next = 0 ; 
			moves++;
			if(moves>100)
				next = 'k';
			else {
				analyze(current);
				/*System.out.println(current.x+" , "+current.y+"[PIT: "+current.hasPit+",WUMPUS: "+current.hasWumpus+",GOLD: "+current.hasGold
					+	",BREEZE: "+current.hasBreeze+",STENCH: "+current.hasStench+")");*/
				for(Square nm : current.getNeighbors()) {
					/*System.out.println(nm.x+","+nm.y+" pitrisk: "+nm.pitRisk+" is safe?"+nm.isSafe);
					System.out.println(nextMove.x+","+nextMove.y+"pitrisk: "+nextMove.pitRisk+" is safe?"+nextMove.isSafe);*/
					if( !nm.unreachable && ((nm.pitRisk+nm.visitedRisk+nm.wumpusRisk)<(nextMove.wumpusRisk+nextMove.pitRisk+nextMove.visitedRisk) ) )
						nextMove = nm;
					else if(nm.pitRisk+nm.visitedRisk+nm.wumpusRisk==nextMove.wumpusRisk+nextMove.pitRisk+nextMove.visitedRisk && !nm.unreachable)
						nextMove = nm;
				}
				
				if(looping==25)
					nextMove = takeRisks(current.getNeighbors()[Facing]);
				
				if(current.getNeighbors()[Facing]!=nextMove) {
					next = 't';
					
				}
					
				else {
					if(current.getNeighbors()[Facing].unreachable)
						next = 't';
					else
						next = 'f';
				}
				if(nextMove.unreachable)
					next='t';
				if(current.hasGlitter) 
					next = 'g';
				else if(current.hasWumpus || current.hasPit)
					next = 'd';
				else if(current.hasStench && !current.getNeighbors()[Facing].unreachable) {
					if (arrows>0)
						next = 's';
					else {
						if(current.getNeighbors()[Facing]!=nextMove || current.getNeighbors()[Facing].unreachable ) 
							next = 't';
						else
							next = 'f';
					}	
				}
			}
			looping++;
			/*System.in.read();*/
			switch(next) {
			case 's':
				AudioPlayer.player.start(audio[4]);
				if(shoot())
					AudioPlayer.player.start(audio[3]);
				/*System.out.println(current.x+" , "+current.y);*/
				break;
			
			case 'f':
				current.visited = true;
				if(moveForward())
					AudioPlayer.player.start(audio[5]);
				/*System.out.println("FORWARD: "+current.x+" , "+current.y+"   , to the   "+Facing);*/
				break;
			
			case 't':
				int idk = rnd.nextInt(2);
				int relativeRight = Facing==3?0:Facing+1;
				int relativeLeft = Facing ==0?3:Facing-1;
				/*System.out.println(Facing);
				System.out.println(current.x+","+current.y);
				System.out.println(current.getNeighbors()[relativeRight].x+","+current.getNeighbors()[relativeRight].y);
				System.out.println(current.getNeighbors()[relativeLeft].x+","+current.getNeighbors()[relativeLeft].y);
				System.out.println(nextMove.x+","+nextMove.y);*/
				if(current.getNeighbors()[relativeRight]==nextMove) {
					turn90right();
				}
					
				else if(current.getNeighbors()[relativeLeft]==nextMove) {
					turn90left();
				}
				
				else {
					if(idk==0)
						turn90left();
					else
						turn90right();
				}
					
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
		
	private Square takeRisks(Square tile) {
		this.looping = 0;
		return tile;
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
		tile.visited=true;
		tile.isSafe=true;
	}
}
