class ShortMagic extends GameMagic {
	public ShortMagic (String path, int x, int y, int width, int height, int speed) {
		super(path, x, y, width, height, speed);
	}

	@Override 
	public void invokeMagic(Stick stick) {
		if(stick.getWidth() >= stick.getInitWidth()) {
			stick.setWidth(stick.getWidth() / 2);
		}
	}
}
