package com.example.bird;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

public class MyImageButton extends Actor {

	private Drawable imageUp;
	private Label Lab;

	public MyImageButton(Drawable imageUp) {
		// TODO Auto-generated constructor stub
		this.imageUp = imageUp;
	}

	@Override
	public void act(float delta) {
		// TODO Auto-generated method stub
		super.act(delta);
	}

	public void setLabel(Label Lab) {
		Lab.setPosition(getX(), getY());
		Lab.setSize(getWidth(), getHeight());
		Lab.setAlignment(Align.center | Align.top);
		this.Lab = Lab;

	}

	@Override
	public void draw(SpriteBatch batch, float parentAlpha) {
		// TODO Auto-generated method stub
		super.draw(batch, parentAlpha);
		if (imageUp != null) {
			imageUp.draw(batch, getX(), getY(), getWidth(), getHeight());
		}
		Lab.draw(batch, parentAlpha);

	}
}
