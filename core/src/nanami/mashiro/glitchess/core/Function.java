package nanami.mashiro.glitchess.core;

import nanami.mashiro.glitchess.screen.Coord;
import nanami.mashiro.glitchess.screen.Matrix;

import com.badlogic.gdx.graphics.g3d.particles.influencers.ColorInfluencer.Random;

public class Function {
	public static int GetType(int id) {
		int absVal = Math.abs(id);

		if (absVal <= 5) {
			// Jol
			return 1;
		} else if (absVal <= 7) {
			// Po
			return 2;
		} else if (absVal <= 9) {
			// Cha
			return 3;
		} else if (absVal <= 11) {
			// Sang
			return 4;
		} else if (absVal <= 13) {
			// Ma
			return 5;
		} else if (absVal <= 15) {
			// Sa
			return 6;
		} else {
			// King
			return 7;
		}
	}

	public static int GetSize(int id) {
		int absVal = Math.abs(id);

		if (absVal <= 5) {
			// Jol
			return 1;
		} else if (absVal <= 7) {
			// Po
			return 2;
		} else if (absVal <= 9) {
			// Cha
			return 2;
		} else if (absVal <= 11) {
			// Sang
			return 2;
		} else if (absVal <= 13) {
			// Ma
			return 2;
		} else if (absVal <= 15) {
			// Sa
			return 1;
		} else {
			// King
			return 3;
		}
	}

	public static int GetImageID(int type, int id) {
		if (type * id > 0) {
			return type;
		} else {
			return type + 10;
		}
	}

	private static int[][] startPoint = new int[][] {
			{ 0, 0, 2, 4, 6, 8, 1, 7, 0, 8, 1, 7, 2, 6, 3, 5, 4 },
			{ 0, 3, 3, 3, 3, 3, 2, 2, 0, 0, 0, 0, 0, 0, 0, 0, 1 } };

	public static Matrix GetStartPosition(int id) {
		int team = id > 0 ? 1 : -1;
		int absID = Math.abs(id);

		return new Matrix(startPoint[0][absID], team > 0 ? startPoint[1][absID] : Board.MAXCOL
				- startPoint[1][absID] - 1);
	}

	public static Coord GetCoord(int row, int col) {
		return new Coord(337 + 67 * col, 60 + 75 * row);
	}

	public static Coord GetCoord(Matrix matrix) {
		return GetCoord(matrix.row, matrix.col);
	}

	public static Matrix GetMatrix(int x, int y) {
		int xx, yy;
		for (int i = 0; i < Board.MAXROW; i++) {
			for (int j = 0; j < Board.MAXCOL; j++) {
				xx = 337 + 67 * j;
				yy = 60 + 75 * i;

				if (Math.abs(x - xx) <= 25 && Math.abs(y - yy) <= 25) {
					return new Matrix(i, j);
				}
			}
		}

		return new Matrix();
	}

	public static Boolean InnerBoard(int nr, int nc, int lr, int lc, int ur, int uc) {
		if (nr < lr || nc < lc || nr >= ur || nc >= uc) {
			return false;
		}
		return true;
	}

	private static Random random = new Random();

	public static int GetRandomDamage(int damage) {
		int low = damage / 2;
		int upp = (damage * 6) / 5;

		return low + (int) (Math.random() * (upp - low + 1));
	}
}
