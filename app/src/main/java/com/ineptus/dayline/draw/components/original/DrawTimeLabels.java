package com.ineptus.dayline.draw.components.original;

import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.text.format.Time;

import com.ineptus.dayline.Contour;

public class DrawTimeLabels {
	
public static void simpleHours(Contour c) {
		
	
		int mode = 24;
		if(c.use12hours) {
			mode = 12;
		};
		
		int pxOfFirstHour = (int) Math.floor(c.now.minute*c.pxPerMinute);
		
	    float textSize = c.textSize;
	    int everyH = 1;

	    while(c.axisHeight/c.range < textSize*3/everyH  || 24%everyH!=0) {
	    	everyH++;
	    }

	    //HOURS PAINT
	    Paint tPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
	    tPaint.setFakeBoldText(true);
	    tPaint.setColor(0xffdddddd);
	    tPaint.setTextSize(textSize);
	    tPaint.setTextAlign(Align.CENTER);
	    
	    
	    //SHADOW PAINT
	    Paint sPaint = new Paint(tPaint);
	    sPaint.setColor(0x55000000);
	    
	    //LINES PAINT
	    Paint lPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
	    lPaint.setColor(0xbbffffff);  

	    int hoursYpos;
	    int linesYpos;
	    
	    float hoursX;
	    if(c.mirror) {
	    	hoursX = c.axisX - (tPaint.measureText("23")/2f + c.eventWidth/2f + 10);
	    } else {
	    	hoursX = c.axisX + (tPaint.measureText("23")/2f + c.eventWidth/2f + 10);
	    }
	    int axisLeftX = c.axisX-c.axisWidth/2;
	    int axisRightX = c.axisX+c.axisWidth/2;
   
	    for(int i = 0; i < c.range; i++) {
	    	
	    	int absH = (c.now.hour+1 +i);
	    	int cH = absH % mode;
	    	if(absH % 12 == 0 && absH % 24 != 0) {
	    		cH = 12;
	    	}
	    	hoursYpos = (int) Math.floor(c.marginTop + textSize*0.34f + c.pxPerHour*(i+1) - pxOfFirstHour);
	    	linesYpos = (int) Math.floor(c.marginTop + c.pxPerHour*(i+1) - pxOfFirstHour);
	    	
	    	c.canvas.drawRect(axisLeftX, linesYpos-0.5f, axisRightX, linesYpos+0.5f, lPaint);
	    	
	    	if(cH%everyH != 0) {
	    		c.canvas.drawCircle(hoursX, linesYpos, 2, tPaint);
	    		continue;
	    	}
	    	
	    	//FORMAT HOUR NUMBER
	    	String fH;
	    	if(cH<10) {
	    		fH = "0"+cH;	    		
	    	} else {
	    		fH = ""+cH;	
	    	}
	    	
	    	//SET SHADOW
	    	tPaint.setShadowLayer(3, 1, 1, 0xaa000000);
	    	
	    	if(cH != 0){
		    	c.canvas.drawText(fH, hoursX, hoursYpos, tPaint);
	    	
	    	} else {   //DRAW DAYCHANGE

	    		Time then = new Time();
	    		then.set(c.now.toMillis(true) + (60*60*1000)*absH);
	    		
	    		String day = then.monthDay+"";
	    		Paint dPaint = new Paint(tPaint);
	    		dPaint.setColor(0xffDD99FF);
	    		dPaint.setTextSize(textSize);
	    		
	    		c.canvas.drawText(day, hoursX, hoursYpos, dPaint);

	    	}
	    	
	    }
	    
	}

}
