package nanami.mashiro.glitchess.screen;

public class Coord {
	public int x;
	public int y;

	public Coord() {
		x = -1;
		y = -1;
	}

	public Coord(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public Boolean equals(Coord other) {
		if (this.x == other.x && this.y == other.y) {
			return true;
		}
		return false;
	}
}
