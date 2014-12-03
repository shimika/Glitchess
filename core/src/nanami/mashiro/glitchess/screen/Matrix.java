package nanami.mashiro.glitchess.screen;

public class Matrix {
	public int row;
	public int col;

	public Matrix() {
		row = -1;
		col = -1;
	}

	public Matrix(int row, int col) {
		this.row = row;
		this.col = col;
	}

	public Boolean equals(Matrix other) {
		if (this.row == other.row && this.col == other.col) {
			return true;
		}
		return false;
	}
}
