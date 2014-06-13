package com.example.bird;

import cn.waps.AppConnect;
import android.app.Activity;
import android.util.Log;

public class FirstGame extends Game {

	MainActivity Main;

	public FirstGame(MainActivity Main) {

		// TODO Auto-generated constructor stub
		this.Main = Main;
	}

	@Override
	public void create() {
		// this.setScreen(new InfoScreen(this));
		this.setScreen(new InfoScreen(this));
	}

	public void EndGame() {
		Main.finish();
	}

	public void ShowOff() {
		Main.showAdStatic(0);
	}

}
