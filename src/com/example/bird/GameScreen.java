package com.example.bird;

import android.util.Log;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;

public class GameScreen extends ScreenAdapter {

	SpriteBatch Batch;
	private TextureAtlas atlas;
	private Sprite sprite;
	FirstGame Gm;
	private OrthographicCamera camera;
	bird bd;
	Array<TextureRegion> Texturelist = new Array<TextureRegion>();
	Stage Ui;
	Image Title;
	Image Rate;
	private Image button_play;
	private Sprite background;
	private int df_x = 0;
	float df = 0;
	private int size;

	private void creatPipe(Stage Ui) {

	}

	// //// 缩放值为 320*560
	public GameScreen(FirstGame tGm) {
		// TODO Auto-generated constructor stub
		camera = new OrthographicCamera(320, 560);
		camera.position.set(320 / 2, 560 / 2, 0);
		Ui = new Stage();
		this.Gm = tGm;
		Batch = new SpriteBatch();
		atlas = new TextureAtlas(Gdx.files.internal("data/bird/birdImgs.atlas"));
		sprite = atlas.createSprite("bg_day");
		sprite.setSize(320, 560);
		float h;
		float w;
		h = atlas.findRegion("title").getRegionHeight();
		w = atlas.findRegion("title").getRegionWidth();
		Title = new Image(atlas.findRegion("title"));
		// 标题
		Title.setPosition(320 / 2 - w / 2, 560 - 180);
		Texturelist.clear();
		Texturelist.add(atlas.findRegion("bird0_0"));
		Texturelist.add(atlas.findRegion("bird0_1"));
		Texturelist.add(atlas.findRegion("bird0_2"));
		float h1 = atlas.findRegion("bird0_0").getRegionHeight();
		float w1 = atlas.findRegion("bird0_0").getRegionWidth();
		bd = new bird(0.16f, Texturelist);
		bd.setPosition(320 / 2 - w1 / 2, Title.getY() - h - 20);
		bd.addAction(Actions.forever(Actions.sequence(
				Actions.moveBy(0, -10, 0.3f), Actions.moveBy(0, 10, 0.3f))));
		// 小鸟
		h = atlas.findRegion("button_rate").getRegionHeight();
		w = atlas.findRegion("button_rate").getRegionWidth();
		Rate = new Image(atlas.findRegion("button_rate"));
		Rate.setPosition(320 / 2 - w / 2, bd.getY() - h1 - 20);
		Rate.addListener(new ClickListener() {
			public void clicked(InputEvent event, float x, float y) {
				MainActivity.showAdStatic(1);// 这里启动插屏广告
			}
		});
		// Rate
		// play
		h1 = atlas.findRegion("button_play").getRegionHeight();
		w1 = atlas.findRegion("button_play").getRegionWidth();
		button_play = new Image(atlas.findRegion("button_play"));
		button_play.setPosition(320 / 2 - w1 / 2, Rate.getY() - h - 40);
		button_play.addListener(new ClickListener() {
			public void clicked(InputEvent event, float x, float y) {
				Gm.setScreen(new RunScreen(Gm));
			}
		});
		background = new Sprite(atlas.findRegion("land"));
		// 加入场景里面
		Ui.addActor(bd);
		Ui.addActor(Title);
		Ui.addActor(Rate);
		Ui.addActor(button_play);
		// 给场景添加摄像机
		Ui.setCamera(camera);
		Gdx.input.setInputProcessor(Ui);
	}

	@Override
	public void render(float delta) {
		// TODO Auto-generated method stub
		super.render(delta);
		camera.update();
		Batch.setProjectionMatrix(camera.combined);
		Batch.begin();
		sprite.draw(Batch);
		df_x = df_x - 3;
		background.setX(df_x % 336);
		background.draw(Batch);
		background.setX(336 + df_x % 336);
		background.draw(Batch);
		Batch.end();
		Ui.act();
		Ui.draw();
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
		Ui.dispose();

	}

}
