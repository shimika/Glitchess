package nanami.mashiro.glitchess;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;

public class MenuScreen extends AbstractScreen {
	private static GlitchessMain game;

	public MenuScreen(GlitchessMain g) {
		super(game);
		game = g;
	}

	@Override
	public void show() {
		super.show();
		// ���̺� ��������
		Table table = super.getTable();
		table.add("Welcome to Pug GAME").spaceBottom(50);

		// start game ��ư �߰�
		table.row();
		TextButton startGameButton = new TextButton("Start Game", getSkin());
		startGameButton.addListener(new ActorGestureListener() {
			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				super.touchUp(event, x, y, pointer, button);
				if (!checkvalidtouch(Gdx.input.getX(), Gdx.input.getY())) {
					return;
				}
				// ���� ����ȭ������ �̵�����~
				Gdx.app.log(GlitchessMain.LOG, "���� ����ȭ������ �̵� ����!");

				// game.setScreen(new GameScreen(game));
				game.game();
			}
		});
		table.add(startGameButton).size(300, 60).uniform().spaceBottom(10);

		/*
		 * // options ��ư �߰� table.row(); TextButton optionsButton = new
		 * TextButton("Options", getSkin()); optionsButton.addListener(new
		 * ActorGestureListener() {
		 * 
		 * @Override public void touchUp(InputEvent event, float x, float y, int
		 * pointer, int button) { super.touchUp(event, x, y, pointer, button);
		 * if (!checkvalidtouch(Gdx.input.getX(), Gdx.input.getY())) { return; }
		 * } }); table.add(optionsButton).uniform().fill().spaceBottom(10);
		 */

		// exit ��ư �߰�
		table.row();
		TextButton exitButton = new TextButton("Exit", getSkin());
		exitButton.addListener(new ActorGestureListener() {
			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				super.touchUp(event, x, y, pointer, button);
				if (!checkvalidtouch(Gdx.input.getX(), Gdx.input.getY())) {
					return;
				}
				Gdx.app.exit();
			}
		});
		table.add(exitButton).uniform().fill().spaceBottom(10);
	}
}
