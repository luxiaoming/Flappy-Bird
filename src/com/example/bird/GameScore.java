package com.example.bird;

import android.util.Log;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.utils.Array;

public class GameScore {

	private int Score = 0;
	private BitmapFont font;
	private float x = 0;
	private int bitNum = 1;// ¼ÆËãÎ»Êý
	private float W = 0;
	private float H = 0;
	private Array<TextureRegion> Numlist = new Array<TextureRegion>();

	// 8*14
	public GameScore(TextureAtlas atlas) {
		// TODO Auto-generated constructor stub
		Numlist.add(atlas.findRegion("number_large_00"));
		Numlist.add(atlas.findRegion("number_large_01"));
		Numlist.add(atlas.findRegion("number_large_02"));
		Numlist.add(atlas.findRegion("number_large_03"));
		Numlist.add(atlas.findRegion("number_large_04"));
		Numlist.add(atlas.findRegion("number_large_05"));
		Numlist.add(atlas.findRegion("number_large_06"));
		Numlist.add(atlas.findRegion("number_large_07"));
		Numlist.add(atlas.findRegion("number_large_08"));
		Numlist.add(atlas.findRegion("number_large_09"));
		W = atlas.findRegion("number_large_00").getRegionWidth() / 40f;
		H = atlas.findRegion("number_large_00").getRegionHeight() / 40f;
	}

	public void addScore(int Score) {
		this.Score += Score;
	}

	public void cleanScore() {
		this.Score = 0;
	}

	public void draw(SpriteBatch batch) {
		// TODO Auto-generated method stub
		float firstposx = 4 + ((bitNum - 1) * W) / 2 - W / 2;
		int currNum = 0;
		int score = Score / 2;
		bitNum = String.valueOf(score).length();
		for (int index = bitNum; index > 0; index--) {
			currNum = score % 10;
			score = score / 10;

			batch.draw(Numlist.get(currNum), firstposx - (bitNum - index) * W,
					10.0f, W / 2, H / 2, W, H, 1f, 1f, 0);
			;

		}
	}
}
