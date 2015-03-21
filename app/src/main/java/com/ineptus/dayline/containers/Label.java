package com.ineptus.dayline.containers;



public class Label {

	public String text;
	public float orginY;
	public float y;
	public int color;
	public float textScale = 1;
	public int priority = 1;
	public LineBox box;
	public Event event;
	public int steps = 0;
	public boolean lastUp = false;
	public boolean reachedTop = false;
	public boolean reachedBottom = false;
	
	
	
	/**
	 * @param event - original event
	 * @param y - desired Y position
	 * @param priority
	 */
	public Label(Event event, float y, int priority) {
		
		this.event = event;
		this.text = event.title;
		this.orginY = y;
		this.y = y;
		this.priority = priority;
		this.color = event.color;
		
	}
	
	public Label(String text, int color, float y, int priority) {
		
		this.text = text;
		this.orginY = y;
		this.y = y;
		this.priority = priority;
		this.color = color;
		
	}
	
	
	
	public Label(Label label) {
		this.text = label.text;
		this.orginY = label.orginY;
		this.y = label.y;
		this.color = label.color;
	}

	public void setY(float newY) {
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


	
}
