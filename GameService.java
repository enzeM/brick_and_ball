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
	private int frameW = -1;
	private int frameH = -1;

	//x, y, width, height
	private Ball ball = new Ball("img/ball.gif", 150, 200, 20, 20);
	private Stick stick = new Stick("img/stick_norm.png", 150, 380, 70, 20);
	private Brick[][] bricks = null;
	
	public GameService(GameFrame gFrame) {
		this.gFrame = gFrame;
		this.frameW = gFrame.getWidth();
		this.frameH = gFrame.getHeight();
		this.bricks = generateBricks(5, 3);
	}

	//auto generate game bricks
	public Brick[][] generateBricks(int row, int column) {
		Brick[][] bricks = new Brick[row][column];
		Random r = new Random();
		for(int x = 0; x < bricks.length; x ++) {
			for(int y=0; y< bricks[x].length; y++) {
				int brickSize = 300/bricks.length;
				int brickPos = brickSize;
				Brick brick = new Brick("img/mars.png" , x * brickPos, y * brickPos, brickSize, brickSize);	
				//each row have about 9 items
				double rand = (int)(Math.random() * 10);
				boolean visible = rand < 7 ? true : false;
				brick.setVisible(visible);
				//set magic
				if(brick.isVisible() && rand > 5) {
					brick.setMagic(Brick.LONG_MAGIC);
				} else if(brick.isVisible() && rand <= 5) {
					brick.setMagic(Brick.SHORT_MAGIC);
				}
				bricks[x][y] = brick;
			}
		}
		return bricks;
	}

	//set game move position for magic item in the game
	public void setMagicPos() {
		for(int x=0; x<bricks.length; x++) {
			for(int y=0; y<bricks[x].length; y++) {
				GameMagic magic = bricks[x][y].getMagic();
				if(magic != null && 
						bricks[x][y].isVisible() == false && 
						magic.getY() < gFrame.getHeight()) {
					magic.setY(magic.getY() + magic.getSpeed());
					if(isHitItem(magic)) {
						magic.invokeMagic(this.stick);
					}
				}
			}
		}
	}

	//game control
	public void setStickPos(KeyEvent ke) {
		if(ke.getKeyCode() == KeyEvent.VK_W) {
			//can not move up more than one third of the screen
			if(stick.getY() - stick.getSpeed() > gFrame.getWidth()/1.5) {
				stick.setY(stick.getY() - stick.getSpeed());
			}
		} else if(ke.getKeyCode() == KeyEvent.VK_S) {
			if(stick.getY() + stick.getSpeed() < gFrame.getHeight() - 1) {
				stick.setY(stick.getY() + stick.getSpeed());
			} else {
				stick.setY(gFrame.getHeight() - stick.getHeight());
			}
		} else if(ke.getKeyCode() == KeyEvent.VK_A) {
			if(stick.getX() - stick.getSpeed() > 0) {
				stick.setX(stick.getX() - stick.getSpeed());
			} else {
				stick.setX(0);
			}
		} else if(ke.getKeyCode() == KeyEvent.VK_D) {
			if(stick.getX() + stick.getSpeed() < gFrame.getWidth() - stick.getWidth()) {
				stick.setX(stick.getX() + stick.getSpeed());
			} else {
				stick.setX(gFrame.getWidth() - stick.getWidth());
			}
		//restart game
		} else if(ke.getKeyCode() == KeyEvent.VK_R) {
			try {
				gFrame.init();
			} catch(IOException ioe) {
				ioe.printStackTrace();
			}
		}
	}

	//ball movement in the game
	public void setBallPos() {
		int absSpeedX = Math.abs(ball.getSpeedX());
		int absSpeedY = Math.abs(ball.getSpeedY());

		if(ball.getX() - absSpeedX < 0) {
			ball.setSpeedX(ball.getSpeedX() * -1);
		}
		if(ball.getX() + absSpeedX > gFrame.getWidth() - ball.getWidth()) {
			ball.setSpeedX( - ball.getSpeedX());
		}
		if(ball.getY() - absSpeedY < 0) {
			ball.setSpeedY( - ball.getSpeedY());
		}
		if(isHitItem(ball)) {
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

	//colliston test between ball and game item
	public boolean isHitItem(GameComponent item) {
		if(item.getY() + item.getHeight() >= stick.getY() &&
				item.getY() < stick.getY() + stick.getHeight()/2 &&
				item.getX() + item.getWidth() > stick.getX() &&
				item.getX() < stick.getX() + stick.getWidth()) {
			return true;
		}
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
			brick.setVisible(false);
			return true;
		}
		return false;
	}

	//detect winning of the game
	public boolean isWon() {
		for(int x=0; x<bricks.length; x++) {
			for(int y=0; y<bricks[x].length; y++) {
				if(bricks[x][y].isVisible()) {
					return false;
				}
			}
		}
		return true;
	}

	//draw relevant items on game panel
	public void draw(Graphics g) {
		if(isWon()) {
			try {
				Image image = ImageIO.read(new File("img/winner.png"));
				Image restartImg = ImageIO.read(new File("img/restart.png"));
				int imageX = (gFrame.getWidth() - image.getWidth(null))/2;
				int imageY = (gFrame.getHeight() - image.getHeight(null))/2;
				g.drawImage(image, imageX, imageY, null); 
				g.drawImage(restartImg, imageX - 20, imageY + 50, null);
			} catch(IOException ioe) {
				ioe.printStackTrace();
			}
		} else if(ball.isStop()) {
			try {
				Image image = ImageIO.read(new File("img/loser.png"));
				Image restartImg = ImageIO.read(new File("img/restart.png"));
				int imageX = (gFrame.getWidth() - image.getWidth(null))/2;
				int imageY = (gFrame.getHeight() - image.getHeight(null))/2;
				g.drawImage(image, imageX, imageY, null); 
				g.drawImage(restartImg, imageX - 20, imageY + 50, null);
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
					GameComponent magic = bricks[x][y].getMagic();
					if(bricks[x][y].isVisible()) {
						Brick brick = bricks[x][y];
						g.drawImage(brick.getImage(), brick.getX(), brick.getY(), null);
					} else if (bricks[x][y].isVisible() == false && magic != null) {
						g.drawImage(magic.getImage(), magic.getX(), magic.getY(), null);	
					}
				}
			}
		}
	}

	public void run() {
		setBallPos();
		setMagicPos();
	}
}
