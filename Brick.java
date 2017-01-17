import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import javax.swing.*;
import javax.swing.ImageIcon;
import javax.imageio.ImageIO;

class Brick extends GameComponent {
	private boolean visible = true;
	private GameMagic magic = null;

	public static final int LONG_MAGIC = 1;
	public static final int SHORT_MAGIC = 2;

	public Brick(String path, int x, int y, int width, int height) {
		super(path, x, y, width, height);
	}

	public boolean isVisible() {
		return this.visible;
	}

	public GameMagic getMagic() {
		return this.magic;
	}
	
	public void setVisible(boolean visible) {
		this.visible = visible;;
	}
	
	public void setMagic(int magic) {
		int speed = 5;
		if(magic == Brick.LONG_MAGIC) {
			this.magic = new LongMagic("img/long.png", this.getX(), this.getY(), 
					this.getWidth()/2, this.getHeight()/2, speed);
		} else if(magic == Brick.SHORT_MAGIC) {
			this.magic = new ShortMagic("img/short.png", this.getX(), this.getY(),
					this.getWidth()/2, this.getHeight()/2, speed);
		} else {
			this.magic = null;
		}
	}
}
