package nanami.mashiro.glitchess.desktop;

import nanami.mashiro.glitchess.screen.GlitchessMain;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class DesktopLauncher {
	public static void main(String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "GlitChess";
		config.width = 1280;
		config.height = 720;
		config.resizable = false;

		new LwjglApplication(new GlitchessMain(), config);
	}
}
