package nanami.mashiro.glitchess.piece;

import java.util.ArrayList;

import nanami.mashiro.glitchess.core.Board;
import nanami.mashiro.glitchess.core.Function;
import nanami.mashiro.glitchess.screen.Matrix;

public class JolPiece extends Piece {
	public JolPiece(int id) {
		super(id);
	}

	private static int[] kr = new int[] { 1, 0, -1 };
	private static int[] kc = new int[] { 0, 1, 0 };

	@Override
	public ArrayList<Matrix> GetCandidate(int[][] board, Boolean isNight) {
		ArrayList<Matrix> listCand = new ArrayList<Matrix>();
		int lim = isNight ? 2 : 1;

		for (int i = 0; i < 3; i++) {
			for (int j = 1; j <= lim; j++) {
				int nr = this.GetPosition().row + kr[i] * j;
				int nc = this.GetPosition().col + kc[i] * j * this.GetTeam();

				if (!Function.InnerBoard(nr, nc, 0, 0, Board.MAXROW, Board.MAXCOL)) {
					break;
				}

				if (board[nr][nc] * this.GetTeam() <= 0) {
					listCand.add(new Matrix(nr, nc));

					if (board[nr][nc] * this.GetTeam() < 0) {
						break;
					}
				} else {
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
		this.SetAtk(15);
	}

	@Override
	public int GetAtk(Boolean isNight) {
		if (isNight) {
			return this.GetAtk() * 3;
		}
		return this.GetAtk();
	}
}
