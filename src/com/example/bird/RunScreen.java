package com.example.bird;

import android.util.Log;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.lxm.tools.BoxWorld;
import com.lxm.tools.MyRandom;

public class RunScreen extends ScreenAdapter implements InputProcessor {
	Vector3 testPoint = new Vector3();
	SpriteBatch Batch;
	private TextureAtlas atlas; // 加载资源使用
	private Sprite sprite; // 背景
	FirstGame Gm; // game接口
	private OrthographicCamera camera; // 正交照相机，为了将世界投向box的世界里面
	private Sprite background; // 地面背景
	private float df_x = 0;
	float df = 0; // 为了获取小鸟的第几帧做的变量
	World world; // box 世界
	boolean is_set_impo = false; // 当开始掉落，给个向下的速度，保证快速落下
	/** our boxes **/
	private Array<MyBody> boxes = new Array<MyBody>();// 当前没有用，后面为管道做的结构
	private AtlasRegion land; // 大地
	Array<TextureRegion> Texturelist = new Array<TextureRegion>();
	private Animation birdAni; // 小鸟动画
	Body birdBox; // 小鸟的box
	float bird_y; // 纪录小鸟的当前y值，作为后面计算是否为上升使用它
	private Box2DDebugRenderer debugRenderer; // 调试渲染，为找出代码问题，做的模拟
	private boolean isfall;
	private AtlasRegion pipe_down;
	private AtlasRegion pipe_up;
	private short BirdBit = 1 << 0;
	private short GroudBit = 1 << 1;
	private short PipeBit = 1 << 2;
	private boolean isover;
	private float timeforcreatPipe = 0f;
	private boolean isstart = false;
	private Sprite tutorial;
	GameScore GS;
	private Sprite text_ready;
	Stage Ui;
	private Image button_play;
	private Image button_score;

	// 创建管道，当前还未实现
	private void creatPipe() {
		MyBody tmp = new MyBody();
		float pos_f = MyRandom.getInstance().getFloat(7.0f) - 0.4f;
		tmp.body = BoxWorld.createBoxes(world, 0.65f, 4f,
				BodyDef.BodyType.DynamicBody, 8 + 0.65f, pos_f, 1f,
				(short) PipeBit, (short) BirdBit, false);
		tmp.body.setGravityScale(0f);// 失重，不要受重力影响
		tmp.body.setLinearVelocity(-2f, 0f);
		tmp.isup = false;
		tmp.body.setFixedRotation(true);
		boxes.add(tmp);
		tmp = new MyBody();
		tmp.body = BoxWorld.createBoxes(world, 0.65f, 4f,
				BodyDef.BodyType.DynamicBody, 8 + 0.65f,
				(float) (pos_f + 10.67), 1f, (short) PipeBit, (short) BirdBit,
				false);
		tmp.body.setGravityScale(0f);// 失重，不要受重力影响
		tmp.body.setLinearVelocity(-2f, 0f);
		tmp.isup = true;
		tmp.body.setFixedRotation(true);
		boxes.add(tmp);
	}

	// //// 缩放值为 320*560---8*14
	public RunScreen(FirstGame tGm) {
		// TODO Auto-generated constructor stub
		Batch = new SpriteBatch();
		Gm = tGm;
		// 照相机
		camera = new OrthographicCamera(8, 14);
		camera.position.set(8 / 2, 14 / 2, 0);
		atlas = new TextureAtlas(Gdx.files.internal("data/bird/birdImgs.atlas"));
		// 背景
		if (MyRandom.getInstance().getInt(1) == 0) {
			sprite = atlas.createSprite("bg_day");
		} else {
			sprite = atlas.createSprite("bg_night");
		}
		Ui = new Stage();
		Ui.setCamera(camera);
		float h1 = atlas.findRegion("button_play").getRegionHeight() / 40f;
		float w1 = atlas.findRegion("button_play").getRegionWidth() / 40f;
		button_play = new Image(atlas.findRegion("button_play"));
		button_play.setPosition(8 / 2 - w1 - 0.2f, (12 - h1) / 2);
		button_play.setSize(w1, h1);
		button_play.addListener(new ClickListener() {
			public void clicked(InputEvent event, float x, float y) {
				// 重新开始
				isover = false;
				boxes.clear();
				GS.cleanScore();
				// birdBox.s
				world.destroyBody(birdBox);
				isstart = false;
				isfall = false;
				// 构造一个鸟
				birdBox = BoxWorld.createBoxes(world, 0.3f, 0.3f,
						BodyDef.BodyType.DynamicBody, 3f, 7f, 1f,
						(short) BirdBit, (short) (GroudBit | PipeBit), false);
				Gdx.input.setInputProcessor(RunScreen.this);
			}
		});
		Ui.addActor(button_play);
		h1 = atlas.findRegion("button_score").getRegionHeight() / 40f;
		w1 = atlas.findRegion("button_score").getRegionWidth() / 40f;
		button_score = new Image(atlas.findRegion("button_score"));
		button_score.setPosition(8 / 2 + 0.2f, (12 - h1) / 2);
		button_score.setSize(w1, h1);
		button_score.addListener(new ClickListener() {
			public void clicked(InputEvent event, float x, float y) {
				Gm.ShowOff();
			}
		});
		Ui.addActor(button_score);
		GS = new GameScore(atlas);
		tutorial = atlas.createSprite("tutorial");
		tutorial.setSize(2.85f, 2.45f);
		tutorial.setPosition(2.8f, 5.6f);
		text_ready = atlas.createSprite("text_ready");
		text_ready.setSize(4.9f, 1.3f);
		text_ready.setPosition(1.8f, 8.2f);
		// 大地
		land = atlas.findRegion("land");
		sprite.setSize(8, 14);
		pipe_down = atlas.findRegion("pipe_down");// 52/40=1.3f 320/40=8
		pipe_up = atlas.findRegion("pipe_up");
		// 创建世界box
		world = BoxWorld.createPhysicsWorld();
		world.setContactListener(new ContactListener() {

			@Override
			public void preSolve(Contact contact, Manifold oldManifold) {
				// TODO Auto-generated method stub

			}

			@Override
			public void postSolve(Contact contact, ContactImpulse impulse) {
				// TODO Auto-generated method stub

			}

			@Override
			public void endContact(Contact contact) {
				// TODO Auto-generated method stub

			}

			@Override
			public void beginContact(Contact contact) {
				// TODO Auto-generated method stub
				// Log.i("lxm", contact.)
				isover = true;
				Gdx.input.setInputProcessor(Ui);
			}
		});
		// 构造一个鸟
		birdBox = BoxWorld.createBoxes(world, 0.3f, 0.3f,
				BodyDef.BodyType.DynamicBody, 3f, 7f, 1f, (short) BirdBit,
				(short) (GroudBit | PipeBit), false);

		// 构造一个地面
		BoxWorld.createBoxes(world, 5f, 1.4f, BodyDef.BodyType.StaticBody, 5f,
				1.4f, 1f, (short) GroudBit, (short) BirdBit, false);
		creatPipe();
		// 小鸟动画
		int index = MyRandom.getInstance().getInt(2);
		if (index == 0) {
			Texturelist.add(atlas.findRegion("bird0_0"));
			Texturelist.add(atlas.findRegion("bird0_1"));
			Texturelist.add(atlas.findRegion("bird0_2"));
		} else if (index == 1) {
			Texturelist.add(atlas.findRegion("bird1_0"));
			Texturelist.add(atlas.findRegion("bird1_1"));
			Texturelist.add(atlas.findRegion("bird1_2"));
		} else {
			Texturelist.add(atlas.findRegion("bird2_0"));
			Texturelist.add(atlas.findRegion("bird2_1"));
			Texturelist.add(atlas.findRegion("bird2_2"));
		}
		birdAni = new Animation(0.16f, Texturelist, Animation.LOOP);
		// 配套的动画
		background = new Sprite(atlas.findRegion("land")); // 大地
		background.setSize(8.4f, 2.8f);
		debugRenderer = new Box2DDebugRenderer();
		Gdx.input.setInputProcessor(this);
	}

	@Override
	public void render(float delta) {
		// TODO Auto-generated method stub
		super.render(delta);
		df_x -= 1.5f;
		df += delta;
		if (!isstart) {
			Vector2 position = birdBox.getPosition();
			camera.update();
			Batch.setProjectionMatrix(camera.combined);
			Batch.begin();
			sprite.draw(Batch);// 画背景
			Batch.draw(birdAni.getKeyFrame(df), position.x - 0.6f,
					position.y - 0.6f, 0.6f, 0.6f, 1.2f, 1.2f, 1f, 1f, 0);
			tutorial.draw(Batch);
			text_ready.draw(Batch);
			float x = (df_x % 336) / 40;
			background.setX(x);
			background.draw(Batch);
			background.setX(336 / 40 + x);
			background.draw(Batch);
			GS.draw(Batch);
			Batch.end();
			return;
		}
		timeforcreatPipe += delta;
		if (isover) {
			for (MyBody pipe : boxes) {
				pipe.body.setType(BodyType.StaticBody);

			}
		}
		// 游戏未结束，继续创建管道
		if (timeforcreatPipe > 2.5f && !isover && isstart) {
			timeforcreatPipe = 0f;
			creatPipe();
		}

		// 如果相撞，则设置管道和小鸟不碰撞，穿过
		if (isover) {
			for (int index = boxes.size - 1; index >= 0; index--) {
				MyBody pipe = boxes.get(index);
				Fixture Fixture = pipe.body.getFixtureList().get(0);
				Filter FilterData = Fixture.getFilterData();
				FilterData.maskBits = PipeBit;
				Fixture.setFilterData(FilterData);
			}
		}
		BoxWorld.renden(world);
		// 在世界前模拟会丢失当前的一次处理，因此搬在这里，实现的非常完美，呵呵
		Vector2 position = birdBox.getPosition();
		// 是否下落
		if (bird_y > position.y) {
			// 下落
			isfall = true;
			if (!is_set_impo) {
				// birdBox.setLinearVelocity(0, -2.5f);
				is_set_impo = true;

			}
		} else {
			is_set_impo = false; // 向上清除掉这个
		}

		bird_y = position.y; // 记录着当前小鸟的y值
		Vector2 posVector2;
		// 有些多余，主要是测试使用，所以这样子去写了
		if (isfall) {
			posVector2 = new Vector2(position.x - 1, position.y);// 制作俯角90度
		} else {
			posVector2 = new Vector2(position.x + 1, position.y + 2f);// 制作仰角45度
		}
		Vector2 toTarget = new Vector2(posVector2.sub(position));
		float desiredAngle = MathUtils.atan2(toTarget.x, toTarget.y);
		float totalRotation = desiredAngle - birdBox.getAngle();
		// Log.i("newAngle----", "desiredAngle=" + desiredAngle
		// * MathUtils.radiansToDegrees + "totalRotation" + totalRotation
		// * MathUtils.radiansToDegrees);
		float angle = MathUtils.radiansToDegrees * birdBox.getAngle();
		while (totalRotation < -180 * MathUtils.degreesToRadians)
			totalRotation += 360 * MathUtils.degreesToRadians;//
		// 调整设定角度到-180到180度之间
		while (totalRotation > 180 * MathUtils.degreesToRadians)
			totalRotation -= 360 * MathUtils.degreesToRadians;
		float change = 8 * MathUtils.degreesToRadians; // 每次间隔允许最大旋转角度
		if (isfall) {
			change = 10 * MathUtils.degreesToRadians;
		}
		float newAngle = birdBox.getAngle()
				+ Math.min(change, Math.max(-change, totalRotation));
		// 如果上升阶段，
		if (!isfall && newAngle < 0) {
			// Log.i("jiaodu", "" + newAngle * MathUtils.radiansToDegrees);
			newAngle = 25 * MathUtils.degreesToRadians;
		} else {
			// Log.i("jiaodu", "" + newAngle * MathUtils.radiansToDegrees);
		}
		// Log.i("newAngle", newAngle + "--start");
		// 卡下角度
		if (newAngle < -90 * MathUtils.degreesToRadians) {
			newAngle = -90 * MathUtils.degreesToRadians;
			// Log.i("newAngle", "-90");
		}

		if (newAngle > 30 * MathUtils.degreesToRadians) {
			newAngle = 30 * MathUtils.degreesToRadians;
			// Log.i("newAngle", ">30");
		}
		// Log.i("newAngle", newAngle + "--end");
		birdBox.setTransform(birdBox.getPosition(), newAngle);

		position = birdBox.getPosition();

		camera.update();
		Batch.setProjectionMatrix(camera.combined);
		Batch.begin();

		sprite.draw(Batch);// 画背景

		for (int index = boxes.size - 1; index >= 0; index--) {
			MyBody pipe = boxes.get(index);
			Vector2 pos = pipe.body.getPosition();
			if (pipe.isup) {
				Batch.draw(pipe_down, pos.x - 0.65f, pos.y - 4f, 0.65f, 4f,
						1.3f, 8f, 1f, 1f, 0);
			} else {
				Batch.draw(pipe_up, pos.x - 0.65f, pos.y - 4f, 0.65f, 4f, 1.3f,
						8f, 1f, 1f, 0);
			}
			if (pos.x + 0.65f < 3f) {
				if (!pipe.isScore) {
					GS.addScore(1);
					pipe.isScore = true;
				}
			}
			if (pos.x + 0.65f < 0f) {
				world.destroyBody(pipe.body);
				boxes.removeIndex(index);
			}
		}
		// 这里需要将坐标位置调整到减去鸟的宽高加上originX originY的大小，原因是模拟和绘制坐标不一致
		Batch.draw(birdAni.getKeyFrame(df), position.x - 0.6f,
				position.y - 0.6f, 0.6f, 0.6f, 1.2f, 1.2f, 1f, 1f, angle);
		float x = (df_x % 336) / 40;
		background.setX(x);
		background.draw(Batch);
		background.setX(336 / 40 + x);
		background.draw(Batch);
		GS.draw(Batch);
		Batch.end();
		if (isover) {
			Ui.act();
			Ui.draw();
		}
		// 调试使用代码
		// debugRenderer.render(world, camera.combined);

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		super.resume();
		Gdx.input.setInputProcessor(this);
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
		super.show();
		Gdx.input.setInputProcessor(this);
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
		debugRenderer.dispose();
	}

	@Override
	public boolean keyDown(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		testPoint.set(screenX, screenY, 0);
		camera.unproject(testPoint);// 通过这个接口，将原始坐标转化为照相机对应坐标
		isstart = true;
		if (birdBox.getPosition().y < 14 && !isover) {
			birdBox.setLinearVelocity(0, 7.5f);// (0f, 300f,
			birdBox.setAwake(true);
			// 如果小鸟高度小于照相机投射高度，增加一个线性速度，y方向
		}

		// 改变行为
		isfall = false;
		return true;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}

}
