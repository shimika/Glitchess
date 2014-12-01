package nanami.mashiro.glitchess;

import nanami.mashiro.glitchess.tween.SpriteAccessor;
import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenManager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class SplashScreen implements Screen {
	private SpriteBatch spriteBatch;
	private Sprite splash;
	private Texture splashimage;
	private TweenManager tweenManager;
	private static GlitchessMain game;

	public SplashScreen(GlitchessMain g) {
		game = g;
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		tweenManager.update(delta);

		spriteBatch.begin();
		splash.draw(spriteBatch);
		spriteBatch.end();

		if (Gdx.input.justTouched()) {
			// game.setScreen(new MenuScreen(game));
			game.menu();
			Gdx.app.log("Tap", Gdx.input.getX() + " : " + Gdx.input.getY());
		}
	}

	@Override
	public void show() {
		spriteBatch = new SpriteBatch();
		tweenManager = new TweenManager();
		Tween.registerAccessor(Sprite.class, new SpriteAccessor());

		Texture splashTexture = new Texture("yuubari.png");
		splash = new Sprite(splashTexture);
		splash.setSize(300, 300);

		Tween.set(splash, SpriteAccessor.ALPHA).target(0).start(tweenManager);
		Tween.to(splash, SpriteAccessor.ALPHA, 2).target(1).start(tweenManager);
		Tween.to(splash, SpriteAccessor.ALPHA, 2).target(0).delay(2).start(tweenManager).setCallback(callbackAtBegin).setCallbackTriggers(TweenCallback.COMPLETE);
	}

	private static TweenCallback callbackAtBegin = new TweenCallback() {
		@Override
		public void onEvent(int type, BaseTween<?> source) {
			if (type == TweenCallback.BEGIN) {
				Gdx.app.log("Begin", "!!");
			} else {
				Gdx.app.log("Complete", "!!");
				game.setScreen(new GameScreen(game));
			}
		}
	};

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
	}
}
