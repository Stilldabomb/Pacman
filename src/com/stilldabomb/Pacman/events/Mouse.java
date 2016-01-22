package com.stilldabomb.Pacman.events;

import java.awt.event.MouseEvent;

import javax.swing.event.MouseInputListener;

import lombok.Getter;

public class Mouse implements MouseInputListener {
	
	private static Mouse instance;
	@Getter
	private static boolean leftMouseDown = false, rightMouseDown = false, middleMouseDown = false;
	@Getter
	private static int x = 0, y = 0;
	
	public Mouse() {
		instance = this;
	}
	
	public static Mouse getInstance() {
		return instance();
	}
	
	private static Mouse instance() {
		if (instance == null)
			return new Mouse();
		return instance;
	}

	public void mouseClicked(MouseEvent e) {
		
	}

	public void mousePressed(MouseEvent e) {
		if (e.getButton() == MouseEvent.BUTTON1) 
			leftMouseDown = true;
		if (e.getButton() == MouseEvent.BUTTON2)
			middleMouseDown = true;
		if (e.getButton() == MouseEvent.BUTTON3)
			rightMouseDown = true;
	}

	public void mouseReleased(MouseEvent e) {
		if (e.getButton() == MouseEvent.BUTTON1) 
			leftMouseDown = false;
		if (e.getButton() == MouseEvent.BUTTON2)
			middleMouseDown = false;
		if (e.getButton() == MouseEvent.BUTTON3)
			rightMouseDown = false;
	}

	public void mouseEntered(MouseEvent e) {
		
	}

	public void mouseExited(MouseEvent e) {
		
	}

	public void mouseDragged(MouseEvent e) {
		x = e.getX();
		y = e.getY();
	}

	public void mouseMoved(MouseEvent e) {
		x = e.getX();
		y = e.getY();
	}

}
