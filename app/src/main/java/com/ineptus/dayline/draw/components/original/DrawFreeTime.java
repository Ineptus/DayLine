package com.ineptus.dayline.draw.components.original;

import android.graphics.Paint;
import android.graphics.RectF;

import com.ineptus.dayline.Contour;
import com.ineptus.dayline.containers.LineBox;

public class DrawFreeTime {
	
	public static void simply(Contour c, LineBox box) {
		
	    RectF eRect = new RectF();
	    Paint ePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		
		
		float top = (float) (box.getRelStart()*c.pxPerMili + c.marginTop);
		float bottom = (float) (box.getRelEnd()*c.pxPerMili + c.marginTop);
		eRect.set(c.axisX-c.eventWidth/2f, top, c.axisX+c.eventWidth/2f, bottom+1);
		
		
		ePaint.setColor(0xff559922);
		ePaint.setAlpha(100);
		c.canvas.drawRect(eRect, ePaint);
		
	}
	
	public static void green(Contour c, LineBox box) {
		
	    RectF eRect = new RectF();
	    Paint ePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		
		
		float top = (float) (box.getRelStart()*c.pxPerMili + c.marginTop);
		float bottom = (float) (box.getRelEnd()*c.pxPerMili + c.marginTop);
		eRect.set(c.axisX-c.eventWidth/2f, top, c.axisX+c.eventWidth/2f, bottom+1);
		
		
		ePaint.setColor(0xff559922);
		ePaint.setAlpha(255);
		c.canvas.drawRect(eRect, ePaint);
		
	}
	
	
}
