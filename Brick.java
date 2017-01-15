import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import javax.swing.*;
import javax.swing.ImageIcon;
import javax.imageio.ImageIO;

class Brick extends GameComponent {
	private boolean visible = true;
	public Brick(String path, int x, int y, int width, int height) {
		super(path, x, y, width, height);
	}

	public boolean isVisible() {
		return this.visible;
	}
	
	public void setVisible(boolean visible) {
		this.visible = visible;;
	}
}
