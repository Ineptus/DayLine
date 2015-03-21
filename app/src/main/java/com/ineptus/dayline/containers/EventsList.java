package com.ineptus.dayline.containers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class EventsList {
	
	private ArrayList<Event> mList;
	private int position = 0;
	
	public EventsList() {
		
		mList = new ArrayList<Event>();
	}
	
	//ADDING
	
	public void add(Event event) {
		
		mList.add(event);

		sort();
	}
	
	public void addAll(EventsList aList) {
		
		mList.addAll(aList.getList());
		sort();
	}
	
	
	public ArrayList<Event> getList() {
		return mList;
	}
	
	
	
	
	//ITERATOR
	
	public boolean moveToFirst() {	
		if(!mList.isEmpty()) {
			position = 0;
			return true;
		} else {
			return false;
		}
	}
	
	public boolean moveToNext() {
		if(position+1 < mList.size()) {
			position++;
			return true;
		} else {
			return false;
		}
	}
	
	public Event current() {
		return mList.get(position);
	}
	
	public int size() {
		return mList.size();
	}
	
	public Event get(int n) {
		return mList.get(n);
	}

	
	
	private void sort() {
		
	    Collections.sort(mList, new Comparator<Event>() {
	    	
	        public int compare(Event e1, Event e2) {
	            return( (int) (e1.start - e2.start) );
	        }
	    });
	}
			
	
}
