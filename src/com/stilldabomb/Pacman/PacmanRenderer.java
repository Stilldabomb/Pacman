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

public class PacmanRenderer extends JPanel {
	
	@Getter
	private static PacmanRenderer instance;
	private static int bsize = 30;
	@Getter
	private static int windowSize = (bsize + 1)*Pacman.pacmanSize;
	private long lastUpdate;
	@Getter
	private List<Entity> entities = new ArrayList<Entity>() {{
		add(new EntityPacman());
	}};
	
	public PacmanRenderer() {
		instance = this;
		this.setBackground(Color.BLACK);
		this.setPreferredSize(new Dimension(windowSize, windowSize));
		// Top and left walls
		for (int i = 0; i < bsize; i++) {
			entities.add(new EntityWall(i, -1));
			entities.add(new EntityWall(i, 0));
			if (i != bsize / 2)
				entities.add(new EntityWall(-1, i));
			if (i + 1 != bsize / 2)
				entities.add(new EntityWall(0, i + 1));
			if (i == bsize - 1) {
				entities.add(new EntityWall(-1, i + 1));
				entities.add(new EntityWall(i + 1, -1));
			}
			if (i != bsize / 2)
				entities.add(new EntityWall(bsize, i));
			if (i != bsize / 2)
				entities.add(new EntityWall(bsize + 1, i));
			entities.add(new EntityWall(i + 1, bsize));
		}
		
		for (int i = -1; i < bsize + 3; i++) {
			entities.add(new EntityWall(i, bsize + 1));
			entities.add(new EntityWall(i, bsize + 2));
		}
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
		
		
		
		try {Thread.sleep(0);}catch(Exception e){e.printStackTrace();}
		repaint();
	}
	
}
