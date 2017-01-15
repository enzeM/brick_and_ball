import java.awt.*;
import java.io.*;
import java.awt.image.BufferedImage;
import javax.swing.*;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import java.util.*;
import java.awt.event.*;

class GameService {
	
	private GameFrame gFrame = null;
	private int frameW;
	private int frameH;

	//x, y, width, height
	private Ball ball = new Ball("img/ball.gif", 150, 200, 20, 20);
	private Stick stick = new Stick("img/stick_norm.png", 150-20, 350, 70, 20);
	private Brick[][] bricks = generateBricks();

	
	public GameService(GameFrame gFrame) {
		this.gFrame = gFrame;
		this.frameW = gFrame.getWidth();
		this.frameH = gFrame.getHeight();
	}

	public Brick[][] generateBricks() {
		Brick[][] bricks = new Brick[1][1];
		Random r = new Random();
		for(int x = 0; x < bricks.length; x ++) {
			for(int y=0; y< bricks[x].length; y++) {
				int brickSize = 300/bricks.length;
				int brickPos = brickSize;
				Brick brick = new Brick("img/mars.png", x * brickPos, y * brickPos, brickSize, brickSize);	
				//each row have about 8 items
				double rand = (int)(Math.random() * 10);
				boolean visible = rand < 8 ? true : false;
				brick.setVisible(visible);
				bricks[x][y] = brick;
			}
		}
		return bricks;
	}
	
	//set stick position through keyboard
	public void setStickPos(KeyEvent ke) {
		if(ke.getKeyCode() == KeyEvent.VK_W) {
			if(stick.getY() - stick.getSpeed() > 0) {
				stick.setY(stick.getY() - stick.getSpeed());
			}
		} else if(ke.getKeyCode() == KeyEvent.VK_S) {
			if(stick.getY() + stick.getSpeed() < gFrame.getHeight() - stick.getHeight()) {
				stick.setY(stick.getY() + stick.getSpeed());
			}
		} else if(ke.getKeyCode() == KeyEvent.VK_A) {
			if(stick.getX() - stick.getSpeed() > 0) {
				stick.setX(stick.getX() - stick.getSpeed());
			}
		} else if(ke.getKeyCode() == KeyEvent.VK_D) {
			if(stick.getX() + stick.getSpeed() < gFrame.getWidth() - stick.getWidth()) {
				stick.setX(stick.getX() + stick.getSpeed());
			}
		}
	}

	public void setBallPos() {
		int absSpeedX = Math.abs(ball.getSpeedX());
		int absSpeedY = Math.abs(ball.getSpeedY());

		if(ball.getX() - absSpeedX < 0) {
			ball.setSpeedX(ball.getSpeedX() * -1);
		}
		if(ball.getX() + absSpeedX > gFrame.getWidth() - ball.getWidth()) {
			//ball.setX(frameW - ball.getWidth() * 2); //I think should be / 2
			ball.setSpeedX( - ball.getSpeedX());
		}
		if(ball.getY() - absSpeedY < 0) {
			ball.setSpeedY( - ball.getSpeedY());
		}
		/*
		if(ball.getY() + absSpeedY >= stick.getX() && isHitStick(stick)) {
			ball.setY(frameH - ball.getHeight() * 2);
			ball.setSpeedY( - ball.getSpeedY());
		}
		*/
		if(isHitStick(stick)) {
			ball.setSpeedY( - ball.getSpeedY());
		}

		if(ball.getY() + ball.getHeight() > gFrame.getHeight()) {
			ball.setStop(true);
		}
		
		//hit brick collision test
		for(int x=0; x<bricks.length; x++) {
			for(int y=0; y<bricks[x].length; y++) {
				if(isHitBrick(this.bricks[x][y])) {
					ball.setSpeedY( - ball.getSpeedY());
				}
			}
		}
		ball.setX(ball.getX() + ball.getSpeedX());
		ball.setY(ball.getY() + ball.getSpeedY());
	}
	
	//colliston test between ball and brick
	public boolean isHitStick(Stick stick) {
		if(ball.getY() + ball.getHeight() >= stick.getY() &&
				ball.getY() < stick.getY() + stick.getHeight() &&
				ball.getX() + ball.getWidth() > stick.getX() && 
				ball.getX() < stick.getX() + stick.getWidth()) {
			return true;
		}
		//ball.setStop(true);
		return false;
	}

	//colliston test between ball and brick
	public boolean isHitBrick(Brick brick) {
		if(! brick.isVisible() ) {
			return false;
		}
		//collision in game ball center pos x y and brick center pos x y
		double brickX = brick.getX() + brick.getImage().getWidth(null)/2;
		double brickY = brick.getY() + brick.getImage().getHeight(null)/2;
		double ballX = ball.getX() + ball.getImage().getWidth(null)/2;
		double ballY = ball.getY() + ball.getImage().getHeight(null)/2;
		double distance = Math.sqrt(
				Math.pow(ballX - brickX, 2) + Math.pow(ballY - brickY, 2));
		double hitDist = ball.getImage().getWidth(null)/2 + brick.getImage().getWidth(null)/2;
		if(distance < hitDist) {
			//System.out.println("hitted brick");
			brick.setVisible(false);
			return true;
		}
		return false;
	}


	public boolean isWon() {
		for(int x=0; x<bricks.length; x++) {
			for(int y=0; y<bricks[x].length; y++) {
				if(bricks[x][y].isVisible()) {
					//System.out.println("not win yet");
					return false;
				}
			}
		}
		return true;
	}

	//draw relevant on game panel
	public void draw(Graphics g) {
		if(isWon()) {
			try {
				Image image = ImageIO.read(new File("img/winner.png"));
				int imageW = image.getWidth(null);
				int imageH = image.getHeight(null);
				g.drawImage(image, (gFrame.getWidth() - imageW)/2, 
						(gFrame.getHeight() - imageH)/2, null);
			} catch(IOException ioe) {
				ioe.printStackTrace();
			}
		} else if(ball.isStop()) {
			try {
				Image image = ImageIO.read(new File("img/loser.png"));
				int imageW = image.getWidth(null);
				int imageH = image.getHeight(null);
				g.drawImage(image, (gFrame.getWidth() - imageW)/2, 
						(gFrame.getHeight() - imageH)/2, null);
			} catch(IOException ioe) {
				ioe.printStackTrace();
			}
		} else {
			//image, posx, posy
			g.drawImage(stick.getImage(), stick.getX(), stick.getY(), 
					stick.getWidth(), stick.getHeight(), null);
			g.drawImage(ball.getImage(), ball.getX(), ball.getY(),
					ball.getWidth(), ball.getHeight(), null);
			//print bricks
			for(int x=0; x<bricks.length; x++) {
				for(int y=0; y<bricks[x].length; y++) {
					if(bricks[x][y].isVisible()) {
						Brick brick = bricks[x][y];
						g.drawImage(brick.getImage(), brick.getX(), brick.getY(), null);
					}
				}
			}
		}
	}

	public void run() {
		setBallPos();
	}
}
