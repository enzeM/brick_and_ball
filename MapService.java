import java.io.*;
import java.io.BufferedReader;
import java.util.*;

class MapService {
	private String mapPath;
	/* 
	 * BRICK_NORM: brick without any magic
	 * BRICK_LONG: brick with long magic
	 * BRICK_SHORT: brick with short magic
	 */
	private static final int BRICK_NULL = 0;
	private static final int BRICK_NORM = 1;
	private static final int BRICK_LONG = 2;
	private static final int BRICK_SHORT = 3;

	public int row;
	public int column;
	private String[][] src;

	public MapService(String mapPath) {
		this.mapPath = mapPath;
		this.src = readMapFile();
	}

	//get two dimension result for whole map
	private String[][] readMapFile() {
		try(BufferedReader br = new BufferedReader(new FileReader(mapPath))) {
			/* 
			 * get number of line in file and prevent line number exceed MAX_VALUE
			 * the LineNumberReader object should be closed to prevent resource leak
			 */
			LineNumberReader lnr = new LineNumberReader(new FileReader(mapPath));
			lnr.skip(Long.MAX_VALUE);
			lnr.close();

			int row = lnr.getLineNumber();
			String[][] result = new String[row][];
			for(int x=0; x<result.length; x++) {
				String[] line = br.readLine().split("\\s*(\\s|,)\\s*");
				result[x] = line;
			}
			return result;
		} catch(IOException ioe) {
			ioe.printStackTrace();
		}
		return null;
	}

	public Brick[][] generateBricks() {
		Brick[][] bricks = new Brick[src.length][src[0].length];
		int size = 300/src[0].length;
		//operate column first then row
		for(int y=0; y<src[0].length; y++) {
			for(int x=0; x<src.length; x++) {
				//ensure the map is mapping the matrix from the file
				bricks[x][y] = new Brick("img/mars.png", y*size, x*size, size, size);
				int num = Integer.parseInt(src[x][y]);
				if(num == MapService.BRICK_LONG) {
					bricks[x][y].setMagic(Brick.LONG_MAGIC);
				} else if(num == MapService.BRICK_SHORT) {
					bricks[x][y].setMagic(Brick.SHORT_MAGIC);
				} else if(num == MapService.BRICK_NULL) {
					bricks[x][y].setVisible(false);
				}
			}
		}
		return bricks;
	}
}
