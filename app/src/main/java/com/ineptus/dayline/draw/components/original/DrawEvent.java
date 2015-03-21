package com.ineptus.dayline.draw.components.original;

import android.graphics.Paint;
import android.graphics.RectF;

import com.ineptus.dayline.Contour;
import com.ineptus.dayline.containers.LineBox;

public class DrawEvent {

	public static void simply(Contour c, LineBox box) {
		
	    RectF eRect = new RectF();
	    Paint ePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		//ePaint.setShadowLayer(2, 1, 1, 0x55000000);
		
		float top = (float) (box.getRelStart()*c.pxPerMili + c.marginTop);
		float bottom = (float) (box.getRelEnd()*c.pxPerMili + c.marginTop);
		eRect.set(c.axisX-c.eventWidth/2f, top, c.axisX+c.eventWidth/2f, bottom+1);
		
		ePaint.setColor(box.getFirstColor());
		ePaint.setAlpha(255);
		c.canvas.drawRect(eRect, ePaint);
		
		ePaint.setStyle(Paint.Style.STROKE);
		ePaint.setColor(0x22000000);
		ePaint.setStrokeWidth(2);
		//c.canvas.drawRect(eRect, ePaint);
		
	}
	
}
