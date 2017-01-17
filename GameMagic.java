abstract class GameMagic extends GameComponent {
	private int speed;
	public GameMagic(String path, int x, int y, int width, int height, int speed) {
		super(path, x, y, width, height);
		this.speed = speed;
	}

	public int getSpeed() {
		return this.speed;
	}

	abstract void invokeMagic(Stick stick);
}
