package com.ineptus.dayline.containers;

import java.util.ArrayList;

import android.util.Log;

import com.ineptus.dayline.Contour;

public class SimpleLabelsManager extends LabelsManager {
	
	public final static int PRIORITY_HIGH = 2;
	public final static int PRIORITY_NORMAL = 1;
	public final static int PRIORITY_LOW = 0;
	
	public final static float TEXT_SIZE_LARGE = 1.2f;
	public final static float TEXT_SIZE_NORMAL = 1f;
	public final static float TEXT_SIZE_SMALL = 0.8f;
	
	private Contour c;

	private ArrayList<Label> list;
	
	private int position = 0;
	
	public SimpleLabelsManager(Contour contour) {
		
		list = new ArrayList<Label>();
		c = contour;
	}
	
	public void addFromEvent(Event event, int priority) {
		
		if(event.title == null || event.title.equals("")) {
			return;
		}
		
		Label label = new Label(event, c.getEventCenterY(event), priority);
		
		putLabel(label);		
	}
	
	public void addCustom(String text, int color, float y, int priority) {
		Label label = new Label(text, color, y, priority);
		
		putLabel(label);		
	}
	
	private void putLabel(Label label) {
		//Log.d("putLabel", label.text+" Putting new label...");
		//IF SOMETHING WENT WRONG
		if(label.steps > 200) {list.add(label); return;}
		//Log.d("putLabel", label.text+" steps: "+label.steps);

 		//IF THIS LABEL IS ALREADY FUCKED
		if(label.reachedBottom && label.reachedTop) {
			return;
		}
		
		
		//IF COLIDES WITH TOP OR BOTTOM
		if(collisionWithTop(label) > 0) {
			label.move(collisionWithTop(label));
			label.setReachedTop();
			putLabel(label);
			return;
		}
		if(collisionWithBottom(label) > 0) {
			label.move(0 - collisionWithBottom(label));
			label.setReachedBottom();
			putLabel(label);
			return;
		}
		
		
		//IF LEFT EVENT
		if(labelLeftEvent(label)) {	
			return;
		}
		
		//CHECK COLLISIONS WITH OTHER LABEL
		for(int i = 0; i<list.size(); i++) {

			if(collide(label, list.get(i))) {
				Label other = list.get(i);
				list.remove(i);
				solveCollision(label, other);
				return;
			}
		}
		
		//EVERYTHING JUST FINE
		list.add(label);
	}
	
	



	private boolean labelLeftEvent(Label label) {

		if(label.event == null) {
			return false;
		}

		return label.y < c.getEventStartY(label.event)-10 || label.y > c.getEventEndY(label.event)+10;
	}

	private float collisionWithTop(Label label) {
		return 0-label.y+c.textSize/2;
	}

	private float collisionWithBottom(Label label) {
		return (float) (label.y+c.textSize/2 - (c.end-c.start)*c.pxPerMili);
	}

	private boolean collide(Label label, Label other) {
        return !(other.y >= label.y + c.textSize + c.textVertSpace || other.y <= label.y - c.textSize - c.textVertSpace);
	}
	
	private void solveCollision(Label label, Label other) {
		
		float overlap = (float) Math.ceil(c.textSize - Math.abs(label.y-other.y) + c.textVertSpace);
		
		//CANCEL IF PRIORITY == 0
		if(label.priority == 0) {
			list.add(other);
			return;
		}
		
		//IF FULL OVERLAP
		if(label.y == other.y) {
			if(label.event.start<other.event.start) {
				moveLabel(label, 0-overlap/2);
				moveLabel(other, overlap/2);
				return;
			} else {
				moveLabel(label, overlap/2);
				moveLabel(other, 0-overlap/2);
				return;
			}	
		} else if (label.y < other.y) {
			moveLabel(label, 0-overlap/2);
			moveLabel(other, overlap/2);
			return;
		} else {
			moveLabel(label, overlap/2);
			moveLabel(other, 0-overlap/2);
			return;
		}
		
		
		
		

		
	}
	
	private void moveLabel(Label label, float step) {
		
		label.move(step);
		
		Label collidor = collidor(label);
		
		if (collidor != null) {
			list.remove(collidor);
			moveLabel(collidor, step);
		}
		putLabel(label);
	}

	private int testMove(Label label, float step) {
		
		int mess = 0;	
		Label test = new Label(label);
		
		
		return mess;
		
	}
	
	@SuppressWarnings("unused")
	private int testSingleMove(Label label, float step) {
		
		int mess = 0;
		label.move(step);
		
		if(collides(label)) {
			mess += 1;
			return mess + testMove(collidor(label), step);
		}
		
		return mess;
		
	}
	
	
	private boolean collides(Label label) {
		
		for(int i = 0; i<list.size(); i++) {

			if(collide(label, list.get(i))) {
				return true;
			}
		}
		
		return false;
		
	}
	
	private Label collidor(Label label) {
		
		for(int i = 0; i<list.size(); i++) {

			if(collide(label, list.get(i))) {
				return list.get(i);
			}
		}
		
		return null;
		
	}
	
	
	@SuppressWarnings("unused")
	private void solveCollisionOld(Label label, Label other) {
		
		float overlap = c.textSize - Math.abs(label.y-other.y) + c.textVertSpace;
		//Log.d("solveCollision", "overlap is "+overlap );
		
		if(label.y < other.y) {
			label.move(0-overlap/2f);
			//Log.d("solveCollision", label.text+" moved " + (0-overlap/2f));
			other.move(overlap/2f);
			//Log.d("solveCollision", other.text+" moved "+ (overlap/2f) );
			putLabel(other);
			putLabel(label);
		} else {
			label.move((int)Math.ceil(overlap/2f));
			//Log.d("solveCollision", label.text+" moved " + (overlap/2f));
			other.move((int)Math.ceil(0-overlap/2f));
			//Log.d("solveCollision", other.text+" moved "+ (0-overlap/2f) );
			putLabel(other);
			putLabel(label);
		}
		
		
		
	}
	
	
	//ITERATOR
	
		public boolean moveToFirst() {	
			if(!list.isEmpty()) {
				position = 0;
				return true;
			} else {
				return false;
			}
		}
		
		public boolean moveToNext() {
			if(position+1 < list.size()) {
				position++;
				return true;
			} else {
				return false;
			}
		}
		
		public Label current() {
			return list.get(position);
		}
		
		public int size() {
			return list.size();
		}
	
}
