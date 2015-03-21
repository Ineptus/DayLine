package com.ineptus.dayline.draw.components.original;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.Log;

import com.ineptus.dayline.Contour;
import com.ineptus.dayline.containers.Event;
import com.ineptus.dayline.containers.Layers;
import com.ineptus.dayline.containers.LineBox;



public class DrawBoxes {
	
	
	public static void newWay(Contour c) {
        
		Paint tP = new Paint(Paint.ANTI_ALIAS_FLAG);
		tP.setTextSize(c.textSize);
		tP.setTextAlign(Align.LEFT);
		tP.setTypeface(Typeface.DEFAULT_BOLD);
		
		Layers layers = new Layers();
		
		int eventLeft = c.axisX-c.eventWidth/2;
		int eventRight = c.axisX+c.eventWidth/2;
		
		if(c.line.moveToFirst()) {
			do{ 
				LineBox current = c.line.current();
				
				if(!current.isFree()) {					
					layers.update(current.getEvents().getList());
					
			        RectF eRect = new RectF();	
					float top = (float) (current.getRelStart()*c.pxPerMili + c.marginTop);
					float bottom = (float) (current.getRelEnd()*c.pxPerMili + c.marginTop);
					
					eRect.set(eventLeft, top, eventRight, bottom+1);	
					
					for(Event e : current.getEvents().getList()) {
						Paint p = layers.getPaint(e);
						c.canvas.drawRect(eRect, p);
					}
				}
			} while(c.line.moveToNext());	
		}
	}
	
	public static void simply(Contour c) {
        
		int i = 1;
		Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
		p.setTextSize(c.textSize);
		p.setTextAlign(Align.LEFT);
		p.setTypeface(Typeface.DEFAULT_BOLD);
		
		
		if(c.line.moveToFirst()) {
			do{ 
				if(c.line.current().isFree()) {	
					Log.d("DrawBoxes", "Drawing FreeTime");
					//DrawFreeTime.simply(c, c.line.current());	
				} else if(c.line.current().isCollision()) {	
					Log.d("DrawBoxes", "Drawing Collision");
					DrawCollision.newStripes(c, c.line.current());
				} else {
					Log.d("DrawBoxes", "Drawing "+c.line.current().getEvents().get(0).title);
					DrawEvent.simply(c, c.line.current());
				}
				
				//DEBUGGING
				
				p.setColor(Color.RED);
				p.setShadowLayer(1, 0, 0, Color.YELLOW);
				c.canvas.drawText(""+i + " - " + c.line.current().getDuration()/60000, 0, (float) (c.getBoxCenterY(c.line.current())+c.textSize*0.7), p);
				i++;
				
				//--
				
			} while(c.line.moveToNext());
			
		}
	}
	
}
