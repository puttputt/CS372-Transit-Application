package com.basementbobs.transitdemo;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;

public class BusMarker extends Drawable {
	
	Paint mPaint;
	
	public BusMarker() {
		mPaint = new Paint();
		mPaint.setARGB(175, (int)(Math.random()*255), (int)(Math.random()*255), (int)(Math.random()*255));
		mPaint.setAntiAlias(true);
	}

	@Override
	public void draw(Canvas canvas) {
		canvas.drawCircle(0, 0, 10, mPaint);
	}

	@Override
	public int getOpacity() {
		return 0;
	}

	@Override
	public void setAlpha(int alpha) {

	}

	@Override
	public void setColorFilter(ColorFilter cf) {

	}

}
