package com.lxm.tools;

import java.io.File;
import java.nio.ByteBuffer;

import android.util.Log;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.PixmapIO;
import com.badlogic.gdx.utils.ScreenUtils;

public class ScreenshotFactory {

	private static int counter = 1;
	private static int time = 0;// ´ÎÊý
	private static float df = 0.1f;// ¼ä¸ô

	private static void saveScreenshot_ex() {
		try {
			FileHandle fh;
			do {
				new File(Gdx.files.getExternalStoragePath() + "save").mkdir();
				fh = new FileHandle(Gdx.files.getExternalStoragePath()
						+ "save/" + "screenshot" + counter++ + ".png");
			} while (fh.exists());
			Pixmap pixmap = getScreenshot(0, 0, Gdx.graphics.getWidth(),
					Gdx.graphics.getHeight(), true);
			PixmapIO.writePNG(fh, pixmap);
			pixmap.dispose();
		} catch (Exception e) {
			Log.i("lxm", "error!");
		}
	}

	private static Pixmap getScreenshot(int x, int y, int w, int h,
			boolean yDown) {
		final Pixmap pixmap = ScreenUtils.getFrameBufferPixmap(x, y, w, h);

		if (yDown) {
			// Flip the pixmap upside down
			ByteBuffer pixels = pixmap.getPixels();
			int numBytes = w * h * 4;
			byte[] lines = new byte[numBytes];
			int numBytesPerLine = w * 4;
			for (int i = 0; i < h; i++) {
				pixels.position((h - i - 1) * numBytesPerLine);
				pixels.get(lines, i * numBytesPerLine, numBytesPerLine);
			}
			pixels.clear();
			pixels.put(lines);
		}

		return pixmap;
	}

	public static void saveScreenshot(float delta) {
		df += delta;
		if (time < 50) {

			if (df > 0.8f) {
				time += 1;
				df = 0;
				saveScreenshot_ex();
			}
		}
	}
}
