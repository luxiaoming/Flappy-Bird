package com.example.bird;

import android.util.Log;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pools;

public class Hero extends Actor {

	Animation hreo;
	float stateTime = 0f;
	TextureRegion Draw;
	private int status = eIsRuning;
	static final public int eIsOvering = 1;
	static final public int eIsPause = 2;
	static final public int eIsRuning = 3;
	static final public int eIsOver = 4;
	Animation hreoOver;

	public Hero(float df, Array<TextureRegion> Texturelist,
			Array<TextureRegion> TextureOverlist) {
		// TODO Auto-generated constructor stub
		super();
		hreo = new Animation(df, Texturelist);
		hreoOver = new Animation(0.16f, TextureOverlist);
	}

	@Override
	public void act(float delta) {
		// TODO Auto-generated method stub
		super.act(delta);
		stateTime += delta;
		if (status == eIsRuning) {
			Draw = hreo.getKeyFrame(stateTime, true);
		} else if (status == eIsOvering) {
			Draw = hreoOver.getKeyFrame(stateTime, false);
			Log.i("isGameOver", stateTime + "--" + hreoOver.isAnimationFinished(stateTime));
			if (hreoOver.isAnimationFinished(stateTime)) {
				status = eIsOver;
			}
		}

	}

	public void setStatus(int Status) {
		status = Status;
		if (status == eIsOvering) {
			stateTime = 0; // 清除，开始计算over动画
		}
	}

	public boolean isOver() {
		return status == eIsOver;
	}

	@Override
	public void draw(SpriteBatch batch, float parentAlpha) {
		// TODO Auto-generated method stub
		super.draw(batch, parentAlpha);
		batch.draw(Draw, getX(), getY());
	}

	public Rectangle getrectangle() {
		Rectangle Tmp = Pools.obtain(Rectangle.class);
		Tmp.set(getX() + 28, getY() + 28, 38, 92 - 8);// 去掉周边因素
		return Tmp;

	}

}
