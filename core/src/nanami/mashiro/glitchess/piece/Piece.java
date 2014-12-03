package nanami.mashiro.glitchess.piece;

import java.util.ArrayList;

import nanami.mashiro.glitchess.core.Function;
import nanami.mashiro.glitchess.screen.Matrix;

public abstract class Piece {
	private int id, type, imgID, size, team;
	private Matrix position;

	private int nowHp, maxHp, atk;

	public Piece(int id) {
		this.id = id;
		this.type = Function.GetType(this.id);
		this.imgID = Function.GetImageID(this.type, this.id);
		this.size = Function.GetSize(this.id);
		this.team = id > 0 ? 1 : -1;

		this.nowHp = this.maxHp = 1;

		this.Reset();
	}

	public void ResetPosition() {
		this.position = Function.GetStartPosition(this.id);
	}

	public int GetID() {
		return this.id;
	}

	public int GetTeam() {
		return this.team;
	}

	public int GetType() {
		return this.type;
	}

	public int GetImageID() {
		return this.imgID;
	}

	public int GetSize() {
		return this.size;
	}

	public Matrix GetPosition() {
		return this.position;
	}

	public void SetPosition(Matrix position) {
		this.position = position;
	}

	public void SetPosition(int row, int col) {
		this.position.row = row;
		this.position.col = col;
	}

	public int GetHp() {
		return this.nowHp;
	}

	public void SetHp(int hp) {
		this.nowHp = hp;
	}

	protected void SetAtk(int atk) {
		this.atk = atk;
	}

	protected int GetAtk() {
		return this.atk;
	}

	public abstract ArrayList<Matrix> GetCandidate(int[][] board, Boolean isNight);

	public abstract void Reset();

	public abstract int GetAtk(Boolean isNight);
}
