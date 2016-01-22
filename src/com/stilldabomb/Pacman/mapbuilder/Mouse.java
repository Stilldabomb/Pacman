package com.stilldabomb.Pacman.mapbuilder;

import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.event.MouseEvent;

import javax.swing.event.MouseInputListener;

import com.stilldabomb.Pacman.entities.Entity;

import lombok.Getter;

public class Mouse implements MouseInputListener {
	
	private static Mouse instance;
	@Getter
	private static boolean leftMouseDown = false, rightMouseDown = false, middleMouseDown = false;
	@Getter
	private static int x = 0, y = 0;
	private double offsetX = 0, offsetY = 0;
	private boolean moveWindow = false;
	
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

	public void mouseClicked(MouseEvent event) {
		if (event.getButton() == MouseEvent.BUTTON1) {
			try {
				Entity e = MapBuilderRenderer.getInstance().getEntities().stream().filter(en -> en.collidesWith(event.getX(), event.getY())).findFirst().get();
				MapBuilderRenderer.getInstance().setCurrentlySelected(e);
				MapBuilderRenderer.getInstance().repaint();
			} catch (Exception ex){}
		}
	}

	public void mousePressed(MouseEvent e) {
		if (e.getButton() == MouseEvent.BUTTON1) {
			leftMouseDown = true;
			if (!MapBuilderRenderer.getInstance().getEntities().stream().anyMatch(en -> en.collidesWith(e.getX(), e.getY()))) {
				moveWindow = true;
				Point p = MapBuilder.getInstance().getLocation();
				offsetX = MouseInfo.getPointerInfo().getLocation().getX() - p.getX();
				offsetY = MouseInfo.getPointerInfo().getLocation().getY() - p.getY();
			}
		}
		if (e.getButton() == MouseEvent.BUTTON2)
			middleMouseDown = true;
		if (e.getButton() == MouseEvent.BUTTON3)
			rightMouseDown = true;
	}

	public void mouseReleased(MouseEvent e) {
		if (e.getButton() == MouseEvent.BUTTON1) {
			leftMouseDown = false;
			moveWindow = false;
		}
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
		if (moveWindow) {
			Point mp = MouseInfo.getPointerInfo().getLocation();
			Point p = new Point((int)(mp.getX() - offsetX), (int)(mp.getY() - offsetY));
			MapBuilder.getInstance().setLocation(p);
		}
		x = e.getX();
		y = e.getY();
	}

	public void mouseMoved(MouseEvent e) {
		x = e.getX();
		y = e.getY();
	}

}
