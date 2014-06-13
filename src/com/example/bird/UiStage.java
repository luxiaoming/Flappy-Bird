package com.example.bird;

import android.util.Log;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.InputEvent.Type;
import com.badlogic.gdx.utils.Pools;

public class UiStage extends Stage {
	public UiStage() {
		this(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false, null);
	}

	/**
	 * Creates a stage with the specified
	 * {@link #setViewport(float, float, boolean) viewport} that doesn't keep
	 * the aspect ratio. The stage will use its own {@link SpriteBatch}, which
	 * will be disposed when the stage is disposed.
	 */
	public UiStage(float width, float height) {
		this(width, height, false, null);
	}

	/**
	 * Creates a stage with the specified
	 * {@link #setViewport(float, float, boolean) viewport}. The stage will use
	 * its own {@link SpriteBatch}, which will be disposed when the stage is
	 * disposed.
	 */
	public UiStage(float width, float height, boolean keepAspectRatio) {
		this(width, height, keepAspectRatio, null);
	}

	/**
	 * Creates a stage with the specified
	 * {@link #setViewport(float, float, boolean) viewport} and
	 * {@link SpriteBatch}. This can be used to avoid creating a new SpriteBatch
	 * (which can be somewhat slow) if multiple stages are used during an
	 * applications life time.
	 * 
	 * @param batch
	 *            Will not be disposed if {@link #dispose()} is called. Handle
	 *            disposal yourself.
	 */
	public UiStage(float width, float height, boolean keepAspectRatio,
			SpriteBatch batch) {
		super(width, height, keepAspectRatio, batch);
	}

}
