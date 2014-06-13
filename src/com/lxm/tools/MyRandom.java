package com.lxm.tools;

import java.util.Random;

public class MyRandom {
	private static MyRandom mr;
	private Random r;

	public static MyRandom getInstance() {
		if (mr == null) {
			mr = new MyRandom();// 创建一个随机数
		}
		return mr;
	}

	public MyRandom() {
		// TODO Auto-generated constructor stub
		r = new Random(1000);// 创建一个随机数
	}

	// 返回 0-max之间的数字
	public int getInt(int max) {
		return r.nextInt(max + 1);

	}

	// 返回 min-max之间的数字
	public int getInt(int min, int max) {
		return min + r.nextInt(max + 1 - min);

	}

	// 返回 min-max之间的float数字
	public float getFloat(float min, float max) {
		return min + (max - min) * r.nextFloat();

	}

	// 返回 0-max之间的float数字
	public float getFloat(float max) {
		return max * r.nextFloat();

	}

}
