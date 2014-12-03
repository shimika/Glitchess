package nanami.mashiro.glitchess.screen;

import java.util.ArrayList;

import nanami.mashiro.glitchess.core.Board;
import nanami.mashiro.glitchess.core.Function;
import nanami.mashiro.glitchess.core.Play;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class GameScreen implements Screen {
	private SpriteBatch spriteBatch;

	private Texture bgDay, bgNight, imgMain, imgNext;
	private Texture imgLeftOn, imgRightOn, imgLeftOff, imgRightOff;
	private Texture imgLeftNoteOn, imgRightNoteOn, imgLeftNoteOff, imgRightNoteOff;
	private Texture[] imgPiece, imgPieceName, imgScript, imgSelect, imgTarget, imgFlare;
	private Texture[][] imgNumber;
	private Texture imgLeftWin, imgRightWin;

	private Texture imgMenuBack;

	private enum Status {
		SplashDisposing, ChangeDayAndNight, Pending, Moving, Shooting, Finished,
	};

	private Status nowStatus;

	private int splashMargin;
	private int opacity, nightOpacity, victoryOpacity;

	public GameScreen() {
		nowStatus = Status.SplashDisposing;
		splashMargin = 0;
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		spriteBatch.begin();

		spriteBatch.setColor(1.0f, 1.0f, 1.0f, 1.0f);

		// Draw Board
		if (nowStatus == Status.SplashDisposing) {
			opacity = 0;
			victoryOpacity = 0;
			splashMargin = ScreenFunction.EasingFunction(splashMargin, 720, 10);

			// Gdx.app.log("Easing", String.format("%d", splashMargin));

			spriteBatch.draw(bgDay, 0, splashMargin / 2 - 360, 1280, 720);
			spriteBatch.draw(imgMain, 0, splashMargin, 1280, 720);

			splashMargin = Math.min(720, splashMargin);
			Gdx.app.log("Opening", splashMargin + "");

			if (splashMargin == 720) {
				nowStatus = Status.Pending;
			}
		} else {
			spriteBatch.draw(bgDay, 0, splashMargin / 2 - 360, 1280, 720);

			if (Play.IsNight()) {
				nightOpacity = Math.min(50, nightOpacity + 1);
				spriteBatch.setColor(1.0f, 1.0f, 1.0f, (float) nightOpacity / 100);
			} else {
				nightOpacity = Math.max(0, nightOpacity - 1);
				spriteBatch.setColor(1.0f, 1.0f, 1.0f, (float) nightOpacity / 100);
			}

			spriteBatch.draw(imgMenuBack, 0, 0, 1280, 720);
		}

		// Draw flare
		if (!Play.IsNight() && nowStatus != Status.SplashDisposing) {
			spriteBatch.setColor(1, 1, 1, 1);
			spriteBatch.draw(imgFlare[Play.GetTurn()], 0, 0, 1280, 720);
		}

		int exceptID = 0;
		int targetID = Play.GetTargetID();

		// If finished
		if (Play.GetWinTeam() != 0) {
			nowStatus = Status.Finished;
			DrawComponent(0, 1, 0);

			victoryOpacity = Math.min(100, victoryOpacity + 1);

			spriteBatch.setColor(1.0f, 1.0f, 1.0f, (float) victoryOpacity / 100);
			if (Play.GetWinTeam() > 0) {
				spriteBatch.draw(imgLeftWin, 128, 104, 1024, 512);
			} else {
				spriteBatch.draw(imgRightWin, 128, 104, 1024, 512);
			}
		} else {
			if (targetID != 0) {
				nowStatus = Status.Shooting;
			} else {
				exceptID = Play.GetMovingID();

				if (exceptID != 0) {
					nowStatus = Status.Moving;
				} else if (nowStatus != Status.SplashDisposing) {
					nowStatus = Status.Pending;
				}
			}

			opacity = Math.min(100, opacity + 5);

			if (nowStatus == Status.Pending) {
				DrawComponent(Play.GetTeam(), opacity, exceptID);
			} else if (nowStatus == Status.Moving || nowStatus == Status.Shooting) {
				DrawComponent(Play.GetTeam() * -1, opacity, exceptID);
			}
		}

		if (nowStatus == Status.Shooting) {
			int damage = Play.GetDamage();

			Matrix matrix = Board.GetPiecePosition(targetID);
			Coord coord = Function.GetCoord(matrix);

			DrawDamage(damage, coord);
			// Gdx.app.log("Matrix", damage + " " + targetID + " : " +
			// matrix.row + " : " + matrix.col);
		}

		if (nowStatus == Status.Moving) {
			Coord nowCoord = Play.GetMovingPosition();
			Coord towCoord = Play.GetMovingTarget();

			int x = ScreenFunction.EasingFunction(nowCoord.x, towCoord.x, 5);
			int y = nowCoord.y;
			if (nowCoord.x == towCoord.x) {
				y = ScreenFunction.EasingFunction(nowCoord.y, towCoord.y, 5);
			}

			DrawPiece(exceptID, new Coord(x, y));
			Play.SetMovingTarget(new Coord(x, y));
		}

		if (Gdx.input.justTouched()) {
			if (nowStatus == Status.Pending) {
				int x = Gdx.input.getX();
				int y = Gdx.input.getY();

				Matrix matrix = Function.GetMatrix(x, y);
				Gdx.app.log("Tap", x + ", " + y + " -> " + matrix.row + " by " + matrix.col);

				Play.Select(matrix);
			}
		}

		if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE) && nowStatus != Status.SplashDisposing) {
			// isPaused = !isPaused;
			opacity = 0;
			victoryOpacity = 0;
			Play.ResetGame();

			nowStatus = Status.Pending;
		}

		if (false) {
			spriteBatch.setColor(1.0f, 1.0f, 1.0f, 0.8f);
			spriteBatch.draw(imgMenuBack, 0, 0, 1280, 720);
		}

		spriteBatch.end();
	}

	private void DrawPiece(int id, int team, Matrix matrix) {
		int imgid = Board.GetImageID(id);
		Boolean isDead = Board.GetPieceHP(id) <= 0;

		// This piece is dead
		if (isDead) {
			return;
		}

		int size = Board.GetSize(id, true, true);
		Coord cd = Function.GetCoord(matrix.row, matrix.col);

		if (id * team >= 0) {
			spriteBatch.setColor(1.0f, 1.0f, 1.0f, (float) opacity / 100);
		} else {
			spriteBatch.setColor(1.0f, 1.0f, 1.0f, Math.min((float) opacity / 100, 0.75f));
		}

		spriteBatch.draw(imgPiece[imgid], cd.x - size / 2, 720 - cd.y - size / 2, size, size);

		String hpString = String.format("%2d/%2d", Board.GetPieceHP(id),
				Board.GetPieceAtk(id, Play.IsNight()));

		Coord numCoord = cd;
		numCoord.x -= 30;
		numCoord.y = 720 - numCoord.y - size / 2 - 20;

		DrawString(hpString, numCoord);
	}

	private void DrawPiece(int id, Coord coord) {
		int imgid = Board.GetImageID(id);

		// This piece is dead
		if (imgid == 0) {
			return;
		}

		int size = Board.GetSize(id, true, true);

		spriteBatch.setColor(1.0f, 1.0f, 1.0f, (float) opacity / 100);
		spriteBatch.draw(imgPiece[imgid], coord.x - size / 2, 720 - coord.y - size / 2, size, size);
	}

	private void DrawString(String str, Coord coord) {
		for (char m : str.toCharArray()) {
			if (m == '/') {
				spriteBatch.draw(imgNumber[0][10], coord.x, coord.y, 11, 16);
			} else if (m == ' ') {

			} else {
				spriteBatch.draw(imgNumber[0][Integer.parseInt(Character.toString(m))], coord.x,
						coord.y, 11, 16);
			}
			coord.x += 12;
		}
	}

	private void DrawDamage(int damage, Coord coord) {
		coord.x -= 10;
		coord.y = 720 - coord.y + (30 - Play.GetTimeout() * 30 / 50);

		spriteBatch.setColor(1.0f, 1.0f, 1.0f, (float) Play.GetTimeout() / 50);

		for (char m : Integer.toString(damage).toCharArray()) {
			spriteBatch.draw(imgNumber[1][Integer.parseInt(Character.toString(m))], coord.x,
					coord.y, 30, 40);
			coord.x += 32;
		}
	}

	private void DrawComponent(int team, int op, int exceptID) {
		spriteBatch.setColor(1.0f, 1.0f, 1.0f, 1.0f);

		if (team >= 0) {
			spriteBatch.draw(imgLeftOn, 0, 370, 255, 358);
		} else {
			spriteBatch.draw(imgLeftOff, 0, 370, 255, 358);
		}

		if (team <= 0) {
			spriteBatch.draw(imgRightOn, 1010, 10, 272, 336);
		} else {
			spriteBatch.draw(imgRightOff, 1010, 10, 272, 336);
		}

		int selectID = Play.GetSelectedID();
		int imgType = 0;
		if (selectID != 0) {
			imgType = Board.GetImageID(selectID);
		}

		if (selectID > 0) {
			spriteBatch.draw(imgLeftNoteOn, 0, 0, 285, 413);
			spriteBatch.draw(imgRightNoteOff, 990, 680, 291, 37);

			spriteBatch.draw(imgPieceName[imgType], 60, 260, 135, 50);
			if (Play.IsNight()) {
				spriteBatch.draw(imgScript[imgType % 10], 60, 85, 123, 31);
			}
		} else if (selectID < 0) {
			spriteBatch.draw(imgLeftNoteOff, 0, 0, 280, 33);
			spriteBatch.draw(imgRightNoteOn, 991, 300, 290, 417);

			spriteBatch.draw(imgPieceName[imgType], 1080, 583, 140, 50);
			if (Play.IsNight()) {
				spriteBatch.draw(imgScript[imgType % 10], 1080, 412, 123, 31);
			}
		} else {
			spriteBatch.draw(imgLeftNoteOff, 0, 0, 280, 33);
			spriteBatch.draw(imgRightNoteOff, 990, 680, 291, 37);
		}

		if (selectID != 0) {

			int sizetype = Board.GetSize(selectID, false, false);
			int size = Board.GetSize(selectID, false, true);

			Matrix matrix = Board.GetPiecePosition(selectID);
			Coord cd = Function.GetCoord(matrix.row, matrix.col);

			spriteBatch.draw(imgSelect[sizetype], cd.x - size / 2, 720 - cd.y - size / 2, size,
					size);

			ArrayList<Matrix> list = Play.GetCandMatrix();
			for (Matrix m : list) {
				int id = Board.GetPieceID(m.row, m.col);
				Coord cd2 = Function.GetCoord(m.row, m.col);

				if (id == 0) {
					spriteBatch.draw(imgNext, cd2.x - 16, 720 - cd2.y - 16, 32, 32);
				} else {
					int targetType = Board.GetSize(id, false, false);
					int targetSize = Board.GetSize(id, false, true);
					spriteBatch.draw(imgTarget[targetType], cd2.x - targetSize / 2, 720 - cd2.y
							- targetSize / 2, targetSize, targetSize);
				}
			}
		}

		for (int i = 0; i < Board.MAXROW; i++) {
			for (int j = 0; j < Board.MAXCOL; j++) {
				int id = Board.GetPieceID(i, j);
				if (id != 0 && id != exceptID) {
					DrawPiece(id, team, new Matrix(i, j));
				}
			}
		}
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void show() {
		Gdx.gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
		spriteBatch = new SpriteBatch();

		bgDay = new Texture("img/field/bg_day.jpg");
		bgNight = new Texture("img/field/bg_night.jpg");
		imgMain = new Texture("img/main.jpg");

		imgPiece = new Texture[18];
		imgPieceName = new Texture[18];
		imgScript = new Texture[8];
		for (int i = 0; i <= 1; i++) {
			for (int j = 1; j <= 7; j++) {
				imgPiece[i * 10 + j] = new Texture(String.format("img/piece/%d.png", i * 10 + j));
				imgPieceName[i * 10 + j] = new Texture(String.format("img/script/name/%d.png", i
						* 10 + j));

				if (i == 0) {
					imgScript[j] = new Texture(String.format("img/script/content/%d.png", j));
				}
			}
		}

		imgNext = new Texture("img/piece/next.png");
		imgSelect = new Texture[4];
		imgTarget = new Texture[4];
		for (int i = 1; i <= 3; i++) {
			imgTarget[i] = new Texture(String.format("img/piece/target%d.png", i));
			imgSelect[i] = new Texture(String.format("img/piece/select%d.png", i));
		}

		imgNumber = new Texture[4][11];
		for (int i = 0; i <= 9; i++) {
			imgNumber[0][i] = new Texture(String.format("img/number/small/%d.png", i));
			imgNumber[1][i] = new Texture(String.format("img/number/red/%d.png", i));
		}
		imgNumber[0][10] = new Texture("img/number/small/s.png");

		imgLeftOn = new Texture("img/field/lefton.png");
		imgLeftOff = new Texture("img/field/leftoff.png");
		imgRightOn = new Texture("img/field/righton.png");
		imgRightOff = new Texture("img/field/rightoff.png");

		imgLeftNoteOn = new Texture("img/script/base/lefton.png");
		imgLeftNoteOff = new Texture("img/script/base/leftoff.png");
		imgRightNoteOn = new Texture("img/script/base/righton.png");
		imgRightNoteOff = new Texture("img/script/base/rightoff.png");

		imgFlare = new Texture[13];
		for (int i = 1; i <= 11; i++) {
			imgFlare[i] = new Texture(String.format("img/flare/%d.png", i));
		}

		imgMenuBack = new Texture("img/menu/mask.png");

		imgLeftWin = new Texture("img/victory/left.png");
		imgRightWin = new Texture("img/victory/right.png");

		Play.StartGame();
	}

	@Override
	public void hide() {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void dispose() {
	}
}
