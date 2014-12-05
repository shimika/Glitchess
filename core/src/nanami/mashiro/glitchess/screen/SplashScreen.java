package nanami.mashiro.glitchess.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class SplashScreen implements Screen {
	private SpriteBatch spriteBatch;
	private Texture imgSplash, imgMain;

	private static GlitchessMain game;
	private static Boolean isMenu = false;

	private static Sound soundMain;

	private enum SoundStatus {
		Play, Stop,
	};

	SoundStatus soundStatus = SoundStatus.Stop;

	public SplashScreen(GlitchessMain g) {
		game = g;
	}

	int count = 0;

	@Override
	public void render(float delta) {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		spriteBatch.begin();

		if (count == 350) {
			isMenu = true;
		}

		if (isMenu) {
			if (soundStatus == SoundStatus.Stop) {
				soundStatus = SoundStatus.Play;
				long id = soundMain.play();
				soundMain.setLooping(id, true);
			}

			spriteBatch.setColor(1, 1, 1, 1);
			spriteBatch.draw(imgMain, 0, 0, 1280, 720);
		} else {
			float opacity = 1;
			if (count <= 60) {
				opacity = (float) count / 60;
			} else if (count > 250) {
				opacity = Math.max(0, ((float) (310 - count) / 60));
			}

			spriteBatch.setColor(1, 1, 1, opacity);
			spriteBatch.draw(imgSplash, 0, 0, 1280, 720);

			count++;
		}

		spriteBatch.end();

		if (Gdx.input.justTouched() && isMenu) {
			soundMain.stop();

			Sound soundStart = Gdx.audio.newSound(Gdx.files.internal("sound/startup.mp3"));
			soundStart.play();

			game.StartGame();
			Gdx.app.log("Tap", Gdx.input.getX() + " : " + Gdx.input.getY());
		}
	}

	@Override
	public void show() {
		spriteBatch = new SpriteBatch();

		imgSplash = new Texture("img/splash.png");
		imgMain = new Texture("img/main.jpg");

		soundMain = Gdx.audio.newSound(Gdx.files.internal("sound/main.mp3"));
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
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
		soundMain.dispose();
	}
}
