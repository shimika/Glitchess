package nanami.mashiro.glitchess.piece;

import java.util.ArrayList;

import nanami.mashiro.glitchess.core.Board;
import nanami.mashiro.glitchess.core.Function;
import nanami.mashiro.glitchess.screen.Matrix;

public class MaPiece extends Piece {

	public MaPiece(int id) {
		super(id);
	}

	private static int[] kr = new int[] { -2, -2, -1, 1, 2, 2, 1, -1 };
	private static int[] kc = new int[] { -1, 1, 2, 2, 1, -1, -2, -2 };
	private static int[] br = new int[] { -1, -1, 0, 0, 1, 1, 0, 0 };
	private static int[] bc = new int[] { 0, 0, 1, 1, 0, 0, -1, -1 };

	@Override
	public ArrayList<Matrix> GetCandidate(int[][] board, Boolean isNight) {
		ArrayList<Matrix> listCand = new ArrayList<Matrix>();

		for (int i = 0; i < 8; i++) {
			int nr = this.GetPosition().row + kr[i];
			int nc = this.GetPosition().col + kc[i];
			int mr = this.GetPosition().row + br[i];
			int mc = this.GetPosition().col + bc[i];

			if (!Function.InnerBoard(nr, nc, 0, 0, Board.MAXROW, Board.MAXCOL)) {
				continue;
			}

			if (board[nr][nc] * this.GetTeam() <= 0 && board[mr][mc] == 0) {
				listCand.add(new Matrix(nr, nc));
			}
		}

		return listCand;
	}

	@Override
	public void Reset() {
		super.ResetPosition();
		this.SetHp(15);
		this.SetAtk(30);
	}

	@Override
	public int GetAtk(Boolean isNight) {
		if (isNight) {
			return this.GetAtk() * 2;
		}
		return this.GetAtk();
	}
}
