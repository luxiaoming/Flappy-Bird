package com.example.bird;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.WindowManager;
import cn.waps.AdInfo;
import cn.waps.AppConnect;

import com.badlogic.gdx.backends.android.AndroidApplication;

public class MainActivity extends AndroidApplication {
	Game Gm;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		AppConnect.getInstance("dca242344a805d9afae7b59c263148f6", "default",
				this);
		AppConnect.getInstance(this).initPopAd(this);
		AppConnect.getInstance(this).initUninstallAd(this);
		Gm = new FirstGame(MainActivity.this);
		initialize(Gm, false);
		mContext = this;
	}

	private static Handler handler = handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {

			case 0:
				// 显示推荐列表（综合）
				AppConnect.getInstance(mContext).showOffers(mContext);
				break;
			case 1:
				// 显示插屏广告
				// 判断插屏广告是否已初始化完成，用于确定是否能成功调用插屏广告
				boolean hasPopAd = AppConnect.getInstance(mContext).hasPopAd(
						mContext);
				if (hasPopAd) {
					AppConnect.getInstance(mContext).showPopAd(mContext);
				}
				break;
			case 2:
				// 显示推荐列表（软件）
				AppConnect.getInstance(mContext).showAppOffers(mContext);
				break;
			case 3:
				// 显示推荐列表（游戏）
				AppConnect.getInstance(mContext).showGameOffers(mContext);
				break;

			case 8:
				// 显示自家应用列表
				AppConnect.getInstance(mContext).showMore(mContext);
				break;
			case 9:
				// 根据指定的应用app_id展示其详情
				AppConnect.getInstance(mContext).showMore(mContext,
						"dca242344a805d9afae7b59c263148f6");
				break;
			case 10:
				// 调用功能广告接口（使用浏览器接口）
				String uriStr = "http://www.baidu.com";
				AppConnect.getInstance(mContext).showBrowser(mContext, uriStr);
				break;
			case 11:
				// 用户反馈
				AppConnect.getInstance(mContext).showFeedback(mContext);
				break;

			}
		}
	};
	private static Context mContext;

	public static void showAdStatic(int adTag) {
		Message msg = handler.obtainMessage();
		msg.what = adTag; // 私有静态的整型变量，开发者请自行定义值
		handler.sendMessage(msg);
	}

}
