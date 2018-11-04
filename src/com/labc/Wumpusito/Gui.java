package com.labc.Wumpusito;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.FileInputStream;
import java.io.IOException;

import sun.audio.AudioData;
import sun.audio.AudioPlayer;
import sun.audio.AudioStream;
import sun.audio.ContinuousAudioDataStream;

import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class Gui extends JFrame{
	private ImageIcon hunterIcon, Square;
	private Board board;
	private Agent AI; 
	private JLabel[][] label = new JLabel[Board.COLS][Board.ROWS];
	private JTextArea score, moves, arrows;
	private JPanel panel, panel1;
	
	public Gui() throws IOException, InterruptedException{
		initGame();
		this.setVisible(true);
		AI.sounds[2] = new FileInputStream("sounds/waka.wav");
		AI.audio[2] = new AudioStream(AI.sounds[2]);
		AudioData waka = AI.audio[2].getData();
		AI.wakaCont = new ContinuousAudioDataStream(waka);
		AudioPlayer.player.start(AI.wakaCont);
		while (!AI.getGold() && AI.getAlive()) {
			changeGameState();
			score.setText("Score: "+AI.getScore());
			moves.setText("Moves: "+AI.getMoves());
			arrows.setText("Arrows: "+AI.arrows);
		}
	}
	
	public ImageIcon decideImage() {
		String img = "";
		ImageIcon hunterImage;
		
		if (AI.getFacing()==board.NORTH)
			img = "images/hunter13.jpg";
		else if (AI.getFacing()==board.EAST)
			img = "images/hunter10.jpg";
		else if (AI.getFacing()==board.SOUTH)
			img = "images/hunter11.jpg";
		else if (AI.getFacing()==board.WEST)
			img = "images/hunter12.jpg";
		
		hunterImage = new ImageIcon(img);
		hunterImage.setImage(hunterImage.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH));
				
		return hunterImage;
	}
	
	public ImageIcon decideImageDed() {
		String img = "";
		ImageIcon hunterImage;
		
		if (AI.getFacing()==board.NORTH)
			img = "images/ded13.jpg";
		else if (AI.getFacing()==board.EAST)
			img = "images/ded10.jpg";
		else if (AI.getFacing()==board.SOUTH)
			img = "images/ded11.jpg";
		else if (AI.getFacing()==board.WEST)
			img = "images/ded12.jpg";
		
		hunterImage = new ImageIcon(img);
		hunterImage.setImage(hunterImage.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH));
				
		return hunterImage;
	}
	
	public JLabel decidePos(Square sqr){
		int i = 0; int j = 0; int z = 1;
		while(z<Board.ROWS+1) {
			if(sqr.x==z) {
				i=z-1;
				z=1;
				break;
			}
			z++;
		}
		z=Board.COLS;
		while(z>0) {
			if(sqr.y==z) {
				break;
			}
			j++;
			z--;
		}		
		return label[i][j];
	}
	
	private void changeGameState() throws IOException {
		char next = AI.decideMove();
		ImageIcon tileVisited = new ImageIcon("images/Tile.jpg");
		ImageIcon ded = decideImageDed();
		ImageIcon rotate = decideImage();
		
		tileVisited.setImage(tileVisited.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH));
		ded.setImage(ded.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH));
		
		if ( next == 's') {
			setIcons();
			decidePos(AI.current).setIcon(decideImage());
		}
		if (next=='f') {
			setIcons();
			decidePos(AI.current).setIcon(decideImage());
			decidePos(AI.previous).setIcon(tileVisited);
		}
		if (next =='t') {
			setIcons();
			decidePos(AI.current).setIcon(rotate);
		}
		if (next == 'k') {
			setIcons();
			decidePos(AI.current).setIcon(ded);
		}
		if (next == 'd') {
			setIcons();
			decidePos(AI.current).setIcon(ded);
		}
	}
	
	private void setIcons() {
		ImageIcon pitImage;
		ImageIcon goldImage;
		ImageIcon wumpusImage;
		ImageIcon breezeImage;
		ImageIcon stenchImage;
		ImageIcon breezeStenchImage;
		
		pitImage = new ImageIcon("images/pitc.gif");
		goldImage = new ImageIcon("images/gold.jpg");
		wumpusImage = new ImageIcon("images/wumpus.jpg");
		breezeImage = new ImageIcon("images/breeze.jpg");
		stenchImage = new ImageIcon("images/stench.jpg");
		breezeStenchImage = new ImageIcon("images/breezestench.jpg");
		
		pitImage.setImage(pitImage.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH));
		goldImage.setImage(goldImage.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH));
		wumpusImage.setImage(wumpusImage.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH));
		breezeImage.setImage(breezeImage.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH));
		stenchImage.setImage(stenchImage.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH));
		breezeStenchImage.setImage(breezeStenchImage.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH));
		
		for(int i=1;i<Board.COLS+1;i++)
			for(int j=1;j<Board.ROWS+1;j++) {
				if(board.squares[i][j].hasBreeze && !board.squares[i][j].hasStench)
					decidePos(board.squares[i][j]).setIcon(breezeImage);
				else if(!board.squares[i][j].hasBreeze && board.squares[i][j].hasStench)
					decidePos(board.squares[i][j]).setIcon(stenchImage);
				else if(board.squares[i][j].hasBreeze && board.squares[i][j].hasStench)
					decidePos(board.squares[i][j]).setIcon(breezeStenchImage);
				else
					decidePos(board.squares[i][j]).setIcon(Square);
				if(board.squares[i][j].hasPit)
					decidePos(board.squares[i][j]).setIcon(pitImage);
				else if(board.squares[i][j].hasGold)
					decidePos(board.squares[i][j]).setIcon(goldImage);
				else if(board.squares[i][j].hasWumpus)
					decidePos(board.squares[i][j]).setIcon(wumpusImage);
				
			}	
	}
	
	private void chooseGui() {
		JButton pacman, zelda;
		JPanel chooser, title;
		JLabel titleLabel;
		ImageIcon titleImage;
		pacman = new JButton("PacMan");
		zelda = new JButton("Zelda");
		chooser = new JPanel();
		title = new JPanel();
		titleImage = new ImageIcon("images/title.jpg");
		titleLabel = new JLabel();
		
		chooser.add(pacman);
		chooser.add(zelda);
		createLayout(chooser);
		this.setVisible(true);
		this.setTitle("WumpusWorld");
		this.setPreferredSize(new Dimension(chooser.getWidth(),chooser.getHeight()));
		
	}
	
	private void initGui() {
		panel = new JPanel();
		panel1 = new JPanel();
		score = new JTextArea("Score: "+AI.getScore());
		score.setEditable(false);
		moves = new JTextArea("Moves: "+AI.getMoves());
		moves.setEditable(false);
		arrows = new JTextArea("Arrows: "+AI.arrows);
		arrows.setEditable(false);
		score.setFont(new Font("perro",Font.ITALIC,20));
		moves.setFont(new Font("perro",Font.ITALIC,20));
		arrows.setFont(new Font("perro",Font.ITALIC,20));
		panel1.add(score);
		panel1.add(moves);
		panel1.add(arrows);
		JPanel supreme = new JPanel();
		
		hunterIcon = decideImage();
		
		Square = new ImageIcon("images/Tile.jpg");
		Square.setImage(Square.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH));
		hunterIcon.setImage(hunterIcon.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH));
		
		for (int i=Board.COLS-1; i>=0; i--)
			for(int j=Board.ROWS-1; j>=0; j--) {
				label[i][j] = new JLabel(Square);
				panel.add(label[i][j]);
			}
		setIcons();
		label[0][0].setIcon(hunterIcon);
		
		panel.setLayout(new GridLayout(Board.COLS,Board.ROWS));
		panel1.setLayout(new GridLayout(3,1));
		supreme.add(panel);
		supreme.add(panel1);
		
		createLayout(supreme);
		int height = Square.getIconHeight();
		int width = Square.getIconWidth();
		int height1 = supreme.getHeight();
		int width1 = supreme.getWidth();
		
		this.setSize(new Dimension(width1+100,height1+100));
		this.setLocationRelativeTo(null);
		this.setTitle("WUMPUS WORLD");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	private void initGame() throws IOException{;
		board = new Board();
		AI = new Agent(board.squares[1][Board.COLS], board);
		initGui();
	}
	
	private void createLayout(JComponent... arg) {

        Container pane = getContentPane();
        GroupLayout gl = new GroupLayout(pane);
        pane.setLayout(gl);

        gl.setAutoCreateContainerGaps(true);

        gl.setHorizontalGroup(gl.createSequentialGroup()
                .addComponent(arg[0])
        );

        gl.setVerticalGroup(gl.createParallelGroup()
                .addComponent(arg[0])
        );

        pack();
    }
	
	 public static void main(String[] args) throws IOException, InterruptedException {
	            Gui ex = new Gui();  
	 }
}

