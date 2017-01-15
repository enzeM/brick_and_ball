import java.awt.*;
import java.io.*;
import javax.swing.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

class Ball extends GameComponent {

	private boolean pause = true;
	private int speedX = 8;
	private int speedY = 6;
	private boolean stop = false;

	public Ball(String path, int x, int y, int width, int height) {
		super(path, x, y, width, height);
	}

	public void setPause(boolean pause) {
		this.pause = pause;
	}

	public int getSpeedX() {
		return this.speedX;
	}

	public int getSpeedY() {
		return this.speedY;
	}

	public void setSpeedX(int speedX) {
		this.speedX = speedX;
	}

	public void setSpeedY(int speedY) {
		this.speedY = speedY;
	}

	public boolean isStop() {
		return this.stop;
	}

	public void setStop(boolean stop) {
		this.stop = stop;
	}
}
