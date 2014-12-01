package nanami.mashiro.glitchess;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

public class AbstractScreen implements Screen {
	protected final GlitchessMain game;
	protected final Stage stage;

	private Table table;
	private Skin skin;
	private TextureAtlas atlas;

	private double px, py;

	public AbstractScreen(GlitchessMain game) {
		this.game = game;
		this.stage = new Stage();
	}

	protected Table getTable() {
		if (table == null) {
			table = new Table(getSkin());
			table.setFillParent(true);
			stage.addActor(table);
		}
		return table;
	}

	protected Skin getSkin() {
		if (skin == null) {
			FileHandle skinFile = Gdx.files.internal("skin/uiskin.json");
			skin = new Skin(skinFile);
		}
		return skin;
	}

	/**
	 * �ش� Ŭ���� �̸� ��Ʈ������ ��ȯ�ϴ� �Լ�
	 */
	protected String getName() {
		return getClass().getSimpleName();
	}

	/**
	 * atlas ��������
	 */
	TextureAtlas getAtlas() {
		return null;
		/*
		 * if (atlas == null) { atlas = new
		 * TextureAtlas(Gdx.files.internal("textures/textures.atlas")); } return
		 * atlas;
		 */
	}

	protected boolean checkvalidtouch(double x, double y) {
		Gdx.app.log("Tap", x + ", " + y + " : " + px + ", " + py);
		return Math.max(Math.abs(x - px), Math.abs(y - py)) < 5;
	}

	@Override
	public void render(float delta) {
		// RGB ���������� ȭ���� Ŭ������
		Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		if (Gdx.input.justTouched()) {
			px = Gdx.input.getX();
			py = Gdx.input.getY();
		}

		// actor ������Ʈ
		stage.act(delta);

		// actor�� �׸���
		stage.draw();
	}

	@Override
	public void resize(int width, int height) {
		Gdx.app.log(GlitchessMain.LOG, "ȭ�� ũ�� ����: " + getName() + " �� " + width + " x " + height);
	}

	@Override
	public void show() {
		Gdx.input.setInputProcessor(this.stage);
		Gdx.app.log(GlitchessMain.LOG, "ȭ���� ������: " + getName());
	}

	@Override
	public void hide() {
		Gdx.app.log(GlitchessMain.LOG, "ȭ���� ����: " + getName());
	}

	@Override
	public void pause() {
		Gdx.app.log(GlitchessMain.LOG, "ȭ���� ����: " + getName());
	}

	@Override
	public void resume() {
		Gdx.app.log(GlitchessMain.LOG, "ȭ���� �簳: " + getName());
	}

	@Override
	public void dispose() {
		Gdx.app.log(GlitchessMain.LOG, "ȭ���� ��ȯ: " + getName());
		if (skin != null) {
			skin.dispose();
		}
		if (stage != null) {
			stage.dispose();
		}
		if (atlas != null) {
			atlas.dispose();
		}
	}
}