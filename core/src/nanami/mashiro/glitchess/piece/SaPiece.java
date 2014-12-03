package nanami.mashiro.glitchess.piece;

import java.util.ArrayList;

import nanami.mashiro.glitchess.core.Function;
import nanami.mashiro.glitchess.screen.Matrix;

public class SaPiece extends Piece {

	public SaPiece(int id) {
		super(id);
		// TODO Auto-generated constructor stub
	}

	private static int[] kr = new int[] { -1, -1, 0, 1, 1, 1, 0, -1 };
	private static int[] kc = new int[] { 0, 1, 1, 1, 0, -1, -1, -1 };

	@Override
	public ArrayList<Matrix> GetCandidate(int[][] board, Boolean isNight) {
		ArrayList<Matrix> listCand = new ArrayList<Matrix>();

		int lower = this.GetTeam() > 0 ? 0 : 7;
		int upper = this.GetTeam() > 0 ? 2 : 9;
		int token = (this.GetPosition().row + this.GetPosition().col + lower + 1) % 2 + 1;

		for (int i = 0; i < 8; i += token) {
			for (int j = 1; j <= 1; j++) {
				int nr = this.GetPosition().row + kr[i] * j;
				int nc = this.GetPosition().col + kc[i] * j;

				if (!Function.InnerBoard(nr, nc, 3, lower, 6, upper + 1)) {
					break;
				}

				if (board[nr][nc] * this.GetTeam() <= 0) {
					listCand.add(new Matrix(nr, nc));
				}
				if (board[i][j] * this.GetTeam() != 0) {
					break;
				}
			}
		}

		return listCand;
	}

	@Override
	public void Reset() {
		super.ResetPosition();
		this.SetHp(10);
		this.SetAtk(25);
	}

	@Override
	public int GetAtk(Boolean isNight) {
		return this.GetAtk();
	}
}
