package nanami.mashiro.glitchess.core;

import java.util.ArrayList;

import nanami.mashiro.glitchess.screen.Coord;
import nanami.mashiro.glitchess.screen.Matrix;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

public class Play {
	public static void StartGame() {
		Board.InitBoard();
		ResetGame();

		soundMove = Gdx.audio.newSound(Gdx.files.internal("sound/move.mp3"));
		soundHit = Gdx.audio.newSound(Gdx.files.internal("sound/hit.mp3"));
		soundDestroy = Gdx.audio.newSound(Gdx.files.internal("sound/destroy.mp3"));
		soundWin = Gdx.audio.newSound(Gdx.files.internal("sound/win.mp3"));
	}

	private static int team, selectedID, turn, winTeam;
	private static Boolean isNight;
	private static ArrayList<Matrix> candList;

	private static int movingID;
	private static Coord movingPosition, movingTarget;

	private static int targetID, damage, timeout;

	private static Sound soundBGM, soundWin, soundHit, soundDestroy, soundMove;

	public static int GetTeam() {
		return team;
	}

	public static int GetWinTeam() {
		return winTeam;
	}

	public static Boolean IsNight() {
		return isNight;
	}

	public static int GetSelectedID() {
		return selectedID;
	}

	public static ArrayList<Matrix> GetCandMatrix() {
		return candList;
	}

	// Hit animation

	public static int GetTargetID() {
		timeout--;
		if (timeout <= 0) {
			return 0;
		}
		return targetID;
	}

	public static int GetDamage() {
		return damage;
	}

	public static void SetTargetID(int val) {
		damage = val;
	}

	public static int GetTimeout() {
		return timeout;
	}

	// Moving animation

	public static int GetMovingID() {
		if (movingPosition.x < 0) {
			return 0;
		}
		return movingID;
	}

	public static Coord GetMovingPosition() {
		return movingPosition;
	}

	public static Coord GetMovingTarget() {
		return movingTarget;
	}

	public static void SetMovingTarget(Coord position) {
		movingPosition = position;
		if (movingPosition.equals(movingTarget)) {
			movingID = 0;
			movingPosition = new Coord();
		}
	}

	public static void ResetGame() {
		team = 1;
		turn = 0;
		selectedID = 0;
		isNight = false;
		winTeam = 0;

		targetID = 0;
		movingID = 0;
		movingPosition = new Coord();

		Board.ResetPiece();

		StopMusic();
		soundBGM = Gdx.audio.newSound(Gdx.files.internal("sound/game.mp3"));
		long id = soundBGM.play(0.3f);
		soundBGM.setLooping(id, true);
	}

	private static void StopMusic() {
		try {
			soundWin.stop();
			soundBGM.stop();
		} catch (Exception e) {
		}
	}

	private static void SetNight() {
		int mod = turn % 15;
		if (mod < 11) {
			isNight = false;
		} else {
			isNight = true;
		}
	}

	public static int GetTurn() {
		if (isNight) {
			return 0;
		}
		return turn % 15 + 1;
	}

	private static Boolean IsCandExists() {
		int count = 0;
		Boolean flag = false;
		for (int i = 1; i <= 16; i++) {
			if (Board.GetPieceHP(i * team) <= 0) {
				continue;
			}
			count++;

			ArrayList<Matrix> list = Board.GetCandidatePosition(i * team, isNight);
			Gdx.app.log("GetCand", i * team + " : " + isNight);
			if (list.size() > 0) {
				flag = true;
			}
		}

		if (count <= 1) {
			return false;
		}

		return flag;
	}

	public static void Select(Matrix matrix) {
		// Invailed coordinate select
		if (matrix.row < 0 || matrix.col < 0) {
			return;
		}
		int id = Board.GetPieceID(matrix.row, matrix.col);

		// not selected and click space
		if (selectedID == 0 && id == 0) {
			return;
		}

		// click same piece
		if (id == selectedID) {
			selectedID = 0;
			return;
		} else {
			// not selected or click same team but another piece
			if ((selectedID == 0 || selectedID * id > 0) && team * id > 0) {
				selectedID = id;
				// If id == 0, candList is null
				candList = Board.GetCandidatePosition(id, isNight);

				Gdx.app.log("ChangeSelect", "Changed to " + selectedID);
			} else {
				Boolean isInCand = false;
				if (selectedID != 0) {
					for (Matrix mCand : candList) {
						if (mCand.row == matrix.row && mCand.col == matrix.col) {
							isInCand = true;
							break;
						}
					}
				}

				if (isInCand) {
					Gdx.app.log("Play - Check id", id + "");
					if (id != 0) {
						int hp = Board.GetPieceHP(id);
						int atk = Function.GetRandomDamage(Board.GetPieceAtk(selectedID, isNight));

						atk *= 100;

						timeout = 50;
						targetID = id;
						damage = atk;

						Board.SetPieceHP(id, hp - atk);

						if (Board.GetPieceHP(id) <= 0) {
							soundDestroy.play();
						} else {
							soundHit.play();
						}

						if (Board.GetPieceHP(16) <= 0 || Board.GetPieceHP(-16) <= 0) {
							if (Board.GetPieceHP(16) <= 0) {
								// Right win
								winTeam = -1;
								selectedID = 0;
							} else if (Board.GetPieceHP(-16) <= 0) {
								// Left win
								winTeam = 1;
								selectedID = 0;
							}

							StopMusic();
							soundWin.play();
						}
					}
					Gdx.app.log("King", Board.GetPieceHP(16) + " : " + Board.GetPieceHP(-16));

					if ((id == 0 || Board.GetPieceHP(id) <= 0) && winTeam == 0) {
						if (Math.abs(selectedID) != 16 || !isNight) {
							movingID = selectedID;
							movingPosition = Function.GetCoord(Board.GetPiecePosition(selectedID));
							movingTarget = Function.GetCoord(matrix);
							Board.SetPiecePosition(selectedID, matrix);

							soundMove.play();
						}
					}

					selectedID = 0;
					turn++;
					team *= -1;

					Board.RefreshBoard();

					/*
					 * for (int i = 1; i >= -1; i -= 2) { for (int j = 1; j <=
					 * 16; j++) { System.out.print(String.format("(%3d: %d)", i
					 * * j, Board.GetPieceHP(i * j))); } System.out.println(); }
					 */

					SetNight();

					Gdx.app.log("CandExists", team + " : " + IsCandExists());

					if (!IsCandExists()) {
						winTeam = team * -1;
						selectedID = 0;
					}
				}

				// Gdx.app.log("ChangeSelect", isInCand + "");
			}
		}
	}
}
