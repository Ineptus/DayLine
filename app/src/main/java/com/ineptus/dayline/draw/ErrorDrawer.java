package com.ineptus.dayline.draw;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.widget.RemoteViews;

import com.ineptus.dayline.Contour;
import com.ineptus.dayline.R;

public class ErrorDrawer {
	
	public static void draw(RemoteViews views, Contour contour) {		
		
		RectF r = new RectF(0, 0, contour.width, contour.height);
	    Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
	    p.setColor(0x66111111);
	    String text = "REPLACING WIDGET REQUIRED :(";
	    int maxWidth = contour.width;
	    p.breakText(text, true, maxWidth, null);
	    
	    contour.canvas.drawRoundRect(r, 20, 20, p);
		
		p = new Paint(Paint.FAKE_BOLD_TEXT_FLAG);
		
		p.setTextAlign(Paint.Align.CENTER);
		p.setTextSize(20);
		p.setColor(Color.RED);
		
		contour.canvas.drawText(text, contour.width/2f, contour.height/2f, p);
		
	    views.setImageViewBitmap(R.id.dayline_image, contour.bitmap);
	    
	}
	
	
	
	

}
