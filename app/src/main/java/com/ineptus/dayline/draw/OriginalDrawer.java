package com.ineptus.dayline.draw;

import android.widget.RemoteViews;

import com.ineptus.dayline.Contour;
import com.ineptus.dayline.R;
import com.ineptus.dayline.draw.components.original.DrawAxis;
import com.ineptus.dayline.draw.components.original.DrawBoxes;
import com.ineptus.dayline.draw.components.original.DrawBoxesShadow;
import com.ineptus.dayline.draw.components.original.DrawLabels;
import com.ineptus.dayline.draw.components.original.DrawTimeLabels;

public class OriginalDrawer {

	public static void draw(Contour c, RemoteViews views) {		
		
		DrawAxis.shining(c);
		
		DrawTimeLabels.simpleHours(c);
		
		DrawLabels.simply(c);
		
		DrawBoxesShadow.simply(c);
		
		DrawBoxes.newWay(c);
		
	    views.setImageViewBitmap(R.id.dayline_image, c.bitmap);
	    
	}
	
}
