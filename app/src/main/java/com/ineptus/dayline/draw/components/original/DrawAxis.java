package com.ineptus.dayline.draw.components.original;

import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;

import com.ineptus.dayline.Contour;

public class DrawAxis {


	public static void simple(Contour c) {

		Paint p = new Paint();
		p.setColor(0xff554277);

		RectF aRect = new RectF(c.axisX-c.axisWidth/2f, c.marginTop, c.axisX+c.axisWidth/2f, c.height-c.marginBottom);
	    Paint aPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
	    aPaint.setColor(0x99cccccc);

	    c.canvas.drawRoundRect(aRect, c.axisWidth/4, c.axisWidth/4, aPaint);
	}

	public static void shining(Contour c) {
		RectF r = new RectF(c.axisX-c.axisWidth/2f, c.marginTop, c.axisX+c.axisWidth/2f, c.marginTop+c.axisHeight);
	    Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);

	    int[] colors = {0x88cccccc, 0x66cccccc, 0x88cccccc};

	    LinearGradient g = new LinearGradient(c.axisX-c.axisWidth*5, c.marginTop, c.axisX+c.axisWidth*5, c.height-c.marginBottom, colors, null, Shader.TileMode.MIRROR);
	    p.setShader(g);

	    c.canvas.drawRoundRect(r, c.axisWidth/4, c.axisWidth/4, p);
	}


}
