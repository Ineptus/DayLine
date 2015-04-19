package com.ineptus.dayline.containers;


import java.util.ArrayList;

public class Label implements Comparable<Label> {

	public String text;
	public int originY;
	public int y;
	public int color;
	public float textScale = 1;
	public int priority = 1;
	public LineBox box;
	public Event event;
	public int steps = 0;
	public boolean lastUp = false;
	public boolean reachedTop = false;
	public boolean reachedBottom = false;
    public Label touchAbove = null;
    public Label touchBelow = null;
	
	
	
	/**
	 * @param event - original event
	 * @param y - desired Y position
	 * @param priority - priority
	 */
	public Label(Event event, int y, int priority) {
		
		this.event = event;
		this.text = event.title;
		this.originY = y;
		this.y = y;
		this.priority = priority;
		this.color = event.color;
		
	}
	
	public Label(String text, int color, int y, int priority) {
		
		this.text = text;
		this.originY = y;
		this.y = y;
		this.priority = priority;
		this.color = color;
		
	}
	
	
	
	public Label(Label label) {
		this.text = label.text;
		this.originY = label.originY;
		this.y = label.y;
		this.color = label.color;
	}

	public void setY(int newY) {
		y = newY;
	}
	
	public void addStep(boolean wasUp) {
		steps++;
		lastUp = wasUp;
	}
	
	
	public void move(float step) {
		y += step;
		steps++;
		lastUp = step < 0;
	}
	
	public void setReachedTop() {
		reachedTop = true;
	}
	
	public void setReachedBottom() {
		reachedBottom = true;
	}

    public int countTouchingAbove() {
        if(touchAbove == null) {
            return 0;
        } else {
            return 1+touchAbove.countTouchingAbove();
        }
    }

    public void populateClusterAbove(ArrayList<Label> list) {
        if(touchAbove != null) {
            list.add(touchAbove);
            touchAbove.populateClusterAbove(list);
        }
    }

    public void moveUpClusterBelow(Label last) {
        if(touchBelow != null) {
            touchBelow.moveUpClusterBelow(this);
        }
        y = last.y;
    }

    @Override
    public int compareTo(Label another) {
        return (int) (this.originY -another.originY);
    }
}
