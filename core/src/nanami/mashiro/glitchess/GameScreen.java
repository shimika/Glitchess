package nanami.mashiro.glitchess;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;

public class GameScreen implements Screen {
	private SpriteBatch spriteBatch;
	private ShapeRenderer shapeRenderer;
	private Texture img;
	private GlitchessMain myGame;

	private enum Status {
		Loading, Animating, Pending, Paused,
	};

	private Status status = Status.Pending;

	public GameScreen(GlitchessMain g) {
		myGame = g;
	}

	float x = 0, y = 0, lx = -1, ly = -1;
	float dx, dy;
	int count;

	@Override
	public void render(float delta) {
		if (Gdx.input.justTouched() && status == Status.Pending) {
			lx = Gdx.input.getX();
			ly = 720 - Gdx.input.getY();

			dx = (lx - x) / 120;
			dy = (ly - y) / 120;

			Gdx.app.log("Tap", Gdx.input.getX() + " : " + Gdx.input.getY());

			status = Status.Loading;
		}

		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		spriteBatch.begin();

		if (status == Status.Pending) {
			spriteBatch.draw(img, x, y, 300, 300);
		} else {
			x += dx;
			y += dy;

			count++;
			if (count == 120) {
				count = 0;
				status = Status.Pending;
			}

			spriteBatch.setColor(1.0f, 1.0f, 1.0f, 0.5f);
			spriteBatch.draw(img, x, y, 300, 300);
		}

		spriteBatch.end();

		int width = 100;
		int height = 70;

		int maxrow = 9;
		int maxcol = 8;

		Gdx.gl20.glLineWidth(5);
		shapeRenderer.identity();
		shapeRenderer.setColor(0, 0, 1, 1);

		// Vertical Line
		for (int i = 0; i <= maxcol; i++) {
			drawDottedLine(shapeRenderer, 5, 0, i * height, maxrow * width, i * height);
		}

		// Horizon line
		for (int i = 0; i <= maxrow; i++) {
			drawDottedLine(shapeRenderer, 3, i * width, 0, i * width, maxcol * height);
		}
	}

	private void drawDottedLine(ShapeRenderer shapeRenderer, int dotDist, float x1, float y1, float x2, float y2) {
		shapeRenderer.begin(ShapeType.Point);

		int al = 50;
		x1 += al;
		y1 += al;
		x2 += al;
		y2 += al;

		Vector2 vec2 = new Vector2(x2, y2).sub(new Vector2(x1, y1));
		float length = vec2.len();
		for (int i = 0; i < length; i += dotDist) {
			vec2.clamp(length - i, length - i);
			shapeRenderer.point(x1 + vec2.x, y1 + vec2.y, 0);
		}

		shapeRenderer.end();
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void show() {
		Gdx.gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
		spriteBatch = new SpriteBatch();
		shapeRenderer = new ShapeRenderer();
		img = new Texture("kaminari.png");
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
