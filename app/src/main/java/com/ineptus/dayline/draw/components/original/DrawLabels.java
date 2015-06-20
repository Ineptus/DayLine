package com.ineptus.dayline.draw.components.original;

import com.ineptus.dayline.R;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Rect;
import android.graphics.Typeface;

import com.ineptus.dayline.Contour;
import com.ineptus.dayline.containers.Event;
import com.ineptus.dayline.containers.Label;
import com.ineptus.dayline.containers.LineBox;
import com.ineptus.dayline.containers.PeculiarLabel;
import com.ineptus.dayline.tools.Prefs;

import java.util.ArrayList;

public class DrawLabels {

    private final static int PRIORITY_STANDARD = 1;
    private final static int PRIORITY_LOW = 0;

    private final static int COLOR_FREETIME = 0xff88BB00;


    public static void peculiar(Contour c) {

        final String freeTimeString = c.context.getResources().getString(R.string.of_free_time);

        //Define paint
        Paint paint = defineLabelsPaint(c);

        //Cancel if too narrow
        if (paint.measureText("asw...") > c.getLabelsWidth()) {
            return;
        }


        ////Populate labels manager
        //Add events labels
        for (Event event : c.events.getList()) {
            c.labels.addFromEvent(event, PRIORITY_STANDARD);
        }

        //Add free time labels
        if (c.labelFreeTime) {

            for (LineBox box : c.line.list) {
                if (box.isFree() && c.line.hasSurrounded(box)) {

                    //Set label's text
                    String h = String.valueOf(box.getDuration() / (1000 * 60 * 60)) + " h ";
                    if (h.equals("0 h ")) h = "";
                    String min = String.valueOf((box.getDuration() / (1000 * 60)) % 60) + " m ";
                    if (min.equals("0 m ")) min = "";
                    String text = h + min + freeTimeString;

                    //Add label
                    c.labels.addCustom(text, COLOR_FREETIME, c.getBoxCenterY(box), PRIORITY_LOW);
                }
            }
        }

        //Prepare labels for drawing
        c.labels.fit();

        //Draw labels
        int posX = c.getLabelsPosX();

        while (c.labels.hasNext()) {
            PeculiarLabel label = c.labels.next();

            if (label.text == null || label.text == "") {
                continue;
            }

            paint.setColor(label.color);
            paint.setShadowLayer(3, 1, 1, 0xaa000000);

            String text = cutText(c, paint, label.text);

            c.canvas.drawText(text, posX, c.marginTop + label.y + c.textSize * 0.33f, paint);
        }
    }


    private static Paint defineLabelsPaint(Contour c) {

        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setTextSize(c.textSize);
        if(c.mirror) {
            paint.setTextAlign(Align.LEFT);
        } else {
            paint.setTextAlign(Align.RIGHT);
        }
        paint.setTypeface(Typeface.DEFAULT_BOLD);

        return paint;
    }


	private static String cutText(Contour c, Paint paint, String text) {

		if( paint.measureText(text) > c.getLabelsWidth() && text.length() > 0 ) {

			while( paint.measureText(text+"...") > c.getLabelsWidth() && text.length() > 0 ) {

				text = text.substring(0, text.length()-1);
			}

			text = text+"...";
		}

		return text;
	}


}
