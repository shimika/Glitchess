package nanami.mashiro.glitchess.piece;

import java.util.ArrayList;

import nanami.mashiro.glitchess.core.Board;
import nanami.mashiro.glitchess.core.Function;
import nanami.mashiro.glitchess.screen.Matrix;

public class SangPiece extends Piece {

	public SangPiece(int id) {
		super(id);
		// TODO Auto-generated constructor stub
	}

	private static int[] kr = new int[] { -3, -3, -2, 2, 3, 3, 2, -2 };
	private static int[] kc = new int[] { -2, 2, 3, 3, 2, -2, -3, -3 };
	private static int[][] br = new int[][] { { -1, -1, 0, 0, 1, 1, 0, 0 },
			{ -2, -2, -1, 1, 2, 2, 1, -1 } };
	private static int[][] bc = new int[][] { { 0, 0, 1, 1, 0, 0, -1, -1 },
			{ -1, 1, 2, 2, 1, -1, -2, -2 } };

	@Override
	public ArrayList<Matrix> GetCandidate(int[][] board, Boolean isNight) {
		ArrayList<Matrix> listCand = new ArrayList<Matrix>();

		for (int i = 0; i < 8; i++) {
			int nr = this.GetPosition().row + kr[i];
			int nc = this.GetPosition().col + kc[i];
			int mr = this.GetPosition().row + br[0][i];
			int mc = this.GetPosition().col + bc[0][i];
			int or = this.GetPosition().row + br[1][i];
			int oc = this.GetPosition().col + bc[1][i];

			if (!Function.InnerBoard(nr, nc, 0, 0, Board.MAXROW, Board.MAXCOL)) {
				continue;
			}

			if (board[nr][nc] * this.GetTeam() <= 0 && board[mr][mc] + board[or][oc] == 0) {
				listCand.add(new Matrix(nr, nc));
			}
		}

		return listCand;
	}

	@Override
	public void Reset() {
		super.ResetPosition();
		this.SetHp(30);
		this.SetAtk(15);
	}

	@Override
	public int GetAtk(Boolean isNight) {
		return this.GetAtk();
	}
}
