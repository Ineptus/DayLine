package com.ineptus.dayline.draw.components.original;

import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;

import com.ineptus.dayline.Contour;
import com.ineptus.dayline.containers.Event;
import com.ineptus.dayline.containers.EventsList;
import com.ineptus.dayline.containers.LineBox;

public class DrawCollision {

	public static void obliqueStripes(Contour c, LineBox box) {
		
        RectF eRect = new RectF();
		
		float top = (float) (box.getRelStart()*c.pxPerMili + c.marginTop);
		float bottom = (float) (box.getRelEnd()*c.pxPerMili + c.marginTop);
		eRect.set(c.axisX-c.eventWidth/2f, top, c.axisX+c.eventWidth/2f, bottom+1);
		
		Paint ePaint = new Paint(Paint.ANTI_ALIAS_FLAG);

		int nColors = box.getEvents().size();
		
	    int[] colors = new int[nColors*3+1];
	        
	    for(int i = 0; i < nColors; i++) {
	    	colors[i*3] = box.getEvents().get(i).color;
	    	colors[i*3+1] = box.getEvents().get(i).color;  
	    	colors[i*3+2] = box.getEvents().get(i).color; 
	    }
	    
	    colors[nColors*3] = box.getEvents().get(0).color;
	    
	    int[] newColors = new int[nColors*3-2];
	    
	    for(int i = 0; i<nColors*3-2; i++) {
	    	newColors[i]=colors[i+1];
	    }
	    
	    LinearGradient gradient = new LinearGradient(0, 0, 3*nColors, 4*nColors, colors, null, Shader.TileMode.REPEAT);
	    ePaint.setShader(gradient);
	    ePaint.setAlpha(255);			
		c.canvas.drawRect(eRect, ePaint);
		
		
	}
	
	
	public static void newStripes(Contour c, LineBox box) {
		
        RectF eRect = new RectF();
		
		float top = (float) (box.getRelStart()*c.pxPerMili + c.marginTop);
		float bottom = (float) (box.getRelEnd()*c.pxPerMili + c.marginTop);
		eRect.set(c.axisX-c.eventWidth/2f, top, c.axisX+c.eventWidth/2f, bottom+1);
		
		Paint ePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		
		//GET EVENTS LIST, TRASH FREE
		EventsList list = new EventsList();
		
		for(Event e : box.getEvents().getList()) {
			if(!e.isFree) {
				list.add(e);
		}}
		
		//Drawing background - first event colour
		ePaint.setColor(list.get(0).color);		
		c.canvas.drawRect(eRect, ePaint);
		
		//Add next events
		
		for(int i = 1; i < list.size(); i++) {
			Event e = list.get(i);
			
			
			
		    int[] colors = {e.color, e.color, e.color, Color.TRANSPARENT, Color.TRANSPARENT, Color.TRANSPARENT};
			
		    LinearGradient gradient = new LinearGradient(0, 0, 6, 8, colors, null, Shader.TileMode.REPEAT);
		    ePaint.setShader(gradient);
			c.canvas.drawRect(eRect, ePaint);
			
		}
		
		
	
		
	}
	
	public static void obliqueStripesOld(Contour c, LineBox box) {
		
        RectF eRect = new RectF();
		
		float top = (float) (box.getRelStart()*c.pxPerMili + c.marginTop);
		float bottom = (float) (box.getRelEnd()*c.pxPerMili + c.marginTop);
		eRect.set(c.axisX-c.eventWidth/2f, top, c.axisX+c.eventWidth/2f, bottom+1);
		
		Paint ePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		
		int nColors = box.getEvents().size();
		
	    int[] colors = new int[nColors*3];
	        
	    for(int i = 0; i < nColors; i++) {
	    	colors[i*3] = box.getEvents().get(i).color;
	    	colors[i*3+1] = box.getEvents().get(i).color;  
	    	colors[i*3+2] = box.getEvents().get(i).color; 
	    }
	    
	    //colors[nColors*3] = box.getEvents().get(0).color;
	    
	    int[] newColors = new int[nColors*3-2];
	    
	    for(int i = 0; i<nColors*3-2; i++) {
	    	newColors[i]=colors[i+1];
	    }
	    
	    LinearGradient gradient = new LinearGradient(0, 0, 3, 4, colors, null, Shader.TileMode.MIRROR);
	    ePaint.setShader(gradient);
	    ePaint.setAlpha(255);			
		c.canvas.drawRect(eRect, ePaint);	
	}
	
	
	public static void obliqueStripesProject(Contour c, LineBox box) {
		
        RectF eRect = new RectF();
		
		float top = (float) (box.getRelStart()*c.pxPerMili + c.marginTop);
		float bottom = (float) (box.getRelEnd()*c.pxPerMili + c.marginTop);
		eRect.set(c.axisX-c.eventWidth/2f, top, c.axisX+c.eventWidth/2f, bottom+1);
		
		Paint ePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		
		int nColors = box.getEvents().size();
		
	    int[] colors = new int[nColors*3+1];
	        
	    for(int i = 0; i < nColors; i++) {
	    	colors[i*3] = box.getEvents().get(i).color;
	    	colors[i*3+1] = box.getEvents().get(i).color;  
	    	colors[i*3+2] = box.getEvents().get(i).color; 
	    }
	    
	    colors[nColors*3] = box.getEvents().get(0).color;
	    
	    
	   
	    
	    LinearGradient gradient = new LinearGradient(0, 0, 3*nColors, 6*nColors, colors, null, Shader.TileMode.REPEAT);
	    ePaint.setShader(gradient);
		//ePaint.setShadowLayer(2, 1, 1, 0x55000000);
		c.canvas.drawRect(eRect, ePaint);	
		
		ePaint.setStyle(Paint.Style.STROKE);
		ePaint.setShader(null);
		ePaint.setColor(0x22000000);
		ePaint.setStrokeWidth(2);
		//c.canvas.drawRect(eRect, ePaint);
		
	}
	
	
	public static void black(Contour c, LineBox box) {
		
        RectF eRect = new RectF();
		
		float top = (float) (box.getRelStart()*c.pxPerMili + c.marginTop);
		float bottom = (float) (box.getRelEnd()*c.pxPerMili + c.marginTop);
		eRect.set(c.axisX-c.eventWidth/2f, top, c.axisX+c.eventWidth/2f, bottom+1);
		
		Paint ePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		
		ePaint.setColor(0xff000000);
	    ePaint.setAlpha(255);			
		c.canvas.drawRect(eRect, ePaint);	
	}
	
	
}
	
