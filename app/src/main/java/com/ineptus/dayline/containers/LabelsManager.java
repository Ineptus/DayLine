package com.ineptus.dayline.containers;

import com.ineptus.dayline.Contour;

import java.util.ArrayList;
import java.util.Collections;


public abstract class LabelsManager {

	public final static int PRIORITY_HIGH = 2;
	public final static int PRIORITY_NORMAL = 1;
	public final static int PRIORITY_LOW = 0;
	
	public final static float TEXT_SIZE_LARGE = 1.2f;
	public final static float TEXT_SIZE_NORMAL = 1f;
	public final static float TEXT_SIZE_SMALL = 0.8f;
	
	private ArrayList<Label> list;
	
	private int position = 0;
    private Contour c;
	
	//CREATING	
	
	abstract void addFromEvent(Event event, int priority);
	
	abstract void addCustom(String text, int color, int y, int priority);
	
	
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

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public ArrayList<Label> getList() {
        return list;
    }

    public void setList(ArrayList<Label> list) {
        this.list = list;
    }

    public void sort() {
       Collections.sort(list);
    }

}
