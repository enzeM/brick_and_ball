import java.awt.*;
import java.io.*;
import java.awt.image.BufferedImage;
import javax.swing.*;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import java.util.*;
import java.awt.event.*;
import javax.swing.Timer;

class GameFrame extends GameComponent {
	private GamePanel gamePanel = null; 
	private GameService service = new GameService(this);
	Timer timer = null;

	public GameFrame(String path, int width, int height) throws IOException {
		super(path, width, height);
		this.gamePanel = getGamePanel();
		init();
	}

	//game panel singleton design
	public GamePanel getGamePanel() {
		if(this.gamePanel == null) {
			this.gamePanel = new GamePanel();
		}
		return this.gamePanel;
	}

	public void init() throws IOException {
		JFrame frame = new JFrame();
		frame.setLayout(new FlowLayout());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		ActionListener task = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				service.run();
				gamePanel.repaint();
			}
		};

		//set timer track game flow
		if(timer != null) {
			timer.restart();
		} else {
			timer = new Timer(100, task);
			timer.start();
		}

		KeyAdapter keyProcessor = new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent key) {
				service.run();
				service.setStickPos(key);	
			}
		};
		gamePanel.addKeyListener(keyProcessor);
		frame.addKeyListener(keyProcessor);
		
		//add mouse adapter to test position x y in gamepanel
		MouseAdapter mouseProcessor = new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent me) {
				System.out.println("x:"+me.getX()+" y:"+me.getY());
			}
		};
		gamePanel.addMouseListener(mouseProcessor);
		//frame.setFocusable(true);
		
		
		frame.add(gamePanel);
		frame.pack();
		frame.setVisible(true);
	}

	@SuppressWarnings("serial")
	class GamePanel extends JPanel {

		@Override
		public void paint(Graphics g) {
			g.drawImage(GameFrame.this.getImage(), 0, 0, null);
			GameFrame.this.service.draw(g);
		}

		@Override
		public Dimension getPreferredSize() {
			int width = GameFrame.this.getWidth();
			int height = GameFrame.this.getHeight();
			return new Dimension(width, height);
		}
	}

	public static void main(String[] args) {
		try {
			new GameFrame("img/space.jpg", 300, 400);
		} catch(IOException ioe) {
			ioe.printStackTrace();
		}
	}
}
