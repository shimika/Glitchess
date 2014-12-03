package nanami.mashiro.glitchess.piece;

import java.util.ArrayList;

import nanami.mashiro.glitchess.core.Board;
import nanami.mashiro.glitchess.core.Function;
import nanami.mashiro.glitchess.screen.Matrix;

public class PoPiece extends Piece {

	public PoPiece(int id) {
		super(id);
		// TODO Auto-generated constructor stub
	}

	private static int[] kr = new int[] { 1, 0, -1, 0 };
	private static int[] kc = new int[] { 0, 1, 0, -1 };

	@Override
	public ArrayList<Matrix> GetCandidate(int[][] board, Boolean isNight) {
		ArrayList<Matrix> listCand = new ArrayList<Matrix>();

		for (int i = 0; i < 4; i++) {
			Boolean over = false;
			for (int j = 1;; j++) {
				int nr = this.GetPosition().row + kr[i] * j;
				int nc = this.GetPosition().col + kc[i] * j;

				if (!Function.InnerBoard(nr, nc, 0, 0, Board.MAXROW, Board.MAXCOL)) {
					break;
				}

				if (board[nr][nc] == 0) {
					if (over) {
						listCand.add(new Matrix(nr, nc));
					}
				} else {
					// if po
					int nid = board[nr][nc];
					int type = Math.abs(nid);

					if (type >= 6 && type <= 7) {
						break;
					} else {
						if (over) {
							if (board[nr][nc] * this.GetTeam() < 0 && !isNight) {
								listCand.add(new Matrix(nr, nc));
							}
							break;
						} else {
							over = true;
						}
					}
				}
			}
		}

		return listCand;
	}

	@Override
	public void Reset() {
		super.ResetPosition();
		this.SetHp(15);
		this.SetAtk(25);
	}

	@Override
	public int GetAtk(Boolean isNight) {
		if (isNight) {
			return 0;
		}
		return this.GetAtk();
	}
}
