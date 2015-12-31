package com.stilldabomb.Pacman;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import lombok.Getter;

import com.stilldabomb.Pacman.entities.*;
import com.stilldabomb.Pacman.entities.EntityWall.WallType;

public class PacmanRenderer extends JPanel {
	
	@Getter
	private static PacmanRenderer instance;
	@Getter
	private static int windowSize = 500;
	private long lastUpdate;
	@Getter
	private List<Entity> entities = new ArrayList<Entity>() {{
		add(new EntityPacman());
		add(new EntityWall(5, 5));
		add(new EntityWall(4, 5));
		add(new EntityWall(6, 5));
		add(new EntityWall(5, 4));
		add(new EntityWall(5, 6));
		add(new EntityWall(8, 5));
		add(new EntityWall(8, 6));
	}};
	
	public PacmanRenderer() {
		instance = this;
		this.setBackground(Color.BLACK);
		this.setPreferredSize(new Dimension(windowSize, windowSize));
	}
	
	public void update(double delta) {
		for (Entity entity : entities) {
			entity.update(delta);
		}
	}
	
	public void paint(Graphics graphics) {
		Graphics2D g = (Graphics2D)graphics;
		g.setRenderingHint (RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		long now = System.currentTimeMillis();
		if (lastUpdate == 0) lastUpdate = now;
		update((now - lastUpdate)/1000D);
		lastUpdate = now;
		
		entities.stream().filter(Entity::isNotPacman).forEach(e -> e.draw(g));
		entities.stream().filter(Entity::isPacman).forEach(e -> e.draw(g));
		
		try {Thread.sleep(25);}catch(Exception e){e.printStackTrace();}
		repaint();
	}
	
}
