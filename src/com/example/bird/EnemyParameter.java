package com.example.bird;

import com.badlogic.gdx.math.Rectangle;

public class EnemyParameter {
	Rectangle rectangle = new Rectangle();
	boolean isover; // 是否已经挂掉
	int Type; // 敌人类别
	int AttackValue; // 攻击值
	int Health;// 生命值
	float df; // 挂掉之后流失时间
	float OverTime; // 挂掉时间
	boolean isHit=false; // 是否被打中
	public EnemyParameter() {
		// TODO Auto-generated constructor stub
	}

	public void set(int x, int y, int W, int H, int Type, int AttackValue,
			int Health) {
		// TODO Auto-generated constructor stub
		rectangle.x = x;
		rectangle.y = y;
		rectangle.width = W;
		rectangle.height = H;
		this.Type = Type;
		this.AttackValue = AttackValue;
		this.Health = Health;
	}

}