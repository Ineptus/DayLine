package com.ineptus.dayline.draw.components.original;

import com.ineptus.dayline.R;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Rect;
import android.graphics.Typeface;

import com.ineptus.dayline.Contour;
import com.ineptus.dayline.containers.Label;
import com.ineptus.dayline.tools.Prefs;

public class DrawLabels {
	
	public static void simply(Contour c) {
		
		final String freeTimeString = c.context.getResources().getString(R.string.of_free_time);
		
		//DEFINE PAINT
		Paint lPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		lPaint.setTextSize(c.textSize);
		if(c.mirror) {
			lPaint.setTextAlign(Align.LEFT);
		} else {
			lPaint.setTextAlign(Align.RIGHT);
		}
		lPaint.setTypeface(Typeface.DEFAULT_BOLD);
		
		//CANCEL IF TOO NARROW
		if(lPaint.measureText("asd...") > c.getLabelsWidth()) {
			return;
		}
		
		
		if(c.events.moveToFirst()) {
			do{ 
				if(c.events.current().start < c.start && c.events.current().end > c.end) {
					continue;
				}
				c.labels.addFromEvent(c.events.current(), 1);
			} while(c.events.moveToNext());		
		}
		
		if(c.line.moveToFirst()) {
			do{ 
				if(c.line.current().isFree()) {
					
					if(c.line.hasSurrounded(c.line.current()) && Prefs.load(c, Prefs.LABEL_FREE_TIME, true)) {
						
						String label;
						
						String h = String.valueOf(c.line.current().getDuration()/(1000*60*60)) + " h ";
						if (h.equals("0 h ")) h = "";
						String min = String.valueOf((c.line.current().getDuration()/(1000*60))%60) +  " m ";
						if (min.equals("0 m ")) min = "";
						label = h + min + freeTimeString;
						
						
						int color = 0xff88BB00;
						c.labels.addCustom(label, color, c.getBoxCenterY(c.line.current()), 0);
					}
				}
			} while (c.line.moveToNext());
		}
		
		
		int posX = c.getLabelsPosX();
		
		if(c.labels.moveToFirst()) {
			do{ 
				Label label = c.labels.current();
				
				if(label.text == null || label.text == "") {
					continue;
				}
				
				lPaint.setColor(label.color);
				lPaint.setShadowLayer(3, 1, 1, 0xaa000000);
				
				String text = label.text;
				
				text = cutText(c, lPaint, text);

				c.canvas.drawText(text, posX, c.marginTop+label.y+c.textSize*0.33f, lPaint);
			} while(c.labels.moveToNext());
		}



	}

    public static void inverted(Contour c) {

        final String freeTimeString = c.context.getResources().getString(R.string.of_free_time);

        //DEFINE PAINT
        Paint lPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        lPaint.setTextSize(c.textSize);
        if(c.mirror) {
            lPaint.setTextAlign(Align.LEFT);
        } else {
            lPaint.setTextAlign(Align.RIGHT);
        }
        lPaint.setTypeface(Typeface.DEFAULT_BOLD);

        //BACKGROUND PAINT
        Paint bPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        bPaint.setShadowLayer(2, 1, 1, 0xaa000000);

        //CANCEL IF TOO NARROW
        if(lPaint.measureText("asd...") > c.getLabelsWidth()) {
            return;
        }


        if(c.events.moveToFirst()) {
            do{
                if(c.events.current().start < c.start && c.events.current().end > c.end) {
                    continue;
                }
                c.labels.addFromEvent(c.events.current(), 1);
            } while(c.events.moveToNext());
        }

        if(c.line.moveToFirst()) {
            do{
                if(c.line.current().isFree()) {

                    if(c.line.hasSurrounded(c.line.current()) && Prefs.load(c, Prefs.LABEL_FREE_TIME, true)) {

                        String label;

                        String h = String.valueOf(c.line.current().getDuration()/(1000*60*60)) + " h ";
                        if (h.equals("0 h ")) h = "";
                        String min = String.valueOf((c.line.current().getDuration()/(1000*60))%60) +  " m ";
                        if (min.equals("0 m ")) min = "";
                        label = h + min + freeTimeString;


                        int color = 0xff88BB00;
                        c.labels.addCustom(label, color, c.getBoxCenterY(c.line.current()), 0);
                    }
                }
            } while (c.line.moveToNext());
        }


        int posX = c.getLabelsPosX();

        if(c.labels.moveToFirst()) {
            do{
                Label label = c.labels.current();

                if(label.text == null || label.text == "") {
                    continue;
                }

                lPaint.setColor(0xffffffff);

                String text = label.text;

                text = cutText(c, lPaint, text);

                //BACKGROUND
                bPaint.setColor(label.color);
                Rect bRect = new Rect((int) (posX-lPaint.measureText(label.text)-5), c.marginTop+label.y-c.labelHalfHeight, posX+5, c.marginTop+label.y+c.labelHalfHeight+1);

                c.canvas.drawRect(bRect, bPaint);
                c.canvas.drawText(text, posX, c.marginTop+label.y+c.textSize*0.33f, lPaint);
            } while(c.labels.moveToNext());
        }



    }


    public static void whiteBackground(Contour c) {

        final String freeTimeString = c.context.getResources().getString(R.string.of_free_time);

        //DEFINE PAINT
        Paint lPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        lPaint.setTextSize(c.textSize);
        if(c.mirror) {
            lPaint.setTextAlign(Align.LEFT);
        } else {
            lPaint.setTextAlign(Align.RIGHT);
        }
        lPaint.setTypeface(Typeface.DEFAULT_BOLD);
        lPaint.setShadowLayer(2, 1, 1, 0xaa000000);

        //BACKGROUND PAINT
        Paint bPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        //bPaint.setShadowLayer(1, 1, 1, 0x44000000);
        bPaint.setColor(0xaaffffff);

        //CANCEL IF TOO NARROW
        if(lPaint.measureText("asd...") > c.getLabelsWidth()) {
            return;
        }


        if(c.events.moveToFirst()) {
            do{
                if(c.events.current().start < c.start && c.events.current().end > c.end) {
                    continue;
                }
                c.labels.addFromEvent(c.events.current(), 1);
            } while(c.events.moveToNext());
        }

        if(c.line.moveToFirst()) {
            do{
                if(c.line.current().isFree()) {

                    if(c.line.hasSurrounded(c.line.current()) && Prefs.load(c, Prefs.LABEL_FREE_TIME, true)) {

                        String label;

                        String h = String.valueOf(c.line.current().getDuration()/(1000*60*60)) + " h ";
                        if (h.equals("0 h ")) h = "";
                        String min = String.valueOf((c.line.current().getDuration()/(1000*60))%60) +  " m ";
                        if (min.equals("0 m ")) min = "";
                        label = h + min + freeTimeString;


                        int color = 0xff88BB00;
                        c.labels.addCustom(label, color, c.getBoxCenterY(c.line.current()), 0);
                    }
                }
            } while (c.line.moveToNext());
        }


        int posX = c.getLabelsPosX();

        if(c.labels.moveToFirst()) {
            do{
                Label label = c.labels.current();

                if(label.text == null || label.text == "") {
                    continue;
                }

                lPaint.setColor(label.color);

                String text = label.text;

                text = cutText(c, lPaint, text);

                //BACKGROUND
                Rect bRect = new Rect((int) (posX-lPaint.measureText(label.text)-5), c.marginTop+label.y-c.labelHalfHeight-1, posX+10, c.marginTop+label.y+c.labelHalfHeight);

                c.canvas.drawRect(bRect, bPaint);
                c.canvas.drawText(text, posX, c.marginTop+label.y+c.textSize*0.33f, lPaint);
            } while(c.labels.moveToNext());
        }



    }

	private static String cutText(Contour c, Paint paint, String text) {

		
		if( paint.measureText(text) > c.getLabelsWidth() && text.length() > 0 ) {
			

			
			while( paint.measureText(text+"...") > c.getLabelsWidth() && text.length() > 0 ) {
			
				text = text.substring(0, text.length()-1);
				

				
				//if(text.charAt(text.length()-1) == ' ' && text.length() > 0 ) {
				//	text = text.substring(0, text.length()-1);
				//}
			}
			text = text+"...";
		}
		
		return text;
	}


}
