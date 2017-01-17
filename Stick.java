import java.awt.*;
import java.io.*;
import java.awt.image.BufferedImage;
import javax.swing.*;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import java.util.*;
import java.awt.Image;

class Stick extends GameComponent {
	private double size = -1;
	private Image sitckImg = this.getImage();
	private int speed = 20;
	private int initWidth = -1;
	public Stick(String path, int x, int y, int width, int height) {
		super(path, x, y, width, height);
		initWidth = width;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public Image generateImg(Image img) {
		return img.getScaledInstance(30, 10, Image.SCALE_DEFAULT); 
	}

	public int getSpeed() {
		return this.speed;
	}

	public int getInitWidth() {
		return this.initWidth;
	}
}
