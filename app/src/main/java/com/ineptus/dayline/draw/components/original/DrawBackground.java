package com.ineptus.dayline.draw.components.original;

import android.graphics.Paint;
import android.graphics.RectF;

import com.ineptus.dayline.Contour;

public class DrawBackground {
	
	
	public static void white(Contour c) {
		
		
		RectF r = new RectF(0, 0, c.width, c.height);
	    Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
	    p.setColor(0x44111111);
	    
	    c.canvas.drawRoundRect(r, 20, 20, p);
		
	}

}
