package com.ineptus.dayline.draw.components.original;

import android.graphics.Paint;
import android.graphics.RectF;

import com.ineptus.dayline.Contour;
import com.ineptus.dayline.containers.LineBox;



public class DrawBoxesShadow {
	
	
	public static void simply(Contour c) {
        
		int eventLeft = c.axisX-c.eventWidth/2;
		int eventRight = c.axisX+c.eventWidth/2;
		
		if(c.line.moveToFirst()) {
			do{ 
				LineBox current = c.line.current();
				
				if(!current.isFree()) {	
			        RectF eRect = new RectF();	
					float top = (float) (current.getRelStart()*c.pxPerMili + c.marginTop);
					float bottom = (float) (current.getRelEnd()*c.pxPerMili + c.marginTop);
					eRect.set(eventLeft, top+1, eventRight, bottom+1);
					
					Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
					
					p.setShadowLayer(3, 1, 3, 0x66000000);
					
					c.canvas.drawRect(eRect, p);
				}
				
			} while(c.line.moveToNext());
			
		}
	}
}
