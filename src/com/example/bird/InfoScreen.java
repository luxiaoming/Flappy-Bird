package com.example.bird;

import android.util.Log;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;

public class InfoScreen extends ScreenAdapter {

	SpriteBatch Batch;
	private TextureAtlas atlas;
	private Sprite sprite;
	FirstGame Gm;

	public InfoScreen(FirstGame tGm) {
		// TODO Auto-generated constructor stub
		this.Gm = tGm;
		Batch = new SpriteBatch();
		atlas = new TextureAtlas(Gdx.files.internal("data/show.pack"));
		sprite = atlas.createSprite("splash");
		sprite.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		new Timer().scheduleTask(new Task() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				Log.i("lxmlxm", "timer ok!");
				Gm.setScreen(new MainMenuScreen(Gm));
			}
		}, 2);

	}

	@Override
	public void render(float delta) {
		// TODO Auto-generated method stub
		super.render(delta);
		Batch.begin();
		sprite.draw(Batch);
		Batch.end();

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		super.resume();
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		super.pause();
		Gdx.input.setInputProcessor(null);
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		super.hide();
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		super.dispose();
		Batch.dispose();
		atlas.dispose();

	}

}
