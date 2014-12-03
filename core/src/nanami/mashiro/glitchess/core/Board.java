package nanami.mashiro.glitchess.core;

import java.util.ArrayList;
import java.util.Arrays;

import nanami.mashiro.glitchess.piece.ChaPiece;
import nanami.mashiro.glitchess.piece.JolPiece;
import nanami.mashiro.glitchess.piece.KingPiece;
import nanami.mashiro.glitchess.piece.MaPiece;
import nanami.mashiro.glitchess.piece.Piece;
import nanami.mashiro.glitchess.piece.PoPiece;
import nanami.mashiro.glitchess.piece.SaPiece;
import nanami.mashiro.glitchess.piece.SangPiece;
import nanami.mashiro.glitchess.screen.Matrix;

import com.badlogic.gdx.Gdx;

public class Board {
	public static final int MAXROW = 9, MAXCOL = 10;

	private static Piece[] pieceCollection;
	private static int[][] board;

	public static void InitBoard() {
		pieceCollection = new Piece[32];
		board = new int[Board.MAXROW][Board.MAXCOL];

		int count = 0, type;

		for (int i = 1; i >= -1; i -= 2) {
			for (int j = 1; j <= 16; j++) {
				type = Function.GetType(j);

				switch (type) {
				case 1:
					pieceCollection[count] = new JolPiece(i * j);
					break;
				case 2:
					pieceCollection[count] = new PoPiece(i * j);
					break;
				case 3:
					pieceCollection[count] = new ChaPiece(i * j);
					break;
				case 4:
					pieceCollection[count] = new SangPiece(i * j);
					break;
				case 5:
					pieceCollection[count] = new MaPiece(i * j);
					break;
				case 6:
					pieceCollection[count] = new SaPiece(i * j);
					break;
				case 7:
					pieceCollection[count] = new KingPiece(i * j);
					break;

				default:
					Gdx.app.log("Error", "Board : Cannot get type");
					return;
				}

				count++;
			}
		}

		ResetPiece();
	}

	public static void ResetPiece() {
		for (int i = 0; i < pieceCollection.length; i++) {
			pieceCollection[i].Reset();
		}
		RefreshBoard();
	}

	public static void RefreshBoard() {
		for (int[] row : board) {
			Arrays.fill(row, 0);
		}
		for (int i = 0; i < pieceCollection.length; i++) {
			if (pieceCollection[i].GetHp() <= 0) {
				continue;
			}

			// Gdx.app.log("Check", String.format("%d : %d %d",
			// pieceCollection[i].GetID(),
			// pieceCollection[i].GetPosition().row,
			// pieceCollection[i].GetPosition().col));

			board[pieceCollection[i].GetPosition().row][pieceCollection[i].GetPosition().col] = pieceCollection[i]
					.GetID();
		}
		/*
		 * for (int i = 0; i < MAXROW; i++) { for (int j = 0; j < MAXCOL; j++) {
		 * // System.out.print(String.format("%3d ", board[i][j]));
		 * 
		 * System.out.print(String.format("%3d ", board[i][j])); }
		 * System.out.println(); } System.out.println();
		 */
	}

	public static int GetPieceID(int row, int col) {
		return board[row][col];
	}

	private static Piece GetPiece(int id) {
		for (int i = 0; i < pieceCollection.length; i++) {
			if (pieceCollection[i].GetID() == id) {
				return pieceCollection[i];
			}
		}
		return null;
	}

	public static Matrix GetPiecePosition(int id) {
		return GetPiece(id).GetPosition();
	}

	public static void SetPiecePosition(int id, Matrix matrix) {
		GetPiece(id).SetPosition(matrix);
	}

	public static int GetImageID(int id) {
		return GetPiece(id).GetImageID();
	}

	public static int GetSize(int id, Boolean isPiece, Boolean pixel) {
		int size = GetPiece(id).GetSize();
		if (pixel) {
			switch (size) {
			case 1:
				return isPiece ? 40 : 64;
			case 2:
				return isPiece ? 48 : 128;
			case 3:
				return isPiece ? 76 : 128;
			default:
				return 0;
			}
		}
		return size;
	}

	public static int GetPieceHP(int id) {
		return GetPiece(id).GetHp();
	}

	public static void SetPieceHP(int id, int hp) {
		GetPiece(id).SetHp(hp);
	}

	public static int GetPieceAtk(int id, Boolean isNight) {
		return GetPiece(id).GetAtk(isNight);
	}

	public static ArrayList<Matrix> GetCandidatePosition(int id, Boolean isNight) {
		return GetPiece(id).GetCandidate(Board.board, isNight);
	}
}
