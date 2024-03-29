package com.ineptus.dayline.draw.components.original;

import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Rect;
import android.graphics.Typeface;

import com.ineptus.dayline.Contour;
import com.ineptus.dayline.R;
import com.ineptus.dayline.containers.Event;
import com.ineptus.dayline.containers.LineBox;
import com.ineptus.dayline.containers.PeculiarLabel;
import com.ineptus.dayline.tools.Logger;

public class DrawLabels {

    private final static int COLOR_FREETIME = 0xff88BB00;


    public static void peculiar(Contour c) {

        preparePeculiarLabels(c);

        //Define paint
        Paint paint = defineLabelsPaint(c);

        //Cancel if too narrow
        if (paint.measureText("asw...") > c.getLabelsWidth()) {
            return;
        }

        //Calculate text exact height and position modifier, so labels are placed by center (not baseline)
        Rect testRect = new Rect();
        paint.getTextBounds("a", 0, 1, testRect);
        final float textPosMod = testRect.bottom-testRect.top / 2f;

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

            c.canvas.drawText(text, posX, c.marginTop + label.y + textPosMod, paint);
            Logger.log("DrawLabels", 0, "Drawn " + text + " on " + label.y);
        }
    }

    private static void preparePeculiarLabels(Contour c) {

        final String freeTimeString = c.context.getResources().getString(R.string.of_free_time);

        ////Populate labels manager
        //Add events labels
        for (Event event : c.events.getList()) {
            c.labels.addFromEvent(event, PeculiarLabel.PRIORITY_NORMAL);
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
                    c.labels.addCustom(text, COLOR_FREETIME, c.getBoxCenterY(box), PeculiarLabel.PRIORITY_LOW);
                }
            }
        }

        //Prepare labels for drawing
        //Here LabelsManager really works
        //And causes most of bugs
        c.labels.fit();

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
