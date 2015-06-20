package com.ineptus.dayline.containers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class EventsList {
	
	private ArrayList<Event> list;
	private int position = 0;
	
	public EventsList() {
		
		list = new ArrayList<>();
	}
	
	//ADDING
	
	public void add(Event event) {
		
		list.add(event);
	}
	
	public void addAll(EventsList eventsList) {
		
		list.addAll(eventsList.getList());
	}
	
	
	public ArrayList<Event> getList() {
		return list;
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
	
	public Event current() {
		return list.get(position);
	}
	
	public int size() {
		return list.size();
	}
	
	public Event get(int n) {
		return list.get(n);
	}

	
	
	public void sortByStart() {
		
	    Collections.sort(list, new Comparator<Event>() {
	    	
	        public int compare(Event e1, Event e2) {
	            return( (int) (e1.start - e2.start) );
	        }
	    });
	}

	public void sortByCenter() {

		Collections.sort(list, new Comparator<Event>() {

			public int compare(Event e1, Event e2) {
				return( (int) ((e1.start + e1.getDuration()/2) - (e2.start + e2.getDuration()/2) ));
			}
		});
	}
			
	
}
