package com.ineptus.dayline.containers;

import android.text.format.Time;

import com.ineptus.dayline.Contour;

public class LineBox {
	
	public long start;
	public long end;
	
	private Contour c;
	private EventsList list;

	public LineBox(Contour contour, long start, long end, EventsList events) {
		
		c = contour;
		this.list = events;	
		this.start = start;
		this.end = end;
	}
	
	public LineBox(LineBox other) {
		c = other.getContour();
		this.start = other.start;
		this.end = other.end;
		list = new EventsList();
		list.addAll(other.getEvents());
	}
	
	public boolean collidesWith(LineBox other) {

        return !(start >= other.end || end <= other.start);
	}
	
	public void addEvents(EventsList events) {
		list.addAll(events);}
	
	public EventsList getEvents() {
		return list;
	}
	
	
	public void setStart(long start) {this.start = start;}
	public void setEnd(long end) {this.end = end;}
	
	public Contour getContour() { return c; }
	public int getDuration() { return (int)(end-start);}
	public long getRelStart() {return start-c.now.toMillis(true);}
	public long getRelEnd() {return end-c.now.toMillis(true);}
	public boolean isFree() {return  list.size()==0;}
	public boolean isCollision() {return list.size()>1;}
	public int getFirstColor() {return list.get(0).color;}
	public Time getNow() {return c.now;}
	public boolean isClear() {return list.size()==1;}
	
	public Event getFirstEvent() { return list.get(0); }

	
	public float getStartY() {
		return (float) ((start-c.now.toMillis(true))*c.pxPerMili+c.marginTop);
	}
	
	public float getEndY() {
		return (float) ((end-c.now.toMillis(true))*c.pxPerMili+c.marginTop);
	}
	
	public float getHeight() {
		return (float) ((end-start)*c.pxPerMili);
	}

	public String getName() {
		if(this.isCollision()) {
			String result = "[";
			for(int i = 0; i < this.getEvents().size(); i++) {
				result += "+"+this.getEvents().get(i).title;
			}
			return(result+"]");
		} else if(this.isClear()) {
			return(this.getFirstEvent().title);
		} else {
			return(""+this.getDuration()/60000 + "m of free time");
		}		
	}
	

	
}
	

