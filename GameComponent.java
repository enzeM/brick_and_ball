import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import javax.swing.*;
import javax.swing.ImageIcon;
import javax.imageio.ImageIO;

class GameComponent {
	private int x = -1;
	private int y = -1;

	private String path = "";
	private int width = -1;
	private int height = -1;

	private Image img = null;

	public GameComponent(String path) {
		this.path = path;
	}
	//used to generate a ball, or magic component
	public GameComponent(String path, int x, int y, int width, int height) {
		this.path = path;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}
	//used to generate a frame
	public GameComponent(String path, int width, int height) {
		this.width = width;
		this.height = height;
		this.path = path;
	}

	public int getX() {
		return this.x;
	}

	public int getY() {
		return this.y;
	}

	public int getWidth() {
		return this.width;
	}

	public int getHeight() {
		return this.height;
	}

	public String getPath() {
		return this.path;
	}

	public Image getImage() {
		try {
			//load and scale image from src
			Image image = ImageIO.read(new File(getPath()));
			if(this.width > 0 && this.height > 0) {
				this.img = image.getScaledInstance(this.width, this.height, Image.SCALE_DEFAULT);
			} else {
				this.img = image;
			}
		} catch(IOException ioe) {
			System.out.println("fail to load the image");
		}
		return this.img;
	}

	public void setX(int x) {
		this.x = x;
	}
	
	public void setY(int y) {
		this.y = y;
	}

	public void setWidth(int width) {
		this.width = width;
	}
	
	public void setHeight(int height) {
		this.height = height;
	}

	//set Image through redirect image path
	public void setImage(String path) {
		this.path = path;
	}

}
