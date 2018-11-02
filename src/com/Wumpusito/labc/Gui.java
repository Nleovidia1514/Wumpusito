package com.Wumpusito.labc;

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
	ImageIcon hunterIcon ;
	private Board board;
	private Agent AI; 
	private JLabel[][] label = new JLabel[4][4];
	private JTextArea score;
	private JTextArea moves;
	private JTextArea arrows;
	private JPanel panel;
	private JPanel panel1;
	private String guiType;
	
	public Gui() throws IOException{
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
		int i = 0; int j = 0;
		if(sqr.x==1)
			i=0;
		else if(sqr.x==2)
			i=1;
		else if(sqr.x==3)
			i=2;
		else if(sqr.x==4)
			i=3;
		if(sqr.y==4)
			j=0;
		else if(sqr.y==3)
			j=1;
		else if(sqr.y==2)
			j=2;
		else if(sqr.y==1)
			j=3;
		
		return label[i][j];
	}
	
	private void changeGameState() throws IOException {
		char next = AI.decideMove();
		ImageIcon tileVisited = new ImageIcon("images/Tile.jpg");
		ImageIcon ded = decideImageDed();
		ImageIcon rotate = decideImage();
		
		tileVisited.setImage(tileVisited.getImage().getScaledInstance(150, 150, Image.SCALE_DEFAULT));
		ded.setImage(ded.getImage().getScaledInstance(150, 150, Image.SCALE_DEFAULT));
		
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
		wumpusImage = new ImageIcon("images/wumpusc.jpg");
		breezeImage = new ImageIcon("images/breeze.jpg");
		stenchImage = new ImageIcon("images/stench.jpg");
		breezeStenchImage = new ImageIcon("images/breezestench.jpg");
		
		pitImage.setImage(pitImage.getImage().getScaledInstance(150, 150, Image.SCALE_DEFAULT));
		goldImage.setImage(goldImage.getImage().getScaledInstance(150, 150, Image.SCALE_DEFAULT));
		wumpusImage.setImage(wumpusImage.getImage().getScaledInstance(150, 150, Image.SCALE_DEFAULT));
		breezeImage.setImage(breezeImage.getImage().getScaledInstance(150, 150, Image.SCALE_DEFAULT));
		stenchImage.setImage(stenchImage.getImage().getScaledInstance(150, 150, Image.SCALE_DEFAULT));
		breezeStenchImage.setImage(breezeStenchImage.getImage().getScaledInstance(150, 150, Image.SCALE_DEFAULT));
		
		for(int i=1;i<5;i++)
			for(int j=1;j<5;j++) {
				if(board.squares[i][j].hasBreeze && !board.squares[i][j].hasStench)
					decidePos(board.squares[i][j]).setIcon(breezeImage);
				else if(!board.squares[i][j].hasBreeze && board.squares[i][j].hasStench)
					decidePos(board.squares[i][j]).setIcon(stenchImage);
				else if(board.squares[i][j].hasBreeze && board.squares[i][j].hasStench)
					decidePos(board.squares[i][j]).setIcon(breezeStenchImage);
				if(board.squares[i][j].hasPit)
					decidePos(board.squares[i][j]).setIcon(pitImage);
				else if(board.squares[i][j].hasGold)
					decidePos(board.squares[i][j]).setIcon(goldImage);
				else if(board.squares[i][j].hasWumpus)
					decidePos(board.squares[i][j]).setIcon(wumpusImage);
			}	
	}
	
	private void chooseGui() {
		JButton pacman = new JButton("PacMan");
		JButton zelda = new JButton("Zelda");
		JLabel label = new JLabel();
		JPanel panel2 = new JPanel();
		
		pacman.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
					if(e.getSource()==pacman)
						try {
							panel2.setVisible(false);
							initGame();
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
			}
		});
		
		zelda.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(e.getSource()==zelda)
					try {
						panel2.removeAll();
						initGame();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
			}
		});
		panel2.add(pacman);
		panel2.add(zelda);
		createLayout(panel2);
		this.setPreferredSize(new Dimension(panel2.getWidth(),panel2.getHeight()));
		this.setLocationRelativeTo(null);
		this.setTitle("WUMPUS WORLD");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	private void initGui() {
		panel = new JPanel();
		panel1 = new JPanel();
		score = new JTextArea("Score: "+AI.getScore());
		moves = new JTextArea("Moves: "+AI.getMoves());
		arrows = new JTextArea("Arrows: "+AI.arrows);
		score.setFont(new Font("perro",Font.ITALIC,20));
		moves.setFont(new Font("perro",Font.ITALIC,20));
		arrows.setFont(new Font("perro",Font.ITALIC,20));
		panel1.add(score);
		panel1.add(moves);
		panel1.add(arrows);
		JPanel supreme = new JPanel();
		
		hunterIcon = decideImage();
		
		ImageIcon Square = loadImage();
		Square.setImage(Square.getImage().getScaledInstance(150, 150, Image.SCALE_DEFAULT));
		hunterIcon.setImage(hunterIcon.getImage().getScaledInstance(150, 150, Image.SCALE_DEFAULT));
		
		for (int i=3; i>=0; i--)
			for(int j=3; j>=0; j--) {
				label[i][j] = new JLabel(Square);
				panel.add(label[i][j]);
			}
		setIcons();
		label[0][0].setIcon(hunterIcon);
		
		panel.setLayout(new GridLayout(4,4));
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
		AI = new Agent(board.squares[1][4]);
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
	
	private ImageIcon loadImage() {
		ImageIcon ii = new ImageIcon("images/Tile.jpg");
		return ii;
	}
	
	
	 public static void main(String[] args) throws IOException {
	            Gui ex = new Gui();  
	 }
}

