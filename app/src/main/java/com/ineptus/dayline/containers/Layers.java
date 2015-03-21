package com.ineptus.dayline.containers;

import java.util.ArrayList;
import java.util.Arrays;

import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;

public class Layers {
	
	private Event[] events = new Event[5];
	
	private float[] posX = {0, 0, 0, 2, 2};
	private float[] posY = {0, 0, 0, 3, 3};
	private float[] shiftX = {0, 4, -4, 4, -4};
	private float[] shiftY = {0, 6, 6, 6, 6};
	

	public Layers() {
		
	}
	
	public void put(Event e) {	
		for(int i = 0; i < 5; i++) {		
			if(events[i] == null) {
				events[i] = e;
				return;
	}}}
	
	public void remove(Event e) {	
		for(int i = 0; i < 5; i++) {		
			if(events[i] == e) {
				events[i] = null;
				return;
	}}}
	
	public void update(ArrayList<Event> list) {	

		//REMOVE 'AVAILABLE' IF IN COLLISION
		if(list.size() > 1) {
			for(int i = 0; i < list.size(); i++) {
				if(list.get(i).isFree || list.get(i).isMaybe) {
					list.remove(i);
				}
			}
		}
		
		//REMOVE ABSENT EVENTS
		for(int i = 0; i < 5; i++) {		
			if(!list.contains(events[i])) {
				events[i] = null;
			}
		}
		
		//MOVE EARLIEST EVENT TO SOLID COLOR POSITION
		remove(list.get(0));
		put(list.get(0));
		
		//PUT NEW EVENTS
		for(int i = 0; i < list.size(); i++) {		
			if(!Arrays.asList(events).contains(list.get(i))) {
				put(list.get(i));
			}
		}
		
	}

	
	public Paint getPaint(Event event) {
		
		int n = 0;
		
		for(int i = 0; i < 5; i++) {		
			if(events[i] == event) {
				n = i;
				break;
		}}
		
		Paint p = new Paint();
		Event e = events[n];
		int c = e.color;

		
		
		if(n==0) {
			p.setColor(c);
			return p;
		}
		

		
		int t = 0x0;
		
		int[] colors = {c, c, c, t, t, t};
		
		LinearGradient g = new LinearGradient(posX[n], posY[n], posX[n]+shiftX[n], posY[n]+shiftY[n], colors, null, Shader.TileMode.MIRROR);
		
		p.setShader(g);

		
		return p;
	}


}

