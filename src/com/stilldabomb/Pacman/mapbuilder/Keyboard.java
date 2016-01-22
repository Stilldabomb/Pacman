package com.stilldabomb.Pacman.mapbuilder;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;

public class Keyboard implements KeyListener {
	
	private List<Integer> keys = new ArrayList<Integer>();
	private static Keyboard instance;
	
	public Keyboard() {
		instance = this;
	}
	
	public static Keyboard getInstance() {
		return instance();
	}
	
	private static Keyboard instance() {
		if (instance == null)
			return new Keyboard();
		return instance;
	}
	
	public void keyTyped(KeyEvent e) {
		
	}

	public void keyPressed(KeyEvent e) {
		if (!keys.contains(e.getKeyCode()))
			keys.add(e.getKeyCode());
	}

	public void keyReleased(KeyEvent e) {
		if (keys.contains(e.getKeyCode()))
			keys.remove(keys.indexOf(e.getKeyCode()));
	}
	
	public boolean keyDown(int keyCode) {
		return keys.contains(keyCode);
	}
	
	public static boolean isKeyDown(int keyCode) {
		return instance().keyDown(keyCode);
	}

}
