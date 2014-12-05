package nanami.mashiro.glitchess.screen;

import com.badlogic.gdx.Game;

public class GlitchessMain extends Game {
	public static String LOG = "Log";

	GameScreen gameScreen;
	SplashScreen splashScreen;

	@Override
	public void create() {
		gameScreen = new GameScreen();
		splashScreen = new SplashScreen(this);

		// this.setScreen(gameScreen);
		this.setScreen(splashScreen);
	}

	public void StartGame() {
		this.setScreen(gameScreen);
	}
}
