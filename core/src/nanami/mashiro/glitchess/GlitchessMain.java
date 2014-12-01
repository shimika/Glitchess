package nanami.mashiro.glitchess;

import com.badlogic.gdx.Game;

public class GlitchessMain extends Game {
	public static String LOG = "Log";

	MenuScreen menuScreen;
	GameScreen gameScreen;
	SplashScreen splashScreen;

	@Override
	public void create() {
		menuScreen = new MenuScreen(this);
		gameScreen = new GameScreen(this);
		splashScreen = new SplashScreen(this);

		// this.setScreen(new MenuScreen(this));
		// this.setScreen(splashScreen);
		this.setScreen(new GameScreen(this));
	}

	public void menu() {
		splashScreen.dispose();
		this.setScreen(menuScreen);
	}

	public void game() {
		menuScreen.dispose();
		this.setScreen(gameScreen);
	}
}
