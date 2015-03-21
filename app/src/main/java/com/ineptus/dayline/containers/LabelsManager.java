package com.ineptus.dayline.containers;

import java.util.ArrayList;



public abstract class LabelsManager {

	public final static int PRIORITY_HIGH = 2;
	public final static int PRIORITY_NORMAL = 1;
	public final static int PRIORITY_LOW = 0;
	
	public final static float TEXT_SIZE_LARGE = 1.2f;
	public final static float TEXT_SIZE_NORMAL = 1f;
	public final static float TEXT_SIZE_SMALL = 0.8f;
	
	private ArrayList<Label> list;
	
	private int position = 0;
	
	//CREATING	
	
	abstract void addFromEvent(Event event, int priority);
	
	abstract void addCustom(String text, int color, float y, int priority);
	
	
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