package com.example.bird;

import android.util.Log;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pools;

public class bird extends Actor {

	Animation bird;
	float stateTime = 0f;
	TextureRegion Draw;

	public bird(float df, Array<TextureRegion> Texturelist) {
		// TODO Auto-generated constructor stub
		super();
		bird = new Animation(df, Texturelist);

	}

	@Override
	public void act(float delta) {
		// TODO Auto-generated method stub
		super.act(delta);
		stateTime += delta;
		Draw = bird.getKeyFrame(stateTime, true);

	}

	@Override
	public void draw(SpriteBatch batch, float parentAlpha) {
		// TODO Auto-generated method stub
		super.draw(batch, parentAlpha);
		batch.draw(Draw, getX(), getY());
	}

}
