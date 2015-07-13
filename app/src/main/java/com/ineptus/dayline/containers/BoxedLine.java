package com.ineptus.dayline.containers;

import com.ineptus.dayline.Contour;
import com.ineptus.dayline.tools.Logger;

import java.util.ArrayList;

/**
 * This stores LineBox items in <strike>chronological</strike> no order.
 * Uses iterator to provide items for drawing class.
 * This is like:
 * [freeTime box][event1 box][collision of e1 with e2 box][event2 box]... 
 * 
 * EventList[CalendarEvent]
 * BoxedLine[LineBox]
 * 
 */

public class BoxedLine {
	
	private final Contour c;
	
	public ArrayList<LineBox> list;
	private int position = 0;
	
	
	//EMPTY LINE CONTRUCTOR
	public BoxedLine(Contour contour) {
		
		c = contour;
		list = new ArrayList<>();
		
	}
	
	//LINE FROM EVENTS LIST CONTRUCTOR
	public BoxedLine(Contour contour, EventsList events) {
		
		c = contour;
		
		list = new ArrayList<>();
		
		createFromEventsList(events);
	}

	public void createFromEventsList(EventsList list) {
		
		fillWithFreeTime();
		if(list.moveToFirst()) {
			do{
				addFromEvent(list.current());
			} while(list.moveToNext());
		}
		
		sort();
	}

	public void addFromEvent(Event event) {
		
		if(event.start < c.start && event.end > c.end) {
			return;
		}
		
		LineBox box = createBoxFromEvent(event);

		putBox(box);
		
		
	}
	
	
	
	private LineBox createBoxFromEvent(Event event) {
			
		long eStart = event.start;
		long eEnd = event.end;
		
		if(eStart < c.start) {
			eStart = c.start;
		}
		
		if(eEnd > c.end) {
			eEnd = c.end;
		}
		
		EventsList list = new EventsList();
		list.add(event);
			
		return new LineBox(c, eStart, eEnd, list);
	}

	private void putBox(LineBox box) {
		
		if(box.getDuration() == 0) {
			return;
		}
		
		for(int i = 0; i< list.size(); i++) {
			if(box.collidesWith(list.get(i))) {
				LineBox other = list.get(i);
				list.remove(i);
				solveBoxes(box, other);
				return;
			}
		}
		add(box);
	}
		
	private void solveBoxes(LineBox box, LineBox other) {
		
		//SAME
		if((box.start==other.start) && (box.end==other.end)) {
			Logger.log("solveBoxes", 2, box.getName()+ " same as " + other.getName());
			other.addEvents(box.getEvents());
			add(other);
			return;
		}
		
		//BOX INSIDE OTHER
		if(box.start >= other.start && box.end <= other.end) {
			Logger.log("solveBoxes", 2, box.getName()+ " inside " + other.getName());
			LineBox other2 = new LineBox(other);
			other.setEnd(box.start);
			other2.setStart(box.end);
			box.addEvents(other.getEvents());
			if(other.getDuration() != 0) { 
				add(other);}
			if(other2.getDuration() != 0) {
				add(other2);}
			add(box);
			return;
		}
		
		//OTHER INSIDE BOX
		if(box.start <= other.start && box.end >= other.end) {
			Logger.log("solveBoxes", 2, box.getName()+ " surrounds " + other.getName());
			LineBox box2 = new LineBox(box);
			other.addEvents(box.getEvents());
			list.add(other);
			box.setEnd(other.start);
			box2.setStart(other.end);
			if(box.getDuration() != 0) {
				putBox(box);}
			if(box2.getDuration() != 0) {
				putBox(box2);}
			return;
		}
		
		//PARTIAL OVERLAP
		if(box.start < other.start && box.end < other.end) {
			Logger.log("solveBoxes", 2, box.getName()+ " overlaps< " + other.getName());
			LineBox col = new LineBox(other);
			col.setEnd(box.end);
			col.addEvents(box.getEvents());
			box.setEnd(col.start);
			other.setStart(col.end);
			add(other);
			add(col);
			putBox(box);
			return;
		}
		
		if(box.start > other.start && box.end > other.end) {
			Logger.log("solveBoxes", 2, box.getName()+ " overlaps> " + other.getName());
			LineBox col = new LineBox(other);
			col.setStart(box.start);
			col.addEvents(box.getEvents());
			box.setStart(col.end);
			other.setEnd(col.start);
			add(other);
			add(col);
			putBox(box);
		}
	}
	
	private void fillWithFreeTime() {
		EventsList list = new EventsList();
		LineBox box = new LineBox(c, c.start, c.end, list);
		putBox(box);
	}
	
	private void add(LineBox box) {
		list.add(box);
		Logger.log("add box", 1, box.getName() + " added");
	}
	
	
	//SORTING
	
	private void sort() {
		
		ArrayList<LineBox> sorted = new ArrayList<>();
		
		long curEnd = c.start;
		
		while (sorted.size() != list.size()) {
			for(LineBox box : list) {
				if(box.start == curEnd || box.start == curEnd+1) {
					sorted.add(box);
					curEnd = box.end;
					break;	
		}}}
		list = sorted;
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
	
	public LineBox current() {
		return list.get(position);
	}
	
	public int size() {
		return list.size();
	}
	
	
	//ABOUT BOXES
	
	public boolean hasOnEdge(LineBox box) {return (box.start == c.start) || (box.end == c.end);}
	public boolean hasSurrounded(LineBox box) {return (box.start != c.start) && (box.end != c.end);}
	
}
